package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.transform.IntegerTransformer;
import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.pluginutil.configuration.transform.BoundedTransformer;
import me.monst.pluginutil.configuration.validation.Bound;

public class RepeatDelay extends ConfigurationValue<Integer> {
    
    public RepeatDelay() {
        super("repeat-delay-millis", 1500, new BoundedTransformer<>(
                new IntegerTransformer(),
                Bound.atLeast(0)
        ));
    }
    
}
