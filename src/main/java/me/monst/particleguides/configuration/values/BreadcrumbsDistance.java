package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.ConfigurationValue;
import me.monst.particleguides.configuration.transform.IntegerTransformer;
import me.monst.particleguides.configuration.validation.Bound;

public class BreadcrumbsDistance extends ConfigurationValue<Integer> {
    
    public BreadcrumbsDistance() {
        super("breadcrumbs-distance", 3, new IntegerTransformer().bounded(Bound.atLeast(1)));
    }
    
}
