/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.*;
import org.pikerobodevils.frc2022.commands.trajectory.EasyRamseteCommand;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;
import org.pikerobodevils.frc2022.trajectory.Trajectories;

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
        Trajectories.preInitializeTrajectories();

        drivetrain.setDefaultCommand(new RunCommand(
                () -> drivetrain.arcadeDrive(controlBoard.getSpeed(), controlBoard.getRotation()), drivetrain));
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void autonomousInit() {
        Command ramsete, autonomous;
        ramsete = new EasyRamseteCommand(Trajectories.getSampleTrajectory(), drivetrain);
        autonomous = new InstantCommand(() -> drivetrain.setIdleMode(CANSparkMax.IdleMode.kBrake), drivetrain)
                .andThen(ramsete);

        autonomous.schedule();
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopInit() {
        drivetrain.setIdleMode(CANSparkMax.IdleMode.kCoast);
    }

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
