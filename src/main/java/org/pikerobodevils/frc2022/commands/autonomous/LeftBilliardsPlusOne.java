/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.autonomous;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import org.pikerobodevils.frc2022.Constants;
import org.pikerobodevils.frc2022.commands.arm.SetArmGoalCommand;
import org.pikerobodevils.frc2022.commands.intake.IntakeInCommand;
import org.pikerobodevils.frc2022.commands.trajectory.EasyRamseteCommand;
import org.pikerobodevils.frc2022.subsystems.Arm;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;
import org.pikerobodevils.frc2022.trajectory.Trajectories;

public class LeftBilliardsPlusOne extends SequentialCommandGroup {
    static Trajectory billiardsSecondBall = Trajectories.generateNamedTrajectory(
            "BilliardsSecondBall", Constants.TrajectoryConstants.DEFAULT_CONF_FORWARD);

    public LeftBilliardsPlusOne() {
        addCommands(new BilliardsLeftTarmac());
        addCommands(new EasyRamseteCommand(billiardsSecondBall, Drivetrain.getInstance(), false)
                .disableWhenFinished()
                .raceWith(new IntakeInCommand())
                .alongWith(new WaitCommand(billiardsSecondBall.getTotalTimeSeconds() * 0.5)
                        .andThen(new SetArmGoalCommand(Arm.ArmPosition.SCORE))));
    }
}
