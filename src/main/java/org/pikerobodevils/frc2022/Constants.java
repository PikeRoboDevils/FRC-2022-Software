/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotBase;
import org.pikerobodevils.frc2022.trajectory.Trajectories;

public class Constants {

    public static final double PERIOD = RobotBase.isSimulation() ? 0.02 : 0.01;

    public static class ControlConstants {
        public static final double AUTO_CLIMB_PITCH_THRESHOLD = 5; //degrees
    }

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

        public static final int CURRENT_LIMIT_PER_MOTOR = 50;
        public static final double WHEEL_CIRCUMFERENCE_METERS = Units.inchesToMeters(Math.PI * 6.18);
        public static final double DISTANCE_PER_PULSE_METERS = WHEEL_CIRCUMFERENCE_METERS / 256;

        public static final double TRACK_WIDTH_INCHES = 21.83;

        public static final double TRACK_WIDTH_METERS = 0.61059; // Units.inchesToMeters(TRACK_WIDTH_INCHES);

        public static final double KS = 0.20779; // Volts
        public static final double KV = 1.7472; // V*S/M
        public static final double KA = 1.1591; // V*S/M^2

        public static final double KP_VELOCITY = 2.2611;

        public static final DifferentialDriveKinematics KINEMATICS =
                new DifferentialDriveKinematics(TRACK_WIDTH_METERS);
        public static final SimpleMotorFeedforward FEEDFORWARD = new SimpleMotorFeedforward(KS, KV, KA);
    }

    public static class IntakeConstants {

        public static final int LEADER_ID = 1;
        public static final int FOLLOWER_ID = 2;
    }

    public static class ArmConstants {

        public static final int ARM_LEADER_ID = 7;
        public static final int ARM_ENCODER_ABS_DIO = 4;
        public static final int ARM_ENCODER_QUAD_A_DIO = 5;
        public static final int ARM_ENCODER_QUAD_B_DIO = 6;
        public static final int ARM_ENCODER_QUAD_I_DIO = 7;

        public static final int ARM_TOP_LIMIT_DIO = 8;

        public static final double KP = 0.25;
        public static final double KI = 0;
        public static final double KD = 0;
        public static final double MAX_ACCEL = 260;
        public static final double MAX_VELOCITY = 360;

        /*
         * Obtained from sysid
         */
        public static final double KS = 0.22405;
        public static final double KG = 1.2986;
        public static final double KV = 0.019113;
        public static final double KA = 0.0044777;
    }

    public static class ClimberConstants {
        public static final int LEFT_MOTOR_ID = 8;
        public static final int RIGHT_MOTOR_ID = 9;

        public static final double KP_HOLD = 0.5;

        public static final double RAMP_RATE = 0.15;

        public static final int PERIODIC_STATUS_2_PERIOD = 10; // ms
    }

    public static class ControlBoardConstants {
        public static final boolean ARM_MANUAL_MODE_ALWAYS = false;
    }

    public static class AutoConstants {
        public static final double DEFAULT_ACTION_RANGE = 0.25;
    }

    public static class TrajectoryConstants {
        public static final double MAX_VELOCITY_MPS = 3;
        public static final double MAX_ACCEL_MPS = 1;
        public static final double MAX_VOLTAGE = 5;

        public static final TrajectoryConfig DEFAULT_CONF_FORWARD = new TrajectoryConfig(
                        MAX_VELOCITY_MPS, MAX_ACCEL_MPS)
                .setKinematics(DrivetrainConstants.KINEMATICS)
                .addConstraint(new CentripetalAccelerationConstraint(1))
                .addConstraint(new DifferentialDriveVoltageConstraint(
                        DrivetrainConstants.FEEDFORWARD, DrivetrainConstants.KINEMATICS, MAX_VOLTAGE));
        public static final TrajectoryConfig DEFAULT_CONF_REVERSE =
                Trajectories.copyConfig(DEFAULT_CONF_FORWARD).setReversed(true);
    }

    public static final int MEME_COUNT = 15;

    private Constants() {
        throw new UnsupportedOperationException("Constant utility class, should not be instantiated!");
    }
}
