/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.lib;

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
}
