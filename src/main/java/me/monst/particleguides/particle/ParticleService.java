package me.monst.particleguides.particle;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.Permissions;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

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
    
    // All currently loaded breadcrumbs trails
    private final Set<BreadcrumbsTrail> breadcrumbs;
    // A visualizer for each breadcrumbs trail
    private final Map<BreadcrumbsTrail, BreadcrumbsVisualizer> visualizers;
    // Each player can have only one active breadcrumbs trail at a time
    private final Map<UUID, ActiveBreadcrumbs> activeBreadcrumbs;
    
    public ParticleService(ParticleGuidesPlugin plugin) {
        this.plugin = plugin;
        this.playerGuideMap = new HashMap<>();
        this.incomingRequests = new HashMap<>();
        this.breadcrumbs = new HashSet<>();
        this.visualizers = new HashMap<>();
        this.activeBreadcrumbs = new HashMap<>();
    }
    
    public void addGuideRequest(Player to, Player from, NamedColor color) {
        incomingRequests.computeIfAbsent(to.getUniqueId(),
                        uuid -> CacheBuilder.newBuilder().expireAfterWrite(Duration.ofSeconds(60)).build())
                .put(from, color);
    }
    
    public Map<Player, NamedColor> getIncomingRequests(Player to) {
        Cache<Player, NamedColor> requests = incomingRequests.get(to.getUniqueId());
        if (requests == null)
            return Collections.emptyMap();
        return requests.asMap();
    }
    
    public void removeRequest(Player to, Player from) {
        Cache<Player, NamedColor> requests = incomingRequests.get(to.getUniqueId());
        if (requests == null)
            return;
        requests.invalidate(from);
    }
    
    public ActiveBreadcrumbs getActiveBreadcrumbs(Player player) {
        return activeBreadcrumbs.get(player.getUniqueId());
    }
    
    public void startActiveBreadcrumbs(Player player, Color color) {
        stopActiveBreadcrumbs(player);
        BreadcrumbsTrail breadcrumbs = new BreadcrumbsTrail(player.getLocation().getBlock(), plugin.config().blocksPerBreadcrumb.get());
        ActiveBreadcrumbs activeBreadcrumbs = new ActiveBreadcrumbs(breadcrumbs, player);
        this.activeBreadcrumbs.put(player.getUniqueId(), activeBreadcrumbs);
        BreadcrumbsVisualizer visualizer = new BreadcrumbsVisualizer(plugin, breadcrumbs, player, color);
        visualizers.put(breadcrumbs, visualizer);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, visualizer);
    }
    
    public void stopActiveBreadcrumbs(Player player) {
        ActiveBreadcrumbs activeBreadcrumbs = this.activeBreadcrumbs.remove(player.getUniqueId());
        if (activeBreadcrumbs != null) {
            BreadcrumbsVisualizer visualizer = visualizers.remove(activeBreadcrumbs.getBreadcrumbs());
            if (visualizer != null) {
                visualizer.stop();
            }
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
        if (player.isInvisible() || player.hasPotionEffect(PotionEffectType.INVISIBILITY))
            return null;
        if (player.getGameMode() == GameMode.SPECTATOR)
            return null;
        if (plugin.hasEssentials() && plugin.getEssentials().getUser(player).isVanished())
            return null;
        return player.getLocation();
    }
    
    private void addGuide(Player player, ParticleGuide guide) {
        List<ParticleGuide> guides = playerGuideMap.computeIfAbsent(player.getUniqueId(), k -> new LinkedList<>());
        guides.removeIf(ParticleGuide::isStopped);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, guide);
        guides.add(guide);
    }
    
    public void removeGuides(Player player) {
        List<ParticleGuide> guides = playerGuideMap.remove(player.getUniqueId());
        if (guides != null) {
            guides.forEach(ParticleGuide::stop);
        }
    }
    
}
