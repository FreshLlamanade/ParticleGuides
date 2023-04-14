package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.transform.IntegerTransformer;
import me.monst.pluginutil.configuration.ConfigurationValue;

public class ParticleDelay extends ConfigurationValue<Integer> {
    
    public ParticleDelay() {
        super("particle-delay-millis", 50, new IntegerTransformer().positive());
    }
    
}
