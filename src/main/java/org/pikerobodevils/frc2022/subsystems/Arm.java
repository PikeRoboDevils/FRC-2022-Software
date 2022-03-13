/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.subsystems;

import static org.pikerobodevils.frc2022.Constants.ArmConstants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.pikerobodevils.frc2022.Constants;
import org.pikerobodevils.lib.DefaultCANSparkMax;
import org.pikerobodevils.lib.OffsetQuadEncoder;
import org.pikerobodevils.lib.OneShot;

public class Arm extends SubsystemBase {

    // ALL SETPOINTS ARE IN REFERENCE TO THE LINKAGE
    // The actual arm position should *only* be used for gravity ff.

    private static final double armStartingAngleDegrees = 32;
    private static final double linkageStartingAngleDegrees = 104;

    private static final double armLowAngleDegrees = 0;
    private static final double linkageLowAngleDegrees = 5;

    private static final double chainReduction = 54.0 / 15.0;

    private final CANSparkMax armMotor;
    private final DutyCycleEncoder absoluteEncoder;
    private final OffsetQuadEncoder quadEncoder;
    private final RelativeEncoder neoEncoder;
    private final DigitalInput lowerLimit = new DigitalInput(ARM_TOP_LIMIT_DIO);

    private final OneShot lowerLimitRisingEdge = new OneShot(() -> !lowerLimit.get());

    private final ProfiledPIDController controller = new ProfiledPIDController(
            KP, KI, KD, new TrapezoidProfile.Constraints(MAX_VELOCITY, MAX_ACCEL), Constants.PERIOD);

    private final ArmFeedforward feedforward = new ArmFeedforward(KS, KG, KV, KA);

    private boolean closedLoopEnabled = false;

    NetworkTable armDataTable = NetworkTableInstance.getDefault().getTable("Arm");

    /**
     * Creates a new instance of this Arm. This constructor
     * is private since this class is a Singleton. Code should use
     * the {@link #getInstance()} method to get the singleton instance.
     */
    private Arm() {

        armMotor = new DefaultCANSparkMax(ARM_LEADER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        armMotor.setInverted(false);
        armMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        armMotor.setSmartCurrentLimit(80);
        // TODO: tune current limit. 30a is too low. 80A is probably higher than we need but we'll see.

        neoEncoder = armMotor.getEncoder();

        absoluteEncoder = new DutyCycleEncoder(ARM_ENCODER_ABS_DIO);
        absoluteEncoder.setDutyCycleRange(1.0 / 1025.0, 1024.0 / 1025.0);

        quadEncoder = new OffsetQuadEncoder(
                ARM_ENCODER_QUAD_A_DIO, ARM_ENCODER_QUAD_B_DIO, true, CounterBase.EncodingType.k4X);
        quadEncoder.setDistancePerPulse(360 / chainReduction / 2048); // getDistance will report LINKAGE angle
        quadEncoder.setSamplesToAverage(10);
        quadEncoder.reset();
        quadEncoder.setDistance(linkageStartingAngleDegrees);

        controller.setTolerance(4);
        setGoal(ArmPosition.SCORE);
        enableClosedLoop();

        initTelemetry();
    }

    /**
     * If closed loop mode is enabled, resets the internal controller to prevent jumps and re-enables closed loop mode.
     */
    public void enableClosedLoop() {
        // If closed loop is currently disabled, reset the controller so the controller can begin a profile towards the
        // goal
        // Subsequent calls do nothing.
        if (!isClosedLoopEnabled()) {
            controller.reset(getLinkagePosition());
            closedLoopEnabled = true;
        }
    }

    /**
     * If closed loop mode is enabled, disables the actuator and disables closed loop mode.
     */
    public void disableClosedLoop() {
        // If closed loop is enabled, stop the motor to prevent stuck values. Subsequent calls won't do anything.
        if (isClosedLoopEnabled()) {
            disable();
            closedLoopEnabled = false;
        }
    }

    /**
     * Returns true if closed loop mode is enabled
     * @return true if closed loop is enabled, otherwise false
     */
    public boolean isClosedLoopEnabled() {
        return closedLoopEnabled;
    }

    /**
     * Sets the constraints for the internal controller
     * @param constraints Max velocity and acceleration for the arm
     * @see TrapezoidProfile.Constraints
     * @see TrapezoidProfile#TrapezoidProfile(TrapezoidProfile.Constraints, TrapezoidProfile.State)
     */
    public void setConstraints(TrapezoidProfile.Constraints constraints) {
        controller.setConstraints(constraints);
    }

    public void setGoal(TrapezoidProfile.State goal) {
        controller.setGoal(goal);
    }

    public void setGoal(double goal) {
        controller.setGoal(goal);
    }

    public void setGoal(ArmPosition goal) {
        setGoal(goal.position);
    }

    public void setVoltage(double voltage) {
        armMotor.setVoltage(voltage);
    }

    public void setOutput(double output) {
        armMotor.set(output);
    }

    public void runOpenLoop(double output) {
        disableClosedLoop();
        setVoltage(output * 6);
    }

    public void disable() {
        setOutput(0);
    }

    /**
     * Returns the estimated arm position in degrees based off the linkage position
     * @return estimated arm position
     * @see #linkageDegreesToArmDegrees(double)
     */
    public double getArmPositionDegrees() {
        return linkageDegreesToArmDegrees(getLinkagePosition());
    }

    /**
     * Returns the position in degrees of the first link of the arm linkage
     * @return position of first link in degrees
     */
    public double getLinkagePosition() {
        return quadEncoder.getDistance();
    }

    /**
     * Returns whether the controller is at its end goal
     * @return true if arm is at its goal, otherwise false
     */
    public boolean atGoal() {
        return controller.atGoal();
    }

    /**
     * Converts linkage position to arm position
     *
     * Linear interpolation based conversion
     * Very naive implementation, assumes linearity.
     *
     * @param linkageDegrees position of the linkage
     * @return Position of the arm based on <code>linkageDegrees</code>
     */
    private static double linkageDegreesToArmDegrees(double linkageDegrees) {
        // Linear interpolate between link1 degrees and arm degrees
        // Poor assumption, but I don't know how to solve the equations for 4 bar linkages
        var tLinkage =
                (linkageDegrees - linkageLowAngleDegrees) / (linkageStartingAngleDegrees - linkageLowAngleDegrees);

        return ((armStartingAngleDegrees - armLowAngleDegrees) * tLinkage) + armLowAngleDegrees;
    }

    @Override
    public void periodic() {

        // if(lowerLimitRisingEdge.get()) {
        //    quadEncoder.setDistance(linkageLowAngleDegrees + 1);
        // }

        if (isClosedLoopEnabled() && RobotState.isEnabled()) {
            var setpoint = controller.getSetpoint();
            var ffVoltage = feedforward.calculate(
                    Units.degreesToRadians(linkageDegreesToArmDegrees(setpoint.position)),
                    Units.degreesToRadians(setpoint.velocity));

            // If the controller is set to the top OR  bottom AND it is at its goal, disable feedforward to prevent
            // damage
            if (controller.atGoal()
                    && (controller.getGoal().position == ArmPosition.SCORE.position
                            || controller.getGoal().position == ArmPosition.INTAKE.position)) {
                ffVoltage = 0;
            }
            var output = ffVoltage + controller.calculate(getLinkagePosition());
            setVoltage(output);
        } else if (RobotState.isDisabled()) { // If the robot is disabled constantly reset to avoid jumps
            controller.reset(getLinkagePosition());
            setVoltage(0);
        }

        updateTelemetry();
    }

    /**
     * Initializes any telemetry entries that need to be read from
     */
    private void initTelemetry() {
        armDataTable.getEntry("kP").setDefaultDouble(KP);
        armDataTable.getEntry("kI").setDefaultDouble(KI);
        armDataTable.getEntry("kD").setDefaultDouble(KD);

        armDataTable.getEntry("MaxAcceleration").setDefaultDouble(MAX_ACCEL);
        armDataTable.getEntry("MaxVelocity").setDefaultDouble(MAX_VELOCITY);
    }

    /**
     * Sends telemetry data to NetworkTables.
     */
    private void updateTelemetry() {
        armDataTable.getEntry("ArmPosition").setDouble(getLinkagePosition());
        armDataTable.getEntry("ArmVelocity").setDouble(quadEncoder.getRate());
        armDataTable.getEntry("AppliedOutputVoltage").setDouble(armMotor.getAppliedOutput() * armMotor.getBusVoltage());
        armDataTable.getEntry("ArmCurrent").setDouble(armMotor.getOutputCurrent());
        armDataTable.getEntry("IsAtGoal").setBoolean(atGoal());
        armDataTable.getEntry("GoalPosition").setDouble(controller.getGoal().position);
        armDataTable.getEntry("ClosedLoopEnabled").setBoolean(isClosedLoopEnabled());

        armDataTable.getEntry("SetpointPosition").setDouble(controller.getSetpoint().position);
        armDataTable.getEntry("SetpointVelocity").setDouble(controller.getSetpoint().velocity);

        controller.setP(armDataTable.getEntry("kP").getDouble(KP));
        controller.setI(armDataTable.getEntry("kI").getDouble(KI));
        controller.setD(armDataTable.getEntry("kD").getDouble(KD));

        var maxAccel = armDataTable.getEntry("MaxAcceleration").getDouble(MAX_ACCEL);
        var maxVelocity = armDataTable.getEntry("MaxVelocity").getDouble(MAX_VELOCITY);

        controller.setConstraints(new TrapezoidProfile.Constraints(maxVelocity, maxAccel));
    }

    public enum ArmPosition {
        SCORE(linkageStartingAngleDegrees),
        INTAKE(linkageLowAngleDegrees);

        public final double position;

        ArmPosition(double position) {
            this.position = position;
        }
    }

    /**
     * The Singleton instance of this Arm. Code should use
     * the {@link #getInstance()} method to get the single instance (rather
     * than trying to construct an instance of this class.)
     */
    private static final Arm INSTANCE = new Arm();

    /**
     * Returns the Singleton instance of this Arm. This static method
     * should be used, rather than the constructor, to get the single instance
     * of this class. For example: {@code Arm.getInstance();}
     */
    @SuppressWarnings("WeakerAccess")
    public static Arm getInstance() {
        return INSTANCE;
    }
}
