/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.subsystems;

import static org.pikerobodevils.frc2022.Constants.ArmConstants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase {
    private final CANSparkMax armLeader;
    private final CANSparkMax armFollower;
    // With eager singleton initialization, any static variables/fields used in the
    // constructor must appear before the "INSTANCE" variable so that they are initialized
    // before the constructor is called when the "INSTANCE" variable initializes.

    /**
     * Creates a new instance of this Arm. This constructor
     * is private since this class is a Singleton. Code should use
     * the {@link #getInstance()} method to get the singleton instance.
     */
    private Arm() {
        armLeader = new CANSparkMax(ARM_LEADER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        armFollower = new CANSparkMax(ARM_FOLLOWER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
    }

    /**
     * The Singleton instance of this Arm. Code should use
     * the {@link #getInstance()} method to get the single instance (rather
     * than trying to construct an instance of this class.)
     */
    private static final Arm INSTANCE = new Arm();

    /**
     * Returns the Singleton instance of this Arm. This static method
     * should be used, rather than the constructor, to get the single instance
     * of this class. For example: {@code Arm.getInstance();}
     */
    @SuppressWarnings("WeakerAccess")
    public static Arm getInstance() {
        return INSTANCE;
    }
}
