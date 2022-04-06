/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.pikerobodevils.frc2022.subsystems.Intake;

public class IntakeInCommand extends CommandBase {
    private final Intake intake = Intake.getInstance();

    public IntakeInCommand() {
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.intakeIn();
    }

    @Override
    public void end(boolean interrupted) {
        intake.disable();
    }
}
