package org.pikerobodevils.lib;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class WaitAndReserveCommand extends WaitCommand {
    public WaitAndReserveCommand(double seconds, Subsystem... requirements) {
        super(seconds);
        addRequirements(requirements);
    }
}
