/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib.util;

import java.util.function.BooleanSupplier;

public class OneShot implements BooleanSupplier {
    private BooleanSupplier supplier;
    private boolean prev = false;

    public OneShot(BooleanSupplier supplier) {
        this.supplier = supplier;
    }

    public boolean get() {
        var value = supplier.getAsBoolean();
        var returnValue = value && !prev;
        prev = value;
        return returnValue;
    }

    @Override
    public boolean getAsBoolean() {
        return get();
    }
}
