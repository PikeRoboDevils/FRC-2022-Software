/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.subsystems;

import static org.pikerobodevils.frc2022.Constants.ClimberConstants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
    private final CANSparkMax climberLeader, climberFollower;

    private Climber() {
        climberLeader = new CANSparkMax(CLIMBER_LEADER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        climberFollower = new CANSparkMax(CLIMBER_FOLLOWER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

        climberLeader.restoreFactoryDefaults();
        climberFollower.restoreFactoryDefaults();

        climberFollower.follow(climberLeader);
        climberFollower.setInverted(true);
    }

    public void setClimberSpeed(double speed) {
        climberLeader.setVoltage(speed * RobotController.getBatteryVoltage());
    }

    public void extendClimber() {
        setClimberSpeed(1);
    }

    public void retractClimber() {
        setClimberSpeed(-1);
    }

    public void disable() {
        setClimberSpeed(0);
    }

    private static final Climber INSTANCE = new Climber();

    public static Climber getInstance() {
        return INSTANCE;
    }
}
