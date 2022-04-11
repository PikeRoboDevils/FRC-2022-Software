/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.autonomous;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import java.util.function.Supplier;
import org.pikerobodevils.frc2022.Constants;
import org.pikerobodevils.frc2022.commands.trajectory.EasyRamseteCommand;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;
import org.pikerobodevils.frc2022.trajectory.Trajectories;

public class RunPathCommand extends CommandBase {
    Supplier<String> pathSupplier;

    Command trajectoryCommand;

    public RunPathCommand(Supplier<String> toRun) {
        pathSupplier = toRun;
        addRequirements(Drivetrain.getInstance());
    }

    @Override
    public void initialize() {
        var trajectory = Trajectories.generateNamedTrajectory(
                pathSupplier.get(), Constants.TrajectoryConstants.DEFAULT_CONF_FORWARD);
        trajectoryCommand = new EasyRamseteCommand(trajectory, Drivetrain.getInstance(), true);
        trajectoryCommand.initialize();
    }

    @Override
    public void execute() {
        trajectoryCommand.execute();
    }

    @Override
    public void end(boolean interrupted) {
        trajectoryCommand.end(interrupted);
    }

    @Override
    public boolean isFinished() {
        return trajectoryCommand.isFinished();
    }
}
