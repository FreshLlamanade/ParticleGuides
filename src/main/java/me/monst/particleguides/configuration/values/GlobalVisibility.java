package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.transform.BooleanTransformer;
import me.monst.pluginutil.configuration.ConfigurationValue;

public class GlobalVisibility extends ConfigurationValue<Boolean> {
    
    public GlobalVisibility() {
        super("global-visibility", false, new BooleanTransformer());
    }
    
}
