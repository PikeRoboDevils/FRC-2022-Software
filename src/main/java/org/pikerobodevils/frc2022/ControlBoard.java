/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import edu.wpi.first.wpilibj.Joystick;

public class ControlBoard {

    private Joystick left, right;

    private ControlBoard() {
        left = new Joystick(0);
        right = new Joystick(1);
    }

    public double getSpeed() {
        return left.getY();
    }

    public double getRotation() {
        return right.getX();
    }

    private static final ControlBoard INSTANCE = new ControlBoard();

    public static ControlBoard getInstance() {
        return INSTANCE;
    }
}
