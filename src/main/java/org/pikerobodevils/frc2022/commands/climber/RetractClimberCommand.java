/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.pikerobodevils.frc2022.subsystems.Climber;

public class RetractClimberCommand extends CommandBase {
    private Climber climber = Climber.getInstance();

    public RetractClimberCommand() {
        addRequirements(climber);
    }

    @Override
    public void initialize() {
        climber.setSpeed(-1);
    }

    @Override
    public void end(boolean interrupted) {
        climber.setSpeed(0);
    }
}
