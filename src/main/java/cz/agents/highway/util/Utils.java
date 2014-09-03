package cz.agents.highway.util;

import javax.vecmath.Vector3f;
import java.net.URL;

import static java.lang.Math.atan2;

public class Utils {
	
	public static URL getResourceUrl(String resource){
		URL ret = Utils.class.getClassLoader().getResource(resource);
		return ret;
	}

    public static int name2ID(String name) {
        return Integer.parseInt(name);
    }

    public static double vectorAngle(Vector3f a, Vector3f b){
        return -(atan2(b.y,b.x) - atan2(a.y,a.x));
    }

}
