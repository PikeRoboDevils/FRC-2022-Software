/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;

public class DefaultCANSparkMax extends CANSparkMax {
    public DefaultCANSparkMax(int deviceId, MotorType type) {
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
}
