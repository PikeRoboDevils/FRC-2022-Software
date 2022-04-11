/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.subsystems;

import static org.pikerobodevils.frc2022.Constants.ClimberConstants.*;
import static org.pikerobodevils.lib.motorcontrol.DevilCANSparkMax.check;

import com.revrobotics.*;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.pikerobodevils.lib.motorcontrol.DevilCANSparkMax;

public class Climber extends SubsystemBase {
    private final DevilCANSparkMax leftClimber =
            new DevilCANSparkMax(LEFT_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final DevilCANSparkMax rightClimber =
            new DevilCANSparkMax(RIGHT_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

    private final RelativeEncoder leftEncoder = leftClimber.getEncoder();
    private final RelativeEncoder rightEncoder = rightClimber.getEncoder();

    private final SparkMaxPIDController leftController = leftClimber.getPIDController();
    private final SparkMaxPIDController rightController = rightClimber.getPIDController();

    private NetworkTable climberDataTable = NetworkTableInstance.getDefault().getTable("Climber");

    private double kP = KP_HOLD;

    private Climber() {
        leftClimber.initialize(this::initLeftController);
        rightClimber.initialize(this::initRightController);

        climberDataTable.getEntry("kP").setDefaultDouble(kP);
    }

    private boolean initCommon(CANSparkMax controller) {
        boolean ok = true;
        ok &= check(controller.restoreFactoryDefaults());
        ok &= check(controller.setIdleMode(CANSparkMax.IdleMode.kBrake));
        ok &= check(controller.setOpenLoopRampRate(RAMP_RATE));
        ok &= check(controller.setSmartCurrentLimit(60));
        ok &= check(controller.setSecondaryCurrentLimit(90));
        ok &= check(controller.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, 94));
        ok &= check(controller.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true));
        ok &= check(controller.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, 0));
        ok &= check(controller.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true));

        ok &= check(controller.setPeriodicFramePeriod(
                CANSparkMaxLowLevel.PeriodicFrame.kStatus2, PERIODIC_STATUS_2_PERIOD));

        ok &= check(controller.getEncoder().setPosition(0));

        ok &= check(controller.getPIDController().setP(kP));

        ok &= check(controller.burnFlash());

        return ok;
    }

    private boolean initLeftController(CANSparkMax controller) {
        boolean ok = true;
        ok &= initCommon(controller);

        return ok;
    }

    private boolean initRightController(CANSparkMax controller) {
        boolean ok = true;
        ok &= initCommon(controller);
        controller.setInverted(true);
        ok &= check(controller.burnFlash());
        return ok;
    }

    private boolean setFramePeriods(CANSparkMax controller) {
        boolean ok = true;
        ok &= check(controller.setPeriodicFramePeriod(
                CANSparkMaxLowLevel.PeriodicFrame.kStatus2, PERIODIC_STATUS_2_PERIOD));
        return ok;
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

    public void setPositionClosedLoop(double position) {
        leftController.setReference(position, CANSparkMax.ControlType.kPosition);
        rightController.setReference(position, CANSparkMax.ControlType.kPosition);
    }

    public boolean isFullyRetracted() {
        // return rightClimber.getFault(CANSparkMax.FaultID.kSoftLimitRev) &&
        // leftClimber.getFault(CANSparkMax.FaultID.kSoftLimitRev);
        return rightEncoder.getPosition() < 5 && leftEncoder.getPosition() < 5;
    }

    private void updateTelemetry() {
        climberDataTable.getEntry("LeftPosition").setDouble(leftEncoder.getPosition());
        climberDataTable.getEntry("RightPosition").setDouble(rightEncoder.getPosition());
        climberDataTable.getEntry("LeftAppliedOutput").setDouble(leftClimber.getAppliedOutput());
        climberDataTable.getEntry("RightAppliedOutput").setDouble(rightClimber.getAppliedOutput());
        climberDataTable.getEntry("LeftOutputCurrent").setDouble(leftClimber.getOutputCurrent());
        climberDataTable.getEntry("RightOutputCurrent").setDouble(rightClimber.getOutputCurrent());
        climberDataTable.getEntry("FullyRetracted").setBoolean(isFullyRetracted());

        double ntkP = climberDataTable.getEntry("kP").getDouble(0);
        if (ntkP != kP) {
            leftController.setP(ntkP);
            rightController.setP(ntkP);
            kP = ntkP;
        }
    }

    @Override
    public void periodic() {
        leftClimber.ifHasReset(this::setFramePeriods);
        rightClimber.ifHasReset(this::setFramePeriods);

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
