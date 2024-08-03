package me.monst.particleguides.command.guide;

import me.monst.deutschlearnersplugin.DeutschLearnersPlugin;
import me.monst.deutschlearnersplugin.persistence.entity.Waypoint;
import me.monst.particleguides.configuration.values.Colors;
import me.monst.particleguides.particle.NamedColor;
import me.monst.particleguides.particle.ParticleService;
import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

class GuideWaypoint implements Command {
    
    private final ParticleService particleService;
    private final Colors colors;
    private final DeutschLearnersPlugin dlp;
    
    GuideWaypoint(ParticleService particleService, Colors colors, DeutschLearnersPlugin dlp) {
        this.particleService = particleService;
        this.colors = colors;
        this.dlp = dlp;
    }
    
    @Override
    public String getName() {
        return "waypoint";
    }
    
    @Override
    public String getDescription() {
        return "Guides you to one of your saved waypoints.";
    }
    
    @Override
    public String getUsage() {
        return "/guide waypoint <name> [color]";
    }
    
    @Override
    public void execute(CommandSender sender, Arguments args) throws CommandExecutionException {
        Player player = Command.playerOnly(sender);
        Location waypoint = args.first()
                .map(name -> getWaypoint(player, name))
                .expect("You must specify a waypoint.");
        NamedColor color = args.second().map(colors::get).orElseGet(colors::random);
        
        if (particleService.hasMaximumGuides(player))
            throw new OutOfGuidesException();
        
        player.sendMessage(ChatColor.YELLOW + "Guiding you to '" + args.first().get() + "' in " + color.getName() + "...");
        particleService.addGuide(player, waypoint, color.getColor());
    }
    
    private Location getWaypoint(Player player, String waypointName) throws CommandExecutionException {
        Waypoint waypoint = dlp.getWaypointService().findByPlayerIdAndName(player.getUniqueId(), waypointName);
        if (waypoint == null) {
            throw Command.fail("You do not have a waypoint named '" + waypointName + "'.");
        }
        return waypoint.getLocation();
    }
    
    @Override
    public List<String> getTabCompletions(Player player, Arguments args) {
        if (!dlp.isEnabled())
            return Collections.emptyList();
        if (args.size() == 1)
            return dlp.getWaypointService().searchNames(player.getUniqueId(), args.first().orElse(""));
        if (args.size() == 2)
            return args.second().map(colors::search).orElseGet(colors::names);
        return Collections.emptyList();
    }
    
}
