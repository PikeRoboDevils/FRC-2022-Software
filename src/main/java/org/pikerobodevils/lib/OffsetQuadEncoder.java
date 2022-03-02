/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib;

import edu.wpi.first.wpilibj.DigitalSource;
import edu.wpi.first.wpilibj.Encoder;

public class OffsetQuadEncoder extends Encoder {

    protected double offset = 0;

    public OffsetQuadEncoder(int channelA, int channelB, boolean reverseDirection) {
        super(channelA, channelB, reverseDirection);
    }

    public OffsetQuadEncoder(int channelA, int channelB) {
        super(channelA, channelB);
    }

    public OffsetQuadEncoder(int channelA, int channelB, boolean reverseDirection, EncodingType encodingType) {
        super(channelA, channelB, reverseDirection, encodingType);
    }

    public OffsetQuadEncoder(int channelA, int channelB, int indexChannel, boolean reverseDirection) {
        super(channelA, channelB, indexChannel, reverseDirection);
    }

    public OffsetQuadEncoder(int channelA, int channelB, int indexChannel) {
        super(channelA, channelB, indexChannel);
    }

    public OffsetQuadEncoder(DigitalSource sourceA, DigitalSource sourceB, boolean reverseDirection) {
        super(sourceA, sourceB, reverseDirection);
    }

    public OffsetQuadEncoder(DigitalSource sourceA, DigitalSource sourceB) {
        super(sourceA, sourceB);
    }

    public OffsetQuadEncoder(
            DigitalSource sourceA, DigitalSource sourceB, boolean reverseDirection, EncodingType encodingType) {
        super(sourceA, sourceB, reverseDirection, encodingType);
    }

    public OffsetQuadEncoder(
            DigitalSource sourceA, DigitalSource sourceB, DigitalSource indexSource, boolean reverseDirection) {
        super(sourceA, sourceB, indexSource, reverseDirection);
    }

    public OffsetQuadEncoder(DigitalSource sourceA, DigitalSource sourceB, DigitalSource indexSource) {
        super(sourceA, sourceB, indexSource);
    }

    public void setDistance(double distance) {
        offset = distance - super.getDistance();
    }

    @Override
    public void reset() {
        super.reset();
        offset = 0;
    }

    @Override
    public double getDistance() {
        return super.getDistance() + offset;
    }

    public double getOffset() {
        return offset;
    }
}
