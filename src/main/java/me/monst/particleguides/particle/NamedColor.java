package me.monst.particleguides.particle;

import org.bukkit.Color;

/**
 * A color with a name. This class is used to represent the colors configured in the plugin's config.yml.
 */
public class NamedColor {

    private final String name;
    private final Color color;
    
    public NamedColor(String name, Color color) {
        this.name = name;
        this.color = color;
    }
    
    public String getName() {
        return name;
    }
    
    public Color getColor() {
        return color;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamedColor that = (NamedColor) o;
        return name.equals(that.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
}
