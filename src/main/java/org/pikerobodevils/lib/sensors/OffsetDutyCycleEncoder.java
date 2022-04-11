/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib.sensors;

import edu.wpi.first.wpilibj.DigitalSource;
import edu.wpi.first.wpilibj.DutyCycle;
import edu.wpi.first.wpilibj.DutyCycleEncoder;

public class OffsetDutyCycleEncoder extends DutyCycleEncoder {
    protected double positionOffset = 0;

    public OffsetDutyCycleEncoder(int channel) {
        super(channel);
    }

    public OffsetDutyCycleEncoder(DutyCycle dutyCycle) {
        super(dutyCycle);
    }

    public OffsetDutyCycleEncoder(DigitalSource source) {
        super(source);
    }

    @Override
    public void reset() {
        positionOffset = 0;
        super.reset();
    }

    public void setPositionOffset(double positionOffset) {
        this.positionOffset = positionOffset;
    }

    @Override
    public double get() {
        return super.get() + positionOffset;
    }
}
