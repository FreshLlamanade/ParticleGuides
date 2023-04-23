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

public class ParticleService {
    
    private final ParticleGuidesPlugin plugin;
    private final Map<UUID, List<ParticleGuide>> playerGuideMap;
    private final Map<UUID, Breadcrumbs> playerBreadcrumbsMap;
    private final Map<UUID, Cache<Player, NamedColor>> incomingRequests;
    
    public ParticleService(ParticleGuidesPlugin plugin) {
        this.plugin = plugin;
        this.playerGuideMap = new HashMap<>();
        this.playerBreadcrumbsMap = new HashMap<>();
        this.incomingRequests = new HashMap<>();
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
        if (guides != null)
            guides.forEach(ParticleGuide::stop);
    }
    
}
