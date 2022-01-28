/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import edu.wpi.first.wpilibj2.command.RunCommand;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;

public class RobotContainer {

    private ControlBoard controls = ControlBoard.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();

    public void configureButtonBindings() {
        controls.getIntakeInButton().whenHeld(new RunCommand(() -> {}));
    }

    public static RobotContainer getInstance() {
        return INSTANCE;
    }

    private RobotContainer() {}

    private static final RobotContainer INSTANCE = new RobotContainer();
}
