package me.monst.particleguides.particle;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.particle.BreadcrumbsTrail.Breadcrumb;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class BreadcrumbsVisualizer extends ParticleGuide {
    
    private final BreadcrumbsTrail breadcrumbs;
    private boolean reversed = true; // normally we want to show the breadcrumbs going backwards from the player's current location
    
    public BreadcrumbsVisualizer(ParticleGuidesPlugin plugin, BreadcrumbsTrail breadcrumbs, Player player, Color color) {
        super(plugin, player, color);
        this.breadcrumbs = breadcrumbs;
    }
    
    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }
    
    @Override
    void show() {
        List<Breadcrumb> breadcrumbList = breadcrumbs.list();
        int i = reversed ? breadcrumbList.size() - 1 : 0;
        while (isRunning && i >= 0 && i < breadcrumbList.size()) {
            boolean isAtEndOfTrail = reversed ? i == 0 : i == breadcrumbList.size() - 1;
            Location location = breadcrumbList.get(i).getLocation();
            if (isAtEndOfTrail)
                highlight(location);
            else
                spawnParticle(location);
            sleep(plugin.config().particleDelay.get());
            i += reversed ? -1 : 1;
        }
    }
}
