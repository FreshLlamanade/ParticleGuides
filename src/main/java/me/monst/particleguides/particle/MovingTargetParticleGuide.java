package me.monst.particleguides.particle;

import me.monst.particleguides.ParticleGuidesPlugin;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.function.Supplier;

/**
 * A particle guide toward a moving target. The guide will track the target dynamically as it moves.
 * Since the target might disappear, this particle guide will automatically disable itself if it cannot find the target
 * more than {@code GRACE_PERIOD} times in a row.
 */
public class MovingTargetParticleGuide extends ParticleGuide {
    
    private static final int GRACE_PERIOD = 5;
    
    private final Supplier<Location> target;
    private int timesSinceTargetLastSeen = 0;
    
    MovingTargetParticleGuide(ParticleGuidesPlugin plugin, Player player, Supplier<Location> target, Color color) {
        super(plugin, player, color);
        this.target = target;
    }
    
    @Override
    void show() {
        // Get the location where the player was initially standing
        Location startLocation = getPlayerLocation();
        for (int multiplier = 1; multiplier <= plugin.config().guideLength.get(); multiplier++) {
            Location currentTargetLocation = this.target.get(); // Get the current target location
            if (currentTargetLocation == null || differentWorlds(startLocation.getWorld(), currentTargetLocation.getWorld())) {
                // Target is either in a different world or has completely disappeared, count up to automatically disable
                if (timesSinceTargetLastSeen++ >= GRACE_PERIOD)
                    stop();
                return;
            }
            timesSinceTargetLastSeen = 0;
            if (startLocation.distance(currentTargetLocation) <= plugin.config().guideLength.get()) {
                // If the player is within range, make a puff of particles around the target to highlight it
                highlight(currentTargetLocation);
                break;
            }
            // Get the direction between the player's initial position and the target's current position
            Vector targetDirection = getVectorBetween(startLocation, currentTargetLocation).normalize();
            // The player may have moved some from their initial position
            Vector playerMoved = getVectorBetween(startLocation, getPlayerLocation());
            // Get the distance the player moved in the direction of the where the target now is (could be negative)
            double movedTowardsTarget = playerMoved.dot(targetDirection);
            // Make sure the next particle's location is offset by the distance the player moved
            // This ensures that the player will always be able to see the guide, even when moving very fast
            Location nextParticleLocation = startLocation.clone().add(targetDirection.multiply(multiplier + movedTowardsTarget));
            spawnParticle(nextParticleLocation);
            sleep(plugin.config().particleDelay.get());
        }
    }
    
}
