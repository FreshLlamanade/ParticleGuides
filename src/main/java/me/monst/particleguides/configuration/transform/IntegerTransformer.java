package me.monst.particleguides.configuration.transform;


import me.monst.pluginutil.configuration.exception.ArgumentParseException;
import me.monst.pluginutil.configuration.exception.UnreadableValueException;
import me.monst.pluginutil.configuration.exception.ValueOutOfBoundsException;
import me.monst.pluginutil.configuration.transform.Transformer;
import me.monst.pluginutil.configuration.validation.Bound;

public class IntegerTransformer implements Transformer<Integer> {
    
    @Override
    public Integer parse(String input) throws ArgumentParseException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException("'" + input + "' is not an integer.");
        }
    }
    
    @Override
    public Integer convert(Object object) throws ValueOutOfBoundsException, UnreadableValueException {
        if (object instanceof Integer)
            return (Integer) object;
        else if (object instanceof Number)
            throw new ValueOutOfBoundsException(((Number) object).intValue());
        return parse(object.toString());
    }
    
    @Override
    public Object toYaml(Integer value) {
        return value;
    }
    
}
