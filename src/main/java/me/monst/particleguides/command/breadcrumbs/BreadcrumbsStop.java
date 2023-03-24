package me.monst.particleguides.command.breadcrumbs;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.PlayerExecutable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

class BreadcrumbsStop implements PlayerExecutable {
    
    private final ParticleGuidesPlugin plugin;
    
    BreadcrumbsStop(ParticleGuidesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "stop";
    }
    
    @Override
    public String getDescription() {
        return "Stops all breadcrumbs.";
    }
    
    @Override
    public String getUsage() {
        return "/breadcrumbs stop";
    }
    
    @Override
    public void execute(Player player, List<String> args) {
        player.sendMessage(ChatColor.YELLOW + "Breadcrumbs cleared.");
        plugin.getParticleService().removeBreadcrumbs(player);
    }
    
}
