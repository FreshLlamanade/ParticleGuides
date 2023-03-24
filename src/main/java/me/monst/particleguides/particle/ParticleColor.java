package me.monst.particleguides.particle;

import org.bukkit.Color;

import java.util.Objects;

public class ParticleColor {

    private final String name;
    private final Color color;
    
    public ParticleColor(String name, Color color) {
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
        ParticleColor that = (ParticleColor) o;
        return Objects.equals(color, that.color);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
    
}
