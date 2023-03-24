package me.monst.particleguides.command.guide;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.PlayerExecutable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

class GuideStop implements PlayerExecutable {
    
    private final ParticleGuidesPlugin plugin;
    
    GuideStop(ParticleGuidesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "stop";
    }
    
    @Override
    public String getDescription() {
        return "Stops all particle guides.";
    }
    
    @Override
    public String getUsage() {
        return "/guide stop";
    }
    
    @Override
    public void execute(Player player, List<String> args) {
        player.sendMessage(ChatColor.YELLOW + "Guides cleared.");
        plugin.getParticleService().removeGuides(player);
    }
    
}
