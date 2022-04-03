/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib.drivestrategy;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public interface DriveStrategy {
    DifferentialDrive.WheelSpeeds getWheelSpeeds();
}
