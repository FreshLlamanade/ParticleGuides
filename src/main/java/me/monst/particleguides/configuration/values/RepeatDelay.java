package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.transform.IntegerTransformer;
import me.monst.pluginutil.configuration.ConfigurationValue;

public class RepeatDelay extends ConfigurationValue<Integer> {
    
    public RepeatDelay() {
        super("repeat-delay-millis", 1500, new IntegerTransformer().positive());
    }
    
}
