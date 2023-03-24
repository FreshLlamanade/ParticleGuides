package me.monst.particleguides.command.guide;

import com.earth2me.essentials.Essentials;
import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.CommandExecutionException;
import me.monst.particleguides.command.Permission;
import me.monst.particleguides.command.Permissions;
import me.monst.particleguides.command.PlayerExecutable;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

class GuideHome implements PlayerExecutable {
    
    private final ParticleGuidesPlugin plugin;
    private final Essentials essentials;
    
    GuideHome(ParticleGuidesPlugin plugin, Essentials essentials) {
        this.plugin = plugin;
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
    public void execute(Player player, List<String> args) throws CommandExecutionException {
        if (!essentials.isEnabled())
            throw new CommandExecutionException("Essentials is not enabled.");
        if (args.isEmpty())
            throw new CommandExecutionException("You must specify a home.");
        
        String homeName = args.get(0);
        Location home = getHome(player, homeName);
    
        player.sendMessage(ChatColor.YELLOW + "Guiding you to '" + homeName + "'...");
        plugin.getParticleService().addGuide(player, home, plugin.config().colorOptions.findColorOrRandom(args.size() == 1 ? null : args.get(1)));
    }
    
    private Location getHome(Player player, String homeName) throws CommandExecutionException {
        if (essentials.getUser(player).hasHome(homeName)) {
            try {
                Location home = essentials.getUser(player).getHome(homeName);
                if (home != null)
                    return home;
            } catch (Exception ignored) {}
        }
        throw new CommandExecutionException("You do not have a home named " + homeName + ".");
    }
    
    @Override
    public List<String> getTabCompletions(Player player, List<String> args) {
        if (!essentials.isEnabled())
            return Collections.emptyList();
        if (args.size() == 1)
            return essentials.getUser(player).getHomes();
        if (args.size() == 2)
            return plugin.config().colorOptions.searchColors(args.get(1));
        return Collections.emptyList();
    }
    
}
