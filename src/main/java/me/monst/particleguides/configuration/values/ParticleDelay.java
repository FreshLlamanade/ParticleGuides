package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.transform.IntegerTransformer;
import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.pluginutil.configuration.transform.BoundedTransformer;
import me.monst.pluginutil.configuration.validation.Bound;

public class ParticleDelay extends ConfigurationValue<Integer> {
    
    public ParticleDelay() {
        super("particle-delay-millis", 50, new BoundedTransformer<>(
                new IntegerTransformer(),
                Bound.atLeast(0)
        ));
    }
    
}
