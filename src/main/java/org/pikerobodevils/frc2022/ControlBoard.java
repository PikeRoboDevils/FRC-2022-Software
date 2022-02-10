/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class ControlBoard {

    private Joystick left, right, buttons;

    private ControlBoard() {
        left = new Joystick(0);
        right = new Joystick(1);
        buttons = new Joystick(2);
    }

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

    public Button getExtendClimberButton() {
        return new JoystickButton(buttons, 1);
    }

    public Button getRetractClimberButton() {
        return new JoystickButton(buttons, 2);
    }

    private static final ControlBoard INSTANCE = new ControlBoard();

    public static ControlBoard getInstance() {
        return INSTANCE;
    }
}
