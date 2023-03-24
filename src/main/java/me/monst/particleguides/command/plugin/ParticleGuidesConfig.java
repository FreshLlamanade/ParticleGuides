package me.monst.particleguides.command.plugin;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.CommandExecutionException;
import me.monst.particleguides.command.Executable;
import me.monst.particleguides.configuration.ConfigurationBranch;
import me.monst.particleguides.configuration.ConfigurationNode;
import me.monst.particleguides.configuration.ConfigurationValue;
import me.monst.particleguides.configuration.exception.ArgumentParseException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

class ParticleGuidesConfig implements Executable {
    
    private final ParticleGuidesPlugin plugin;
    
    public ParticleGuidesConfig(ParticleGuidesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "config";
    }
    
    @Override
    public String getDescription() {
        return "Manage the plugin configuration.";
    }
    
    @Override
    public String getUsage() {
        return "/particleguides config <path> <value>";
    }
    
    @Override
    public void execute(CommandSender sender, List<String> args) throws CommandExecutionException {
        plugin.config().load();
        ListIterator<String> iterator = args.listIterator();
        ConfigurationNode targetNode = plugin.config().deepSearch(iterator);
        String path = String.join(".", args.subList(0, iterator.nextIndex()));
        
        if (!(targetNode instanceof ConfigurationValue<?>))
            throw new CommandExecutionException(path + " is not a configuration value.");
        ConfigurationValue<?> configValue = (ConfigurationValue<?>) targetNode;
        
        String input = String.join(" ", args.subList(iterator.nextIndex(), args.size()));
        String previousValue = configValue.toString();
        
        try {
            configValue.feed(input.isEmpty() ? null : input);
        } catch (ArgumentParseException e) {
            throw new CommandExecutionException(e.getMessage());
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
        plugin.config().save();
    }
    
    @Override
    public List<String> getTabCompletions(Player player, List<String> args) {
        ListIterator<String> iterator = args.listIterator();
        ConfigurationNode targetNode = plugin.config().deepSearch(iterator);
        if (targetNode instanceof ConfigurationBranch)
            return ((ConfigurationBranch) targetNode).getChildren().keySet().stream()
                    .filter(child -> child.startsWith(args.get(iterator.nextIndex())))
                    .collect(Collectors.toList());
        if (targetNode instanceof ConfigurationValue<?>)
            return ((ConfigurationValue<?>) targetNode).getTabCompletions(player, args.subList(iterator.nextIndex(), args.size()));
        return Collections.emptyList();
    }
    
}
