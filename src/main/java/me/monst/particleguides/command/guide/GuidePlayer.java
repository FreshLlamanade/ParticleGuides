package me.monst.particleguides.command.guide;

import me.monst.particleguides.command.Permissions;
import me.monst.particleguides.configuration.values.Colors;
import me.monst.particleguides.particle.NamedColor;
import me.monst.particleguides.particle.ParticleService;
import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.Permission;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class GuidePlayer implements Command {
    
    private final ParticleService particleService;
    private final Colors colors;
    
    GuidePlayer(ParticleService particleService, Colors colors) {
        this.particleService = particleService;
        this.colors = colors;
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
    public void execute(CommandSender sender, Arguments args) throws CommandExecutionException {
        Player player = Command.playerOnly(sender);
        Player target = args.first()
                .tryMap(this::findPlayer)
                .expect("Please specify the name of the player to locate.");
        if (target.equals(player))
            Command.fail("We all need a little guidance sometimes...");
        if (!Objects.equals(target.getWorld(), player.getWorld()))
            Command.fail("That player is currently in a different world.");
    
        NamedColor color = args.second().map(colors::get).orElseGet(colors::random);
        player.sendMessage(ChatColor.YELLOW + "Guiding you to " + target.getName() + " in " + color.getName() + "...");
        particleService.addGuide(player, target, color.getColor());
    }
    
    private Player findPlayer(String name) throws CommandExecutionException {
        Player player = Bukkit.getServer().getPlayer(name);
        if (player == null)
            Command.fail("Player not found.");
        return player;
    }
    
    @Override
    public List<String> getTabCompletions(Player player, Arguments args) {
        if (args.size() == 1) {
            String arg = args.first().orElse("");
            return Bukkit.getServer().getOnlinePlayers().stream()
                    .filter(p -> !p.equals(player))
                    .map(Player::getName)
                    .filter(name -> StringUtil.startsWithIgnoreCase(name, arg))
                    .collect(Collectors.toList());
        }
        if (args.size() == 2)
            return args.second().map(colors::search).orElseGet(colors::names);
        return Collections.emptyList();
    }
    
}
