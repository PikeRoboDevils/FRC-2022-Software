/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.autonomous;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import org.pikerobodevils.frc2022.Constants;
import org.pikerobodevils.frc2022.commands.trajectory.EasyRamseteCommand;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;
import org.pikerobodevils.frc2022.trajectory.Trajectories;

public class UniversalDriveBackAutonomous extends SequentialCommandGroup {

    Trajectory driveBackPath = Trajectories.generateNamedTrajectory(
            "UniversalDriveBack", Constants.TrajectoryConstants.DEFAULT_CONF_REVERSE);

    public UniversalDriveBackAutonomous() {

        addCommands(new InstantCommand(() -> Drivetrain.getInstance().setIdleMode(CANSparkMax.IdleMode.kBrake)));
        addCommands(new EasyRamseteCommand(driveBackPath, Drivetrain.getInstance(), true));
        addCommands(new InstantCommand(() -> Drivetrain.getInstance().setLeftAndRightVoltage(0, 0)));
        addCommands(new WaitUntilCommand(Drivetrain.getInstance()::isStopped));
        addCommands(new InstantCommand(() -> Drivetrain.getInstance().setIdleMode(CANSparkMax.IdleMode.kCoast)));
    }
}
