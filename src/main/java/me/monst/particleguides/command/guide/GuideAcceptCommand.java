package me.monst.particleguides.command.guide;

import me.monst.particleguides.particle.NamedColor;
import me.monst.particleguides.particle.ParticleService;
import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuideAcceptCommand extends GuideRequestResponseCommand {
    
    public GuideAcceptCommand(ParticleService particleService) {
        super(particleService);
    }
    
    @Override
    public String getName() {
        return "guideaccept";
    }
    
    @Override
    public String getDescription() {
        return "Accepts a guide request from another player.";
    }
    
    @Override
    public String getUsage() {
        return "/guideaccept [player]";
    }
    
    @Override
    public void execute(CommandSender sender, Arguments args) throws CommandExecutionException {
        Player player = Command.playerOnly(sender);
        Result result = processRequest(player, args);
        Player requester = result.requester;
        NamedColor color = result.color;
        
        player.sendMessage(ChatColor.YELLOW + "You have accepted " + requester.getName() + "'s guide request.");
        requester.sendMessage(ChatColor.GREEN + player.getName() + " has accepted your guide request.");
        
        requester.sendMessage(ChatColor.YELLOW + "Guiding you to " + player.getName() + " in " + color.getName() + "...");
        particleService.addGuide(requester, player, color.getColor());
    }
    
}
