/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.drivetrain;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import java.util.function.DoubleSupplier;

public class ArcadeDriveStrategy implements DriveStrategy {

    private DoubleSupplier speedSupplier, turnSupplier;
    private boolean squareSpeed = false;
    private boolean squareTurn = false;

    public ArcadeDriveStrategy(DoubleSupplier speedSupplier, DoubleSupplier turnSupplier) {
        this.speedSupplier = speedSupplier;
        this.turnSupplier = turnSupplier;
    }

    public ArcadeDriveStrategy() {
        this(() -> 0, () -> 0);
    }

    public ArcadeDriveStrategy withSpeed(DoubleSupplier throttle) {
        this.speedSupplier = throttle;
        return this;
    }

    public ArcadeDriveStrategy withRotation(DoubleSupplier turn) {
        this.turnSupplier = turn;
        return this;
    }

    public ArcadeDriveStrategy squareSpeed(boolean squareSpeed) {
        this.squareSpeed = squareSpeed;
        return this;
    }

    public ArcadeDriveStrategy squareTurn(boolean squareTurn) {
        this.squareTurn = squareTurn;
        return this;
    }

    public ArcadeDriveStrategy squareInputs(boolean squareInputs) {
        squareSpeed(squareInputs);
        squareTurn(squareInputs);
        return this;
    }

    @Override
    public DifferentialDrive.WheelSpeeds getWheelSpeeds() {
        double speed = speedSupplier.getAsDouble();
        double turn = turnSupplier.getAsDouble();

        if (squareSpeed) speed = Math.copySign(Math.pow(speed, 2), speed);
        if (squareTurn) turn = Math.copySign(Math.pow(turn, 2), speed);
        return DifferentialDrive.arcadeDriveIK(speed, turn, false);
    }
}
