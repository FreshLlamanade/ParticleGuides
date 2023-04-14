package me.monst.particleguides.configuration.transform;

import me.monst.pluginutil.configuration.exception.ArgumentParseException;
import me.monst.pluginutil.configuration.transform.Transformer;
import org.bukkit.Color;

public class ColorTransformer implements Transformer<Color> {
    
    @Override
    public Color parse(String input) throws ArgumentParseException {
        try {
            return Color.fromRGB(Integer.parseInt(input, 16));
        } catch (NumberFormatException e) {
            throw new ArgumentParseException("Invalid RGB color hex code.");
        }
    }
    
    @Override
    public String format(Color value) {
        return String.format("%06X", value.asRGB()); // 0xRRGGBB
    }
    
    @Override
    public Object toYaml(Color value) {
        return format(value);
    }
    
}
