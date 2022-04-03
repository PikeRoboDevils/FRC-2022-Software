/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib.drivestrategy;

import java.util.function.DoubleSupplier;

public class DriveStrategies {
    public static ArcadeDriveStrategy arcadeDrive(DoubleSupplier speed, DoubleSupplier turn) {
        return new ArcadeDriveStrategy(speed, turn);
    }

    public static ArcadeDriveStrategy arcadeDrive() {
        return new ArcadeDriveStrategy();
    }
}
