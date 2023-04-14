package me.monst.particleguides.command.guide;

import me.monst.particleguides.particle.ParticleService;
import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class GuideStop implements Command {
    
    private final ParticleService particleService;
    
    GuideStop(ParticleService particleService) {
        this.particleService = particleService;
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
    public void execute(CommandSender sender, Arguments args) throws CommandExecutionException {
        Player player = Command.playerOnly(sender);
        player.sendMessage(ChatColor.YELLOW + "Guides cleared.");
        particleService.removeGuides(player);
    }
    
}
