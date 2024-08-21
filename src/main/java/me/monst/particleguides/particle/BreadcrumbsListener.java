package me.monst.particleguides.particle;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import java.util.Objects;

/**
 * Listens for player movement events and updates the breadcrumbs accordingly.
 */
public class BreadcrumbsListener implements Listener {

    private final ParticleService particleService;
    
    public BreadcrumbsListener(ParticleService particleService) {
        this.particleService = particleService;
    }
    
    @EventHandler
    @SuppressWarnings("unused")
    public void onVehicleMove(VehicleMoveEvent event) {
        Block from = event.getFrom().getBlock();
        Block to = event.getTo().getBlock();
        if (Objects.equals(from, to)) {
            return;
        }
        for (Entity passenger : event.getVehicle().getPassengers()) {
            if (!(passenger instanceof Player))
                continue;
            BreadcrumbsTrail breadcrumbs = particleService.getBreadcrumbsTrail((Player) passenger);
            if (breadcrumbs == null)
                return;
            // The breadcrumbs will be in the ground if we don't adjust the location upwards
            breadcrumbs.stepOnBlock(to.getRelative(BlockFace.UP));
        }
    }
    
    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getPlayer().isInsideVehicle())
            return; // Handle this in the dedicated vehicle handler
        Block from = event.getFrom().getBlock();
        Block to = event.getTo().getBlock();
        if (Objects.equals(from, to))
            return;
        BreadcrumbsTrail breadcrumbs = particleService.getBreadcrumbsTrail(event.getPlayer());
        if (breadcrumbs == null)
            return;
        breadcrumbs.stepOnBlock(to);
    }
    
    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        BreadcrumbsTrail breadcrumbs = particleService.getBreadcrumbsTrail(player);
        if (breadcrumbs != null) {
            breadcrumbs.setPauseState(BreadcrumbsTrail.PauseState.PAUSED_AUTO_RESUME);
            breadcrumbs.getVisualizer().setDirection(BreadcrumbsVisualizer.Direction.OLDEST_TO_YOUNGEST);
            player.sendMessage("Breadcrumbs automatically paused on death.");
        }
    }
    
}
