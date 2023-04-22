package me.monst.particleguides.command.guide;

import me.monst.particleguides.particle.NamedColor;
import me.monst.particleguides.particle.ParticleService;
import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.Input;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

abstract class GuideRequestResponseCommand implements Command {
    
    final ParticleService particleService;
    
    GuideRequestResponseCommand(ParticleService particleService) {
        this.particleService = particleService;
    }
    
    @Override
    public Iterable<?> getTabCompletions(Player player, Arguments args) {
        if (args.size() != 1)
            return Collections.emptyList();
        return particleService.getIncomingRequests(player).keySet().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }
    
    Result processRequest(Player player, Arguments args) throws CommandExecutionException {
        Map<Player, NamedColor> requests = particleService.getIncomingRequests(player);
        if (requests.isEmpty())
            Command.fail("You have no incoming guide requests.");
        
        Player requester = args.first()
                .map(Input.toPlayer(name -> "Player not found."))
                .orElseGet(() -> requests.keySet().iterator().next());
        
        if (!requests.containsKey(requester))
            Command.fail("You have no incoming guide requests from " + requester.getName() + ".");
        
        NamedColor color = requests.get(requester);
        particleService.removeRequest(player, requester);
        return new Result(requester, color);
    }
    
    static class Result {
        public final Player requester;
        public final NamedColor color;
        
        public Result(Player requester, NamedColor color) {
            this.requester = requester;
            this.color = color;
        }
    }

}
