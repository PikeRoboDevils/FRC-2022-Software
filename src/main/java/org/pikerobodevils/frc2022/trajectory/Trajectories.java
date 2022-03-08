/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.trajectory;

/* spotless:off
   Credit due to team 6391 for the fromWaypoints function
   spotless:on
*/

import static org.pikerobodevils.frc2022.Constants.DrivetrainConstants;
import static org.pikerobodevils.frc2022.Constants.TrajectoryConstants;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.*;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveKinematicsConstraint;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.math.trajectory.constraint.TrajectoryConstraint;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.pikerobodevils.frc2022.Constants;

public class Trajectories {

    private static final DifferentialDriveVoltageConstraint voltageConstraint =
            new DifferentialDriveVoltageConstraint(DrivetrainConstants.FEEDFORWARD, DrivetrainConstants.KINEMATICS, 7);

    private static final DifferentialDriveKinematicsConstraint kinematicsConstraint =
            new DifferentialDriveKinematicsConstraint(
                    DrivetrainConstants.KINEMATICS, TrajectoryConstants.MAX_VELOCITY_MPS);

    private static TrajectoryConfig defaultConfig = new TrajectoryConfig(
                    TrajectoryConstants.MAX_VELOCITY_MPS, Constants.TrajectoryConstants.MAX_ACCEL_MPS)
            .setKinematics(DrivetrainConstants.KINEMATICS)
            .addConstraint(voltageConstraint)
            .addConstraint(kinematicsConstraint);

    public static Trajectory generateTrajectory(
            Pose2d start,
            List<Translation2d> waypoints,
            Pose2d end,
            boolean reversed,
            TrajectoryConstraint... constraints) {

        TrajectoryConfig config = new TrajectoryConfig(
                        TrajectoryConstants.MAX_VELOCITY_MPS, TrajectoryConstants.MAX_ACCEL_MPS)
                .setKinematics(DrivetrainConstants.KINEMATICS);
        for (TrajectoryConstraint constraint : constraints) {
            config.addConstraint(constraint);
        }
        config.setReversed(reversed);
        return TrajectoryGenerator.generateTrajectory(start, waypoints, end, config);
    }

    public static TrajectoryConfig copyConfig(final TrajectoryConfig config) {
        return new TrajectoryConfig(config.getMaxVelocity(), config.getMaxAcceleration())
                .addConstraints(config.getConstraints());
    }

    public static Trajectory generateNamedTrajectory(String name, TrajectoryConfig conf) {
        try {
            var filepath = Filesystem.getDeployDirectory().toPath().resolve(Paths.get("Paths", name));
            return fromWaypoints(filepath, conf);
        } catch (IOException exception) {
            DriverStation.reportError("Failed to load auto trajectory: " + name, false);
            return new Trajectory();
        }
    }

    public static Trajectory fromWaypoints(Path path, TrajectoryConfig config) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            var interiorWaypoints = new ArrayList<Translation2d>();
            Pose2d start = new Pose2d();
            Pose2d end = new Pose2d();
            int loop = 0;
            String line;
            String lastline = "";

            while ((line = reader.readLine()) != null) {
                if (loop == 0 || loop == 2) {
                    ; // skip the header and the second line because we are logging last
                } else if (loop == 1) {
                    start = createPoseWaypoint(line);
                } else {
                    interiorWaypoints.add(createTranslationWaypoint(lastline));
                }
                lastline = line;
                loop++;
            }

            end = createPoseWaypoint(lastline);

            return TrajectoryGenerator.generateTrajectory(start, interiorWaypoints, end, config);
        }
    }

    private static Pose2d createPoseWaypoint(String input) {
        String[] arrOfStr = input.split(",", 0);
        // 8.21m is the Height of the field PathWeaver and traj use different starting points
        return new Pose2d(
                new Translation2d(
                        Double.parseDouble(arrOfStr[0]),
                        8.21 + Double.parseDouble(arrOfStr[1])), // add field height to adjust to
                new Rotation2d(Double.parseDouble(arrOfStr[2]), Double.parseDouble(arrOfStr[3])));
    }

    private static Translation2d createTranslationWaypoint(String input) {
        String[] arrOfStr = input.split(",", 0);
        return new Translation2d(Double.parseDouble(arrOfStr[0]), 4.572 + Double.parseDouble(arrOfStr[1]));
    }
}
