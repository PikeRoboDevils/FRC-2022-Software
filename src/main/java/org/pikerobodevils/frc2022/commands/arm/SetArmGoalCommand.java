/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.arm;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.pikerobodevils.frc2022.subsystems.Arm;

public class SetArmGoalCommand extends CommandBase {
    private final double goalPosition;

    private final Arm arm = Arm.getInstance();

    private final Timer timer = new Timer();

    public SetArmGoalCommand(double goal) {
        setName(String.format(getClass().getSimpleName() + "(%f)", goal));
        addRequirements(arm);
        this.goalPosition = goal;
    }

    public SetArmGoalCommand(Arm.ArmPosition goal) {
        this(goal.position);
        setName(String.format(getClass().getSimpleName() + "(%s)", goal.name()));
    }

    @Override
    public void initialize() {
        arm.setGoal(goalPosition);
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {}

    @Override
    public void end(boolean interrupted) {
        timer.stop();
    }

    @Override
    public boolean isFinished() {
        if (RobotBase.isReal()) {
            return arm.atGoal();
        } else {
            return timer.hasElapsed(2);
        }
    }
}
