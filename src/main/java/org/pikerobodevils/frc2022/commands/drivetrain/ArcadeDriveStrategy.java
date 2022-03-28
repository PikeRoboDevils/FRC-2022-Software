/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.drivetrain;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import java.util.function.DoubleSupplier;

public class ArcadeDriveStrategy implements DriveStrategy {

    private DoubleSupplier throttle, turn;

    public ArcadeDriveStrategy(DoubleSupplier throttle, DoubleSupplier turn) {
        this.throttle = throttle;
        this.turn = turn;
    }

    @Override
    public DifferentialDrive.WheelSpeeds getWheelSpeeds() {
        return DifferentialDrive.arcadeDriveIK(throttle.getAsDouble(), turn.getAsDouble(), false);
    }
}
