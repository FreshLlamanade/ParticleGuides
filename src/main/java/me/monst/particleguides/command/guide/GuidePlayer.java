package me.monst.particleguides.command.guide;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.Permissions;
import me.monst.particleguides.configuration.values.Colors;
import me.monst.particleguides.particle.NamedColor;
import me.monst.particleguides.particle.ParticleService;
import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.Input;
import me.monst.pluginutil.command.Permission;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class GuidePlayer implements Command {
    
    private final ParticleGuidesPlugin plugin;
    private final ParticleService particleService;
    private final Colors colors;
    
    GuidePlayer(ParticleGuidesPlugin plugin, ParticleService particleService, Colors colors) {
        this.plugin = plugin;
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
        return "/guide player [name] <color>";
    }
    
    @Override
    public Permission getPermission() {
        return Permissions.GUIDE_PLAYER;
    }
    
    @Override
    public void execute(CommandSender sender, Arguments args) throws CommandExecutionException {
        Player player = Command.playerOnly(sender);
        Player target = args.first()
                .map(Input.toPlayer(name -> "Player not found."))
                .expect("Please specify the name of the player to locate.");
        if (target.equals(player))
            Command.fail("We all need a little guidance sometimes...");
        if (plugin.hasEssentials() && plugin.getEssentials().getUser(player).isVanished())
            Command.fail("Player not found.");
        if (!Objects.equals(target.getWorld(), player.getWorld()))
            Command.fail("That player is currently in a different world.");
        if (player.getGameMode() == GameMode.SPECTATOR)
            Command.fail("That player is currently in spectator mode.");
        if (player.isInvisible() || player.hasPotionEffect(PotionEffectType.INVISIBILITY))
            Command.fail("That player is currently invisible.");
    
        NamedColor color = args.second().map(colors::get).orElseGet(colors::random);
        
        if (particleService.hasMaximumGuides(player))
            throw new OutOfGuidesException();
        
        if (Permissions.GUIDE_PLAYER_NO_ASK.ownedBy(player)) {
            player.sendMessage(ChatColor.YELLOW + "Guiding you to " + target.getName() + " in " + color.getName() + "...");
            particleService.addGuide(player, target, color.getColor());
            return;
        }
        
        target.sendMessage(
                ChatColor.YELLOW + player.getName() + " is requesting to create a guide to you.",
                ChatColor.YELLOW + "To accept, use " + ChatColor.GREEN + "/guideaccept",
                ChatColor.YELLOW + "To deny, use " + ChatColor.RED + "/guidedeny"
        );
        player.sendMessage(ChatColor.YELLOW + "Request sent to " + target.getName() + ".");
        particleService.addGuideRequest(target, player, color);
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
