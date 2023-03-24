package me.monst.particleguides.command.breadcrumbs;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.Executable;
import me.monst.particleguides.command.Permission;
import me.monst.particleguides.command.Permissions;
import me.monst.particleguides.command.TopLevelDelegator;
import me.monst.particleguides.particle.ParticleService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class BreadcrumbsCommand implements TopLevelDelegator {
    
    private final Map<String, Executable> subCommands = new LinkedHashMap<>();
    
    public BreadcrumbsCommand(ParticleGuidesPlugin plugin) {
        Stream.of(
                new BreadcrumbsStart(plugin),
                new BreadcrumbsStop(plugin)
        ).forEach(subCommand -> subCommands.put(subCommand.getName(), subCommand));
    }
    
    @Override
    public String getName() {
        return "breadcrumbs";
    }
    
    @Override
    public String getDescription() {
        return "Turn on/off breadcrumbs.";
    }
    
    @Override
    public String getUsage() {
        return "/breadcrumbs <start|stop>";
    }
    
    @Override
    public Permission getPermission() {
        return Permissions.BREADCRUMBS;
    }
    
    @Override
    public Map<String, Executable> getSubCommands() {
        return subCommands;
    }
    
}
