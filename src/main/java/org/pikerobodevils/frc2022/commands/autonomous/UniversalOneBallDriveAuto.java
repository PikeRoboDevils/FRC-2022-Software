/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.autonomous;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.pikerobodevils.frc2022.commands.intake.IntakeOutCommand;

public class UniversalOneBallDriveAuto extends SequentialCommandGroup {

    public UniversalOneBallDriveAuto() {
        addCommands(new IntakeOutCommand().withTimeout(1));
        addCommands(new UniversalDriveBackAutonomous());
    }
}
