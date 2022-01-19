/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib;

import com.revrobotics.CANSparkMax;

public class DefaultCANSparkMax extends CANSparkMax {
    public DefaultCANSparkMax(int deviceId, MotorType type) {
        super(deviceId, type);
        restoreFactoryDefaults();
    }
}
