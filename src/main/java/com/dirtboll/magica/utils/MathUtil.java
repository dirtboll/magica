package com.dirtboll.magica.utils;

import net.minecraft.world.phys.Vec3;

public class MathUtil {
    /**
     * Given rotational unit vector 'up', rotate vector 'v' for 'deg' degrees.
     */
    public static Vec3 rodriguesRot(Vec3 v, Vec3 up, float deg) {
        var rad = java.lang.Math.toRadians(deg);
        var co = java.lang.Math.cos(rad);
        var si = java.lang.Math.sin(rad);
        var d = up.dot(v);
        var oneM = 1 - co;
        return v.multiply(co, co, co).add(up.cross(v).multiply(si, si, si)).add(up.multiply(d, d, d).multiply(oneM, oneM, oneM));
    }
}
