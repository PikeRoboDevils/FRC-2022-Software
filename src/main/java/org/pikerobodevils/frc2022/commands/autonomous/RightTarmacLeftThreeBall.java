/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.autonomous;

import static org.pikerobodevils.frc2022.Constants.TrajectoryConstants.DEFAULT_CONF_FORWARD;
import static org.pikerobodevils.frc2022.Constants.TrajectoryConstants.DEFAULT_CONF_REVERSE;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import org.pikerobodevils.frc2022.commands.arm.SetArmGoalCommand;
import org.pikerobodevils.frc2022.commands.intake.IntakeInCommand;
import org.pikerobodevils.frc2022.commands.intake.IntakeOutCommand;
import org.pikerobodevils.frc2022.commands.trajectory.EasyRamseteCommand;
import org.pikerobodevils.frc2022.subsystems.Arm;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;
import org.pikerobodevils.frc2022.trajectory.Trajectories;

public class RightTarmacLeftThreeBall extends SequentialCommandGroup {
    static Trajectory rightHubToRightBall1 =
            Trajectories.generateNamedTrajectory("RightHubToRightBall1", DEFAULT_CONF_REVERSE);
    static Trajectory rightHubToRightBall2 =
            Trajectories.generateNamedTrajectory("RightHubToRightBall2", DEFAULT_CONF_FORWARD);
    static Trajectory rightBallToRightHub =
            Trajectories.generateNamedQuinticTrajectory("RightBallToRightHub", DEFAULT_CONF_FORWARD);
    static Trajectory rightHubBackUp = Trajectories.generateNamedTrajectory("RightHubBackUp", DEFAULT_CONF_REVERSE);

    public RightTarmacLeftThreeBall() {
        addCommands(new RightTarmacLeftTwoBall());
        addCommands(new EasyRamseteCommand(rightHubToRightBall1, Drivetrain.getInstance(), false)
                .disableWhenFinished()
                .alongWith(new WaitCommand(1).andThen(new SetArmGoalCommand(Arm.ArmPosition.INTAKE).withTimeout(0.5))));
        addCommands(new EasyRamseteCommand(rightHubToRightBall2, Drivetrain.getInstance(), false)
                .disableWhenFinished()
                .raceWith(new IntakeInCommand()));
        addCommands(new SetArmGoalCommand(Arm.ArmPosition.SCORE).withTimeout(1));
        addCommands(new EasyRamseteCommand(rightBallToRightHub, Drivetrain.getInstance(), false).disableWhenFinished());
        addCommands(new IntakeOutCommand().withTimeout(1));
        // addCommands(new EasyRamseteCommand(rightHubBackUp, Drivetrain.getInstance(), false).disableWhenFinished());
    }
}
