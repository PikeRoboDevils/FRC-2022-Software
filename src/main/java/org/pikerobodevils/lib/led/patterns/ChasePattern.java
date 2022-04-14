/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib.led.patterns;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import org.pikerobodevils.lib.led.LEDPattern;

public class ChasePattern implements LEDPattern {
    private Color[] m_Colors;
    private int m_SegmentWidth;
    private int m_offset;

    public ChasePattern(int segmentWidth, Color... colors) {
        super();
        m_Colors = colors;
        m_SegmentWidth = segmentWidth;
    }

    @Override
    public void setLEDs(AddressableLEDBuffer buffer, boolean restart) {
        int numberOfColors = m_Colors.length;
        int effectiveIndex;
        int colorIndex;
        int bufferLength = buffer.getLength();
        for (int index = 0; index < bufferLength; index++) {
            effectiveIndex = (index + m_offset) % bufferLength;
            colorIndex = (index / m_SegmentWidth) % numberOfColors;
            buffer.setLED(effectiveIndex, m_Colors[colorIndex]);
        }

        m_offset = (m_offset + 1) % bufferLength;
    }

    @Override
    public boolean isAnimated() {
        return true;
    }
}
