/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.trajectory;

import static org.pikerobodevils.frc2022.Constants.DrivetrainConstants.*;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import org.pikerobodevils.frc2022.DriverDashboard;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;

public class EasyRamseteCommand extends RamseteCommand {
    private final Drivetrain drivetrain;
    public final Trajectory trajectory;

    private final boolean resetOdometry;

    public EasyRamseteCommand(Trajectory trajectory, Drivetrain drivetrain, boolean resetOdometry) {
        super(
                trajectory,
                drivetrain::getPose,
                drivetrain.getPathController(),
                drivetrain.getFeedforward(),
                drivetrain.getKinematics(),
                drivetrain::getWheelSpeeds,
                drivetrain.getLeftVelocityPID(),
                drivetrain.getRightVelocityPID(),
                drivetrain::setLeftAndRightVoltage);
        this.trajectory = trajectory;
        this.drivetrain = drivetrain;
        this.resetOdometry = resetOdometry;

        // each subsystem used by the command must be passed into the
        // addRequirements() method (which takes a vararg of Subsystem)
        addRequirements(this.drivetrain);
    }

    public Command disableWhenFinished() {
        return this.andThen(drivetrain::disable);
    }

    @Override
    public void initialize() {
        drivetrain.resetPID();
        if (resetOdometry) {
            drivetrain.resetOdometry(trajectory.getInitialPose());
        }
        DriverDashboard.getInstance().displayTrajectory(trajectory);
        super.initialize();
    }
}
