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

    private final PIDController controller = new PIDController(KP, KI, KD, Constants.PERIOD);

    private Climber() {

        leftClimb = new DefaultCANSparkMax(LEFT_CLIMBER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        leftClimb.setSmartCurrentLimit(30);
        rightClimb = new DefaultCANSparkMax(RIGHT_CLIMBER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightClimb.setSmartCurrentLimit(30);

        leftClimbEncoder = leftClimb.getEncoder();
        rightClimbEncoder = rightClimb.getEncoder();
    }

    public void setClimberSpeed() {}

    public void Climberup() {}

    public void climberDown() {}

    public void getDifference() {}

    @Override
    public void periodic() {
        super.periodic();
    }

    private static final Climber INSTANCE = new Climber();

    public static Climber getInstance() {
        return INSTANCE;
    }
}
