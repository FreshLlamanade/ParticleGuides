package me.monst.particleguides.command.guide;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.CommandExecutionException;
import me.monst.particleguides.command.Permission;
import me.monst.particleguides.command.Permissions;
import me.monst.particleguides.command.PlayerExecutable;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class GuidePlayer implements PlayerExecutable {
    
    private final ParticleGuidesPlugin plugin;
    
    GuidePlayer(ParticleGuidesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "player";
    }
    
    @Override
    public String getDescription() {
        return "Guides you to the specified player.";
    }
    
    @Override
    public String getUsage() {
        return "/guide player <name> [color]";
    }
    
    @Override
    public Permission getPermission() {
        return Permissions.GUIDE_PLAYER;
    }
    
    @Override
    public void execute(Player player, List<String> args) throws CommandExecutionException {
        Player target = plugin.getServer().getPlayer(args.get(0));
        if (target == null)
            throw new CommandExecutionException("Player not found.");
        if (target.equals(player))
            throw new CommandExecutionException("We all need a little guidance sometimes...");
        if (!Objects.equals(target.getWorld(), player.getWorld()))
            throw new CommandExecutionException("That player is currently in a different world.");
        
        player.sendMessage(ChatColor.YELLOW + "Guiding you to " + target.getName() + "...");
        Color color = plugin.config().colorOptions.findColorOrRandom(args.size() == 1 ? null : args.get(1));
        plugin.getParticleService().addGuide(player, target, color);
    }
    
    @Override
    public List<String> getTabCompletions(Player player, List<String> args) {
        if (args.size() == 1)
            return plugin.getServer().getOnlinePlayers().stream()
                    .filter(p -> !p.equals(player))
                    .map(Player::getName)
                    .filter(name -> StringUtil.startsWithIgnoreCase(name, args.get(0)))
                    .collect(Collectors.toList());
        if (args.size() == 2)
            return plugin.config().colorOptions.searchColors(args.get(1));
        return Collections.emptyList();
    }
    
}
