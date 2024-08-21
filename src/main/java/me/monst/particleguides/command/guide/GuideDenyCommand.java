package me.monst.particleguides.command.guide;

import me.monst.particleguides.particle.ParticleService;
import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuideDenyCommand extends GuideRequestResponseCommand {
    
    public GuideDenyCommand(ParticleService particleService) {
        super(particleService);
    }
    
    @Override
    public String getName() {
        return "guidedeny";
    }
    
    @Override
    public String getDescription() {
        return "Denies a guide request from another player.";
    }
    
    @Override
    public String getUsage() {
        return "/guidedeny [player]";
    }
    
    @Override
    public void execute(CommandSender sender, Arguments args) throws CommandExecutionException {
        Player player = Command.playerOnly(sender);
        Request request = processRequest(player, args);
        Player requester = request.requester;
        
        player.sendMessage(ChatColor.YELLOW + "You have denied " + requester.getName() + "'s guide request.");
        requester.sendMessage(ChatColor.RED + player.getName() + " has denied your guide request.");
    }
    
}
