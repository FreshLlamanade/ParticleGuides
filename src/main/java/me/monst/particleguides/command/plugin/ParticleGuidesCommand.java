package me.monst.particleguides.command.plugin;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.Executable;
import me.monst.particleguides.command.Permission;
import me.monst.particleguides.command.Permissions;
import me.monst.particleguides.command.TopLevelDelegator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ParticleGuidesCommand implements TopLevelDelegator {
    
    private final Map<String, Executable> subCommands = new LinkedHashMap<>();
    
    public ParticleGuidesCommand(ParticleGuidesPlugin plugin) {
        Stream.of(
                new ParticleGuidesConfig(plugin)
        ).forEach(subCommand -> subCommands.put(subCommand.getName(), subCommand));
    }
    
    @Override
    public String getName() {
        return "particleguides";
    }
    
    @Override
    public String getDescription() {
        return "Manage the plugin.";
    }
    
    @Override
    public String getUsage() {
        return "/particleguides <config>";
    }
    
    @Override
    public Permission getPermission() {
        return Permissions.ADMIN;
    }
    
    @Override
    public Map<String, Executable> getSubCommands() {
        return subCommands;
    }
    
}
