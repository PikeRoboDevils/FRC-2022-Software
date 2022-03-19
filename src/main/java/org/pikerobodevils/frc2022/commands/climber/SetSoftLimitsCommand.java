/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.climber;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import org.pikerobodevils.frc2022.subsystems.Climber;

public class SetSoftLimitsCommand extends InstantCommand {
    private boolean enable;

    public SetSoftLimitsCommand(boolean enable) {
        setName(getName() + "(" + enable + ")");
        this.enable = enable;
    }

    @Override
    public void initialize() {
        Climber.getInstance().enableSoftLimits(enable);
        System.out.println(this.getName());
    }
}
