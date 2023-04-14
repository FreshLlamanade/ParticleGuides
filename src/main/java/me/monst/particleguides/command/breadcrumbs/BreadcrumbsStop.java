package me.monst.particleguides.command.breadcrumbs;

import me.monst.particleguides.particle.ParticleService;
import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class BreadcrumbsStop implements Command {
    
    private final ParticleService particleService;
    
    BreadcrumbsStop(ParticleService particleService) {
        this.particleService = particleService;
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
    public void execute(CommandSender sender, Arguments args) throws CommandExecutionException {
        Player player = Command.playerOnly(sender);
        player.sendMessage(ChatColor.YELLOW + "Breadcrumbs cleared.");
        particleService.removeBreadcrumbs(player);
    }
    
}
