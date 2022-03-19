/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.autonomous;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.pikerobodevils.frc2022.Constants;
import org.pikerobodevils.frc2022.commands.trajectory.EasyRamseteCommand;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;
import org.pikerobodevils.frc2022.trajectory.Trajectories;

public class LeftStartTwoBall extends SequentialCommandGroup {

    private final Drivetrain drivetrain = Drivetrain.getInstance();
    Trajectory startToBall =
            Trajectories.generateNamedTrajectory("LeftStartToBall", Constants.TrajectoryConstants.DEFAULT_CONF_FORWARD);
    Trajectory leftBallToHub =
            Trajectories.generateNamedTrajectory("LeftBallToHub", Constants.TrajectoryConstants.DEFAULT_CONF_FORWARD);

    public LeftStartTwoBall() {
        addCommands(new EasyRamseteCommand(startToBall, drivetrain, true));

        addCommands(new EasyRamseteCommand(leftBallToHub, drivetrain, false));
        var endState = startToBall.getStates().get(startToBall.getStates().size() - 1);
        /*var command = new EasyRamseteCommand(
                AngularTrajectoryGenerator.generateAngularTrajectory(
                        endState.poseMeters,
                        endState.poseMeters.getRotation().rotateBy(Rotation2d.fromDegrees(180)),
                        new TrapezoidProfile.Constraints(Units.degreesToRadians(360), Units.degreesToRadians(360)),
                        Constants.PERIOD),
                drivetrain,
                true);
        addCommands(command);*/
    }
}
