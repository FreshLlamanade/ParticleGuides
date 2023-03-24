package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.ConfigurationValue;
import me.monst.particleguides.configuration.transform.ColorTransformer;
import me.monst.particleguides.configuration.transform.MapTransformer;
import me.monst.particleguides.configuration.transform.StringTransformer;
import me.monst.particleguides.configuration.validation.Bound;
import org.bukkit.Color;

import java.util.*;
import java.util.stream.Collectors;

public class ColorOptions extends ConfigurationValue<Map<String, Color>> {
    
    public ColorOptions() {
        super("color-options",
                getDefaultColorOptions(),
                new MapTransformer<>(HashMap::new, new StringTransformer(), new ColorTransformer())
                        .bounded(Bound.disallowing(Map::isEmpty, empty -> onlyWhite())));
    }
    
    private static Map<String, Color> getDefaultColorOptions() {
        Map<String, Color> defaultColorOptions = new HashMap<>();
        defaultColorOptions.put("red", Color.RED);
        defaultColorOptions.put("green", Color.GREEN);
        defaultColorOptions.put("blue", Color.BLUE);
        defaultColorOptions.put("white", Color.WHITE);
        defaultColorOptions.put("black", Color.BLACK);
        defaultColorOptions.put("yellow", Color.YELLOW);
        defaultColorOptions.put("orange", Color.ORANGE);
        defaultColorOptions.put("purple", Color.PURPLE);
        defaultColorOptions.put("pink", Color.FUCHSIA);
        defaultColorOptions.put("gray", Color.GRAY);
        defaultColorOptions.put("light-gray", Color.SILVER);
        defaultColorOptions.put("dark-gray", Color.GRAY);
        defaultColorOptions.put("dark-red", Color.MAROON);
        defaultColorOptions.put("dark-green", Color.GREEN);
        defaultColorOptions.put("dark-blue", Color.NAVY);
        return defaultColorOptions;
    }
    
    private static Map<String, Color> onlyWhite() {
        Map<String, Color> onlyWhite = new HashMap<>();
        onlyWhite.put("white", Color.WHITE);
        return onlyWhite;
    }
    
    public Color findColorOrRandom(String name) {
        return findColor(name).orElseGet(this::randomColor);
    }
    
    public Optional<Color> findColor(String name) {
        if (name == null)
            return Optional.empty();
        return Optional.ofNullable(get().get(name));
    }
    
    public Color randomColor() {
        Collection<Color> colors = get().values();
        return colors.stream().skip((int) (colors.size() * Math.random())).findFirst().orElse(Color.WHITE);
    }
    
    public List<String> searchColors(String search) {
        String lowerCaseSearch = search.toLowerCase();
        return get().keySet().stream().filter(key -> key.contains(lowerCaseSearch)).collect(Collectors.toList());
    }
    
}
