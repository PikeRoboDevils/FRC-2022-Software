/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib.led.patterns;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import org.pikerobodevils.lib.led.LEDPattern;

public class SolidLEDPattern implements LEDPattern {
    private final Color color;

    public SolidLEDPattern(Color color) {
        this.color = color;
    }

    @Override
    public void setLEDs(AddressableLEDBuffer buffer) {
        for (int i = 0; i < buffer.getLength(); i++) {
            buffer.setLED(i, color);
        }
    }

    @Override
    public boolean isAnimated() {
        return true;
    }
}
