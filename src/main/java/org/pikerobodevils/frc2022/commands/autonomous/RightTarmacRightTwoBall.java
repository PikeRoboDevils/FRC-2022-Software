/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.autonomous;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.pikerobodevils.frc2022.Constants;
import org.pikerobodevils.frc2022.commands.arm.SetArmGoalCommand;
import org.pikerobodevils.frc2022.commands.intake.IntakeInCommand;
import org.pikerobodevils.frc2022.commands.intake.IntakeOutCommand;
import org.pikerobodevils.frc2022.commands.trajectory.EasyRamseteCommand;
import org.pikerobodevils.frc2022.subsystems.Arm;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;
import org.pikerobodevils.frc2022.trajectory.Trajectories;

public class RightTarmacRightTwoBall extends SequentialCommandGroup {
    Drivetrain drivetrain = Drivetrain.getInstance();

    static Trajectory rightTarmacRightToBall = Trajectories.generateNamedTrajectory(
            "RightTarmacRightToBall", Constants.TrajectoryConstants.DEFAULT_CONF_FORWARD);
    static Trajectory rightTarmacRightBallToHub = Trajectories.generateNamedQuinticTrajectory(
            "RightTarmacRightBallToHub", Constants.TrajectoryConstants.DEFAULT_CONF_FORWARD);

    public RightTarmacRightTwoBall(Command... commands) {
        addRequirements(drivetrain);
        addCommands(new SetArmGoalCommand(Arm.ArmPosition.INTAKE).withTimeout(1));
        addCommands(new EasyRamseteCommand(rightTarmacRightToBall, drivetrain, true)
                .disableWhenFinished()
                .raceWith(new IntakeInCommand())
                .andThen(new IntakeInCommand().withTimeout(0.5)));
        addCommands(new SetArmGoalCommand(Arm.ArmPosition.SCORE).withTimeout(1));
        addCommands(new EasyRamseteCommand(rightTarmacRightBallToHub, drivetrain, false).disableWhenFinished());
        addCommands(new IntakeOutCommand().withTimeout(0.5));
    }
}
