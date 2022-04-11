/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.util.WPILibVersion;
import edu.wpi.first.wpilibj2.command.*;
import org.pikerobodevils.frc2022.subsystems.Climber;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;
import org.pikerobodevils.frc2022.subsystems.MemeMachine;
import org.pikerobodevils.lib.util.Util;

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

    private Drivetrain drivetrain;
    private Climber climber;
    private ControlBoard controlBoard;
    private DriverDashboard dashboard;
    private RobotContainer container;
    private PowerDistribution pdh;

    Command autoCommand = null;

    /**
     * This method is run when the robot is first started up and should be used for any initialization
     * code.
     */
    @Override
    public void robotInit() {
        setNetworkTablesFlushEnabled(true);

        if (isReal()) {
            DataLogManager.start();
        } else {
            DataLogManager.start(Filesystem.getOperatingDirectory()
                    .toPath()
                    .resolve("sim_logs")
                    .toString());
        }
        DriverStation.startDataLog(DataLogManager.getLog());

        if (isSimulation() | true) DriverStation.silenceJoystickConnectionWarning(true); // Uncomment when testing

        DataLogManager.log("Initializing Robot...");
        DataLogManager.log("Build debug info:");
        Util.getManifestAttributesForClass(this).forEach((name, value) -> DataLogManager.log(name + ": " + value));
        DataLogManager.log("Software versions:");
        DataLogManager.log("Java: " + System.getProperty("java.vendor") + " " + System.getProperty("java.version"));
        DataLogManager.log("WPILib: " + WPILibVersion.Version);
        DataLogManager.log("RevLib: " + CANSparkMax.kAPIVersion);

        drivetrain = Drivetrain.getInstance();
        climber = Climber.getInstance();
        controlBoard = ControlBoard.getInstance();
        dashboard = DriverDashboard.getInstance();
        container = RobotContainer.getInstance();

        pdh = new PowerDistribution();
        pdh.clearStickyFaults();

        container.configureButtonBindings();

        MemeMachine.initializeMemes();

        // DevilAddressableLED led = new DevilAddressableLED(4, 15);
        // led.setPattern(new ScannerPattern(Color.kBlue, Color.kRed, 3));
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
