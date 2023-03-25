package me.monst.particleguides.command.guide;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.CommandExecutionException;
import me.monst.particleguides.command.Permission;
import me.monst.particleguides.command.Permissions;
import me.monst.particleguides.command.PlayerExecutable;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class GuideCoords implements PlayerExecutable {
    
    private final ParticleGuidesPlugin plugin;
    
    GuideCoords(ParticleGuidesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "coords";
    }
    
    @Override
    public String getDescription() {
        return "Guides you to the specified coordinates.";
    }
    
    @Override
    public String getUsage() {
        return "/guide coords <x> <y> <z> [color]";
    }
    
    @Override
    public Permission getPermission() {
        return Permissions.GUIDE_COORDS;
    }
    
    @Override
    public void execute(Player player, List<String> args) throws CommandExecutionException {
        if (args.size() < 3)
            throw new CommandExecutionException("Please specify the x, y, and z coordinates to locate.");
        Iterator<String> argIterator = args.iterator();
        int x = parseCoordinate(argIterator.next());
        int y = parseCoordinate(argIterator.next());
        int z = parseCoordinate(argIterator.next());
        player.sendMessage(ChatColor.YELLOW + "Guiding you to coordinates " + x + ", " + y + ", " + z + "...");
        Location coordinates = new Location(player.getWorld(), x + 0.5, y + 0.5, z + 0.5);
        Color color = plugin.config().colors.findColorOrRandom(args.size() == 3 ? null : args.get(3));
        plugin.getParticleService().addGuide(player, coordinates, color);
    }
    
    private int parseCoordinate(String arg) throws CommandExecutionException {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new CommandExecutionException(arg + " is not a valid coordinate.");
        }
    }
    
    @Override
    public List<String> getTabCompletions(Player player, List<String> args) {
        if (args.size() <= 3) {
            List<String> coordinateCompletions = new ArrayList<>(1);
            Block block = player.getLocation().getBlock();
            String x = "" + block.getX();
            String y = "" + block.getY();
            String z = "" + block.getZ();
            if (args.size() == 1)
                coordinateCompletions.add(String.join(" ", x, y, z));
            else if (args.size() == 2)
                coordinateCompletions.add(String.join(" ", y, z));
            else
                coordinateCompletions.add(z);
            return coordinateCompletions;
        }
        if (args.size() == 4)
            return plugin.config().colors.searchColors(args.get(3));
        return Collections.emptyList();
    }
    
}
