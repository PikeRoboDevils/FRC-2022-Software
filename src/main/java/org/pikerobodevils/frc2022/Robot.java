/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.util.WPILibVersion;
import edu.wpi.first.wpilibj2.command.*;
import org.pikerobodevils.frc2022.subsystems.Climber;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;
import org.pikerobodevils.lib.Util;

/**
 * The VM is configured to automatically run this class, and to call the methods corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    public Robot() {
        super(Constants.PERIOD);
    }

    Drivetrain drivetrain = Drivetrain.getInstance();
    Climber climber = Climber.getInstance();
    ControlBoard controlBoard = ControlBoard.getInstance();
    DriverDashboard dashboard = DriverDashboard.getInstance();
    RobotContainer container = RobotContainer.getInstance();
    PowerDistribution pdh = new PowerDistribution();

    Command autoCommand = null;

    /**
     * This method is run when the robot is first started up and should be used for any initialization
     * code.
     */
    @Override
    public void robotInit() {
        setNetworkTablesFlushEnabled(true);

        DataLogManager.start();
        DriverStation.startDataLog(DataLogManager.getLog());
        // DriverStation.silenceJoystickConnectionWarning(true); // Uncomment when testing

        System.out.println("Initializing Robot...");
        System.out.println("Build debug info:");
        Util.getManifestAttributesForClass(this).forEach((name, value) -> System.out.println(name + ": " + value));
        System.out.println("Software versions:");
        System.out.println("WPILib: " + WPILibVersion.Version);
        System.out.println("RevLib: " + CANSparkMax.kAPIVersion);

        pdh.clearStickyFaults();

        container.configureButtonBindings();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        dashboard.updateEntries();
    }

    @Override
    public void autonomousInit() {
        Command ramsete, autonomous;

        autoCommand = dashboard.getSelectedAutoCommand();
        autoCommand.schedule();
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopInit() {
        if (autoCommand != null) autoCommand.cancel();
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
