package me.monst.particleguides.command.breadcrumbs;

import me.monst.particleguides.particle.BreadcrumbsTrail;
import me.monst.particleguides.particle.ParticleService;
import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class BreadcrumbsHide implements Command {
    
    private final ParticleService particleService;
    
    BreadcrumbsHide(ParticleService particleService) {
        this.particleService = particleService;
    }
    
    @Override
    public String getName() {
        return "hide";
    }
    
    @Override
    public String getDescription() {
        return "Makes your breadcrumbs invisible until you show them again.";
    }
    
    @Override
    public String getUsage() {
        return "/breadcrumbs hide";
    }
    
    @Override
    public void execute(CommandSender sender, Arguments args) throws CommandExecutionException {
        Player player = Command.playerOnly(sender);
        BreadcrumbsTrail breadcrumbs = particleService.getBreadcrumbsTrail(player);
        if (breadcrumbs == null) {
            Command.fail("You are not currently dropping breadcrumbs.");
        }
        if (breadcrumbs.getVisualizer().isRunning()) {
            breadcrumbs.getVisualizer().stop();
            player.sendMessage(ChatColor.YELLOW + "Breadcrumbs hidden.");
        } else {
            player.sendMessage(ChatColor.YELLOW + "Breadcrumbs are already hidden. Show them with /breadcrumbs show.");
        }
    }
    
}
