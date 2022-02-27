/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
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
    private final Button intakeOutButton = new JoystickButton(buttons, 14);

    private final Button armUpButton = new JoystickButton(buttons, 1);
    private final Button armDownButton = new JoystickButton(buttons, 2);

    private final Button manualModeSwitch = new JoystickButton(buttons, 3);

    private ControlBoard() {}

    public double getSpeed() {
        double speed = left.getY();
        if (speed == 0) return 0;
        return left.getY() * -1;
    }

    public double getRotation() {
        return right.getX();
    }

    public Button getIntakeInButton() {
        return new JoystickButton(buttons, 11);
    }

    public Button getIntakeOutButton() {
        return new JoystickButton(buttons, 14);
    }

    public Button getArmUpButton() {
        return new Button(armUpButton.or(armUpAxisTrigger));
    }

    public Button getArmDownButton() {
        return new Button(armDownButton.or(armDownAxisTrigger));
    }

    public double getArmManualOutput() {
        return operatorJoystick.getY();
    }

    public Button getManualModeSwitch() {
        return manualModeSwitch;
    }

    public boolean isManualMode() {
        return manualModeSwitch.get();
    }

    private static final ControlBoard INSTANCE = new ControlBoard();

    public static ControlBoard getInstance() {
        return INSTANCE;
    }
}
