/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import java.util.function.DoubleSupplier;

public class AxisTrigger extends Trigger {

    DoubleSupplier supplier;
    double threshold;
    boolean lessThan;

    public AxisTrigger(DoubleSupplier supplier, double threshold, boolean lessThan) {
        this.supplier = supplier;
        this.threshold = threshold;
        this.lessThan = lessThan;
    }

    public AxisTrigger(DoubleSupplier supplier, double threshold) {
        this(supplier, threshold, false);
    }

    @Override
    public boolean getAsBoolean() {
        if (lessThan) {
            return supplier.getAsDouble() <= threshold;
        } else {
            return supplier.getAsDouble() >= threshold;
        }
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
