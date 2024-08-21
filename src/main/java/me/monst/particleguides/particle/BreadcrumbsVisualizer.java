package me.monst.particleguides.particle;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.particle.BreadcrumbsTrail.Breadcrumb;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BreadcrumbsVisualizer extends ParticleGuide {
    
    /**
     * The direction the breadcrumb particles travel
     */
    public enum Direction {
        YOUNGEST_TO_OLDEST, // Starting at the most recent breadcrumb moving back to the oldest breadcrumb
        OLDEST_TO_YOUNGEST // Starting at the oldest breadcrumb moving forward to the most recent breadcrumb
    }
    
    private final ArrayList<Breadcrumb> breadcrumbs;
    private boolean youngestToOldest = true;
    
    public BreadcrumbsVisualizer(ParticleGuidesPlugin plugin, ArrayList<Breadcrumb> breadcrumbs, Player player, Color color) {
        super(plugin, player, color);
        this.breadcrumbs = breadcrumbs;
    }
    
    public void setDirection(Direction direction) {
        this.youngestToOldest = direction == Direction.YOUNGEST_TO_OLDEST;
    }
    
    @Override
    void showGuide() {
        int i = youngestToOldest ? breadcrumbs.size() - 1 : 0;
        while (isRunning && i >= 0 && i < breadcrumbs.size()) {
            boolean isAtEndOfTrail = youngestToOldest ? i == 0 : i == breadcrumbs.size() - 1;
            Location location = breadcrumbs.get(i).getBlock().getLocation().add(0.5, 0.5, 0.5);
            if (isAtEndOfTrail) {
                highlight(location);
            } else {
                spawnParticle(location);
            }
            sleep(plugin.config().particleDelay.get());
            i += youngestToOldest ? -1 : 1;
        }
    }
}
