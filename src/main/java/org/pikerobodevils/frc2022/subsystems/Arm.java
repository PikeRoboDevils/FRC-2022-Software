/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.subsystems;

import static org.pikerobodevils.frc2022.Constants.ArmConstants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.pikerobodevils.frc2022.Constants;

public class Arm extends SubsystemBase {
    private final CANSparkMax armMotor;
    private final DutyCycleEncoder absoluteEncoder;
    private final Encoder quadEncoder;
    private final DigitalInput topLimit;

    private final ProfiledPIDController controller = new ProfiledPIDController(
            KP, KI, KD, new TrapezoidProfile.Constraints(MAX_VELOCITY, MAX_ACCEL), Constants.PERIOD);

    private boolean closedLoopEnabled = false;

    NetworkTable armDataTable = NetworkTableInstance.getDefault().getTable("Arm");
    // With eager singleton initialization, any static variables/fields used in the
    // constructor must appear before the "INSTANCE" variable so that they are initialized
    // before the constructor is called when the "INSTANCE" variable initializes.

    /**
     * Creates a new instance of this Arm. This constructor
     * is private since this class is a Singleton. Code should use
     * the {@link #getInstance()} method to get the singleton instance.
     */
    private Arm() {

        armMotor = new CANSparkMax(ARM_LEADER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        armMotor.setInverted(true);
        armMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        armMotor.setSmartCurrentLimit(30);

        absoluteEncoder = new DutyCycleEncoder(ARM_ENCODER_ABS_DIO);
        absoluteEncoder.setDutyCycleRange(1.0 / 1025.0, 1024.0 / 1025.0);

        quadEncoder = new Encoder(ARM_ENCODER_QUAD_A_DIO, ARM_ENCODER_QUAD_B_DIO, false, CounterBase.EncodingType.k4X);
        quadEncoder.setDistancePerPulse(1 / 2048.0); // TODO: get actual dpp, should be pretty close

        topLimit = new DigitalInput(ARM_TOP_LIMIT_DIO);

        initTelemetry();
        enableClosedLoop();
    }

    public void enableClosedLoop() {
        // If closed loop is currently disabled, reset the controller so the current state becomes the desired state.
        // Subsequent calls do nothing.
        if (!isClosedLoopEnabled()) {
            controller.reset(getPosition());
            closedLoopEnabled = true;
        }
    }

    public void disableClosedLoop() {
        // If closed loop is enabled, stop the motor to prevent stuck values. Subsequent calls won't do anything.
        if (isClosedLoopEnabled()) {
            disable();
            closedLoopEnabled = false;
        }
    }

    public boolean isClosedLoopEnabled() {
        return closedLoopEnabled;
    }

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
        setVoltage(output * 12);
    }

    public void disable() {
        setOutput(0);
    }

    public double getPosition() {
        return quadEncoder.getDistance();
    }

    public boolean atGoal() {
        return controller.atGoal();
    }

    @Override
    public void periodic() {
        if (isClosedLoopEnabled()) {
            var output = controller.calculate(getPosition());
            setVoltage(output);
        }

        updateTelemetry();
    }

    private void initTelemetry() {
        armDataTable.getEntry("kP").setDefaultDouble(KP);
        armDataTable.getEntry("kI").setDefaultDouble(KI);
        armDataTable.getEntry("kD").setDefaultDouble(KD);

        armDataTable.getEntry("MaxAcceleration").setDefaultDouble(MAX_ACCEL);
        armDataTable.getEntry("MaxVelocity").setDefaultDouble(MAX_VELOCITY);
    }

    private void updateTelemetry() {
        armDataTable.getEntry("ArmPosition").setDouble(getPosition());
        armDataTable.getEntry("AppliedOutput").setDouble(armMotor.getAppliedOutput());
        armDataTable.getEntry("ArmCurrent").setDouble(armMotor.getOutputCurrent());
        armDataTable.getEntry("IsAtGoal").setBoolean(atGoal());
        armDataTable.getEntry("GoalPosition").setDouble(controller.getGoal().position);
        armDataTable.getEntry("ClosedLoopEnabled").setBoolean(isClosedLoopEnabled());

        controller.setP(armDataTable.getEntry("kP").getDouble(KP));
        controller.setI(armDataTable.getEntry("kI").getDouble(KI));
        controller.setD(armDataTable.getEntry("kD").getDouble(KD));

        var maxAccel = armDataTable.getEntry("MaxAcceleration").getDouble(MAX_ACCEL);
        var maxVelocity = armDataTable.getEntry("MaxVelocity").getDouble(MAX_VELOCITY);

        controller.setConstraints(new TrapezoidProfile.Constraints(maxVelocity, maxAccel));
    }

    public enum ArmPosition {
        SCORE(1),
        INTAKE(0);

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
