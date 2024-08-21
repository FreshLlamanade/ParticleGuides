package me.monst.particleguides.particle;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.Permissions;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.*;

/**
 * A service that manages particle guides and breadcrumbs.
 */
public class ParticleService {
    
    private final ParticleGuidesPlugin plugin;
    // Each player can have multiple guides at once
    private final Map<UUID, List<ParticleGuide>> playerGuideMap;
    // Each player can have guide requests from multiple other players at a time
    private final Map<UUID, Cache<Player, NamedColor>> incomingRequests;
    // Each player can have only one active breadcrumbs trail at a time
    private final Map<UUID, BreadcrumbsTrail> breadcrumbsTrails;
    
    public ParticleService(ParticleGuidesPlugin plugin) {
        this.plugin = plugin;
        this.playerGuideMap = new HashMap<>();
        this.incomingRequests = new HashMap<>();
        this.breadcrumbsTrails = new HashMap<>();
    }
    
    public void addGuideRequest(Player to, Player from, NamedColor color) {
        incomingRequests.computeIfAbsent(to.getUniqueId(),
                        uuid -> CacheBuilder.newBuilder().expireAfterWrite(Duration.ofSeconds(60)).build())
                .put(from, color);
    }
    
    public Map<Player, NamedColor> getIncomingRequests(Player to) {
        Cache<Player, NamedColor> requests = incomingRequests.get(to.getUniqueId());
        if (requests == null) {
            return Collections.emptyMap();
        }
        return requests.asMap();
    }
    
    public void removeRequest(Player to, Player from) {
        Cache<Player, NamedColor> requests = incomingRequests.get(to.getUniqueId());
        if (requests == null) {
            return;
        }
        requests.invalidate(from);
    }
    
    public BreadcrumbsTrail getBreadcrumbsTrail(Player player) {
        return breadcrumbsTrails.get(player.getUniqueId());
    }
    
    /**
     * Create a new breadcrumb trail belonging to the provided player.
     * @param player the player
     */
    public void startBreadcrumbs(Player player, Color color) {
        removeBreadcrumbs(player); // overwrite any previous breadcrumbs trail
        BreadcrumbsTrail trail = new BreadcrumbsTrail(player, plugin.config().blocksPerBreadcrumb.get());
        BreadcrumbsVisualizer visualizer = new BreadcrumbsVisualizer(plugin, trail.breadcrumbsList(), player, color);
        visualizer.start();
        trail.setVisualizer(visualizer);
        this.breadcrumbsTrails.put(player.getUniqueId(), trail);
    }
    
    /**
     * Removes the player's breadcrumbs trail and its visualization, if they exist
     * @param player the player
     */
    public void removeBreadcrumbs(Player player) {
        BreadcrumbsTrail trail = this.breadcrumbsTrails.remove(player.getUniqueId());
        if (trail != null) {
            trail.getVisualizer().stop();
        }
    }
    
    public boolean hasMaximumGuides(Player player) {
        return Permissions.GUIDE_LIMIT.getPermissionLimitInt(player)
                .map(limit -> playerGuideMap.getOrDefault(player.getUniqueId(), Collections.emptyList()).size() >= limit)
                .orElse(false);
    }
    
    public void addGuide(Player player, Location target, Color color) {
        addGuide(player, new FixedLocationParticleGuide(plugin, player, target.clone(), color));
    }
    
    public void addGuide(Player player, Player target, Color color) {
        addGuide(player, new MovingTargetParticleGuide(plugin, player, () -> locate(target), color));
    }
    
    private Location locate(Player player) {
        if (!player.isOnline())
            return null;
        if (player.getGameMode() == GameMode.SPECTATOR)
            return null;
        if (plugin.hasEssentials() && plugin.getEssentials().getUser(player).isVanished())
            return null;
        return player.getLocation();
    }
    
    private void addGuide(Player player, ParticleGuide guide) {
        List<ParticleGuide> guides = this.playerGuideMap.computeIfAbsent(player.getUniqueId(), k -> new LinkedList<>());
        guides.removeIf(particleGuide -> !particleGuide.isRunning());
        guide.start();
        guides.add(guide);
    }
    
    public void removeGuides(Player player) {
        List<ParticleGuide> guides = this.playerGuideMap.remove(player.getUniqueId());
        if (guides != null) {
            guides.forEach(ParticleGuide::stop);
        }
    }
    
}
