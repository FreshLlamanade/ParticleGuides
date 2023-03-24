package me.monst.particleguides.particle;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class BreadcrumbsListener implements Listener {

    private final ParticleService particleService;
    
    public BreadcrumbsListener(ParticleService particleService) {
        this.particleService = particleService;
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Block from = event.getFrom().getBlock();
        Block to = event.getTo().getBlock();
        if (Objects.equals(from, to))
            return;
        Breadcrumbs breadcrumbs = particleService.getBreadcrumbs(event.getPlayer());
        if (breadcrumbs == null)
            return;
        breadcrumbs.stepOnBlock(to);
    }
    
}
