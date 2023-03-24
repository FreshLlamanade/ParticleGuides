package me.monst.particleguides.particle;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class Breadcrumbs extends ParticleGuide {
    
    private final List<Breadcrumb> breadcrumbList;
    private final Map<Breadcrumb, Integer> breadcrumbIndexMap;
    private int permissionMax = calculateMaxLength();
    private boolean maxReached = false;
    
    public Breadcrumbs(ParticleGuidesPlugin plugin, Player player, Color color) {
        super(plugin, player, color);
        this.breadcrumbList = new ArrayList<>();
        this.breadcrumbIndexMap = new HashMap<>();
        dropBreadcrumb(new Breadcrumb(player.getLocation().getBlock()) {
            @Override
            void spawnParticle() {
                Breadcrumbs.this.highlight(getParticleLocation());
            }
        });
    }
    
    public void stepOnBlock(Block block) {
        Breadcrumb breadcrumb = new Breadcrumb(block);
        if (!breadcrumbIndexMap.containsKey(breadcrumb)) {
            if (checkLimitReached())
                return;
            dropBreadcrumb(breadcrumb);
        } else {
            int index = breadcrumbIndexMap.get(breadcrumb);
            if (index < breadcrumbList.size() - 1)
                pickUpBreadcrumbs(index + 1);
        }
    }
    
    private boolean checkLimitReached() {
        if (breadcrumbList.size() < permissionMax) {
            maxReached = false; // We are below the max length, reset the flag
            return false;
        }
        if (maxReached) {
            return true; // We are still above the max length from last time, already warned the player
        }
        // We are above the max length for the first time, recalculate the max length to see if it has changed
        permissionMax = calculateMaxLength();
        if (breadcrumbList.size() < permissionMax) {
            maxReached = false; // The max length changed, and we have not reached it after all, so reset the flag
            return false;
        }
        // Warn the player that they have reached the max length
        player.sendMessage(ChatColor.RED + "You have run out of breadcrumbs.");
        maxReached = true; // We have warned the player, so set the flag
        return true;
    }
    
    private int calculateMaxLength() {
        return Permissions.BREADCRUMBS.getPermissionLimitInt(player).orElse(-1);
    }
    
    private void dropBreadcrumb(Breadcrumb breadcrumb) {
        breadcrumbList.add(breadcrumb);
        breadcrumbIndexMap.put(breadcrumb, breadcrumbList.size() - 1);
    }
    
    private void pickUpBreadcrumbs(int index) {
        List<Breadcrumb> cut = breadcrumbList.subList(index, breadcrumbList.size());
        cut.forEach(breadcrumbIndexMap::remove);
        cut.clear();
    }
    
    @Override
    void show() {
        for (int i = breadcrumbList.size() - 1; i >= 0; i--) {
            if (i >= breadcrumbList.size())
                return;
            Breadcrumb breadcrumb = breadcrumbList.get(i);
            if (differentWorlds(player.getWorld(), breadcrumb.getWorld()))
                continue;
            breadcrumb.spawnParticle();
            sleep(plugin.config().particleDelay.get());
        }
    }
    
    private class Breadcrumb {
        
        private static final int SIZE = 3;
        
        private final Block block;
        
        Breadcrumb(Block block) {
            this.block = block;
        }
    
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Breadcrumb)) return false;
            Breadcrumb that = (Breadcrumb) o;
            return this.block.getWorld().equals(that.block.getWorld()) &&
                    this.block.getX() / SIZE == that.block.getX() / SIZE &&
                    this.block.getY() / SIZE == that.block.getY() / SIZE &&
                    this.block.getZ() / SIZE == that.block.getZ() / SIZE;
        }
    
        @Override
        public int hashCode() {
            return Objects.hash(this.block.getWorld(),
                    this.block.getX() / SIZE,
                    this.block.getY() / SIZE,
                    this.block.getZ() / SIZE);
        }
        
        World getWorld() {
            return block.getWorld();
        }
        
        Location getParticleLocation() {
            return block.getLocation().add(0.5, 0.5, 0.5);
        }
        
        void spawnParticle() {
            Breadcrumbs.this.spawnParticle(getParticleLocation());
        }
    
        @Override
        public String toString() {
            return "(" + block.getX() + ", " + block.getY() + ", " + block.getZ() + ")";
        }
    }
    
}
