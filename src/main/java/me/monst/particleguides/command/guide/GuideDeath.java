package me.monst.particleguides.command.guide;

import me.monst.particleguides.command.Permissions;
import me.monst.particleguides.configuration.values.Colors;
import me.monst.particleguides.particle.NamedColor;
import me.monst.particleguides.particle.ParticleService;
import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.Permission;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

class GuideDeath implements Command {
    
    private final ParticleService particleService;
    private final Colors colors;
    
    GuideDeath(ParticleService particleService, Colors colors) {
        this.colors = colors;
        this.particleService = particleService;
    }
    
    @Override
    public String getName() {
        return "death";
    }
    
    @Override
    public String getDescription() {
        return "Guides you to the location where you last died.";
    }
    
    @Override
    public String getUsage() {
        return "/guide death <color>";
    }
    
    @Override
    public Permission getPermission() {
        return Permissions.GUIDE_DEATH;
    }
    
    @Override
    public void execute(CommandSender sender, Arguments args) throws CommandExecutionException {
        Player player = Command.playerOnly(sender);
        Location lastDeath = player.getLastDeathLocation();
        if (lastDeath == null || lastDeath.getWorld() == null)
            Command.fail("No death location found.");
        if (!Objects.equals(lastDeath.getWorld(), player.getWorld()))
            Command.fail("Death location is in world '" + lastDeath.getWorld().getName() + "'.");
    
        NamedColor color = args.first().map(colors::get).orElseGet(colors::random);
        
        if (particleService.hasMaximumGuides(player))
            throw new OutOfGuidesException();
        
        player.sendMessage(ChatColor.YELLOW + "Guiding you to your last death location in " + color.getName() + "...");
        particleService.addGuide(player, lastDeath, color.getColor());
    }
    
    @Override
    public List<String> getTabCompletions(Player player, Arguments args) {
        if (args.size() > 1)
            return Collections.emptyList();
        return args.first().map(colors::search).orElseGet(colors::names);
    }
    
}
