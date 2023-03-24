package me.monst.particleguides.command.guide;

import com.earth2me.essentials.Essentials;
import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.Executable;
import me.monst.particleguides.command.Permission;
import me.monst.particleguides.command.Permissions;
import me.monst.particleguides.command.TopLevelDelegator;

import java.util.LinkedHashMap;
import java.util.Map;

public class GuideCommand implements TopLevelDelegator {
    
    private final Map<String, Executable> subCommands = new LinkedHashMap<>();
    
    public GuideCommand(ParticleGuidesPlugin plugin, Essentials essentials) {
        addSubCommand(new GuideCoords(plugin));
        addSubCommand(new GuideDeath(plugin));
        addSubCommand(new GuideHere(plugin));
        if (essentials != null)
            addSubCommand(new GuideHome(plugin, essentials));
        addSubCommand(new GuidePlayer(plugin));
        addSubCommand(new GuideStop(plugin));
    }
    
    private void addSubCommand(Executable subCommand) {
        subCommands.put(subCommand.getName(), subCommand);
    }
    
    @Override
    public String getName() {
        return "guide";
    }
    
    @Override
    public String getDescription() {
        return "Create a particle trail which guides you to a specified location.";
    }
    
    @Override
    public String getUsage() {
        return "/guide <coords|death|here|home|player|stop>";
    }
    
    @Override
    public Permission getPermission() {
        return Permissions.GUIDE;
    }
    
    @Override
    public Map<String, Executable> getSubCommands() {
        return subCommands;
    }
    
}
