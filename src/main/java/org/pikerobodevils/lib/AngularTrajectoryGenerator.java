/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import java.util.ArrayList;
import java.util.List;

public class AngularTrajectoryGenerator {
    public static Trajectory generateAngularTrajectory(
            Pose2d startingPose, Rotation2d endAngle, TrapezoidProfile.Constraints constraints, double period) {

        TrapezoidProfile profile = new TrapezoidProfile(
                constraints,
                new TrapezoidProfile.State(endAngle.getRadians(), 0),
                new TrapezoidProfile.State(startingPose.getRotation().getRadians(), 0));

        List<Trajectory.State> states = new ArrayList<>();
        double time = 0;
        while (!profile.isFinished(time - period)) {
            TrapezoidProfile.State trapezoidState = profile.calculate(time);

            Trajectory.State state = new Trajectory.State(
                    time,
                    0,
                    0,
                    new Pose2d(startingPose.getTranslation(), new Rotation2d(trapezoidState.position)),
                    Double.POSITIVE_INFINITY);

            states.add(state);
            time += period;
        }

        Trajectory trajectory = new Trajectory(states);
        return trajectory;
    }
}
