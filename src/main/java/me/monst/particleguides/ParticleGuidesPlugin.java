package me.monst.particleguides;

import com.earth2me.essentials.Essentials;
import me.monst.particleguides.command.CommandRegisterService;
import me.monst.particleguides.command.breadcrumbs.BreadcrumbsCommand;
import me.monst.particleguides.command.guide.GuideCommand;
import me.monst.particleguides.command.plugin.ParticleGuidesCommand;
import me.monst.particleguides.configuration.Configuration;
import me.monst.particleguides.particle.BreadcrumbsListener;
import me.monst.particleguides.particle.ParticleService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ParticleGuidesPlugin extends JavaPlugin {
    
    private final Configuration configuration = new Configuration(this);
    private final ParticleService particleService = new ParticleService(this);
    
    @Override
    public void onEnable() {
        new CommandRegisterService(this).register(
                new GuideCommand(this, findEssentials()),
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
    
}
