package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.transform.IntegerTransformer;
import me.monst.pluginutil.configuration.ConfigurationValue;

public class ParticleDensity extends ConfigurationValue<Integer> {
    
    public ParticleDensity() {
        super("particle-density", 5, new IntegerTransformer().positive());
    }
    
}
