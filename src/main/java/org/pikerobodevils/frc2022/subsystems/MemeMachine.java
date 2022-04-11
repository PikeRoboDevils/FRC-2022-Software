/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.subsystems;

import edu.wpi.first.hal.FRCNetComm;
import edu.wpi.first.hal.HAL;
import org.pikerobodevils.frc2022.Constants;

public class MemeMachine {

    public static void initializeMemes() {
        for (int i = 0; i < Constants.MEME_COUNT; i++) {
            HAL.report(FRCNetComm.tResourceType.kResourceType_NidecBrushless, i + 1);
        }
    }
}