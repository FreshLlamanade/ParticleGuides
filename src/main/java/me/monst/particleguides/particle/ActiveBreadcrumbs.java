package me.monst.particleguides.particle;

import me.monst.particleguides.command.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import me.monst.particleguides.particle.BreadcrumbsTrail.Breadcrumb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An "active" breadcrumbs is a trail of breadcrumbs which is currently being modified by a player stepping on blocks.
 * Only one active breadcrumbs trail can exist per player at a time.
 * This does not include the visualizer for the breadcrumbs: see {@link BreadcrumbsVisualizer}.
 */
public class ActiveBreadcrumbs {
    
    public enum PauseState {
        ACTIVE, PAUSED, PAUSED_AUTO_RESUME
    }

    private final BreadcrumbsTrail breadcrumbs;
    private final Player player;
    
    private final Map<Breadcrumb, Integer> breadcrumbIndexMap;
    private int permissionMax = calculateMaxLength();
    private boolean maxLengthReached = false;
    private PauseState pauseState = PauseState.ACTIVE;
    
    public ActiveBreadcrumbs(BreadcrumbsTrail breadcrumbs, Player player) {
        this.breadcrumbs = breadcrumbs;
        this.player = player;
        this.breadcrumbIndexMap = new HashMap<>();
        for (int i = 0; i < breadcrumbs.size(); i++) {
            breadcrumbIndexMap.put(breadcrumbs.list().get(i), i);
        }
    }
    
    public BreadcrumbsTrail getBreadcrumbs() {
        return breadcrumbs;
    }
    
    public void setPauseState(PauseState pauseState) {
        this.pauseState = pauseState;
    }
    
    public boolean isPaused() {
        return pauseState != PauseState.ACTIVE;
    }
    
    public void stepOnBlock(Block block) {
        if (pauseState == PauseState.PAUSED)
            return;
        Breadcrumb breadcrumb = breadcrumbs.new Breadcrumb(block);
        Integer existingIndex = breadcrumbIndexMap.get(breadcrumb);
        if (existingIndex == null) {
            if (pauseState == PauseState.PAUSED_AUTO_RESUME)
                return;
            if (checkLimitReached())
                return;
            dropBreadcrumb(breadcrumb);
        } else {
            if (pauseState == PauseState.PAUSED_AUTO_RESUME) {
                if (existingIndex == breadcrumbs.size() - 1) {
                    pauseState = PauseState.ACTIVE;
                }
            } else if (existingIndex < breadcrumbs.size() - 1) {
                pickUpBreadcrumbs(existingIndex + 1);
            }
        }
    }
    
    private int calculateMaxLength() {
        return Permissions.BREADCRUMBS_LIMIT.getPermissionLimitInt(player).orElse(Integer.MAX_VALUE);
    }
    
    private void dropBreadcrumb(Breadcrumb breadcrumb) {
        breadcrumbs.list().add(breadcrumb);
        breadcrumbIndexMap.put(breadcrumb, breadcrumbs.size() - 1);
    }
    
    private void pickUpBreadcrumbs(int index) {
        List<Breadcrumb> cut = breadcrumbs.list().subList(index, breadcrumbs.size());
        cut.forEach(breadcrumbIndexMap::remove);
        cut.clear();
    }
    
    private boolean checkLimitReached() {
        if (this.breadcrumbs.size() < permissionMax) {
            maxLengthReached = false; // We are below the max length, reset the flag
            return false;
        }
        if (maxLengthReached) {
            return true; // We are still above the max length from last time, already warned the player
        }
        // We are above the max length for the first time, recalculate the max length to see if it has changed
        permissionMax = calculateMaxLength();
        if (breadcrumbs.size() < permissionMax) {
            maxLengthReached = false; // The max length changed, and we have not reached it after all, so reset the flag
            return false;
        }
        // Warn the player that they have reached the max length
        player.sendMessage(ChatColor.RED + "You have run out of breadcrumbs.");
        maxLengthReached = true; // We have warned the player, so set the flag
        return true;
    }

}
