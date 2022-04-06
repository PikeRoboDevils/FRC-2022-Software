/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVLibError;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import java.util.function.Consumer;
import java.util.function.Function;

public class DevilCANSparkMax extends CANSparkMax {
    public static final int PARAMETER_SET_ATTEMPT_COUNT = 5;

    public DevilCANSparkMax(int deviceId, MotorType type) {
        super(deviceId, type);
        restoreFactoryDefaults();
    }

    @Override
    public void set(double speed) {
        if (RobotBase.isReal()) {
            super.set(speed);
        } else {
            setVoltage(RobotController.getBatteryVoltage() * speed);
        }
    }

    /**
     * Initializes this controller with <code>initialize</code>
     * Applies <code>initialize</code> to this object. If it returns false, prints a warning and attempts again. This repeats until either the function succeeds (returns <code>true</code>) or the number of attempts reaches {@link DevilCANSparkMax#PARAMETER_SET_ATTEMPT_COUNT}.
     * @param initialize the function to run to initialize this controller. Make this return true if successful, otherwise false.
     */
    public void initialize(Function<CANSparkMax, Boolean> initialize) {
        int setAttemptNumber = 0;
        while (initialize.apply(this) != true) {
            DriverStation.reportWarning(
                    String.format(
                            "Spark Max ID %d: Failed to initialize, attempt %d of %d",
                            getDeviceId(), setAttemptNumber, PARAMETER_SET_ATTEMPT_COUNT),
                    false);
            setAttemptNumber++;

            if (setAttemptNumber >= PARAMETER_SET_ATTEMPT_COUNT) {
                DriverStation.reportError(
                        String.format("Spark Max ID %d: Failed to initialize!!", getDeviceId()), false);
                break;
            }
        }
    }

    /**
     * Checks if this controller has reset, and runs <code>toRun</code> if true.
     * @param toRun function to run if controller has reset. This object is passed to the function.
     * @return true if controller has reset (and function is run), otherwise false
     */
    public boolean ifHasReset(Consumer<CANSparkMax> toRun) {
        if (getFault(FaultID.kHasReset)) {
            toRun.accept(this);
            return true;
        }
        return false;
    }

    public static boolean check(REVLibError error) {
        return error == REVLibError.kOk;
    }

    public static REVLibError worstOne(REVLibError... errors) {
        for (REVLibError error : errors) {
            if (error != REVLibError.kOk) return error;
        }
        return REVLibError.kOk;
    }
}
