package me.monst.particleguides.particle;

import org.bukkit.Location;

public class Vector {
    
    private final double x;
    private final double y;
    private final double z;
    
    public static Vector between(Location start, Location end) {
        return new Vector(end.getX() - start.getX(), end.getY() - start.getY(), end.getZ() - start.getZ());
    }
    
    private Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Location awayFrom(Location location) {
        return location.clone().add(x, y, z);
    }
    
    public Vector multiply(double scalar) {
        return new Vector(x * scalar, y * scalar, z * scalar);
    }
    
    public Vector divide(double scalar) {
        return new Vector(x / scalar, y / scalar, z / scalar);
    }
    
    public double dot(Vector other) {
        return x * other.x + y * other.y + z * other.z;
    }
    
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }
    
}
