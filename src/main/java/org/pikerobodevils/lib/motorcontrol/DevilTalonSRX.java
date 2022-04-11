/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib.motorcontrol;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DriverStation;
import java.util.function.Function;

public class DevilTalonSRX extends WPI_TalonSRX {

    public static final int PARAMETER_SET_ATTEMPT_COUNT = 5;

    public DevilTalonSRX(int deviceNumber) {
        super(deviceNumber);
    }

    public void configAllSettingsWithRetry(TalonSRXConfiguration allConfigs) {
        initialize((controller) -> configAllSettings(allConfigs).equals(ErrorCode.OK));
    }

    /**
     * Initializes this controller with <code>initialize</code>
     * Applies <code>initialize</code> to this object. If it returns false, prints a warning and attempts again. This repeats until either the function succeeds (returns <code>true</code>) or the number of attempts reaches {@link DevilCANSparkMax#PARAMETER_SET_ATTEMPT_COUNT}.
     * @param initialize the function to run to initialize this controller. Make this return true if successful, otherwise false.
     */
    public void initialize(Function<TalonSRX, Boolean> initialize) {
        int setAttemptNumber = 0;
        while (initialize.apply(this) != true) {
            DriverStation.reportWarning(
                    String.format(
                            "TalonSRX ID %d: Failed to initialize, attempt %d of %d",
                            getDeviceID(), setAttemptNumber, PARAMETER_SET_ATTEMPT_COUNT),
                    false);
            setAttemptNumber++;

            if (setAttemptNumber >= PARAMETER_SET_ATTEMPT_COUNT) {
                DriverStation.reportError(
                        String.format("TalonSRX ID %d: Failed to initialize!!", getDeviceID()), false);
                break;
            }
        }
    }
}
