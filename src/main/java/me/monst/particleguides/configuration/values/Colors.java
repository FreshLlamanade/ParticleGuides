package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.transform.ColorTransformer;
import me.monst.particleguides.particle.NamedColor;
import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.pluginutil.configuration.transform.BoundedTransformer;
import me.monst.pluginutil.configuration.transform.MapTransformer;
import me.monst.pluginutil.configuration.validation.Bound;
import org.bukkit.Color;

import java.util.*;
import java.util.stream.Collectors;

public class Colors extends ConfigurationValue<Map<String, Color>> {
    
    public Colors() {
        super("colors",
                getDefaultColorOptions(),
                new BoundedTransformer<>(
                        new MapTransformer<>(HashMap::new, name -> name, new ColorTransformer()),
                        Bound.disallowing(Map::isEmpty, empty -> onlyWhite())
                )
        );
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
        return Collections.singletonMap("white", Color.WHITE);
    }
    
    public NamedColor get(String name) {
        name = name.toLowerCase();
        Color color = get().get(name);
        return new NamedColor(name, color);
    }
    
    public NamedColor random() {
        return get().entrySet().stream()
                .skip((int) (get().size() * Math.random())).findFirst()
                .map(entry -> new NamedColor(entry.getKey(), entry.getValue()))
                .orElseGet(() -> new NamedColor("white", Color.WHITE));
    }
    
    public List<String> search(String search) {
        String lowerCaseSearch = search.toLowerCase();
        return get().keySet().stream().filter(key -> key.contains(lowerCaseSearch)).collect(Collectors.toList());
    }
    
    public List<String> names() {
        return new ArrayList<>(get().keySet());
    }
    
}
