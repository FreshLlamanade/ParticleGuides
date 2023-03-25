package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.ConfigurationValue;
import me.monst.particleguides.configuration.transform.BooleanTransformer;

public class GlobalVisibility extends ConfigurationValue<Boolean> {
    
    public GlobalVisibility() {
        super("global-visibility", false, new BooleanTransformer());
    }
    
}
