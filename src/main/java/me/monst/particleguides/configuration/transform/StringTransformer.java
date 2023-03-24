package me.monst.particleguides.configuration.transform;


import me.monst.particleguides.configuration.exception.ArgumentParseException;
import me.monst.particleguides.configuration.exception.UnreadableValueException;
import me.monst.particleguides.configuration.exception.ValueOutOfBoundsException;

public class StringTransformer implements Transformer<String> {
    
    @Override
    public String parse(String input) throws ArgumentParseException {
        return input;
    }
    
}
