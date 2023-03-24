package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.ConfigurationValue;
import me.monst.particleguides.configuration.transform.IntegerTransformer;

public class GuideDensity extends ConfigurationValue<Integer> {
    
    public GuideDensity() {
        super("guide-density", 5, new IntegerTransformer().positive());
    }
    
}
