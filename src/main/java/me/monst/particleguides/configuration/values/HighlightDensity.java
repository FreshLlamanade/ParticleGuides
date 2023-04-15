package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.transform.IntegerTransformer;
import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.pluginutil.configuration.transform.BoundedTransformer;
import me.monst.pluginutil.configuration.validation.Bound;

public class HighlightDensity extends ConfigurationValue<Integer> {
    
    public HighlightDensity() {
        super("highlight-density", 20, new BoundedTransformer<>(
                new IntegerTransformer(),
                Bound.atLeast(0)
        ));
    }
    
}
