package me.monst.particleguides.command.guide;

import me.monst.particleguides.command.Permissions;
import me.monst.particleguides.configuration.values.Colors;
import me.monst.particleguides.particle.ParticleService;
import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.Permission;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

class GuideHere implements Command {
    
    private final ParticleService particleService;
    private final Colors colors;
    
    GuideHere(ParticleService particleService, Colors colors) {
        this.colors = colors;
        this.particleService = particleService;
    }
    
    @Override
    public String getName() {
        return "here";
    }
    
    @Override
    public String getDescription() {
        return "Guides you back to your current location.";
    }
    
    @Override
    public String getUsage() {
        return "/guide here [color]";
    }
    
    @Override
    public Permission getPermission() {
        return Permissions.GUIDE_HERE;
    }
    
    @Override
    public void execute(CommandSender sender, Arguments args) throws CommandExecutionException {
        Player player = Command.playerOnly(sender);
        Color color = args.first().map(colors::get).orElseGet(colors::random);
        player.sendMessage(ChatColor.YELLOW + "Guiding you to your current location...");
        particleService.addGuide(player, player.getLocation(), color);
    }
    
    @Override
    public List<String> getTabCompletions(Player player, Arguments args) {
        if (args.size() > 1)
            return Collections.emptyList();
        return args.first().map(colors::search).orElseGet(colors::names);
    }
    
}
