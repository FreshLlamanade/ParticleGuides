package me.monst.particleguides.particle;

import me.monst.particleguides.command.CommandExecutionException;
import org.bukkit.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParticleColors {

    private static final Map<String, Color> COLOR_MAP = new HashMap<>();
    static {
        COLOR_MAP.put("red", Color.RED);
        COLOR_MAP.put("lime", Color.LIME);
        COLOR_MAP.put("green", Color.GREEN);
        COLOR_MAP.put("blue", Color.BLUE);
        COLOR_MAP.put("black", Color.BLACK);
        COLOR_MAP.put("white", Color.WHITE);
        COLOR_MAP.put("yellow", Color.YELLOW);
        COLOR_MAP.put("orange", Color.ORANGE);
        COLOR_MAP.put("purple", Color.PURPLE);
        COLOR_MAP.put("aqua", Color.AQUA);
        COLOR_MAP.put("gray", Color.SILVER);
        COLOR_MAP.put("pink", Color.fromRGB(255, 192, 203));
        COLOR_MAP.put("cyan", Color.fromRGB(0, 255, 255));
        COLOR_MAP.put("magenta", Color.fromRGB(255, 0, 255));
    }
    
    @Deprecated
    public static Color get(String name) throws CommandExecutionException {
        Color color = COLOR_MAP.get(name);
        if (color == null)
            throw new CommandExecutionException("'" + name + "' is not a valid color.");
        return color;
    }
    
    @Deprecated
    public static List<String> search(String search) {
        return COLOR_MAP.keySet().stream()
                .filter(key -> key.contains(search))
                .collect(Collectors.toList());
    }
    
    @Deprecated
    public static Color random() {
        return COLOR_MAP.values().stream()
                .skip((int) (Math.random() * COLOR_MAP.size()))
                .findFirst()
                .orElse(Color.WHITE);
    }

}
