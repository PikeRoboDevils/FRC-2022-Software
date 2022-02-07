/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.subsystems;

import static org.pikerobodevils.frc2022.Constants.DrivetrainConstants.*;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.HashSet;
import java.util.Set;
import org.pikerobodevils.lib.DefaultCANSparkMax;

public class Drivetrain extends SubsystemBase {

    private final CANSparkMax leftLeader, leftFollowerOne, leftFollowerTwo;
    private final CANSparkMax rightLeader, rightFollowerOne, rightFollowerTwo;
    private final Set<CANSparkMax> leftControllers, rightControllers, followers, all;
    private final Encoder leftEncoder, rightEncoder;

    private final AHRS navX;

    private final DifferentialDriveOdometry odometry;

    private final RamseteController pathController = new RamseteController(2, 0.7);

    private Pose2d pose = new Pose2d();

    private final Field2d field = new Field2d();

    /**
     * Creates a new instance of this Drivetrain. This constructor
     * is private since this class is a Singleton. Code should use
     * the {@link #getInstance()} method to get the singleton instance.
     */
    private Drivetrain() {

        leftLeader = new DefaultCANSparkMax(LEFT_LEADER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        leftFollowerOne = new DefaultCANSparkMax(LEFT_FOLLOWER_ONE_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        leftFollowerTwo = new DefaultCANSparkMax(LEFT_FOLLOWER_TWO_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

        rightLeader = new DefaultCANSparkMax(RIGHT_LEADER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightFollowerOne = new DefaultCANSparkMax(RIGHT_FOLLOWER_ONE_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightFollowerTwo = new DefaultCANSparkMax(RIGHT_FOLLOWER_TWO_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

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

        leftControllers = Set.of(leftLeader, leftFollowerOne, leftFollowerTwo);
        rightControllers = Set.of(rightLeader, rightFollowerOne, rightFollowerTwo);
        followers = Set.of(leftFollowerOne, leftFollowerTwo, rightFollowerOne, rightFollowerTwo);
        all = new HashSet<>();
        all.addAll(leftControllers);
        all.addAll(rightControllers);

        configCANFrames();

        leftEncoder = new Encoder(LEFT_ENCODER_A, LEFT_ENCODER_B, false, CounterBase.EncodingType.k4X);
        leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_METERS);
        rightEncoder = new Encoder(RIGHT_ENCODER_A, RIGHT_ENCODER_B, true, CounterBase.EncodingType.k4X);
        rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_METERS);

        navX = new AHRS(SPI.Port.kMXP);
        navX.enableBoardlevelYawReset(true);
        System.out.println(navX.isConnected());
        // Resetting the gyro does not work while the navX is calibrating
        var counter = 0;
        while (navX.isCalibrating()) {
            System.out.println("Waiting on Gyro Calibration...");
            Timer.delay(0.5);
        }

        resetYaw();
        System.out.println("Gyro calibrated, Yaw reset!");

        odometry = new DifferentialDriveOdometry(getGyroAngle());

        SmartDashboard.putData(field);
    }

    public void arcadeDrive(double speed, double rotation) {
        DifferentialDrive.WheelSpeeds speeds = DifferentialDrive.arcadeDriveIK(speed, rotation, false);
        driveOpenLoop(speeds.left, speeds.right);
    }

    public void driveOpenLoop(double left, double right) {
        leftLeader.set(left);
        rightLeader.set(right);
    }

    public void setLeftAndRightVoltage(double leftVoltage, double rightVoltage) {
        leftLeader.setVoltage(leftVoltage);
        rightLeader.setVoltage(rightVoltage);
    }

    public void setIdleMode(CANSparkMax.IdleMode mode) {
        all.forEach(controller -> {
            controller.setIdleMode(mode);
        });
    }

    public Rotation2d getGyroAngle() {
        return navX.getRotation2d();
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

    public SimpleMotorFeedforward getFeedforward() {
        return FEEDFORWARD;
    }

    public DifferentialDriveKinematics getKinematics() {
        return KINEMATICS;
    }

    public RamseteController getPathController() {
        return pathController;
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

    public void displayTrajectory(Trajectory trajectory) {
        field.getObject("traj").setTrajectory(trajectory);
    }

    private void configCANFrames() {
        leftLeader.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus1, 200);
        leftLeader.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 500);

        rightLeader.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus1, 200);
        rightLeader.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 500);

        followers.forEach(follower -> {
            follower.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus0, 500);
            follower.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus1, 500);
            follower.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 500);
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
        SmartDashboard.putBoolean("calibrating", navX.isCalibrating());
        SmartDashboard.putNumber("gyro angle", getGyroAngle().getDegrees());

        SmartDashboard.putNumber("Velocity", (leftEncoder.getRate() + rightEncoder.getRate()) / 2);

        pose = odometry.update(getGyroAngle(), leftEncoder.getDistance(), rightEncoder.getDistance());

        SmartDashboard.putNumber("X", pose.getX());
        SmartDashboard.putNumber("Y", pose.getY());
        SmartDashboard.putNumber("Rotation", pose.getRotation().getDegrees());

        field.setRobotPose(pose);

        checkForResetAndConfig();
    }

    private static void configureController(CANSparkMax controller) {
        controller.setSmartCurrentLimit(CURRENT_LIMIT_PER_MOTOR);
        controller.setIdleMode(CANSparkMax.IdleMode.kCoast);
        controller.burnFlash();
    }

    /**
     * The Singleton instance of this Drivetrain. Code should use
     * the {@link #getInstance()} method to get the single instance (rather
     * than trying to construct an instance of this class.)
     */
    private static final Drivetrain INSTANCE = new Drivetrain();

    /**
     * Returns the Singleton instance of this Drivetrain. This static method
     * should be used, rather than the constructor, to get the single instance
     * of this class. For example: {@code Drivetrain.getInstance();}
     */
    // @SuppressWarnings("WeakerAccess")
    public static Drivetrain getInstance() {
        return INSTANCE;
    }
}
