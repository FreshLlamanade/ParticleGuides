package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.ConfigurationValue;
import me.monst.particleguides.configuration.transform.IntegerTransformer;

public class HighlightDensity extends ConfigurationValue<Integer> {
    
    public HighlightDensity() {
        super("highlight-density", 20, new IntegerTransformer().positive());
    }
    
}
