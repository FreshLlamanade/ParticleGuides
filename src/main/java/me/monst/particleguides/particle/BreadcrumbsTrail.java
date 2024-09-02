package me.monst.particleguides.particle;

import me.monst.particleguides.command.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * A trail of breadcrumbs which can be used to guide a player back to a specific location.
 * The breadcrumbs are stored as a list of blocks representing the path taken.
 * For efficiency reasons, the distance between the breadcrumbs is fixed at creation time and cannot be changed later
 * (we would need a more advanced and expensive data structure to support this, such as a KD-Tree).
 */
public class BreadcrumbsTrail {
    
    public enum PauseState {
        ACTIVE, PAUSED, PAUSED_AUTO_RESUME
    }
    
    private final Player player;
    private final int interBreadcrumbDistance;
    private final ArrayList<Breadcrumb> breadcrumbs;
    private final Map<Breadcrumb, Integer> breadcrumbIndexMap;
    
    private BreadcrumbsVisualizer visualizer;
    
    private int maxLength;
    private boolean maxLengthReached;
    private PauseState pauseState = PauseState.ACTIVE;
    
    public BreadcrumbsTrail(Player player, int interBreadcrumbDistance) {
        this.player = player;
        this.interBreadcrumbDistance = interBreadcrumbDistance;
        this.maxLength = getMaxPermissibleLength();
        Breadcrumb firstBreadcrumb = new Breadcrumb(player.getLocation().getBlock());
        this.breadcrumbs = new ArrayList<>();
        this.breadcrumbs.add(firstBreadcrumb);
        this.breadcrumbIndexMap = new HashMap<>();
        this.breadcrumbIndexMap.put(firstBreadcrumb, 0);
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public ArrayList<Breadcrumb> breadcrumbsList() {
        return breadcrumbs;
    }
    
    public void setVisualizer(BreadcrumbsVisualizer visualizer) {
        this.visualizer = visualizer;
    }
    
    public BreadcrumbsVisualizer getVisualizer() {
        return visualizer;
    }
    
    public void setPauseState(PauseState pauseState) {
        this.pauseState = pauseState;
    }
    
    public boolean isPaused() {
        return pauseState != PauseState.ACTIVE;
    }
    
    /**
     * Drop a breadcrumb at the specified block.
     * The breadcrumb is added to the list of breadcrumbs and the breadcrumb index map.
     * @param breadcrumb the breadcrumb to drop
     */
    private void dropBreadcrumb(Breadcrumb breadcrumb) {
        breadcrumbIndexMap.put(breadcrumb, breadcrumbs.size()); // breadcrumbs.size() returns the next index
        breadcrumbs.add(breadcrumb);
    }
    
    /**
     * Pick up all breadcrumbs starting from the specified index.
     * @param index the index from which to pick up breadcrumbs
     */
    private void pickUpBreadcrumbs(int index) {
        List<Breadcrumb> cut = breadcrumbs.subList(index, breadcrumbs.size());
        cut.forEach(breadcrumbIndexMap::remove);
        cut.clear();
    }
    
    /**
     * Calculate the maximum number of breadcrumbs the player is allowed to have.
     * @return the maximum number of breadcrumbs the player is allowed to have
     */
    private int getMaxPermissibleLength() {
        return Permissions.BREADCRUMBS_LIMIT.getPermissionLimitInt(player).orElse(Integer.MAX_VALUE);
    }
    
    /**
     * Check if the player has reached the maximum number of breadcrumbs. If the player has reached the maximum number
     * of breadcrumbs, recalculate the maximum number of breadcrumbs to be sure that the player has not been granted
     * more permissions in the meantime. If the player has reached the maximum number of breadcrumbs for the first time,
     * warn the player. Finally, set the flag to indicate that the player has been warned.
     */
    private void checkMaxLengthReached() {
        if (this.breadcrumbs.size() < maxLength) {
            maxLengthReached = false; // We are below the max length, reset the flag
            return;
        }
        if (maxLengthReached) {
            return; // We are still above the max length from last time, already warned the player
        }
        // We are above the max length for the first time, recalculate the max length to see if it has changed
        maxLength = getMaxPermissibleLength();
        if (breadcrumbs.size() < maxLength) {
            maxLengthReached = false; // The max length changed, and we have not reached it after all, so reset the flag
            return;
        }
        // Warn the player that they have reached the max length
        player.sendMessage(ChatColor.RED + "You have run out of breadcrumbs.");
        maxLengthReached = true; // We have warned the player, so set the flag
    }
    
    /**
     * The player steps on a block.
     * If this is the first time the player steps on this block, a breadcrumb is dropped.
     * If a breadcrumb already exists at the same location, the player picks up all breadcrumbs from that point onwards.
     * @param block the block the player stepped on
     */
    public void stepOnBlock(Block block) {
        if (pauseState == PauseState.PAUSED)
            return;
        Breadcrumb breadcrumb = new Breadcrumb(block);
        Integer existingIndex = breadcrumbIndexMap.get(breadcrumb);
        if (existingIndex == null) {
            if (pauseState == PauseState.PAUSED_AUTO_RESUME) {
                return;
            }
            checkMaxLengthReached();
            if (maxLengthReached) {
                return;
            }
            dropBreadcrumb(breadcrumb);
        } else {
            if (pauseState == PauseState.PAUSED_AUTO_RESUME) {
                if (existingIndex == breadcrumbs.size() - 1) {
                    pauseState = PauseState.ACTIVE;
                    visualizer.setDirection(BreadcrumbsVisualizer.Direction.YOUNGEST_TO_OLDEST);
                    player.sendMessage(ChatColor.YELLOW + "Resuming breadcrumbs trail.");
                }
            } else if (existingIndex < breadcrumbs.size() - 1) {
                pickUpBreadcrumbs(existingIndex + 1);
            }
        }
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
         * @return true if the breadcrumbs fall on the same coordinate after the division, false otherwise
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
        
        Block getBlock() {
            return block;
        }
    }
    
}
