/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.subsystems;

import static org.pikerobodevils.frc2022.Constants.DrivetrainConstants.*;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.Set;
import org.pikerobodevils.lib.DefaultCANSparkMax;

public class Drivetrain extends SubsystemBase {

    private final CANSparkMax leftLeader, leftFollowerOne, leftFollowerTwo;
    private final CANSparkMax rightLeader, rightFollowerOne, rightFollowerTwo;
    private final Set<CANSparkMax> leftMotors, rightMotors;
    private final Encoder leftEncoder, rightEncoder;

    private final AHRS navX;

    private final DifferentialDriveOdometry odometry;

    private final DifferentialDriveKinematics kinematics =
            new DifferentialDriveKinematics(Units.inchesToMeters(TRACK_WIDTH_INCHES));

    private Pose2d pose = new Pose2d();

    private Field2d field = new Field2d();

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

        leftMotors = Set.of(leftLeader, leftFollowerOne, leftFollowerTwo);
        rightMotors = Set.of(rightLeader, rightFollowerOne, rightFollowerTwo);

        leftEncoder = new Encoder(LEFT_ENCODER_A, LEFT_ENCODER_B, false, CounterBase.EncodingType.k4X);
        leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_METERS);
        rightEncoder = new Encoder(RIGHT_ENCODER_A, RIGHT_ENCODER_B, true, CounterBase.EncodingType.k4X);
        rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_METERS);
        navX = new AHRS(I2C.Port.kMXP);

        navX.reset();

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

    public Rotation2d getGyroAngle() {
        return Rotation2d.fromDegrees(-navX.getYaw());
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Left encoder", leftEncoder.getDistance());
        SmartDashboard.putNumber("Right encoder", rightEncoder.getDistance());
        SmartDashboard.putBoolean("calibrating", navX.isCalibrating());

        pose = odometry.update(getGyroAngle(), leftEncoder.getDistance(), rightEncoder.getDistance());

        SmartDashboard.putNumber("X", pose.getX());
        SmartDashboard.putNumber("Y", pose.getY());
        SmartDashboard.putNumber("Rotation", pose.getRotation().getDegrees());

        field.setRobotPose(pose);
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
