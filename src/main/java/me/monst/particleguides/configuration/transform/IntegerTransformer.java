package me.monst.particleguides.configuration.transform;


import me.monst.particleguides.configuration.exception.ArgumentParseException;
import me.monst.particleguides.configuration.exception.UnreadableValueException;
import me.monst.particleguides.configuration.exception.ValueOutOfBoundsException;
import me.monst.particleguides.configuration.validation.Bound;

public class IntegerTransformer implements Transformer<Integer> {
    
    private int radix = 10;
    
    @Override
    public Integer parse(String input) throws ArgumentParseException {
        try {
            return Integer.parseInt(input, radix);
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
    
    public IntegerTransformer radix(int radix) {
        this.radix = radix;
        return this;
    }
    
    public Transformer<Integer> positive() {
        return bounded(Bound.atLeast(0));
    }
    
}
