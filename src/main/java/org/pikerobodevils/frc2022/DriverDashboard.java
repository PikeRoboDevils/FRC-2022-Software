/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.*;
import org.pikerobodevils.frc2022.commands.autonomous.*;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;
import org.pikerobodevils.frc2022.trajectory.Trajectories;
import org.pikerobodevils.lib.motorcontrol.DevilCANSparkMax;
import org.pikerobodevils.lib.motorcontrol.DevilTalonSRX;

public class DriverDashboard {

    private ShuffleboardTab dashTab = Shuffleboard.getTab("Driver Dashboard");

    private ShuffleboardTab cameraTab = Shuffleboard.getTab("Camera");

    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    private NetworkTableEntry pathEntry;

    private NetworkTableEntry delayTime;

    private Field2d fieldVis = new Field2d();

    private DriverDashboard() {
        initAutoChooser();

        dashTab.add("Auto Chooser", autoChooser).withSize(2, 1);
        dashTab.add("Field View", Drivetrain.getInstance().field)
                .withWidget(BuiltInWidgets.kField)
                .withSize(3, 2)
                .withPosition(6, 0);
        delayTime = dashTab.add("Autonomous Delay Time", 0.0)
                .withSize(2, 1)
                .withPosition(3, 0)
                .getEntry();
        delayTime.setDouble(0);
        dashTab.addBoolean(
                        "Initialization Successful?",
                        () -> !DevilCANSparkMax.hasFailedInitialization() && !DevilTalonSRX.hasFailedInitialization())
                .withPosition(0, 1)
                .withSize(2, 1);
        dashTab.addBoolean("Trajectories Successful?", Trajectories::isTrajectoriesSuccessful)
                .withPosition(0, 2)
                .withSize(2, 1);
        dashTab.add("STOP AUTO", new InstantCommand(CommandScheduler.getInstance()::cancelAll).withName("STOP AUTO"));
        // pathEntry = dashTab.add("PathToRun", "").getEntry();
        SmartDashboard.putData(fieldVis);

        if (RobotBase.isReal()) {
            var camera = CameraServer.startAutomaticCapture();
            camera.setFPS(15);
            cameraTab.add(camera).withSize(9, 3).withPosition(0, 0).withWidget(BuiltInWidgets.kCameraStream);
        }
    }

    public void updateEntries() {
        fieldVis.setRobotPose(Drivetrain.getInstance().getPose());
    }

    private void initAutoChooser() {
        autoChooser.addOption("No Auto", new NoAutonomous());
        autoChooser.setDefaultOption("Universal One Ball then Drive", new UniversalOneBallDriveAuto());
        autoChooser.addOption("Universal Drive Back", new UniversalDriveBackAutonomous());
        autoChooser.addOption("Left Tarmac 2 Ball", new LeftStartTwoBall());
        // autoChooser.addOption("Run Path", new RunPathCommand(() -> pathEntry.getString("")));
        autoChooser.addOption("Right Tarmac Left 2 Ball", new RightTarmacLeftTwoBall());
        autoChooser.addOption("Right Tarmac Left 3 Ball", new RightTarmacLeftThreeBall());
        autoChooser.addOption("Right Tarmac Right 2 Ball", new RightTarmacRightTwoBall());
        autoChooser.addOption("Right Tarmac Right 3 Ball", new RightTarmacRightThreeBall());
        autoChooser.addOption("Billiards Left HA HA", new BilliardsLeftTarmac());
        autoChooser.addOption("Billiards Left + One ball", new LeftBilliardsPlusOne());
    }

    public Command getSelectedAutoCommand() {
        return autoChooser.getSelected().beforeStarting(new WaitCommand(delayTime.getDouble(0)));
    }

    public void displayTrajectory(Trajectory trajectory) {
        fieldVis.getObject("traj").setTrajectory(trajectory);
    }

    private static DriverDashboard INSTANCE;

    public static DriverDashboard getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DriverDashboard();
        }
        return INSTANCE;
    }
}
