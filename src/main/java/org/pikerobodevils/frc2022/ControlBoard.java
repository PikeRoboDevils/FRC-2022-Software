/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import edu.wpi.first.wpilibj.Joystick;

public class ControlBoard {

    private Joystick leftStick, rightStick;

    private ControlBoard() {
        leftStick = new Joystick(0);
        rightStick = new Joystick(1);
    }

    public double getThrottle() {
        return leftStick.getY();
    }

    public double getTurn() {
        return leftStick.getX();
    }

    public boolean getQuickTurn() {
        return leftStick.getRawButton(1);
    }

    private static final ControlBoard INSTANCE = new ControlBoard();

    public static ControlBoard getInstance() {
        return INSTANCE;
    }
}
