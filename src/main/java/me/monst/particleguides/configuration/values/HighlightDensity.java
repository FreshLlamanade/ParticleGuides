package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.transform.IntegerTransformer;
import me.monst.pluginutil.configuration.ConfigurationValue;

public class HighlightDensity extends ConfigurationValue<Integer> {
    
    public HighlightDensity() {
        super("highlight-density", 20, new IntegerTransformer().positive());
    }
    
}
