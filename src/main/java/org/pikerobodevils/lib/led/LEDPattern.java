/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib.led;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public interface LEDPattern {
    void setLEDs(AddressableLEDBuffer buffer);

    default void reset() {}

    default boolean isAnimated() {
        return true;
    }
}
