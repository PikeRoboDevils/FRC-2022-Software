/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib.led.patterns;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import org.pikerobodevils.lib.led.LEDPattern;

public class RainbowPattern implements LEDPattern {
    private int firstHue = 0;
    private double speed = 15;

    Timer timer = new Timer();

    public RainbowPattern() {
        timer.start();
    }

    @Override
    public void reset() {
        timer.reset();
    }

    @Override
    public void setLEDs(AddressableLEDBuffer buffer) {
        if (timer.advanceIfElapsed(1 / speed)) {

            int currentHue;
            for (int index = 0; index < buffer.getLength(); index++) {
                currentHue = (firstHue + (index * 180 / buffer.getLength())) % 180;
                buffer.setHSV(index, currentHue, 255, 128);
            }

            firstHue = (firstHue + 3) % 180;
        }
    }

    public RainbowPattern setSpeed(double speed) {
        this.speed = speed;
        return this;
    }

    public boolean isAnimated() {
        return true;
    }
}
