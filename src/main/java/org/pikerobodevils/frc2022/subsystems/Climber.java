/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.subsystems;

import static org.pikerobodevils.frc2022.Constants.ClimberConstants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.pikerobodevils.frc2022.Constants;
import org.pikerobodevils.lib.DefaultCANSparkMax;

public class Climber extends SubsystemBase {

    private final CANSparkMax leftClimb;
    private final CANSparkMax rightClimb;
    private final RelativeEncoder leftClimbEncoder, rightClimbEncoder;

    private final PIDController differentialController = new PIDController(KP, KI, KD, Constants.PERIOD);

    private final double extendSpeed = 1;
    private final double retractSpeed = -1;

    private double desiredSpeed = 0;

    private Climber() {

        leftClimb = new DefaultCANSparkMax(LEFT_CLIMBER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightClimb = new DefaultCANSparkMax(RIGHT_CLIMBER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

        leftClimbEncoder = leftClimb.getEncoder();
        rightClimbEncoder = rightClimb.getEncoder();
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

    public double getDifference() {
        return leftClimbEncoder.getPosition() - rightClimbEncoder.getPosition();
    }

    @Override
    public void periodic() {
        var nominalSpeed = desiredSpeed;
    }

    private static final Climber INSTANCE = new Climber();

    public static Climber getInstance() {
        return INSTANCE;
    }
}
