/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022.commands.arm;

import edu.wpi.first.wpilibj2.command.CommandBase;
import java.util.function.DoubleSupplier;
import org.pikerobodevils.frc2022.subsystems.Arm;

public class ArmOpenLoopCommand extends CommandBase {
    private final DoubleSupplier supplier;

    private final Arm arm = Arm.getInstance();

    public ArmOpenLoopCommand(DoubleSupplier supplier) {
        addRequirements(arm);
        this.supplier = supplier;
    }

    @Override
    public void initialize() {
        arm.disableClosedLoop();
        arm.disable();
    }

    @Override
    public void execute() {
        arm.runOpenLoop(supplier.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        // Disable motor output
        arm.disable();
        // Reenable the controller
        // TODO: maybe not needed/wanted
        arm.enableClosedLoop();
    }
}
