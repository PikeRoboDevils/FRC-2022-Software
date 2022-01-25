/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

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

        public static final int CURRENT_LIMIT_PER_MOTOR = 40;

        public static final double DISTANCE_PER_PULSE_METERS = Units.inchesToMeters(Math.PI * 6) / 256;

        public static final double TRACK_WIDTH_INCHES = 21.83;

        public static final double TRACK_WIDTH_METERS = Units.inchesToMeters(TRACK_WIDTH_INCHES);
    }

    public static class ArmConstants {
        public static final int ARM_LEADER_ID = 7;
        public static final int ARM_FOLLOWER_ID = 8;
    }

    private Constants() {
        throw new UnsupportedOperationException("Constant utility class, should not be instantiated!");
    }
}
