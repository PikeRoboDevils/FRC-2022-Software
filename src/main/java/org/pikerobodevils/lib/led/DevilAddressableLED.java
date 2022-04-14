/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib.led;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.util.Color;
import org.pikerobodevils.lib.led.patterns.SolidLEDPattern;

public class DevilAddressableLED extends AddressableLED {
    private AddressableLEDBuffer buffer;

    private Notifier updateNotifier;

    private LEDPattern pattern;

    private final double animationDT = 0.005;

    public DevilAddressableLED(int port, int length) {
        super(port);
        updateNotifier = new Notifier(() -> {
            update(false);
        });
        buffer = new AddressableLEDBuffer(length);
        setLength(length);
        setData(buffer);
        start();
        turnOff();
    }

    public void setPattern(LEDPattern pattern) {
        updateNotifier.stop();
        this.pattern = pattern;
        update(true);
        if (pattern.isAnimated()) {
            updateNotifier.startPeriodic(animationDT);
        }
    }

    public void turnOff() {
        setPattern(new SolidLEDPattern(Color.kBlack));
    }

    public void update(boolean restart) {
        pattern.setLEDs(buffer, restart);
        setData(buffer);
    }
}
