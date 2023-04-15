package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.transform.IntegerTransformer;
import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.pluginutil.configuration.transform.BoundedTransformer;
import me.monst.pluginutil.configuration.validation.Bound;

public class ParticleDensity extends ConfigurationValue<Integer> {
    
    public ParticleDensity() {
        super("particle-density", 5, new BoundedTransformer<>(
                new IntegerTransformer(),
                Bound.atLeast(0)
        ));
    }
    
}
