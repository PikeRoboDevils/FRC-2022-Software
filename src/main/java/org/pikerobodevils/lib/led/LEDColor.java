/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib.led;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import org.pikerobodevils.lib.util.Util;

public class LEDColor extends Color {
    public LEDColor(double red, double green, double blue) {
        super(red, green, blue);
    }

    public LEDColor(Color8Bit color) {
        super(color);
    }

    public LEDColor(Color color) {
        this(color.red, color.blue, color.green);
    }

    public LEDColor interpolate(Color b, double t) {
        return new LEDColor(Util.interpolateColor(this, b, t));
    }

    public LEDColor intensity(double intensity) {
        return new LEDColor(this.red * intensity, this.blue * intensity, this.green * intensity);
    }
}
