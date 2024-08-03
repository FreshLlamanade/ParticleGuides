package me.monst.particleguides.particle;

import me.monst.particleguides.ParticleGuidesPlugin;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

/**
 * A particle guide toward a moving target. The guide will track the target dynamically as it moves.
 * Since the target might disappear, this particle guide will automatically disable itself if it cannot find the target
 * more than {@code GRACE_PERIOD} times in a row.
 */
public class MovingTargetParticleGuide extends ParticleGuide {
    
    private final Supplier<Location> target;
    
    MovingTargetParticleGuide(ParticleGuidesPlugin plugin, Player player, Supplier<Location> target, Color color) {
        super(plugin, player, color);
        this.target = target;
    }
    
    @Override
    void show() {
        // Get the location where the player was initially standing
        final Location startLocation = getPlayerLocation();
        for (int baseDistance = 1; baseDistance <= plugin.config().guideLength.get(); baseDistance++) {
            if (isStopped())
                return;
            
            // Get the current target location
            Location targetLocation = target.get();
            
            // If the target has disappeared, count up to automatically disable
            if (targetLocation == null) {
                return; // Don't spawn a particle
            }
            
            // Find the vector between the player's initial position and the target's current position
            final Vector startToTarget = Vector.between(startLocation, targetLocation);
            // Find the distance between the player's initial position and the target's current position
            final double startToTargetDistance = startToTarget.length();
            // Find the direction the player needs to move in from their initial position to reach the target
            final Vector startToTargetDirection = startToTarget.divide(startToTargetDistance); // Normalize the vector so that it has a length of 1
            // Find the distance the player did move in that direction (could be negative)
            final double movedTowardsTarget = Vector.between(startLocation, getPlayerLocation()).dot(startToTargetDirection);
            // Find the distance the next particle should be from the player's initial position
            final double nextParticleDistance = baseDistance + movedTowardsTarget;
            
            // If the particle trail has reached within one block of the target (or passed it) then stop
            if (nextParticleDistance > startToTargetDistance - 1) {
                highlight(targetLocation); // Spawn a particle puff at the target location
                return;
            }
            
            // Make sure the next particle's location is offset by the distance the player moved
            // This ensures that the player will always be able to see the guide, even when moving very fast
            Location nextParticle = startToTargetDirection.multiply(nextParticleDistance).awayFrom(startLocation);
            spawnParticle(nextParticle); // Spawn a particle at the next location
            
            sleep(plugin.config().particleDelay.get());
        }
        if (plugin.config().alwaysHighlightTarget.get())
            highlight(target.get()); // Spawn a particle puff at the target location
    }
    
}
