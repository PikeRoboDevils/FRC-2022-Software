/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import org.pikerobodevils.frc2022.commands.autonomous.LeftStartTwoBall;
import org.pikerobodevils.frc2022.commands.autonomous.NoAutonomous;
import org.pikerobodevils.frc2022.commands.autonomous.UniversalDriveBackAutonomous;
import org.pikerobodevils.frc2022.commands.autonomous.UniversalOneBallDriveAuto;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;

public class DriverDashboard {

    private ShuffleboardTab dashTab = Shuffleboard.getTab("Driver Dashboard");

    private ShuffleboardTab cameraTab = Shuffleboard.getTab("Camera");

    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    private Field2d fieldVis = new Field2d();

    private DriverDashboard() {
        initAutoChooser();

        dashTab.add("Auto Chooser", autoChooser).withSize(2, 1);
        dashTab.add("Field View", Drivetrain.getInstance().field)
                .withWidget(BuiltInWidgets.kField)
                .withSize(3, 2)
                .withPosition(6, 0);
        SmartDashboard.putData(fieldVis);

        var camera = CameraServer.startAutomaticCapture();
        cameraTab.add(camera).withSize(9, 3).withPosition(0, 0).withWidget(BuiltInWidgets.kCameraStream);
    }

    public void updateEntries() {
        fieldVis.setRobotPose(Drivetrain.getInstance().getPose());
    }

    private void initAutoChooser() {
        autoChooser.addOption("No Auto", new NoAutonomous());
        autoChooser.setDefaultOption("Universal One Ball then Drive", new UniversalOneBallDriveAuto());
        autoChooser.addOption("Universal Drive Back", new UniversalDriveBackAutonomous());
        autoChooser.addOption("twoball pls dont use yet", new LeftStartTwoBall());
    }

    public Command getSelectedAutoCommand() {
        return autoChooser.getSelected();
    }

    public void displayTrajectory(Trajectory trajectory) {
        fieldVis.getObject("traj").setTrajectory(trajectory);
    }

    private static DriverDashboard INSTANCE = new DriverDashboard();

    public static DriverDashboard getInstance() {
        return INSTANCE;
    }
}
