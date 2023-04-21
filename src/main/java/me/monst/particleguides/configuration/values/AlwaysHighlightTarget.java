package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.transform.BooleanTransformer;
import me.monst.pluginutil.configuration.ConfigurationValue;

public class AlwaysHighlightTarget extends ConfigurationValue<Boolean> {
    
    public AlwaysHighlightTarget() {
        super("always-highlight-target", false, new BooleanTransformer());
    }
    
}
