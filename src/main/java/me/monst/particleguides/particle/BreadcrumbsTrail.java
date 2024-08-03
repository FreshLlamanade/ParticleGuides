package me.monst.particleguides.particle;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.*;

/**
 * A trail of breadcrumbs which can be used to guide a player back to a specific location.
 * The breadcrumbs are stored as a list of blocks representing the path taken.
 * For efficiency reasons, the distance between the breadcrumbs is fixed at creation time and cannot be changed later
 * (we would need a more advanced and expensive data structure in {@link ActiveBreadcrumbs} to support this, such as a KD-Tree).
 */
public class BreadcrumbsTrail {
    
    // A trail will only have a name if it has been saved to the database
    private String name;
    private final List<Breadcrumb> breadcrumbList;
    private final int interBreadcrumbDistance;
    
    public BreadcrumbsTrail(Block firstBlock, int interBreadcrumbDistance) {
        this.breadcrumbList = new ArrayList<>();
        this.breadcrumbList.add(new Breadcrumb(firstBlock));
        this.interBreadcrumbDistance = interBreadcrumbDistance;
    }
    
    public List<Breadcrumb> list() {
        return breadcrumbList;
    }
    
    public int size() {
        return breadcrumbList.size();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * A single breadcrumb in this trail.
     * This inner class is not static because it relies on the trail's inter-breadcrumb distance
     * in order to calculate the hash code and equality, which is a convenient way to only drop one
     * breadcrumb every N blocks.
     */
    public class Breadcrumb {
        
        private final Block block;
        
        Breadcrumb(Block block) {
            this.block = block;
        }
        
        /**
         * Two breadcrumbs are considered equal if they fall on the same coordinate after
         * a lossy division by the inter-breadcrumb distance.
         * This effectively subdivides the X-Y-Z space into a square grid with a side length of interBreadcrumbDistance.
         * @param o the other breadcrumb
         * @return true if the breadcrumbs are equal, false otherwise
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Breadcrumb)) return false;
            Breadcrumb that = (Breadcrumb) o;
            return this.block.getWorld().equals(that.block.getWorld()) &&
                    this.block.getX() / interBreadcrumbDistance == that.block.getX() / interBreadcrumbDistance &&
                    this.block.getY() / interBreadcrumbDistance == that.block.getY() / interBreadcrumbDistance &&
                    this.block.getZ() / interBreadcrumbDistance == that.block.getZ() / interBreadcrumbDistance;
        }
        
        /**
         * The hash code is calculated based on the block's coordinates divided by the inter-breadcrumb distance.
         * Two breadcrumbs that fall on the same coordinate after this division will have the same hash code.
         * @return the hash code
         */
        @Override
        public int hashCode() {
            return Objects.hash(this.block.getWorld(),
                    this.block.getX() / interBreadcrumbDistance,
                    this.block.getY() / interBreadcrumbDistance,
                    this.block.getZ() / interBreadcrumbDistance);
        }
        
        Location getLocation() {
            return block.getLocation().add(0.5, 0.5, 0.5);
        }
        
        @Override
        public String toString() {
            return "(" + block.getX() + ", " + block.getY() + ", " + block.getZ() + ")";
        }
    }
    
}
