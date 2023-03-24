package me.monst.particleguides.configuration;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.configuration.values.*;

public class Configuration extends ConfigurationBranch {
    
    private final YamlFile file;
    
    public final GuideLength length = addChild(new GuideLength());
    
    public final GuideDensity guideDensity = addChild(new GuideDensity());
    
    public final HighlightDensity highlightDensity = addChild(new HighlightDensity());
    
    public final RepeatDelay repeatDelay = addChild(new RepeatDelay());
    
    public final ParticleDelay particleDelay = addChild(new ParticleDelay());
    
    public final ColorOptions colorOptions = addChild(new ColorOptions());
    
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
