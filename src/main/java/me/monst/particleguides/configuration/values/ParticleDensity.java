package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.ConfigurationValue;
import me.monst.particleguides.configuration.transform.IntegerTransformer;

public class ParticleDensity extends ConfigurationValue<Integer> {
    
    public ParticleDensity() {
        super("particle-density", 5, new IntegerTransformer().positive());
    }
    
}
