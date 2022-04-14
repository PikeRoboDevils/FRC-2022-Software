/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.drivetrain;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;
import org.pikerobodevils.lib.drivestrategy.DriveStrategy;

public class GyroCorrectedDriveStrategyCommand extends CommandBase {
    private final DriveStrategy strategy;
    private final BooleanSupplier shouldCorrect;
    private final DoubleSupplier gyro;
    private final PIDController correctionController;

    private boolean prevShouldCorrect = false;

    private Drivetrain drivetrain = Drivetrain.getInstance();

    public GyroCorrectedDriveStrategyCommand(
            DriveStrategy strategy,
            BooleanSupplier shouldCorrect,
            DoubleSupplier gyro,
            PIDController correctionController) {
        this.strategy = strategy;
        this.shouldCorrect = shouldCorrect;
        this.gyro = gyro;
        this.correctionController = correctionController;
        this.correctionController.enableContinuousInput(-180, 180);
        this.correctionController.setTolerance(0.1);
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        correctionController.reset();
        correctionController.setSetpoint(gyro.getAsDouble());
        prevShouldCorrect = false;
    }

    @Override
    public void execute() {
        var speeds = strategy.getWheelSpeeds();
        double correction = 0;
        boolean correct = shouldCorrect.getAsBoolean();
        if (correct && !prevShouldCorrect) {
            correctionController.reset();
            correctionController.setSetpoint(gyro.getAsDouble());
        }
        prevShouldCorrect = correct;
        if (DriverStation.isAutonomous()) {
            correct = false;
        }
        if (correct) {
            correction = correctionController.calculate(gyro.getAsDouble());
            correction = MathUtil.applyDeadband(correction, 0.02);
        }
        speeds.left += correction;
        speeds.right -= correction;
        drivetrain.driveOpenLoop(speeds.left, speeds.right);
    }
}
