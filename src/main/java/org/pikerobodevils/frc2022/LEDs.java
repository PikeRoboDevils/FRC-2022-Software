/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.pikerobodevils.lib.led.DevilAddressableLED;
import org.pikerobodevils.lib.led.LEDPattern;
import org.pikerobodevils.lib.led.patterns.SolidLEDPattern;
import org.pikerobodevils.lib.util.Util;

public class LEDs extends SubsystemBase {
    private DevilAddressableLED led = new DevilAddressableLED(1, 122);

    private LEDs() {}

    public void setPattern(LEDPattern pattern) {
        led.setPattern(pattern);
    }

    public void setColor(Color color) {
        setPattern(new SolidLEDPattern(color));
    }

    public void turnOff() {
        led.turnOff();
    }

    @Override
    public void periodic() {
        if(DriverStation.isEnabled()) {
            var color = Color.kGreen;
            if(DriverStation.getAlliance().equals(DriverStation.Alliance.Blue)) {
                color = Util.setColorIntensity(Color.kBlue, 0.75);
            } else if(DriverStation.getAlliance().equals(DriverStation.Alliance.Red)) {
                color = Util.setColorIntensity(Color.kRed, 0.75);
            }
            setColor(color);
        } else {
            turnOff();
        }
    }

    private static LEDs INSTANCE;

    public static LEDs getInstance() {
        if (INSTANCE == null) INSTANCE = new LEDs();
        return INSTANCE;
    }
}
