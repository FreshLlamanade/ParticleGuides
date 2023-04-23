package me.monst.particleguides.command.guide;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.configuration.values.Colors;
import me.monst.particleguides.particle.ParticleService;
import me.monst.pluginutil.command.SimpleCommandDelegator;

public class GuideCommand extends SimpleCommandDelegator {
    
    public GuideCommand(ParticleGuidesPlugin plugin) {
        super(
                "guide",
                "Create a particle trail which guides you to a specified location."
        );
        ParticleService particleService = plugin.getParticleService();
        Colors colors = plugin.config().colors;
        addSubCommand(new GuideCoords(particleService, colors));
        addSubCommand(new GuideDeath(particleService, colors));
        addSubCommand(new GuideHere(particleService, colors));
        if (plugin.hasEssentials())
            addSubCommand(new GuideHome(particleService, colors, plugin.getEssentials()));
        addSubCommand(new GuidePlayer(plugin, particleService, colors));
        addSubCommand(new GuideStop(particleService));
    }
    
}
