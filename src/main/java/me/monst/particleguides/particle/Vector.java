package me.monst.particleguides.particle;

import org.bukkit.Location;

/**
 * A vector through 3D space.
 */
public class Vector {
    
    private final double x;
    private final double y;
    private final double z;
    
    /**
     * Returns a vector spanning from the start location to the end location.
     * @param start the start location
     * @param end the end location
     * @return the vector from start to end
     */
    public static Vector between(Location start, Location end) {
        return new Vector(end.getX() - start.getX(), end.getY() - start.getY(), end.getZ() - start.getZ());
    }
    
    /**
     * Constructs a new vector with the given components.
     * @param x the x component
     * @param y the y component
     * @param z the z component
     */
    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Returns the location that is *this vector* far away from the given location.
     * @param location the location to add this vector to
     * @return the location that is this vector away from the given location
     */
    public Location awayFrom(Location location) {
        return location.clone().add(x, y, z);
    }
    
    /**
     * Scalar multiplication of this vector.
     * @param scalar the scalar to multiply by
     * @return a new vector which is this vector multiplied by the scalar
     */
    public Vector multiply(double scalar) {
        return new Vector(x * scalar, y * scalar, z * scalar);
    }
    
    /**
     * Scalar division of this vector.
     * @param scalar the scalar to divide by
     * @return a new vector which is this vector divided by the scalar
     */
    public Vector divide(double scalar) {
        return new Vector(x / scalar, y / scalar, z / scalar);
    }
    
    /**
     * Returns the dot product of this vector and another vector.
     * @param other the other vector
     * @return the dot product of this vector and the other vector
     */
    public double dot(Vector other) {
        return x * other.x + y * other.y + z * other.z;
    }
    
    /**
     * Returns the length of this vector.
     * @return the length of this vector
     */
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }
    
}
