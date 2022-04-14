/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.autonomous;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
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

    static Trajectory RightTarmacLeftToNextBall = Trajectories.generateNamedTrajectory(
            "RightTarmacLeftToNextBall", Constants.TrajectoryConstants.DEFAULT_CONF_REVERSE);
    static Trajectory RightTarmacLeftNextToHub = Trajectories.generateNamedTrajectory(
            "RightTarmacLeftNextToHub", Constants.TrajectoryConstants.DEFAULT_CONF_FORWARD);
    static Trajectory RightTarmacLeftIntakeBall = Trajectories.generateNamedQuinticTrajectory(
            "RightTarmacLeftIntakeBall", Constants.TrajectoryConstants.DEFAULT_CONF_FORWARD);

    public RightTarmacRightThreeBall() {
        addCommands(new RightTarmacRightTwoBall());
        // Back  and lower the intake 0.1s after starting
        addCommands(new EasyRamseteCommand(RightTarmacLeftToNextBall, drivetrain, false)
                .disableWhenFinished()
                .alongWith(new WaitCommand(0.1).andThen(new SetArmGoalCommand(Arm.ArmPosition.INTAKE).withTimeout(1))));
        addCommands(new EasyRamseteCommand(RightTarmacLeftIntakeBall, drivetrain, false)
                .disableWhenFinished()
                .raceWith(new IntakeInCommand()));
        addCommands(new SetArmGoalCommand(Arm.ArmPosition.SCORE).dontWait());
        addCommands(new EasyRamseteCommand(RightTarmacLeftNextToHub, drivetrain, false)
                .disableWhenFinished()
                .raceWith(new WaitCommand(RightTarmacLeftNextToHub.getTotalTimeSeconds() - 0.5)
                        .andThen(new IntakeOutCommand())));
        addCommands(new IntakeOutCommand().withTimeout(1));
    }
}
