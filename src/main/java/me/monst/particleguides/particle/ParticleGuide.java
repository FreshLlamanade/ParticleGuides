package me.monst.particleguides.particle;

import me.monst.particleguides.ParticleGuidesPlugin;
import org.bukkit.*;
import org.bukkit.entity.Player;

/**
 * A guide that shows particles to the player.
 */
abstract class ParticleGuide {
    
    protected final ParticleGuidesPlugin plugin;
    protected final Player player;
    protected Particle.DustOptions dustOptions;
    protected boolean isRunning;
    
    ParticleGuide(ParticleGuidesPlugin plugin, Player player, Color color) {
        this.plugin = plugin;
        this.player = player;
        this.dustOptions = new Particle.DustOptions(color, 1);
    }
    
    static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
    
    abstract void showGuide();
    
    private void run() {
        while (isRunning && plugin.isEnabled()) {
            if (player.isOnline()) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, this::showGuide);
            }
            sleep(plugin.config().repeatDelay.get());
        }
        isRunning = false;
    }
    
    public void start() {
        if (isRunning)
            return;
        isRunning = true;
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, this::run);
    }
    
    public void stop() {
        isRunning = false;
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    public void setColor(Color color) {
        this.dustOptions = new Particle.DustOptions(color, 1);
    }
    
    void spawnParticle(Location location) {
        int density = plugin.config().particleDensity.get();
        if (plugin.config().globalVisibility.get()) {
            player.getWorld().spawnParticle(Particle.DUST, location, density, dustOptions);
        } else {
            player.spawnParticle(Particle.DUST, location, density, dustOptions);
        }
    }
    
    void highlight(Location location) {
        int density = plugin.config().highlightDensity.get();
        if (plugin.config().globalVisibility.get()) {
            player.getWorld().spawnParticle(Particle.DUST, location, density, 1, 1, 1, dustOptions);
        } else {
            player.spawnParticle(Particle.DUST, location, density, 1, 1, 1, dustOptions);
        }
    }
    
    Location getPlayerLocation() {
        return player.getEyeLocation().subtract(0, 1, 0);
    }
    
}
