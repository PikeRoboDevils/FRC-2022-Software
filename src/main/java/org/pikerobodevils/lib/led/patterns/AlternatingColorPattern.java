package org.pikerobodevils.lib.led.patterns;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import org.pikerobodevils.lib.led.LEDPattern;

public class AlternatingColorPattern implements LEDPattern {
    private Color[] colors;

    public AlternatingColorPattern(Color... colors){
        this.colors = colors;
    }

    @Override
    public void setLEDs(AddressableLEDBuffer buffer) {
        for (int index = 0; index < buffer.getLength(); index++){
            buffer.setLED(index, colors[index % colors.length]);
        }
    }

    @Override
    public boolean isAnimated() {
        return false;
    }
}