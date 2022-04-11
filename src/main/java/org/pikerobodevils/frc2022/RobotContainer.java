/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import org.pikerobodevils.frc2022.commands.arm.ArmOpenLoopCommand;
import org.pikerobodevils.frc2022.commands.arm.SetArmGoalCommand;
import org.pikerobodevils.frc2022.commands.climber.*;
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
        PIDController controller = new PIDController(0.0025, 0, 0.0025, Constants.PERIOD);
        controller.setIntegratorRange(-0.1, 0.1);

        drivetrainCommand = new GyroCorrectedDriveStrategyCommand(
                new ShivangDriveStrategy(),
                () -> (MathUtil.applyDeadband(controls.getRotation(), 0.1) == 0),
                drivetrain::getYaw,
                controller);
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

        controls.getPlayerTwo()
                .whenHeld(new WaitUntilCommand(() ->
                                Math.abs(drivetrain.getPitch()) > Constants.ControlConstants.AUTO_CLIMB_PITCH_THRESHOLD)
                        .andThen(new RetractClimberCommand()));
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
