/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.util.Units;

public class Constants {
    public static class DrivetrainConstants {
        public static final int LEFT_LEADER_ID = 1;
        public static final int LEFT_FOLLOWER_ONE_ID = 3;
        public static final int LEFT_FOLLOWER_TWO_ID = 5;

        public static final int RIGHT_LEADER_ID = 2;
        public static final int RIGHT_FOLLOWER_ONE_ID = 4;
        public static final int RIGHT_FOLLOWER_TWO_ID = 6;

        public static final int LEFT_ENCODER_A = 0;
        public static final int LEFT_ENCODER_B = 1;

        public static final int RIGHT_ENCODER_A = 2;
        public static final int RIGHT_ENCODER_B = 3;

        public static final int CURRENT_LIMIT_PER_MOTOR = 30;

        public static final double DISTANCE_PER_PULSE_METERS = Units.inchesToMeters(Math.PI * 6) / 256;

        public static final double TRACK_WIDTH_INCHES = 21.83;

        public static final double TRACK_WIDTH_METERS = 0.574; //Units.inchesToMeters(TRACK_WIDTH_INCHES);

        public static final double KS = 0.2185; // Volts
        public static final double KV = 1.8027; // V*S/M
        public static final double KA = 0.01476; // V*S/M^2

        public static final double KP_VELOCITY = 0.5;

        public static final DifferentialDriveKinematics KINEMATICS =
                new DifferentialDriveKinematics(TRACK_WIDTH_METERS);
        public static final SimpleMotorFeedforward FEEDFORWARD = new SimpleMotorFeedforward(KS, KV, KA);
    }

    public static class TrajectoryConstants {
        public static final double MAX_VELOCITY_MPS = 2.5;
        public static final double MAX_ACCEL_MPS = 2.0;
    }

    private Constants() {
        throw new UnsupportedOperationException("Constant utility class, should not be instantiated!");
    }
}
