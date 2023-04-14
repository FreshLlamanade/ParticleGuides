package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.transform.IntegerTransformer;
import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.pluginutil.configuration.validation.Bound;

public class BlocksPerBreadcrumb extends ConfigurationValue<Integer> {
    
    public BlocksPerBreadcrumb() {
        super("blocks-per-breadcrumb", 3, new IntegerTransformer().bounded(Bound.atLeast(1)));
    }
    
}
