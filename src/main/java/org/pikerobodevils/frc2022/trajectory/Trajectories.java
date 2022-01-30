/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.trajectory;

import static org.pikerobodevils.frc2022.Constants.DrivetrainConstants;
import static org.pikerobodevils.frc2022.Constants.TrajectoryConstants;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.*;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveKinematicsConstraint;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
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

    private static Trajectory leftTarmacExit, rightTarmacExit, leftTarmacOneBall, rightTarmacOneBall, sampleTraj;

    private static Pose2d leftTarmacStartingPose;

    private static Pose2d leftTarmacExitPose;

    private static Pose2d leftTarmacOneBallScorePose;

    private static Pose2d rightTarmacStartingPose;

    private static Pose2d rightTarmacExitPose;

    private static Pose2d rightTarmacOneBallScorePose;

    private static Pose2d sampleTrajStart = new Pose2d(0.473, 1, new Rotation2d(0));
    private static List<Translation2d> sampleTrajWaypoints =
            List.of(new Translation2d(4, 1.5), new Translation2d(4, 4.5));
    private static Pose2d sampleTrajEnd = new Pose2d(5.1, 4.5, Rotation2d.fromDegrees(-45));

    public static void preInitializeTrajectories() {
        /*try {
            leftTarmacExit = TrajectoryGenerator.generateTrajectory(
                    leftTarmacStartingPose, List.of(), leftTarmacExitPose, defaultConfig);

            rightTarmacExit = TrajectoryGenerator.generateTrajectory(
                    rightTarmacStartingPose, List.of(), rightTarmacExitPose, defaultConfig);

            leftTarmacOneBall = TrajectoryGenerator.generateTrajectory(
                    leftTarmacStartingPose, List.of(), leftTarmacOneBallScorePose, defaultConfig);

            rightTarmacOneBall = TrajectoryGenerator.generateTrajectory(
                    rightTarmacStartingPose, List.of(), rightTarmacOneBallScorePose, defaultConfig);

        } catch (TrajectoryParameterizer.TrajectoryGenerationException e) {
            e.printStackTrace();
        }*/
        sampleTraj = TrajectoryGenerator.generateTrajectory(
            sampleTrajStart, sampleTrajWaypoints, sampleTrajEnd, defaultConfig);
    }

    public static Trajectory getLeftTarmacExit() {
        return leftTarmacExit;
    }

    public static Trajectory getRightTarmacExit() {
        return rightTarmacExit;
    }

    public static Trajectory getLeftTarmacOneBall() {
        return leftTarmacOneBall;
    }

    public static Trajectory getRightTarmacOneBall() {
        return rightTarmacOneBall;
    }

    public static Trajectory getSampleTrajectory() {
        return sampleTraj;
    }

    public static Trajectory getLeftTarmacOneBallExitTrajectory(Pose2d startingPose) {
        return TrajectoryGenerator.generateTrajectory(startingPose, List.of(), leftTarmacExitPose, defaultConfig);
    }

    public static Trajectory getRightTarmacOneBallExitTrajectory(Pose2d startingPose) {
        return TrajectoryGenerator.generateTrajectory(startingPose, List.of(), rightTarmacExitPose, defaultConfig);
    }
}
