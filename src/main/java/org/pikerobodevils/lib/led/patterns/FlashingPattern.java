/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib.led.patterns;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import org.pikerobodevils.lib.led.LEDPattern;

public class FlashingPattern implements LEDPattern {
    private final Color color;
    private boolean on;
    private double frequency;
    Timer timer = new Timer();

    public FlashingPattern(Color color) {
        this.color = color;
        frequency = 4;
    }

    @Override
    public void setLEDs(AddressableLEDBuffer buffer, boolean restart) {
        if (restart) {
            timer.reset();
            timer.start();
            on = true;
        }
        if (timer.advanceIfElapsed(1 / frequency)) {
            var colorToSet = Color.kBlack;
            on = !on;
            if (on) {
                colorToSet = color;
            }
            for (int i = 0; i < buffer.getLength(); i++) {
                buffer.setLED(i, colorToSet);
            }
        }
    }

    @Override
    public boolean isAnimated() {
        return true;
    }
}
