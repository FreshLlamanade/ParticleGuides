package me.monst.particleguides.command.breadcrumbs;

import me.monst.particleguides.particle.ActiveBreadcrumbs;
import me.monst.particleguides.particle.ParticleService;
import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class BreadcrumbsResume implements Command {
    
    private final ParticleService particleService;
    
    BreadcrumbsResume(ParticleService particleService) {
        this.particleService = particleService;
    }
    
    @Override
    public String getName() {
        return "resume";
    }
    
    @Override
    public String getDescription() {
        return "Resumes dropping breadcrumbs.";
    }
    
    @Override
    public String getUsage() {
        return "/breadcrumbs resume";
    }
    
    @Override
    public void execute(CommandSender sender, Arguments args) throws CommandExecutionException {
        Player player = Command.playerOnly(sender);
        ActiveBreadcrumbs breadcrumbs = particleService.getActiveBreadcrumbs(player);
        if (breadcrumbs == null)
            Command.fail("You are not currently dropping breadcrumbs.");
        if (!breadcrumbs.isPaused())
            Command.fail("Your breadcrumbs are not paused. Did you mean /breadcrumbs pause?");
        breadcrumbs.setPauseState(ActiveBreadcrumbs.PauseState.ACTIVE);
        player.sendMessage(ChatColor.YELLOW + "Resuming breadcrumbs.");
    }
    
}
