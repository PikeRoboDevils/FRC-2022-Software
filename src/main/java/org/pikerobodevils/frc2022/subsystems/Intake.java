/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.subsystems;

import static org.pikerobodevils.frc2022.Constants.IntakeConstants.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class Intake extends SubsystemBase {

    private final TalonSRX intakeLeader, intakeFollower;

    private Intake() {
        intakeLeader = new TalonSRX(LEADER_ID);
        intakeFollower = new TalonSRX(FOLLOWER_ID);

        intakeFollower.follow(intakeLeader);
        intakeFollower.setInverted(InvertType.OpposeMaster);
    }

    public void intakeIn() {
        setIntakeSpeed(0.5); // -1 to 1 so 50% is 0.5
    }

    public void setIntakeSpeed(double percent) {
        intakeLeader.set(ControlMode.PercentOutput, percent);
    }

    private static final Intake INSTANCE = new Intake();

    public static Intake getInstance() {
        return INSTANCE;
    }
}
