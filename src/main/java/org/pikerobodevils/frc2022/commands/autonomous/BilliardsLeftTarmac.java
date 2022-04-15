/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.autonomous;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.pikerobodevils.frc2022.Constants;
import org.pikerobodevils.frc2022.commands.arm.SetArmGoalCommand;
import org.pikerobodevils.frc2022.commands.intake.IntakeOutCommand;
import org.pikerobodevils.frc2022.commands.trajectory.EasyRamseteCommand;
import org.pikerobodevils.frc2022.subsystems.Arm;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;
import org.pikerobodevils.frc2022.trajectory.Trajectories;

public class BilliardsLeftTarmac extends SequentialCommandGroup {

    private final Drivetrain drivetrain = Drivetrain.getInstance();
    static Trajectory billiardsLeftTarmacExit = Trajectories.generateNamedTrajectory(
            "BilliardsLeftTarmacExit", Constants.TrajectoryConstants.DEFAULT_CONF_REVERSE);

    public BilliardsLeftTarmac() {
        addCommands(new SetArmGoalCommand(Arm.ArmPosition.INTAKE).withTimeout(1));
        addCommands(new IntakeOutCommand().withTimeout(1));
        addCommands(new EasyRamseteCommand(billiardsLeftTarmacExit, drivetrain, true).disableWhenFinished());
    }
}
