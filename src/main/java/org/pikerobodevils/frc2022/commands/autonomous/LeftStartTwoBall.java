/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.autonomous;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.pikerobodevils.frc2022.Constants;
import org.pikerobodevils.frc2022.commands.arm.SetArmGoalCommand;
import org.pikerobodevils.frc2022.commands.intake.IntakeInCommand;
import org.pikerobodevils.frc2022.commands.intake.IntakeOutCommand;
import org.pikerobodevils.frc2022.commands.trajectory.EasyRamseteCommand;
import org.pikerobodevils.frc2022.subsystems.Arm;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;
import org.pikerobodevils.frc2022.trajectory.Trajectories;

public class LeftStartTwoBall extends SequentialCommandGroup {

    private final Drivetrain drivetrain = Drivetrain.getInstance();
    Trajectory startToBall =
            Trajectories.generateNamedTrajectory("LeftStartToBall", Constants.TrajectoryConstants.DEFAULT_CONF_FORWARD);
    Trajectory leftBallToHub =
            Trajectories.generateNamedTrajectory("LeftBallToHub", Constants.TrajectoryConstants.DEFAULT_CONF_FORWARD);

    public LeftStartTwoBall() {
        addCommands(new SetArmGoalCommand(Arm.ArmPosition.INTAKE).withTimeout(1));
        addCommands(new EasyRamseteCommand(startToBall, drivetrain, true).raceWith(new IntakeInCommand()));
        addCommands(new InstantCommand(drivetrain::disable));
        addCommands(new SetArmGoalCommand(Arm.ArmPosition.SCORE).withTimeout(1));
        addCommands(new EasyRamseteCommand(leftBallToHub, drivetrain, false));
        addCommands(new InstantCommand(drivetrain::disable));
        addCommands(new IntakeOutCommand().withTimeout(1));
    }
}
