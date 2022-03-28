/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.subsystems;

import static org.pikerobodevils.frc2022.Constants.ClimberConstants.*;

import com.revrobotics.*;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.pikerobodevils.lib.DefaultCANSparkMax;

public class Climber extends SubsystemBase {
    private CANSparkMax leftClimber = new DefaultCANSparkMax(LEFT_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANSparkMax rightClimber = new DefaultCANSparkMax(RIGHT_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

    private RelativeEncoder leftEncoder = leftClimber.getEncoder();
    private RelativeEncoder rightEncoder = rightClimber.getEncoder();

    private SparkMaxPIDController leftController = leftClimber.getPIDController();
    private SparkMaxPIDController rightController = rightClimber.getPIDController();

    private NetworkTable climberDataTable = NetworkTableInstance.getDefault().getTable("Climber");

    private double kP = KP_HOLD;

    private Climber() {
        leftClimber.restoreFactoryDefaults();
        leftClimber.setIdleMode(CANSparkMax.IdleMode.kBrake);
        leftClimber.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, 130);
        leftClimber.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
        leftClimber.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, 0);
        leftClimber.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);

        leftClimber.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 10);

        leftController.setP(kP);

        leftClimber.burnFlash();

        leftEncoder.setPosition(0);

        rightClimber.restoreFactoryDefaults();
        rightClimber.setInverted(true);
        rightClimber.setIdleMode(CANSparkMax.IdleMode.kBrake);
        rightClimber.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, 130);
        rightClimber.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
        rightClimber.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, 0);
        rightClimber.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);

        rightClimber.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 10);

        rightController.setP(kP);

        rightClimber.burnFlash();

        rightEncoder.setPosition(0);

        climberDataTable.getEntry("kP").setDefaultDouble(kP);
    }

    public void setVoltage(double voltage) {
        leftClimber.setVoltage(voltage);
        rightClimber.setVoltage(voltage);
    }

    public void enableSoftLimits(boolean enable) {
        leftClimber.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, enable);
        rightClimber.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, enable);
        leftClimber.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, enable);
        rightClimber.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, enable);
    }

    public void resetEncoders() {
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }

    public double getLeftPosition() {
        return leftEncoder.getPosition();
    }

    public double getRightPosition() {
        return rightEncoder.getPosition();
    }

    public void setSpeed(double speed) {
        leftClimber.set(speed);
        rightClimber.set(speed);
    }

    public void holdPosition() {
        leftController.setReference(leftEncoder.getPosition(), CANSparkMax.ControlType.kPosition);
        rightController.setReference(rightEncoder.getPosition(), CANSparkMax.ControlType.kPosition);
    }

    public void updateTelemetry() {
        climberDataTable.getEntry("LeftPosition").setDouble(leftEncoder.getPosition());
        climberDataTable.getEntry("RightPosition").setDouble(rightEncoder.getPosition());
        climberDataTable.getEntry("LeftAppliedOutput").setDouble(leftClimber.getAppliedOutput());
        climberDataTable.getEntry("RightAppliedOutput").setDouble(rightClimber.getAppliedOutput());
        climberDataTable.getEntry("LeftOutputCurrent").setDouble(leftClimber.getOutputCurrent());
        climberDataTable.getEntry("RightOutputCurrent").setDouble(rightClimber.getOutputCurrent());

        double ntkP = climberDataTable.getEntry("kP").getDouble(0);
        if (ntkP != kP) {
            leftController.setP(ntkP);
            rightController.setP(ntkP);
            kP = ntkP;
        }
    }

    @Override
    public void periodic() {
        updateTelemetry();
    }

    private static Climber INSTANCE;

    public static Climber getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Climber();
        }
        return INSTANCE;
    }
}
