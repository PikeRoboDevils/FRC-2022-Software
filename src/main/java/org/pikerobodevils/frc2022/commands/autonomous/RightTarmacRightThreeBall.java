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

public class RightTarmacRightThreeBall extends SequentialCommandGroup {

    private final Drivetrain drivetrain = Drivetrain.getInstance();
    Trajectory rightTarmacRightToBall = Trajectories.generateNamedTrajectory(
            "RightTarmacRightToBall", Constants.TrajectoryConstants.DEFAULT_CONF_FORWARD);
    Trajectory RightTarmacRightBallToHub = Trajectories.generateNamedQuinticTrajectory(
            "RightTarmacRightBallToHub", Constants.TrajectoryConstants.DEFAULT_CONF_FORWARD);
    Trajectory RightTarmacLeftToNextBall = Trajectories.generateNamedTrajectory(
            "RightTarmacLeftToNextBall", Constants.TrajectoryConstants.DEFAULT_CONF_REVERSE);
    Trajectory RightTarmacLeftNextToHub = Trajectories.generateNamedQuinticTrajectory(
            "RightTarmacLeftNextToHub", Constants.TrajectoryConstants.DEFAULT_CONF_FORWARD);
    Trajectory RightTarmacLeftIntakeBall = Trajectories.generateNamedQuinticTrajectory(
            "RightTarmacLeftIntakeBall", Constants.TrajectoryConstants.DEFAULT_CONF_FORWARD);


    public RightTarmacRightThreeBall(){
        addCommands(new SetArmGoalCommand(Arm.ArmPosition.INTAKE).withTimeout(1));
        addCommands(new EasyRamseteCommand(rightTarmacRightToBall, drivetrain, true).raceWith(new IntakeInCommand()));
        addCommands(new InstantCommand(drivetrain::disable));
        addCommands(new SetArmGoalCommand(Arm.ArmPosition.SCORE).withTimeout(1));
        addCommands(new EasyRamseteCommand(RightTarmacRightBallToHub, drivetrain, false));
        addCommands(new InstantCommand(drivetrain::disable));
        addCommands(new IntakeOutCommand().withTimeout(0.5));
        addCommands(new EasyRamseteCommand(RightTarmacLeftToNextBall, drivetrain, false));
        addCommands(new InstantCommand(drivetrain::disable));
        addCommands(new SetArmGoalCommand(Arm.ArmPosition.INTAKE).withTimeout(1));
        addCommands(new EasyRamseteCommand(RightTarmacLeftIntakeBall, drivetrain, true).raceWith(new IntakeInCommand()));
        addCommands(new InstantCommand(drivetrain::disable));
        addCommands(new SetArmGoalCommand(Arm.ArmPosition.SCORE).withTimeout(1));
        addCommands(new EasyRamseteCommand(RightTarmacLeftNextToHub, drivetrain, false));
        addCommands(new InstantCommand(drivetrain::disable));
        addCommands(new IntakeOutCommand().withTimeout(0.5));
    }
}
