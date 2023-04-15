package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.transform.IntegerTransformer;
import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.pluginutil.configuration.transform.BoundedTransformer;
import me.monst.pluginutil.configuration.validation.Bound;

public class GuideLength extends ConfigurationValue<Integer> {
    
    public GuideLength() {
        super("guide-length", 10, new BoundedTransformer<>(
                new IntegerTransformer(),
                Bound.atLeast(0)
        ));
    }
    
}
