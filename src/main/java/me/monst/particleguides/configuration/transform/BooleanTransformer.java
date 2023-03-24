package me.monst.particleguides.configuration.transform;


import me.monst.particleguides.configuration.exception.ArgumentParseException;
import me.monst.particleguides.configuration.exception.UnreadableValueException;
import me.monst.particleguides.configuration.exception.ValueOutOfBoundsException;

public class BooleanTransformer implements Transformer<Boolean> {
    
    @Override
    public Boolean parse(String s) throws ArgumentParseException {
        if (s.equalsIgnoreCase("true")
                || s.equalsIgnoreCase("yes")
                || s.equalsIgnoreCase("on"))
            return true;
        if (s.equalsIgnoreCase("false")
                || s.equalsIgnoreCase("no")
                || s.equalsIgnoreCase("off"))
            return false;
        throw new ArgumentParseException("'" + s + "' is not a valid boolean.");
    }
    
    @Override
    public Boolean convert(Object object) throws ValueOutOfBoundsException, UnreadableValueException {
        if (object instanceof Boolean)
            return (Boolean) object;
        throw new ValueOutOfBoundsException(parse(object.toString()));
    }
    
    @Override
    public Object toYaml(Boolean value) {
        return value; // Store in file as boolean
    }
    
}
