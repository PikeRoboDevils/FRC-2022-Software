/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.first.wpilibj.simulation.EncoderSim;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestOffsetQuadEncoder {
    private OffsetQuadEncoder encoder;
    private EncoderSim simEncoder;

    @BeforeEach
    void testSetup() {
        TestUtil.hardwareSimTestSetup();
        encoder = new OffsetQuadEncoder(0, 1);
        simEncoder = new EncoderSim(encoder);
    }

    @AfterEach
    void testTeardown() {
        encoder.close();
    }

    @Test
    void testSetDistancePositive() {
        simEncoder.setDistance(0);
        encoder.setDistance(5);

        assertEquals(encoder.getDistance(), 5);

        simEncoder.setDistance(10);

        assertEquals(encoder.getDistance(), 15);

        simEncoder.setDistance(-15);

        assertEquals(encoder.getDistance(), -10);
    }

    @Test
    void testSetDistanceNegative() {
        simEncoder.setDistance(0);
        encoder.setDistance(-10);

        assertEncoderDistance(-10);

        simEncoder.setDistance(5);

        assertEncoderDistance(-5);

        simEncoder.setDistance(-10);

        assertEncoderDistance(-20);
    }

    @Test
    void testReset() {
        simEncoder.setDistance(50);
        encoder.setDistance(30);

        assertEncoderDistance(30);

        encoder.reset();

        assertEquals(simEncoder.getDistance(), 0);
        assertEquals(encoder.getRaw(), 0);
        assertEncoderDistance(0);
    }

    void assertEncoderDistance(double expected) {
        assertEquals(encoder.getDistance(), expected);
    }
}
