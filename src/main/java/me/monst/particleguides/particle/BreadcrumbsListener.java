package me.monst.particleguides.particle;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import java.util.Objects;

public class BreadcrumbsListener implements Listener {

    private final ParticleService particleService;
    
    public BreadcrumbsListener(ParticleService particleService) {
        this.particleService = particleService;
    }
    
    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        Block from = event.getFrom().getBlock();
        Block to = event.getTo().getBlock();
        if (Objects.equals(from, to))
            return;
        for (Entity passenger : event.getVehicle().getPassengers()) {
            if (!(passenger instanceof Player))
                continue;
            Breadcrumbs breadcrumbs = particleService.getBreadcrumbs((Player) passenger);
            if (breadcrumbs == null)
                return;
            // The breadcrumbs will be in the ground if we don't adjust it upwards
            breadcrumbs.stepOnBlock(to.getRelative(BlockFace.UP));
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getPlayer().isInsideVehicle())
            return; // Handle this in the dedicated vehicle handler
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
