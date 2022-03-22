/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib;

import edu.wpi.first.hal.HAL;

public class TestUtil {
    public static void hardwareSimTestSetup() {
        assert HAL.initialize(500, 0);
    }
}
