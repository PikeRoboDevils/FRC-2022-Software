/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.*;
import java.util.List;
import org.pikerobodevils.frc2022.commands.EasyRamseteCommand;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;

/**
 * The VM is configured to automatically run this class, and to call the methods corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    public Robot() {
        super(0.01);
    }

    Drivetrain drivetrain = Drivetrain.getInstance();
    ControlBoard controlBoard = ControlBoard.getInstance();

    /**
     * This method is run when the robot is first started up and should be used for any initialization
     * code.
     */
    @Override
    public void robotInit() {
        drivetrain.setDefaultCommand(new RunCommand(
                () -> drivetrain.arcadeDrive(controlBoard.getSpeed(), controlBoard.getRotation()), drivetrain));
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void autonomousInit() {

        var voltageConstraint =
                new DifferentialDriveVoltageConstraint(drivetrain.getFeedforward(), drivetrain.getKinematics(), 5);

        var config = new TrajectoryConfig(3, 1)
                .setKinematics(drivetrain.getKinematics())
                .addConstraint(voltageConstraint);

        var testTraj = TrajectoryGenerator.generateTrajectory(
                new Pose2d(0, 0, new Rotation2d(0)),
                List.of(),
                new Pose2d(Units.feetToMeters(10), Units.feetToMeters(6), Rotation2d.fromDegrees(0)),
                config);

        Command ramsete, autonomous;

        ramsete = new EasyRamseteCommand(testTraj, drivetrain);
        autonomous = new InstantCommand(() -> drivetrain.setIdleMode(CANSparkMax.IdleMode.kBrake), drivetrain)
                .andThen(ramsete)
                .andThen(() -> drivetrain.setIdleMode(CANSparkMax.IdleMode.kCoast), drivetrain);
        autonomous.schedule();
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopInit() {}

    @Override
    public void teleopPeriodic() {}

    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {}

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {
        LiveWindow.updateValues();
    }
}
