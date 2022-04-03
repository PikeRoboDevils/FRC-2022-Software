/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib.drivestrategy;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import java.util.function.DoubleSupplier;

public class TankDriveStrategy implements DriveStrategy {
    DoubleSupplier left, right;

    public TankDriveStrategy(DoubleSupplier left, DoubleSupplier right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public DifferentialDrive.WheelSpeeds getWheelSpeeds() {
        return new DifferentialDrive.WheelSpeeds(left.getAsDouble(), right.getAsDouble());
    }
}
