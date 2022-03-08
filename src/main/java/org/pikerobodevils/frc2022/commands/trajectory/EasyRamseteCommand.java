/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.trajectory;

import static org.pikerobodevils.frc2022.Constants.DrivetrainConstants.*;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import org.pikerobodevils.frc2022.DriverDashboard;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;

public class EasyRamseteCommand extends RamseteCommand {
    private final Drivetrain drivetrain;
    private final Trajectory trajectory;

    private final boolean resetOdometry;

    public EasyRamseteCommand(Trajectory trajectory, Drivetrain drivetrain, boolean resetOdometry) {
        super(
                trajectory,
                drivetrain::getPose,
                drivetrain.getPathController(),
                drivetrain.getFeedforward(),
                drivetrain.getKinematics(),
                drivetrain::getWheelSpeeds,
                new PIDController(KP_VELOCITY, 0, 0),
                new PIDController(KP_VELOCITY, 0, 0),
                drivetrain::setLeftAndRightVoltage);
        this.trajectory = trajectory;
        this.drivetrain = drivetrain;
        this.resetOdometry = resetOdometry;

        // each subsystem used by the command must be passed into the
        // addRequirements() method (which takes a vararg of Subsystem)
        addRequirements(this.drivetrain);
    }

    @Override
    public void initialize() {

        if (resetOdometry) {
            drivetrain.resetOdometry(trajectory.getInitialPose());
        }
        DriverDashboard.getInstance().displayTrajectory(trajectory);
        super.initialize();
    }
}
