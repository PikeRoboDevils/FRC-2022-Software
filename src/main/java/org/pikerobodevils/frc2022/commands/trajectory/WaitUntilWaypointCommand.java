package org.pikerobodevils.frc2022.commands.trajectory;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;

public class WaitUntilWaypointCommand extends WaitUntilCommand {
    public WaitUntilWaypointCommand(Translation2d translation2d, double range) {
        super(() -> Drivetrain.getInstance().isWithinRange(translation2d, range));
    }
}
