/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.subsystems;

import static org.pikerobodevils.frc2022.Constants.ClimberConstants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.pikerobodevils.frc2022.Constants;
import org.pikerobodevils.lib.DefaultCANSparkMax;

public class Climber extends SubsystemBase {

    private final CANSparkMax leftClimb =
            new DefaultCANSparkMax(LEFT_CLIMBER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax rightClimb =
            new DefaultCANSparkMax(RIGHT_CLIMBER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final RelativeEncoder leftClimbEncoder = leftClimb.getEncoder();
    private final RelativeEncoder rightClimbEncoder = rightClimb.getEncoder();

    //private final PIDController positionController = new PIDController(KP_POSITION, KI_POSITION, KD_POSITION);
    private final PIDController differentialController = new PIDController(KP_DIFFERENTIAL, KI_DIFFERENTIAL, KD_DIFFERENTIAL, Constants.PERIOD);

    private final NetworkTable climberDataTable =
            NetworkTableInstance.getDefault().getTable("Climber");

    private final double extendSpeed = 1;
    private final double retractSpeed = -1;

    private double leftSpeed = 0, rightSpeed = 0;

    private double desiredSpeed = 0;

    
    private enum ClimberState {
        IDLE,
        EXTEND,
        RETRACT_CLIMB,
        HOLD
    }

    private Climber() {

        leftClimb.setIdleMode(CANSparkMax.IdleMode.kBrake);
        leftClimb.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, 130);
        leftClimb.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, 0);
        leftClimb.burnFlash();

        rightClimb.setIdleMode(CANSparkMax.IdleMode.kBrake);
        rightClimb.setInverted(true);
        rightClimb.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, 130);
        rightClimb.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, 0);
        rightClimb.burnFlash();

        differentialController.setSetpoint(0);
        initTelemetry();
    }

    public void setClimberSpeed(double speed) {
        desiredSpeed = speed;
    }

    public void extendClimber() {
        setClimberSpeed(extendSpeed);
    }

    public void retractClimber() {
        setClimberSpeed(retractSpeed);
    }

    public void setLeftRightMotorVoltage(double left, double right) {
        leftClimb.setVoltage(left);
        rightClimb.setVoltage(right);
    }

    public double getDifference() {
        return leftClimbEncoder.getPosition() - rightClimbEncoder.getPosition();
    }

    @Override
    public void periodic() {
        desiredSpeed = climberDataTable.getEntry("DesiredSpeed").getDouble(0);
        var nominalSpeed = desiredSpeed;

        var correctionOutput = differentialController.calculate(getDifference());
        climberDataTable.getEntry("Correction").setDouble(correctionOutput);

        var leftSpeed = nominalSpeed + 0.5 * correctionOutput;
        var rightSpeed = nominalSpeed - 0.5 * correctionOutput;

        // normalize speeds to [-1,1] while maintaining the same correction
        if (Math.abs(leftSpeed) > 1) {
            var direction = Math.signum(nominalSpeed);
            var overage = Math.abs(leftSpeed) - 1;
            leftSpeed = 1 * direction;
            rightSpeed = rightSpeed - (direction * overage);
        } else if (Math.abs(rightSpeed) > 1) {
            var direction = Math.signum(nominalSpeed);
            var overage = Math.abs(rightSpeed) - 1;
            rightSpeed = 1 * direction;
            leftSpeed = leftSpeed - (direction * overage);
        }

        this.leftSpeed = leftSpeed;
        this.rightSpeed = rightSpeed;

        setLeftRightMotorVoltage(leftSpeed * 11, rightSpeed * 11);

        updateTelemetry();
    }

    /**
     * Initializes any telemetry entries that need to be read from
     */
    private void initTelemetry() {
        climberDataTable.getEntry("DesiredSpeed").setDefaultDouble(0);
    }

    /**
     * Sends telemetry data to NetworkTables.
     */
    private void updateTelemetry() {
        climberDataTable.getEntry("LeftSpeed").setDouble(leftSpeed);
        climberDataTable.getEntry("RightSpeed").setDouble(rightSpeed);

        climberDataTable.getEntry("LeftPosition").setDouble(leftClimbEncoder.getPosition());
        climberDataTable.getEntry("RightPosition").setDouble(rightClimbEncoder.getPosition());



    }

    private static final Climber INSTANCE = new Climber();

    public static Climber getInstance() {
        return INSTANCE;
    }
}
