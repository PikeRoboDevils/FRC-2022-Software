/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.trajectory;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class RunAtWayPointCommand extends SequentialCommandGroup {
    public RunAtWayPointCommand(Translation2d waypoint, Command command) {
        addCommands(new WaitUntilWaypointCommand(waypoint), command);
    }

    public RunAtWayPointCommand(Translation2d waypoint, Runnable toRun, Subsystem... requirements) {
        this(waypoint, new InstantCommand(toRun, requirements));
    }
}
