/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.pikerobodevils.frc2022.subsystems.Climber;

public class ClimberSetPositionCommand extends CommandBase {
    private final Climber climber = Climber.getInstance();
    private final double tolerance = 1;
    private final double position;

    public ClimberSetPositionCommand(double position) {
        addRequirements(climber);
        this.position = position;
    }

    @Override
    public void initialize() {
        climber.setPositionClosedLoop(position);
    }

    @Override
    public boolean isFinished() {
        return Math.abs(climber.getLeftPosition() - position) < tolerance
                && Math.abs(climber.getRightPosition() - position) < tolerance;
    }
}
