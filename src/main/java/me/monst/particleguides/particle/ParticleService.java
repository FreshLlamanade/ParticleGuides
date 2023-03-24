package me.monst.particleguides.particle;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.CommandExecutionException;
import me.monst.particleguides.command.Permissions;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Supplier;

public class ParticleService {
    
    private final ParticleGuidesPlugin plugin;
    private final Map<UUID, List<ParticleGuide>> playerGuideMap;
    private final Map<UUID, Breadcrumbs> playerBreadcrumbsMap;
    
    public ParticleService(ParticleGuidesPlugin plugin) {
        this.plugin = plugin;
        this.playerGuideMap = new HashMap<>();
        this.playerBreadcrumbsMap = new HashMap<>();
    }
    
    public Breadcrumbs getBreadcrumbs(Player player) {
        return playerBreadcrumbsMap.get(player.getUniqueId());
    }
    
    public void addBreadcrumbs(Player player, Color color) {
        removeBreadcrumbs(player);
        Breadcrumbs breadcrumbs = new Breadcrumbs(plugin, player, color);
        playerBreadcrumbsMap.put(player.getUniqueId(), breadcrumbs);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, breadcrumbs);
    }
    
    public void removeBreadcrumbs(Player player) {
        Breadcrumbs breadcrumbs = playerBreadcrumbsMap.remove(player.getUniqueId());
        if (breadcrumbs != null)
            breadcrumbs.stop();
    }
    
    public void addGuide(Player player, Location target, Color color) throws CommandExecutionException {
        addGuide(player, new FixedLocationParticleGuide(plugin, player, target.clone(), color));
    }
    
    public void addGuide(Player player, Player target, Color color) throws CommandExecutionException {
        Supplier<Location> targetLocationSupplier = () -> target.isOnline() ? target.getLocation() : null;
        addGuide(player, new MovingTargetParticleGuide(plugin, player, targetLocationSupplier, color));
    }
    
    private void addGuide(Player player, ParticleGuide guide) throws CommandExecutionException {
        List<ParticleGuide> guides = playerGuideMap.computeIfAbsent(player.getUniqueId(), k -> new LinkedList<>());
        guides.removeIf(ParticleGuide::isStopped);
        if (guides.size() >= Permissions.GUIDE.getPermissionLimitInt(player).orElse(0))
            throw new CommandExecutionException("You have reached the maximum number of guides you can have at once.");
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, guide);
        guides.add(guide);
    }
    
    public void removeGuides(Player player) {
        List<ParticleGuide> guides = playerGuideMap.remove(player.getUniqueId());
        if (guides != null)
            guides.forEach(ParticleGuide::stop);
    }
    
}
