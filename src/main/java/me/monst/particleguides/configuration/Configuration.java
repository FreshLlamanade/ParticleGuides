package me.monst.particleguides.configuration;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.configuration.values.*;

public class Configuration extends ConfigurationBranch {
    
    private final YamlFile file;
    
    public final ParticleDensity particleDensity = addChild(new ParticleDensity());
    
    public final HighlightDensity highlightDensity = addChild(new HighlightDensity());
    
    public final RepeatDelay repeatDelay = addChild(new RepeatDelay());
    
    public final ParticleDelay particleDelay = addChild(new ParticleDelay());
    
    public final GuideLength length = addChild(new GuideLength());
    
    public final BlocksPerBreadcrumb blocksPerBreadcrumb = addChild(new BlocksPerBreadcrumb());
    
    public final Colors colors = addChild(new Colors());
    
    public final GlobalVisibility globalVisibility = addChild(new GlobalVisibility());
    
    public Configuration(ParticleGuidesPlugin plugin) {
        super("config.yml");
        this.file = new YamlFile(plugin, getKey()); // Create config.yml file
        reload();
    }
    
    public void reload() {
        load();
        save();
    }
    
    public void load() {
        feed(file.load());
    }
    
    public void save() {
        file.save(getAsYaml());
    }
    
}
