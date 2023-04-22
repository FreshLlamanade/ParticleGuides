package me.monst.particleguides;

import com.earth2me.essentials.Essentials;
import me.monst.particleguides.command.breadcrumbs.BreadcrumbsCommand;
import me.monst.particleguides.command.guide.GuideAcceptCommand;
import me.monst.particleguides.command.guide.GuideCommand;
import me.monst.particleguides.command.guide.GuideDenyCommand;
import me.monst.particleguides.command.plugin.ParticleGuidesCommand;
import me.monst.particleguides.configuration.Configuration;
import me.monst.particleguides.particle.BreadcrumbsListener;
import me.monst.particleguides.particle.ParticleService;
import me.monst.pluginutil.command.CommandRegisterService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ParticleGuidesPlugin extends JavaPlugin {
    
    private final Configuration configuration = new Configuration(this);
    private final ParticleService particleService = new ParticleService(this);
    private Essentials essentials;
    
    @Override
    public void onEnable() {
        this.essentials = findEssentials();
        new CommandRegisterService(this).register(
                new GuideCommand(this),
                new GuideAcceptCommand(particleService),
                new GuideDenyCommand(particleService),
                new BreadcrumbsCommand(this),
                new ParticleGuidesCommand(this)
        );
        Bukkit.getPluginManager().registerEvents(new BreadcrumbsListener(particleService), this);
    }
    
    private Essentials findEssentials() {
        Plugin plugin = getServer().getPluginManager().getPlugin("Essentials");
        if (plugin instanceof Essentials)
            return (Essentials) plugin;
        return null;
    }
    
    public Configuration config() {
        return configuration;
    }
    
    public ParticleService getParticleService() {
        return particleService;
    }
    
    public boolean hasEssentials() {
        return essentials != null && essentials.isEnabled();
    }
    
    public Essentials getEssentials() {
        return essentials;
    }
    
}
