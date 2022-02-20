package org.pikerobodevils.lib;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestAxisTrigger {

    private double axisValue = 0;

    @BeforeEach
    void setUp() {
        setAxisValue(0);
    }

    @Test
    public void testAxisTriggerPositive() {
        Trigger trigger = new AxisTrigger(this::getAxisValue, 0.45);

        setAxisValue(-0.1);
        assertFalse(trigger.get());

        setAxisValue(0.2);
        assertFalse(trigger.get());

        setAxisValue(0.45);
        assertTrue(trigger.get());

        setAxisValue(0.48);
        assertTrue(trigger.get());
    }

    @Test
    void testAxisTriggerNegative() {
        Trigger trigger = new AxisTrigger(this::getAxisValue, -0.45, true);

        setAxisValue(0.24);
        assertFalse(trigger.get());

        setAxisValue(-0.1);
        assertFalse(trigger.get());

        setAxisValue(-0.45);
        assertTrue(trigger.get());

        setAxisValue(-0.48);
        assertTrue(trigger.get());


    }

    public double getAxisValue() {
        return axisValue;
    }

    public void setAxisValue(double axisValue) {
        this.axisValue = axisValue;
    }
}
