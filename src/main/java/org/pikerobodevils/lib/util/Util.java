/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib.util;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.util.Color;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class Util {

    public static Attributes getManifestAttributesForClass(Object object) {
        String className = object.getClass().getSimpleName() + ".class";
        String classPath = object.getClass().getResource(className).toString();
        if (!classPath.startsWith("jar")) {
            return new Attributes();
        }

        URL url = null;
        try {
            url = new URL(classPath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return new Attributes();
        }
        JarURLConnection jarConnection = null;
        Manifest manifest = null;
        try {
            jarConnection = (JarURLConnection) url.openConnection();
            manifest = jarConnection.getManifest();
        } catch (IOException e) {
            e.printStackTrace();
            return new Attributes();
        }
        return manifest.getMainAttributes();
    }

    public static Color interpolateColor(Color one, Color two, double t) {

        var r = MathUtil.interpolate(one.red, two.red, t);
        var g = MathUtil.interpolate(one.green, two.green, t);
        var b = MathUtil.interpolate(one.blue, two.blue, t);
        return new Color(r, g, b);
    }

    public static Color setColorIntensity(Color original, double intensity) {
        return new Color(original.red * intensity, original.green * intensity, original.blue * intensity);
    }
}
