/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import edu.wpi.first.wpilibj2.command.StartEndCommand;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;
import org.pikerobodevils.frc2022.subsystems.Intake;

public class RobotContainer {

    private ControlBoard controls = ControlBoard.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();
    private Intake intake = Intake.getInstance();

    public void configureButtonBindings() {
        controls.getIntakeInButton().whileHeld(new StartEndCommand(intake::intakeIn, intake::disable, intake));
    }

    public static RobotContainer getInstance() {
        return INSTANCE;
    }

    private RobotContainer() {}

    private static final RobotContainer INSTANCE = new RobotContainer();
}
