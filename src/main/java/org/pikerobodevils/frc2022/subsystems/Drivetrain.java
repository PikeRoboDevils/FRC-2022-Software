/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.subsystems;

import static org.pikerobodevils.frc2022.Constants.DrivetrainConstants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.Set;
import org.pikerobodevils.lib.DefaultCANSparkMax;

public class Drivetrain extends SubsystemBase {

    private final CANSparkMax leftLeader, leftFollowerOne, leftFollowerTwo;
    private final CANSparkMax rightLeader, rightFollowerOne, rightFollowerTwo;
    private final Set<CANSparkMax> leftMotors, rightMotors;
    private final Encoder leftEncoder, rightEncoder;

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
        rightEncoder = new Encoder(RIGHT_ENCODER_A, RIGHT_ENCODER_B, true, CounterBase.EncodingType.k4X);
    }

    public void setLeftAndRightVoltage(double leftVoltage, double rightVoltage) {
        leftLeader.setVoltage(leftVoltage);
        rightLeader.setVoltage(rightVoltage);
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
