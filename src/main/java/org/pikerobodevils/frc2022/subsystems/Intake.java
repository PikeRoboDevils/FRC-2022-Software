/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.subsystems;

import static org.pikerobodevils.frc2022.Constants.IntakeConstants.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.pikerobodevils.lib.motorcontrol.DevilTalonSRX;

public class Intake extends SubsystemBase {

    private final DevilTalonSRX intakeMotor;

    private Intake() {

        TalonSRXConfiguration intakeConfig = new TalonSRXConfiguration();
        intakeConfig.continuousCurrentLimit = 40;

        intakeMotor = new DevilTalonSRX(LEADER_ID);

        intakeMotor.configAllSettingsWithRetry(intakeConfig);
        intakeMotor.enableCurrentLimit(true);
        intakeMotor.setNeutralMode(NeutralMode.Brake);

        SmartDashboard.putNumber("Intake In Speed", 1);
        SmartDashboard.putNumber("Intake Out Speed", -1);
    }

    public void setIntakeSpeed(double percent) {
        intakeMotor.set(ControlMode.PercentOutput, percent);
    }

    public void intakeIn() {
        var speed = SmartDashboard.getNumber("Intake In Speed", 0);
        setIntakeSpeed(speed); // -1 to 1 so 50% is 0.5
    }

    public void intakeOut() {
        var speed = SmartDashboard.getNumber("Intake Out Speed", 0);
        setIntakeSpeed(speed);
    }

    public boolean isIntaking() {
        return intakeMotor.getMotorOutputPercent() > 0;
    }

    public boolean isExhausting() {
        return intakeMotor.getMotorOutputPercent() < 0;
    }

    public void disable() {
        setIntakeSpeed(0);
    }

    @Override
    public void periodic() {
        super.periodic();
    }

    private static final Intake INSTANCE = new Intake();

    public static Intake getInstance() {
        return INSTANCE;
    }
}
