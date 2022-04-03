/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import org.pikerobodevils.frc2022.commands.arm.ArmOpenLoopCommand;
import org.pikerobodevils.frc2022.commands.arm.SetArmGoalCommand;
import org.pikerobodevils.frc2022.commands.climber.ClimberHoldPositionCommand;
import org.pikerobodevils.frc2022.commands.climber.ExtendClimberCommand;
import org.pikerobodevils.frc2022.commands.climber.RetractClimberCommand;
import org.pikerobodevils.frc2022.commands.climber.SetSoftLimitsCommand;
import org.pikerobodevils.frc2022.commands.drivetrain.*;
import org.pikerobodevils.frc2022.subsystems.Arm;
import org.pikerobodevils.frc2022.subsystems.Climber;
import org.pikerobodevils.frc2022.subsystems.Drivetrain;
import org.pikerobodevils.frc2022.subsystems.Intake;
import org.pikerobodevils.lib.WaitAndReserveCommand;

public class RobotContainer {

    private final ControlBoard controls = ControlBoard.getInstance();
    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final Intake intake = Intake.getInstance();
    private final Arm arm = Arm.getInstance();
    private final Climber climber = Climber.getInstance();

    public void configureButtonBindings() {
        Command drivetrainCommand;
        drivetrainCommand = new DriveStrategyCommand(new ShivangDriveStrategy());

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
                .whenPressed(new SetSoftLimitsCommand(false))
                .whenReleased(new SetSoftLimitsCommand(true)
                        .andThen(climber::resetEncoders, climber)
                        .andThen(new WaitAndReserveCommand(2, climber)));
    }

    private static RobotContainer INSTANCE = new RobotContainer();

    public static RobotContainer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RobotContainer();
        }
        return INSTANCE;
    }

    private RobotContainer() {}
}
