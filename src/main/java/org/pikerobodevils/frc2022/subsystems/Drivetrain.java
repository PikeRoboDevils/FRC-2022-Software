/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.subsystems;

import static org.pikerobodevils.frc2022.Constants.DrivetrainConstants.*;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.hal.simulation.SimDeviceDataJNI;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.Set;
import org.pikerobodevils.frc2022.Constants;
import org.pikerobodevils.lib.motorcontrol.DevilCANSparkMax;

public class Drivetrain extends SubsystemBase {

    private final CANSparkMax leftLeader =
            new DevilCANSparkMax(LEFT_LEADER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax leftFollowerOne =
            new DevilCANSparkMax(LEFT_FOLLOWER_ONE_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax leftFollowerTwo =
            new DevilCANSparkMax(LEFT_FOLLOWER_TWO_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

    private final CANSparkMax rightLeader =
            new DevilCANSparkMax(RIGHT_LEADER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax rightFollowerOne =
            new DevilCANSparkMax(RIGHT_FOLLOWER_ONE_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax rightFollowerTwo =
            new DevilCANSparkMax(RIGHT_FOLLOWER_TWO_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

    private final Set<CANSparkMax> leftControllers = Set.of(leftLeader, leftFollowerOne, leftFollowerTwo);
    private final Set<CANSparkMax> rightControllers = Set.of(rightLeader, rightFollowerOne, rightFollowerTwo);
    private final Set<CANSparkMax> followers =
            Set.of(leftFollowerOne, leftFollowerTwo, rightFollowerOne, rightFollowerTwo);
    private final Set<CANSparkMax> all =
            Set.of(leftLeader, leftFollowerOne, leftFollowerTwo, rightLeader, rightFollowerOne, rightFollowerTwo);

    private final Encoder leftEncoder =
            new Encoder(LEFT_ENCODER_A, LEFT_ENCODER_B, false, CounterBase.EncodingType.k1X);
    private final Encoder rightEncoder =
            new Encoder(RIGHT_ENCODER_A, RIGHT_ENCODER_B, true, CounterBase.EncodingType.k1X);

    private final AHRS navX = new AHRS(SPI.Port.kMXP, (byte) 150);

    private final PIDController leftVelocityPID = new PIDController(KP_VELOCITY, 0, 0, Constants.PERIOD);
    private final PIDController rightVelocityPID = new PIDController(KP_VELOCITY, 0, 0, Constants.PERIOD);
    private DifferentialDriveWheelSpeeds prevSpeeds = new DifferentialDriveWheelSpeeds(0, 0);

    private final DifferentialDriveOdometry odometry;

    private final RamseteController pathController = new RamseteController(2, 0.7);

    private Pose2d pose = new Pose2d();

    public final Field2d field = new Field2d();

    private NetworkTable drivetrainTable = NetworkTableInstance.getDefault().getTable("Drivetrain");

    // Sim stuff
    private final DifferentialDrivetrainSim driveSim = new DifferentialDrivetrainSim(
            DCMotor.getNEO(3), 7.56, 18, 60, Units.inchesToMeters(3), TRACK_WIDTH_METERS, null);
    private final EncoderSim leftEncoderSim = new EncoderSim(leftEncoder);
    private final EncoderSim rightEncoderSim = new EncoderSim(rightEncoder);
    int dev = SimDeviceDataJNI.getSimDeviceHandle("navX-Sensor[0]");
    SimDouble angle = new SimDouble(SimDeviceDataJNI.getSimValueHandle(dev, "Yaw"));
    // end sim stuff

    /**
     * Creates a new instance of this Drivetrain. This constructor
     * is private since this class is a Singleton. Code should use
     * the {@link #getInstance()} method to get the singleton instance.
     */
    private Drivetrain() {
        System.out.println("Conversion factor :" + WHEEL_CIRCUMFERENCE_METERS);
        leftLeader.setInverted(false);
        configureController(leftLeader);

        leftFollowerOne.follow(leftLeader);
        configureController(leftFollowerOne);

        leftFollowerTwo.follow(leftLeader);
        configureController(leftFollowerTwo);

        rightLeader.setInverted(true);
        configureController(rightLeader);

        rightFollowerOne.follow(rightLeader);
        configureController(rightFollowerOne);

        rightFollowerTwo.follow(rightLeader);
        configureController(rightFollowerTwo);

        configCANFrames();

        leftEncoder.setSamplesToAverage(3);
        rightEncoder.setSamplesToAverage(3);
        leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_METERS);
        rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_METERS);

        navX.enableBoardlevelYawReset(false);
        // Resetting the gyro does not work while the navX is calibrating
        var counter = 0;
        var timedOut = false;
        while (navX.isCalibrating()) {
            if (counter >= 5) {
                timedOut = true;
                break;
            }
            DataLogManager.log("Waiting on Gyro Calibration...");
            Timer.delay(0.5);
            counter++;
        }

        if (!timedOut) {
            resetYaw();
            DataLogManager.log("Gyro calibrated, Yaw reset!");
        } else {
            DataLogManager.log("Gyro Calibration failed! Check NavX Sensor and reset sensor manually!");
            DriverStation.reportError("Gyro Calibration failed! Check NavX Sensor and reset sensor manually!", false);
        }

        odometry = new DifferentialDriveOdometry(getGyroAngle());

        SmartDashboard.putData(field);
    }

    public void arcadeDrive(double speed, double rotation) {
        DifferentialDrive.WheelSpeeds speeds = DifferentialDrive.arcadeDriveIK(speed, rotation, false);
        driveOpenLoop(speeds.left, speeds.right);
    }

    public void driveOpenLoop(double left, double right) {
        if (RobotBase.isReal()) {
            leftLeader.set(left);
            rightLeader.set(right);
        } else {
            leftLeader.setVoltage(left * 12);
            rightLeader.setVoltage(right * 12);
        }
    }

    public void setLeftAndRightVoltage(double leftVoltage, double rightVoltage) {
        leftLeader.setVoltage(leftVoltage);
        rightLeader.setVoltage(rightVoltage);
    }

    public void setLeftAndRightVelocity(DifferentialDriveWheelSpeeds setpoints) {
        var measurement = getWheelSpeeds();

        var leftVoltage = leftVelocityPID.calculate(measurement.leftMetersPerSecond, setpoints.leftMetersPerSecond);
        var rightVoltage = rightVelocityPID.calculate(measurement.rightMetersPerSecond, setpoints.rightMetersPerSecond);

        setLeftAndRightVoltage(leftVoltage, rightVoltage);
    }

    public void setChassisVelocity(ChassisSpeeds velocity) {
        setLeftAndRightVelocity(KINEMATICS.toWheelSpeeds(velocity));
    }

    public void disable() {
        leftLeader.disable();
        rightLeader.disable();
    }

    public void setIdleMode(CANSparkMax.IdleMode mode) {
        all.forEach(controller -> {
            controller.setIdleMode(mode);
        });
    }

    public Rotation2d getGyroAngle() {
        return navX.getRotation2d();
    }

    public double getYaw() {
        return navX.getYaw();
    }

    public double getPitch() {
        return navX.getPitch();
    }

    public double getRoll() {
        return navX.getRoll();
    }

    public Pose2d getPose() {
        return pose;
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(leftEncoder.getRate(), rightEncoder.getRate());
    }

    public boolean isStopped() {
        return leftEncoder.getStopped() && rightEncoder.getStopped() && navX.isRotating();
    }

    public boolean isWithinRange(Translation2d pose, double range) {
        return getPose().getTranslation().getDistance(pose) < range;
    }

    public void resetYaw() {
        navX.reset();
    }

    public void resetOdometry(Pose2d pose) {
        odometry.resetPosition(pose, navX.getRotation2d());
        resetEncoders();
    }

    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    public void resetPID() {
        leftVelocityPID.reset();
        rightVelocityPID.reset();
    }

    public SimpleMotorFeedforward getFeedforward() {
        return FEEDFORWARD;
    }

    public DifferentialDriveKinematics getKinematics() {
        return KINEMATICS;
    }

    public RamseteController getPathController() {
        return pathController;
    }

    public PIDController getLeftVelocityPID() {
        return leftVelocityPID;
    }

    public PIDController getRightVelocityPID() {
        return rightVelocityPID;
    }

    private void configCANFrames() {
        leftLeader.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus1, 200);
        leftLeader.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 500);
        leftLeader.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus3, 65535);

        rightLeader.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus1, 200);
        rightLeader.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 500);
        rightLeader.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus3, 65535);

        followers.forEach(follower -> {
            follower.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus0, 500);
            follower.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus1, 500);
            follower.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 500);
            follower.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus3, 65535);
        });
    }

    private void checkForResetAndConfig() {
        boolean hasReset = false;
        for (CANSparkMax controller : all) {
            hasReset = controller.getFault(CANSparkMax.FaultID.kHasReset);
            if (hasReset) break;
        }
        if (hasReset) {
            configCANFrames();
        }
    }

    @Override
    public void periodic() {
        drivetrainTable.getEntry("LeftVelocity").setDouble(leftEncoder.getRate());
        drivetrainTable.getEntry("RightVelocity").setDouble(rightEncoder.getRate());

        drivetrainTable.getEntry("LeftPosition").setDouble(leftEncoder.getDistance());
        drivetrainTable.getEntry("RightPosition").setDouble(rightEncoder.getDistance());

        drivetrainTable.getEntry("LeftPIDVelocity").setDouble(leftVelocityPID.getSetpoint());
        drivetrainTable.getEntry("RightPIDVelocity").setDouble(rightVelocityPID.getSetpoint());

        drivetrainTable.getEntry("LeftAppliedOuput").setDouble(leftLeader.getAppliedOutput());
        drivetrainTable.getEntry("RightAppliedOutput").setDouble(rightLeader.getAppliedOutput());

        drivetrainTable.getEntry("Yaw").setDouble(getYaw());
        drivetrainTable.getEntry("Pitch").setDouble(navX.getPitch());
        drivetrainTable.getEntry("Roll").setDouble(navX.getRoll());

        pose = odometry.update(getGyroAngle(), leftEncoder.getDistance(), rightEncoder.getDistance());

        field.setRobotPose(pose);

        checkForResetAndConfig();
    }

    @Override
    public void simulationPeriodic() {
        driveSim.setInputs(
                leftLeader.getAppliedOutput() * RobotController.getInputVoltage(),
                rightLeader.getAppliedOutput() * RobotController.getInputVoltage());
        driveSim.update(Constants.PERIOD);

        leftEncoderSim.setDistance(driveSim.getLeftPositionMeters());
        leftEncoderSim.setRate(driveSim.getLeftVelocityMetersPerSecond());
        rightEncoderSim.setDistance(driveSim.getRightPositionMeters());
        rightEncoderSim.setRate(driveSim.getRightVelocityMetersPerSecond());

        angle.set(-driveSim.getHeading().getDegrees());
    }

    private static void configureController(CANSparkMax controller) {
        controller.setSmartCurrentLimit(CURRENT_LIMIT_PER_MOTOR);
        controller.setIdleMode(CANSparkMax.IdleMode.kCoast);
        controller.setOpenLoopRampRate(0.05);
        // controller.setOpenLoopRampRate();
        controller.burnFlash();
    }

    /**
     * The Singleton instance of this Drivetrain. Code should use
     * the {@link #getInstance()} method to get the single instance (rather
     * than trying to construct an instance of this class.)
     */
    private static Drivetrain INSTANCE;

    /**
     * Returns the Singleton instance of this Drivetrain. This static method
     * should be used, rather than the constructor, to get the single instance
     * of this class. For example: {@code Drivetrain.getInstance();}
     */
    // @SuppressWarnings("WeakerAccess")
    public static Drivetrain getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Drivetrain();
        }
        return INSTANCE;
    }
}
