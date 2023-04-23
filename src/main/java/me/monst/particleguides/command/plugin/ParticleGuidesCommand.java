package me.monst.particleguides.command.plugin;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.Permissions;
import me.monst.pluginutil.command.Permission;
import me.monst.pluginutil.command.SimpleCommandDelegator;

public class ParticleGuidesCommand extends SimpleCommandDelegator {
    
    public ParticleGuidesCommand(ParticleGuidesPlugin plugin) {
        super(
                "particleguides",
                "Manage the plugin.",
                new ParticleGuidesConfig(plugin.config())
        );
    }
    
    @Override
    public Permission getPermission() {
        return Permissions.ADMIN;
    }
    
}
