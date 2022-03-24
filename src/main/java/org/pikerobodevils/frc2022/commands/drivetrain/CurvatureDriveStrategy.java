/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.drivetrain;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class CurvatureDriveStrategy implements DriveStrategy {

    DoubleSupplier throttle, turn;
    BooleanSupplier quickTurn;

    public CurvatureDriveStrategy(DoubleSupplier throttle, DoubleSupplier turn, BooleanSupplier quickTurn) {
        this.throttle = throttle;
        this.turn = turn;
        this.quickTurn = quickTurn;
    }

    @Override
    public DifferentialDrive.WheelSpeeds getWheelSpeeds() {
        return DifferentialDrive.curvatureDriveIK(throttle.getAsDouble(), turn.getAsDouble(), quickTurn.getAsBoolean());
    }
}
