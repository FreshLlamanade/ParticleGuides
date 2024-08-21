package me.monst.particleguides.command.breadcrumbs;

import me.monst.particleguides.configuration.values.Colors;
import me.monst.particleguides.particle.BreadcrumbsTrail;
import me.monst.particleguides.particle.NamedColor;
import me.monst.particleguides.particle.ParticleService;
import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class BreadcrumbsShow implements Command {
    
    private final ParticleService particleService;
    private final Colors colors;
    
    BreadcrumbsShow(ParticleService particleService, Colors colors) {
        this.particleService = particleService;
        this.colors = colors;
    }
    
    @Override
    public String getName() {
        return "show";
    }
    
    @Override
    public String getDescription() {
        return "Show breadcrumbs in a certain color.";
    }
    
    @Override
    public String getUsage() {
        return "/breadcrumbs show <color>";
    }
    
    @Override
    public void execute(CommandSender sender, Arguments args) throws CommandExecutionException {
        Player player = Command.playerOnly(sender);
        BreadcrumbsTrail breadcrumbs = particleService.getBreadcrumbsTrail(player);
        if (breadcrumbs == null)
            Command.fail("You are not currently dropping breadcrumbs.");
        NamedColor requestedColor = args.first().map(colors::get).orElse(null);
        if (requestedColor != null) {
            player.sendMessage(ChatColor.YELLOW + "Showing breadcrumbs in " + requestedColor.getName() + "...");
            breadcrumbs.getVisualizer().setColor(requestedColor.getColor());
        } else {
            player.sendMessage(ChatColor.YELLOW + "Showing breadcrumbs...");
        }
        breadcrumbs.getVisualizer().start();
    }
    
    @Override
    public Iterable<?> getTabCompletions(Player player, Arguments args) {
        if (args.size() > 1)
            return null;
        return args.first().map(colors::search).orElseGet(colors::names);
    }
}
