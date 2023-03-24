package me.monst.particleguides.command.guide;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.CommandExecutionException;
import me.monst.particleguides.command.Permission;
import me.monst.particleguides.command.Permissions;
import me.monst.particleguides.command.PlayerExecutable;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.List;

class GuideHere implements PlayerExecutable {
    
    private final ParticleGuidesPlugin plugin;
    
    GuideHere(ParticleGuidesPlugin plugin) {
        this.plugin = plugin;
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
    public void execute(Player player, List<String> args) throws CommandExecutionException {
        player.sendMessage(ChatColor.YELLOW + "Guiding you to your current location...");
        Color color = plugin.config().colorOptions.findColorOrRandom(args.isEmpty() ? null : args.get(0));
        plugin.getParticleService().addGuide(player, player.getLocation(), color);
    }
    
    @Override
    public List<String> getTabCompletions(Player player, List<String> args) {
        return plugin.config().colorOptions.searchColors(args.get(0));
    }
    
}
