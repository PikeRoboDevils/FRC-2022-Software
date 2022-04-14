/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib.led.patterns;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import org.pikerobodevils.lib.led.LEDPattern;
import org.pikerobodevils.lib.util.Util;

public class ScannerPattern implements LEDPattern {

    Timer timer = new Timer();
    private Color m_EyeColor;
    private Color m_BackgroundColor;
    private int m_Length;
    private double period;
    private int m_eyePosition = 0;
    private int m_scanDirection = 1;

    /**
     *
     * @param highColor Brightest color
     */
    public ScannerPattern(Color eyeColor, int length) {
        this(eyeColor, Color.kBlack, length, 15);
    }

    public ScannerPattern(Color eyeColor, Color backgroundColor, int length, double speed) {
        this.m_EyeColor = eyeColor;
        this.m_BackgroundColor = backgroundColor;
        this.m_Length = length;
        this.period = 1 / speed;
    }

    @Override
    public void setLEDs(AddressableLEDBuffer buffer) {
        timer.start();
        int bufferLength = buffer.getLength();
        double intensity;
        double red;
        double green;
        double blue;
        double distanceFromEye;

        if (timer.advanceIfElapsed(period)) {
            for (int index = 0; index < bufferLength; index++) {
                distanceFromEye = MathUtil.clamp(Math.abs(m_eyePosition - index), 0, m_Length);
                intensity = 1 - distanceFromEye / m_Length;
                buffer.setLED(index, Util.interpolateColor(m_BackgroundColor, m_EyeColor, intensity));
            }

            if (m_eyePosition == 0) {
                m_scanDirection = 1;
            } else if (m_eyePosition == bufferLength - 1) {
                m_scanDirection = -1;
            }

            m_eyePosition += m_scanDirection;
        }
    }

    @Override
    public boolean isAnimated() {
        return true;
    }
}
