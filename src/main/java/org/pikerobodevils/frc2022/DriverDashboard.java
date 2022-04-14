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
import edu.wpi.first.wpilibj2.command.Command;
import org.pikerobodevils.frc2022.commands.autonomous.*;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;
import org.pikerobodevils.lib.motorcontrol.DevilCANSparkMax;

public class DriverDashboard {

    private ShuffleboardTab dashTab = Shuffleboard.getTab("Driver Dashboard");

    private ShuffleboardTab cameraTab = Shuffleboard.getTab("Camera");

    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    private NetworkTableEntry pathEntry;

    private Field2d fieldVis = new Field2d();

    private DriverDashboard() {
        initAutoChooser();

        dashTab.add("Auto Chooser", autoChooser).withSize(2, 1);
        dashTab.add("Field View", Drivetrain.getInstance().field)
                .withWidget(BuiltInWidgets.kField)
                .withSize(3, 2)
                .withPosition(6, 0);
        dashTab.addBoolean("Initialization Successful?", () -> !DevilCANSparkMax.hasFailedInitialization())
                .withPosition(3, 0)
                .withSize(2, 1);
        pathEntry = dashTab.add("PathToRun", "").getEntry();
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
        autoChooser.addOption("twoball pls dont use yet", new LeftStartTwoBall());
        autoChooser.addOption("Run Path", new RunPathCommand(() -> pathEntry.getString("")));
        autoChooser.addOption("Right Tarmac dont use", new RightTarmacLeftTwoBall());
        autoChooser.addOption("Right Tarmac Three dont use", new RightTarmacRightThreeBall());
        autoChooser.addOption("Billiards Right HA HA", new BilliardsLeftTarmac());



    }

    public Command getSelectedAutoCommand() {
        return autoChooser.getSelected();
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
