package me.monst.particleguides.command.guide;

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
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class GuideCoords implements Command {
    
    private final ParticleService particleService;
    private final Colors colors;
    
    GuideCoords(ParticleService particleService, Colors colors) {
        this.particleService = particleService;
        this.colors = colors;
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
    public void execute(CommandSender sender, Arguments args) throws CommandExecutionException {
        Player player = Command.playerOnly(sender);
        int x = args.first().tryMap(this::parseCoordinate).expect("Please specify the x, y, and z coordinates to locate.");
        int y = args.second().tryMap(this::parseCoordinate).expect("Please specify the y and z coordinates to locate.");
        int z = args.third().tryMap(this::parseCoordinate).expect("Please specify the z coordinate to locate.");
        Location coordinates = new Location(player.getWorld(), x + 0.5, y + 0.5, z + 0.5);
        NamedColor color = args.fourth().map(colors::get).orElseGet(colors::random);
        player.sendMessage(ChatColor.YELLOW + "Guiding you to coordinates " + x + ", " + y + ", " + z + " in " + color.getName() + "...");
        particleService.addGuide(player, coordinates, color.getColor());
    }
    
    private int parseCoordinate(String arg) throws CommandExecutionException {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw Command.exception(arg + " is not a valid coordinate.");
        }
    }
    
    @Override
    public Iterable<?> getTabCompletions(Player player, Arguments args) {
        if (args.size() <= 3) {
            List<Object> coordinateCompletions = new ArrayList<>(1);
            Block block = player.getLocation().getBlock();
            if (args.size() == 1)
                coordinateCompletions.add(block.getX() + " " + block.getY() + " " + block.getZ());
            else if (args.size() == 2)
                coordinateCompletions.add(block.getY() + " " + block.getZ());
            else
                coordinateCompletions.add(block.getZ());
            return coordinateCompletions;
        }
        if (args.size() == 4)
            return args.fourth().map(colors::search).orElseGet(colors::names);
        return Collections.emptyList();
    }
    
}
