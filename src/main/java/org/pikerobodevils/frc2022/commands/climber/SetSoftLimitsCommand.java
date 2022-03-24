/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.climber;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import org.pikerobodevils.frc2022.subsystems.Climber;

public class SetSoftLimitsCommand extends InstantCommand {

    public SetSoftLimitsCommand(boolean enable) {
        super(() -> Climber.getInstance().enableSoftLimits(enable));
        addRequirements(Climber.getInstance());
        setName(getName() + "(" + enable + ")");
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
