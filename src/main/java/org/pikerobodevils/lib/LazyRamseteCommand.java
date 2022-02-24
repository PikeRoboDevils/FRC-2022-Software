/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class LazyRamseteCommand extends CommandBase {

    private final Supplier<Trajectory> trajectorySupplier;

    private final Supplier<Pose2d> pose;
    private final RamseteController controller;
    private final SimpleMotorFeedforward feedforward;
    private final DifferentialDriveKinematics kinematics;
    private final Supplier<DifferentialDriveWheelSpeeds> wheelSpeeds;
    private final PIDController left, right;
    private final BiConsumer<Double, Double> outputVolts;

    private RamseteCommand command;

    public LazyRamseteCommand(
            Supplier<Trajectory> trajectorySupplier,
            Supplier<Pose2d> pose,
            RamseteController controller,
            SimpleMotorFeedforward feedforward,
            DifferentialDriveKinematics kinematics,
            Supplier<DifferentialDriveWheelSpeeds> wheelSpeeds,
            PIDController leftController,
            PIDController rightController,
            BiConsumer<Double, Double> outputVolts,
            Subsystem... requirements) {
        this.trajectorySupplier = trajectorySupplier;
        this.pose = pose;
        this.controller = controller;
        this.feedforward = feedforward;
        this.kinematics = kinematics;
        this.wheelSpeeds = wheelSpeeds;
        this.right = rightController;
        this.left = leftController;
        this.outputVolts = outputVolts;
        addRequirements(requirements);
    }

    @Override
    public void initialize() {
        command = new RamseteCommand(
                trajectorySupplier.get(),
                pose,
                controller,
                feedforward,
                kinematics,
                wheelSpeeds,
                left,
                right,
                outputVolts);
        command.initialize();
    }

    @Override
    public void execute() {
        if (command != null) {
            command.execute();
        }
    }

    @Override
    public void end(boolean interrupted) {
        if (command != null) {
            command.end(interrupted);
        }
    }

    @Override
    public boolean isFinished() {
        if (command == null) return true;
        return command.isFinished();
    }
}
