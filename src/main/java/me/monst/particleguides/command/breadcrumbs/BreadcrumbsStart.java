package me.monst.particleguides.command.breadcrumbs;

import me.monst.particleguides.configuration.values.Colors;
import me.monst.particleguides.particle.NamedColor;
import me.monst.particleguides.particle.ParticleService;
import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

class BreadcrumbsStart implements Command {
    
    private final ParticleService particleService;
    private final Colors colors;
    
    BreadcrumbsStart(ParticleService particleService, Colors colors) {
        this.particleService = particleService;
        this.colors = colors;
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
        return "/breadcrumbs start [color]";
    }
    
    @Override
    public void execute(CommandSender sender, Arguments args) throws CommandExecutionException {
        Player player = Command.playerOnly(sender);
        NamedColor color = args.first().map(colors::get).orElseGet(colors::random);
        player.sendMessage(ChatColor.YELLOW + "Starting breadcrumbs in " + color.getName() + "...");
        particleService.startActiveBreadcrumbs(player, color.getColor());
    }
    
    @Override
    public List<String> getTabCompletions(Player player, Arguments args) {
        if (args.size() > 1)
            return Collections.emptyList();
        return args.first().map(colors::search).orElseGet(colors::names);
    }
    
}
