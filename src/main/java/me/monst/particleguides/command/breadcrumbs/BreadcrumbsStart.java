package me.monst.particleguides.command.breadcrumbs;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.CommandExecutionException;
import me.monst.particleguides.command.PlayerExecutable;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

class BreadcrumbsStart implements PlayerExecutable {
    
    private final ParticleGuidesPlugin plugin;
    
    BreadcrumbsStart(ParticleGuidesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "start";
    }
    
    @Override
    public String getDescription() {
        return "Leave a trail of particles behind you to guide you back.";
    }
    
    @Override
    public String getUsage() {
        return "/breadcrumbs start";
    }
    
    @Override
    public void execute(Player player, List<String> args) throws CommandExecutionException {
        player.sendMessage(ChatColor.YELLOW + "Starting breadcrumbs...");
        Color color = plugin.config().colorOptions.findColorOrRandom(args.isEmpty() ? null : args.get(0));
        plugin.getParticleService().addBreadcrumbs(player, color);
    }
    
    @Override
    public List<String> getTabCompletions(Player player, List<String> args) {
        if (args.size() > 1)
            return Collections.emptyList();
        return plugin.config().colorOptions.searchColors(args.get(0));
    }
    
}
