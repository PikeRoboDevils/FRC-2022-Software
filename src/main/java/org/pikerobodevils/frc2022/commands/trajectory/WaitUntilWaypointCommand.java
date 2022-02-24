/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.trajectory;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import org.pikerobodevils.frc2022.Constants;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;

public class WaitUntilWaypointCommand extends WaitUntilCommand {
    public WaitUntilWaypointCommand(Translation2d waypoint, double range) {
        super(() -> Drivetrain.getInstance().isWithinRange(waypoint, range));
    }

    public WaitUntilWaypointCommand(Translation2d waypoint) {
        this(waypoint, Constants.AutoConstants.DEFAULT_ACTION_RANGE);
    }
}
