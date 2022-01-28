/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands;

import static org.pikerobodevils.frc2022.Constants.DrivetrainConstants.*;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;

public class EasyRamseteCommand extends RamseteCommand {
    private final Drivetrain drivetrain;
    private final Trajectory trajectory;

    public EasyRamseteCommand(Trajectory trajectory, Drivetrain drivetrain) {
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

        // each subsystem used by the command must be passed into the
        // addRequirements() method (which takes a vararg of Subsystem)
        addRequirements(this.drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.resetOdometry(trajectory.getInitialPose());
        drivetrain.displayTrajectory(trajectory);
        super.initialize();
    }
}
