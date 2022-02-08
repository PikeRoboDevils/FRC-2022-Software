package org.pikerobodevils.frc2022.subsystems;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static org.pikerobodevils.frc2022.Constants.ClimberConstants.*;

public class Climber extends SubsystemBase{   
    private final CANSparkMax climberLeader, climberFollower;

    private Climber(){
        climberLeader = new CANSparkMax(CLIMBER_LEADER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        climberFollower = new CANSparkMax(CLIMBER_FOLLOWER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        climberFollower.follow(climberLeader);

        climberLeader.restoreFactoryDefaults();
    }

    public void raiseClimber(double speed){
        climberLeader.set(speed);
    }

    
    private static final Climber INSTANCE = new Climber();

    public static Climber getInstance() {
        return INSTANCE;
    }

}
