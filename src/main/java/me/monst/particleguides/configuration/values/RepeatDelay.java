package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.ConfigurationValue;
import me.monst.particleguides.configuration.transform.IntegerTransformer;

public class RepeatDelay extends ConfigurationValue<Integer> {
    
    public RepeatDelay() {
        super("repeat-delay-millis", 1500, new IntegerTransformer().positive());
    }
    
}
