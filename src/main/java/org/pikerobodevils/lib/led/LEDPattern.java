package org.pikerobodevils.lib.led;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public interface LEDPattern {
    void setLEDs(AddressableLEDBuffer buffer);
    default boolean isAnimated() {
        return true;
    }
}
