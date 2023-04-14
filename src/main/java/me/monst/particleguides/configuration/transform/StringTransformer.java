package me.monst.particleguides.configuration.transform;


import me.monst.pluginutil.configuration.transform.Transformer;

public class StringTransformer implements Transformer<String> {
    
    @Override
    public String parse(String input) {
        return input;
    }
    
}
