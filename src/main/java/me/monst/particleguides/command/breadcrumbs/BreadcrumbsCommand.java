package me.monst.particleguides.command.breadcrumbs;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.Permissions;
import me.monst.pluginutil.command.Permission;
import me.monst.pluginutil.command.SimpleCommandDelegator;

public class BreadcrumbsCommand extends SimpleCommandDelegator {
    
    public BreadcrumbsCommand(ParticleGuidesPlugin plugin) {
        super(
                "breadcrumbs",
                "Manage breadcrumbs.",
                new BreadcrumbsStart(plugin.getParticleService(), plugin.config().colors),
                new BreadcrumbsStop(plugin.getParticleService()),
                new BreadcrumbsShow(plugin.getParticleService(), plugin.config().colors),
                new BreadcrumbsHide(plugin.getParticleService())
//                new BreadcrumbsPause(plugin.getParticleService()),
//                new BreadcrumbsResume(plugin.getParticleService())
        );
    }
    
    @Override
    public Permission getPermission() {
        return Permissions.BREADCRUMBS;
    }
    
}
