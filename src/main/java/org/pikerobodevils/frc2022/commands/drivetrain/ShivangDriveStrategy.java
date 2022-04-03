/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.drivetrain;

import org.pikerobodevils.frc2022.ControlBoard;

public class ShivangDriveStrategy extends ArcadeDriveStrategy {
    ControlBoard controls = ControlBoard.getInstance();

    public ShivangDriveStrategy() {
        withSpeed(controls::getSpeed).withRotation(controls::getRotation);
    }
}
