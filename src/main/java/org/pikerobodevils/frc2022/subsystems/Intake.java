/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.subsystems;

import static org.pikerobodevils.frc2022.Constants.IntakeConstants.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {

    private final TalonSRX intakeLeader, intakeFollower;

    private Intake() {
        intakeLeader = new WPI_TalonSRX(LEADER_ID);
        intakeFollower = new WPI_TalonSRX(FOLLOWER_ID);

        intakeLeader.configFactoryDefault();
        intakeFollower.configFactoryDefault();

        intakeFollower.follow(intakeLeader);
        intakeFollower.setInverted(InvertType.OpposeMaster);

        SmartDashboard.putNumber("Intake In Speed", 1);
        SmartDashboard.putNumber("Intake Out Speed", -1);
    }

    public void setIntakeSpeed(double percent) {
        intakeLeader.set(ControlMode.PercentOutput, percent);
    }

    public void intakeIn() {
        var speed = SmartDashboard.getNumber("Intake In Speed", 0);
        setIntakeSpeed(speed); // -1 to 1 so 50% is 0.5
    }

    public void intakeOut() {
        var speed = SmartDashboard.getNumber("Intake Out Speed", 0);
        setIntakeSpeed(speed);
    }

    public void disable() {
        setIntakeSpeed(0);
    }

    private static final Intake INSTANCE = new Intake();

    public static Intake getInstance() {
        return INSTANCE;
    }
}
