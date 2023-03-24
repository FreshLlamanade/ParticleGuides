package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.ConfigurationValue;
import me.monst.particleguides.configuration.transform.IntegerTransformer;

public class ParticleDelay extends ConfigurationValue<Integer> {
    
    public ParticleDelay() {
        super("particle-delay-millis", 50, new IntegerTransformer().positive());
    }
    
}
