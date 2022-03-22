/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import org.pikerobodevils.frc2022.commands.arm.ArmOpenLoopCommand;
import org.pikerobodevils.frc2022.commands.arm.SetArmGoalCommand;
import org.pikerobodevils.frc2022.commands.climber.ClimberHoldPositionCommand;
import org.pikerobodevils.frc2022.commands.climber.ExtendClimberCommand;
import org.pikerobodevils.frc2022.commands.climber.RetractClimberCommand;
import org.pikerobodevils.frc2022.subsystems.Arm;
import org.pikerobodevils.frc2022.subsystems.Climber;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;
import org.pikerobodevils.frc2022.subsystems.Intake;

public class RobotContainer {

    private ControlBoard controls = ControlBoard.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();
    private Intake intake = Intake.getInstance();
    private Arm arm = Arm.getInstance();
    private Climber climber = Climber.getInstance();

    public void configureButtonBindings() {
        var drivetrainCommand =
                new RunCommand(() -> drivetrain.arcadeDrive(controls.getSpeed(), controls.getRotation()), drivetrain);
        drivetrainCommand.setName("DrivetrainDefaultCommand");
        drivetrain.setDefaultCommand(drivetrainCommand);

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

        climber.setDefaultCommand(new ClimberHoldPositionCommand());

        controls.getClimberUpButton().whileHeld(new ExtendClimberCommand());
        controls.getClimberDownButton().whileHeld(new RetractClimberCommand());

        controls.getClimberOverride()
                .whenPressed(() -> climber.enableSoftLimits(false))
                .whenReleased(new InstantCommand(() -> climber.enableSoftLimits(true)).andThen(climber::resetEncoders));
    }

    public static RobotContainer getInstance() {
        return INSTANCE;
    }

    private RobotContainer() {}

    private static final RobotContainer INSTANCE = new RobotContainer();
}
