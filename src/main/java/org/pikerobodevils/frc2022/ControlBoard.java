/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import static org.pikerobodevils.frc2022.Constants.ControlBoardConstants.*;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import org.pikerobodevils.lib.AxisTrigger;

public class ControlBoard {

    private final Joystick left = new Joystick(0);
    private final Joystick right = new Joystick(1);
    private final Joystick buttons = new Joystick(2);
    private final Joystick operatorJoystick = new Joystick(3);

    private final double axisTriggerThreshold = 0.5;

    private final AxisTrigger armUpAxisTrigger = new AxisTrigger(() -> -operatorJoystick.getY(), axisTriggerThreshold);
    private final AxisTrigger armDownAxisTrigger =
            new AxisTrigger(() -> -operatorJoystick.getY(), -axisTriggerThreshold, true);

    private final AxisTrigger intakeInAxisTrigger = new AxisTrigger(operatorJoystick::getX, axisTriggerThreshold);
    private final AxisTrigger intakeOutAxisTrigger =
            new AxisTrigger(operatorJoystick::getX, -axisTriggerThreshold, true);

    private final Button intakeInButton = new JoystickButton(buttons, 11);
    private final Button intakeOutButton = new JoystickButton(buttons, 2);

    private final Button driverLeftTrigger = new JoystickButton(left, 1);
    private final Button driverRightTrigger = new JoystickButton(right, 1);

    private final Button driverLeftThumb = new JoystickButton(left, 2);
    private final Button driverRightThumb = new JoystickButton(right, 2);

    private final Button armUpButton = new JoystickButton(buttons, 7);
    private final Button armDownButton = new JoystickButton(buttons, 16);

    private final Button climberUpButton = new JoystickButton(buttons, 3);
    private final Button climberDownButton = new JoystickButton(buttons, 14);

    private final Button climberOverride = new Button(new JoystickButton(buttons, 1).negate());

    private final Button manualModeSwitch = new Button(new JoystickButton(buttons, 10)
            .negate()
            .or(new Trigger(() -> ARM_MANUAL_MODE_ALWAYS))
            .and(new Trigger(() -> DriverStation.isTeleopEnabled())));

    private ControlBoard() {}

    public double getSpeed() {
        double speed = left.getY() * 1;
        if (speed == 0) return 0;
        return left.getY() * -1;
    }

    public double getRotation() {
        var rawTurn = right.getX();
        return Math.signum(rawTurn) * Math.pow(rawTurn * 1, 2);
    }

    public Button getIntakeInButton() {
        return new Button(intakeInButton.or(intakeInAxisTrigger).or(driverRightTrigger));
    }

    public Button getIntakeOutButton() {
        return new Button(intakeOutButton.or(intakeOutAxisTrigger).or(driverLeftTrigger));
    }

    public Button getDriverLeftTrigger() {
        return driverLeftTrigger;
    }

    public Button getDriverRightTrigger() {
        return driverRightTrigger;
    }

    public Button getArmUpButton() {
        return new Button(armUpButton.or(armUpAxisTrigger).or(driverLeftThumb));
    }

    public Button getArmDownButton() {
        return new Button(armDownButton.or(armDownAxisTrigger).or(driverRightThumb));
    }

    public double getArmManualOutput() {
        return -operatorJoystick.getY();
    }

    public Button getManualModeSwitch() {
        return manualModeSwitch;
    }

    public Button getClimberOverride() {
        return climberOverride;
    }

    public Button getClimberUpButton() {
        return climberUpButton;
    }

    public Button getClimberDownButton() {
        return climberDownButton;
    }

    public boolean isManualMode() {
        return manualModeSwitch.get();
    }

    private static final ControlBoard INSTANCE = new ControlBoard();

    public static ControlBoard getInstance() {
        return INSTANCE;
    }
}
