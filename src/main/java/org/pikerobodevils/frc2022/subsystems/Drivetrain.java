/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.subsystems;

import static org.pikerobodevils.frc2022.Constants.DrivetrainConstants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {

    private final CANSparkMax leftLeader, leftFollowerOne, leftFollowerTwo;
    private final CANSparkMax rightLeader, rightFollowerOne, rightFollowerTwo;
    private final Encoder leftEncoder, rightEncoder;

    /**
     * Creates a new instance of this Drivetrain. This constructor
     * is private since this class is a Singleton. Code should use
     * the {@link #getInstance()} method to get the singleton instance.
     */
    private Drivetrain() {

        leftLeader = new CANSparkMax(LEFT_LEADER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        leftFollowerOne = new CANSparkMax(LEFT_FOLLOWER_ONE_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        leftFollowerTwo = new CANSparkMax(LEFT_FOLLOWER_TWO_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

        rightLeader = new CANSparkMax(RIGHT_LEADER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightFollowerOne = new CANSparkMax(RIGHT_FOLLOWER_ONE_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightFollowerTwo = new CANSparkMax(RIGHT_FOLLOWER_TWO_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

        configureController(leftLeader);

        configureController(leftFollowerOne);
        leftFollowerOne.follow(leftLeader);

        configureController(leftFollowerTwo);
        leftFollowerTwo.follow(leftLeader);

        configureController(rightLeader);
        rightLeader.setInverted(true);

        configureController(rightFollowerOne);
        rightFollowerOne.follow(rightLeader);

        configureController(rightFollowerTwo);
        rightFollowerTwo.follow(rightLeader);

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
