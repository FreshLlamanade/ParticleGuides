package me.monst.particleguides.command.plugin;

import me.monst.particleguides.command.Permissions;
import me.monst.particleguides.configuration.Configuration;
import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.Permission;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import me.monst.pluginutil.configuration.ConfigurationBranch;
import me.monst.pluginutil.configuration.ConfigurationNode;
import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.pluginutil.configuration.exception.ArgumentParseException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class ParticleGuidesCommand implements Command {
    
    private final Configuration configuration;
    
    public ParticleGuidesCommand(Configuration configuration) {
        this.configuration = configuration;
    }
    
    @Override
    public String getName() {
        return "particleguides";
    }
    
    @Override
    public String getDescription() {
        return "Configure the plugin.";
    }
    
    @Override
    public String getUsage() {
        return "/particleguides <path> <value>";
    }
    
    @Override
    public Permission getPermission() {
        return Permissions.ADMIN;
    }
    
    @Override
    public void execute(CommandSender sender, Arguments args) throws CommandExecutionException {
        configuration.load();
        ListIterator<String> iterator = args.asList().listIterator();
        ConfigurationNode targetNode = configuration.deepSearch(iterator);
        String path = args.between(0, iterator.nextIndex()).join(".");
        
        if (!(targetNode instanceof ConfigurationValue<?>))
            Command.fail("'" + path + "' is not a configuration value.");
        ConfigurationValue<?> configValue = (ConfigurationValue<?>) targetNode;
        
        String input = String.join(" ", args.between(iterator.nextIndex(), args.size()));
        String previousValue = configValue.toString();
        
        try {
            configValue.feed(input.isEmpty() ? null : input);
        } catch (ArgumentParseException e) {
            Command.fail(e.getMessage());
        }
        
        String newValue = configValue.toString();
        
        sender.sendMessage(
                ChatColor.GREEN + "Set "
                        + ChatColor.GOLD + path
                        + ChatColor.GREEN + " from "
                        + ChatColor.GOLD + previousValue
                        + ChatColor.GREEN + " to "
                        + ChatColor.GOLD + newValue
                        + ChatColor.GREEN + ".");
        configuration.save();
    }
    
    @Override
    public List<String> getTabCompletions(Player player, Arguments args) {
        ListIterator<String> iterator = args.asList().listIterator();
        ConfigurationNode targetNode = configuration.deepSearch(iterator);
        if (targetNode instanceof ConfigurationBranch && !iterator.hasNext()) {
            String lastArg = args.last().orElse("");
            return ((ConfigurationBranch) targetNode).getChildren().keySet().stream()
                    .filter(child -> containsIgnoreCase(child, lastArg))
                    .collect(Collectors.toList());
        } else if (targetNode instanceof ConfigurationValue<?> && iterator.hasNext()) {
            return ((ConfigurationValue<?>) targetNode).getTabCompletions(player,
                    args.between(iterator.nextIndex(), args.size()));
        }
        return Collections.emptyList();
    }
    
    private static boolean containsIgnoreCase(String string, String substring) {
        int len = substring.length();
        int max = string.length() - len;
        for (int i = 0; i <= max; i++)
            if (string.regionMatches(true, i, substring, 0, len))
                return true;
        return false;
    }
    
}
