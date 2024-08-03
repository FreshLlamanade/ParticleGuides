package me.monst.particleguides.particle;

import me.monst.particleguides.ParticleGuidesPlugin;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * A guide that shows particles to the player.
 */
abstract class ParticleGuide implements Runnable {
    
    protected final ParticleGuidesPlugin plugin;
    protected final Player player;
    protected final Particle.DustOptions dustOptions;
    protected boolean isRunning;
    
    ParticleGuide(ParticleGuidesPlugin plugin, Player player, Color color) {
        this.plugin = plugin;
        this.player = player;
        this.dustOptions = new Particle.DustOptions(color, 1);
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public Color getColor() {
        return dustOptions.getColor();
    }
    
    @Override
    public void run() {
        isRunning = true;
        while (isRunning && player.isOnline()) {
            if (!plugin.isEnabled())
                break;
            Bukkit.getScheduler().runTaskAsynchronously(plugin, this::show);
            sleep(plugin.config().repeatDelay.get());
        }
        isRunning = false;
    }
    
    abstract void show();
    
    static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
    
    public void stop() {
        isRunning = false;
    }
    
    public boolean isStopped() {
        return !isRunning;
    }
    
    void spawnParticle(Location location) {
        int density = plugin.config().particleDensity.get();
        if (plugin.config().globalVisibility.get()) {
            player.getWorld().spawnParticle(Particle.REDSTONE, location, density, dustOptions);
        } else {
            player.spawnParticle(Particle.REDSTONE, location, density, dustOptions);
        }
    }
    
    void highlight(Location location) {
        int density = plugin.config().highlightDensity.get();
        if (plugin.config().globalVisibility.get()) {
            player.getWorld().spawnParticle(Particle.REDSTONE, location, density, 1, 1, 1, dustOptions);
        } else {
            player.spawnParticle(Particle.REDSTONE, location, density, 1, 1, 1, dustOptions);
        }
    }
    
    Location getPlayerLocation() {
        return player.getEyeLocation().subtract(0, 1, 0);
    }
    
    static boolean differentWorlds(World world1, World world2) {
        return !Objects.equals(world1, world2);
    }
    
}
