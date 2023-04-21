package me.monst.particleguides.particle;

import me.monst.particleguides.ParticleGuidesPlugin;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * A particle guide to a specific, unmoving location.
 */
public class FixedLocationParticleGuide extends ParticleGuide {
    
    private final Location target;
    
    FixedLocationParticleGuide(ParticleGuidesPlugin plugin, Player player, Location target, Color color) {
        super(plugin, player, color);
        this.target = target;
    }
    
    @Override
    void show() {
        // Cannot show guide to a target in a different world
        if (differentWorlds(player.getWorld(), target.getWorld())) {
            stop();
            return;
        }
        
        // Represents the player's initial position, although the player could be moving this will stay the same
        final Location startLocation = getPlayerLocation();
        // Find the vector between the player's initial position and the target
        final Vector startToTarget = Vector.between(startLocation, target);
        // Find the distance between the player's initial position and the target
        final double startToTargetDistance = startToTarget.length();
        // Find the direction the player needs to move in to reach the target
        final Vector startToTargetDirection = startToTarget.divide(startToTargetDistance);
        
        // Spawn particles at every block between the player and the target
        for (int baseDistance = 1; baseDistance <= plugin.config().guideLength.get(); baseDistance++) {
            // Get the distance the player has moved in the direction of the target (could be negative)
            double movedTowardsTarget = Vector.between(startLocation, getPlayerLocation()).dot(startToTargetDirection);
            double nextParticleDistance = baseDistance + movedTowardsTarget;
            
            // If the particle trail has reached the target (or passed it) then stop
            if (nextParticleDistance >= startToTargetDistance) {
                highlight(target); // Spawn a particle puff at the target location
                return;
            }
            
            // Make sure the next particle's location is offset by the distance the player moved
            // This ensures that the player will always be able to see the guide, even when moving very fast
            Location nextParticle = startToTargetDirection.multiply(nextParticleDistance).awayFrom(startLocation);
            spawnParticle(nextParticle);
            
            sleep(plugin.config().particleDelay.get());
        }
        if (plugin.config().alwaysHighlightTarget.get())
            highlight(target); // Spawn a particle puff at the target location
    }
    
}
