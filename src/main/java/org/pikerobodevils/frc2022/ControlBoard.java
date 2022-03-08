/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import static org.pikerobodevils.frc2022.Constants.ControlBoardConstants.*;

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
    private final Button intakeOutButton = new JoystickButton(buttons, 14);

    private final Button driverLeftTrigger = new JoystickButton(left, 1);
    private final Button driverRightTrigger = new JoystickButton(right, 1);

    private final Button driverLeftThumb = new JoystickButton(left, 2);
    private final Button driverRightThumb = new JoystickButton(right, 2);

    private final Button armUpButton = new JoystickButton(buttons, 6);
    private final Button armDownButton = new JoystickButton(buttons, 2);

    private final Button manualModeSwitch =
            new Button(new JoystickButton(buttons, 19).negate().or(new Trigger(() -> ARM_MANUAL_MODE_ALWAYS)));

    private ControlBoard() {}

    public double getSpeed() {
        double speed = left.getY() * 0.75;
        if (speed == 0) return 0;
        return left.getY() * -1;
    }

    public double getRotation() {
        return right.getX();
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

    public boolean isManualMode() {
        return manualModeSwitch.get();
    }

    private static final ControlBoard INSTANCE = new ControlBoard();

    public static ControlBoard getInstance() {
        return INSTANCE;
    }
}
