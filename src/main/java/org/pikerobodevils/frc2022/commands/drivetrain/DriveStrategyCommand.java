/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;

public class DriveStrategyCommand extends CommandBase {
    private Drivetrain drivetrain = Drivetrain.getInstance();

    protected final DriveStrategy strategy;

    public DriveStrategyCommand(DriveStrategy strategy) {
        addRequirements(drivetrain);
        setName(getName() + "(" + strategy.getClass().getName() + ")");
        this.strategy = strategy;
    }

    @Override
    public void execute() {
        var speeds = strategy.getWheelSpeeds();
        drivetrain.driveOpenLoop(speeds.left, speeds.right);
    }
}
