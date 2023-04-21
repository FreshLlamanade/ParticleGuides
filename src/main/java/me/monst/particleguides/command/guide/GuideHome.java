package me.monst.particleguides.command.guide;

import com.earth2me.essentials.Essentials;
import me.monst.particleguides.command.Permissions;
import me.monst.particleguides.configuration.values.Colors;
import me.monst.particleguides.particle.NamedColor;
import me.monst.particleguides.particle.ParticleService;
import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.Permission;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

class GuideHome implements Command {
    
    private final ParticleService particleService;
    private final Colors colors;
    private final Essentials essentials;
    
    GuideHome(ParticleService particleService, Colors colors, Essentials essentials) {
        this.particleService = particleService;
        this.colors = colors;
        this.essentials = essentials;
    }
    
    @Override
    public String getName() {
        return "home";
    }
    
    @Override
    public String getDescription() {
        return "Guides you to your specified home.";
    }
    
    @Override
    public String getUsage() {
        return "/guide home <name> [color]";
    }
    
    @Override
    public Permission getPermission() {
        return Permissions.GUIDE_HOME;
    }
    
    @Override
    public void execute(CommandSender sender, Arguments args) throws CommandExecutionException {
        Player player = Command.playerOnly(sender);
        Location home = args.first()
                .map(name -> getHome(player, name))
                .expect("You must specify a home.");
    
        NamedColor color = args.second().map(colors::get).orElseGet(colors::random);
        player.sendMessage(ChatColor.YELLOW + "Guiding you to '" + args.first().get() + "' in " + color.getName() + "...");
        particleService.addGuide(player, home, color.getColor());
    }
    
    private Location getHome(Player player, String homeName) throws CommandExecutionException {
        if (essentials.getUser(player).hasHome(homeName)) {
            try {
                Location home = essentials.getUser(player).getHome(homeName);
                if (home != null)
                    return home;
            } catch (Exception ignored) {}
        }
        throw Command.fail("You do not have a home named '" + homeName + "'.");
    }
    
    @Override
    public List<String> getTabCompletions(Player player, Arguments args) {
        if (!essentials.isEnabled())
            return Collections.emptyList();
        if (args.size() == 1)
            return essentials.getUser(player).getHomes();
        if (args.size() == 2)
            return args.second().map(colors::search).orElseGet(colors::names);
        return Collections.emptyList();
    }
    
}
