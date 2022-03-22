/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.trajectory;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;

public class FollowTrajectoryCommand extends CommandBase {

    Drivetrain drivetrain = Drivetrain.getInstance();

    private final Trajectory trajectory;
    private final Timer timer = new Timer();

    public FollowTrajectoryCommand(final Trajectory toFollow) {
        this.trajectory = toFollow;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        timer.reset();
        drivetrain.resetPID();
    }

    @Override
    public void execute() {
        super.execute();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
    }

    @Override
    public boolean isFinished() {
        return super.isFinished();
    }
}
