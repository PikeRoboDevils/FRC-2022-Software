/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import edu.wpi.first.wpilibj2.command.StartEndCommand;
import org.pikerobodevils.frc2022.commands.arm.ArmOpenLoopCommand;
import org.pikerobodevils.frc2022.commands.arm.SetArmGoalCommand;
import org.pikerobodevils.frc2022.subsystems.Arm;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;
import org.pikerobodevils.frc2022.subsystems.Intake;

public class RobotContainer {

    private ControlBoard controls = ControlBoard.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();
    private Intake intake = Intake.getInstance();
    private Arm arm = Arm.getInstance();

    public void configureButtonBindings() {
        controls.getIntakeInButton().whileHeld(new StartEndCommand(intake::intakeIn, intake::disable, intake));
        controls.getIntakeOutButton().whileHeld(new StartEndCommand(intake::intakeOut, intake::disable, intake));

        controls.getArmUpButton().whenPressed(new SetArmGoalCommand(Arm.ArmPosition.SCORE));
        controls.getArmDownButton().whenPressed(new SetArmGoalCommand(Arm.ArmPosition.INTAKE));

        // While manual override switch is active, use joystick input. Do not allow interruptions to prevent Up/Down
        // buttons from doing anything
        // When it's released, reenable the controller //TODO: this might not be desired
        controls.getManualModeSwitch()
                .whileHeld(new ArmOpenLoopCommand(controls::getArmManualOutput), false)
                .whenReleased(arm::enableClosedLoop, arm);
    }

    public static RobotContainer getInstance() {
        return INSTANCE;
    }

    private RobotContainer() {}

    private static final RobotContainer INSTANCE = new RobotContainer();
}
