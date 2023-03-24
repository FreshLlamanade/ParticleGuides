package me.monst.particleguides.configuration;

import me.monst.particleguides.configuration.exception.ArgumentParseException;
import me.monst.particleguides.configuration.exception.MissingValueException;
import me.monst.particleguides.configuration.exception.UnreadableValueException;
import me.monst.particleguides.configuration.exception.ValueOutOfBoundsException;
import me.monst.particleguides.configuration.transform.Transformer;
import org.bukkit.entity.Player;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A configuration value stored in a yaml file.
 * Every configuration value has a default value, which is used when no other value is specified.
 * The value can be changed at runtime by calling {@link #feed(Object)}.
 * @param <T> the type of the value
 */
public class ConfigurationValue<T> extends ConfigurationNode {

    private final T defaultValue;
    private T value;
    private final Transformer<T> transformer;
    private final Set<T> history = new LinkedHashSet<>();

    /**
     * Creates a new configuration value at the specified path in the plugin's configuration file. Calling this
     * constructor will immediately load the value from the file, creating it if it doesn't exist.
     *
     * @param key          the relative path of this value
     * @param defaultValue the default value of this configuration value
     * @param transformer  the transformer used for converting the data to and from the desired type
     */
    public ConfigurationValue(String key, T defaultValue, Transformer<T> transformer) {
        super(key);
        this.defaultValue = defaultValue;
        this.value = addToHistory(defaultValue);
        this.transformer = transformer;
    }
    
    private T addToHistory(T value) {
        history.remove(value); // Remove first to ensure that the most recent value is at the end
        history.add(value);
        return value;
    }
    
    /**
     * Gets the current value of this configuration value.
     * @return the current value
     */
    public final T get() {
        return value;
    }
    
    @Override
    protected final void feed(Object object) {
        set(convert(object));
    }
    
    private T convert(Object object) {
        try {
            transformer.nullCheck(object);
            return transformer.convert(object);
        } catch (ValueOutOfBoundsException e) {
            return e.getReplacement();
        } catch (MissingValueException | UnreadableValueException e) {
            return defaultValue;
        }
    }
    
    /**
     * Parses a user-entered string to a new value, and sets this configuration value.
     * @param input user input to be parsed
     * @throws ArgumentParseException if the input could not be parsed
     */
    public final void feed(String input) throws ArgumentParseException {
        set(transformer.parse(input));
    }
    
    private void set(T value) {
        beforeSet();
        this.value = addToHistory(value);
        afterSet();
    }
    
    @Override
    protected final Object getAsYaml() {
        return transformer.toYaml(value);
    }

    /**
     * An action to be taken before every time this configuration value is set to a new value.
     */
    protected void beforeSet() {

    }

    /**
     * An action to be taken after every time this configuration value is set to a new value.
     */
    protected void afterSet() {

    }

    /**
     * Gets a list of tab-completions to be shown to a player typing in a command.
     * By default, this returns a formatted list of the current value and the default value of this configuration value.
     * @param player the player typing in the command
     * @param args the arguments the player has typed so far
     * @return a list of tab-completions
     */
    public List<String> getTabCompletions(Player player, List<String> args) {
        return history.stream().map(transformer::format).collect(Collectors.toList());
    }

    /**
     * @return the formatted current state of this configuration value
     */
    @Override
    public final String toString() {
        return transformer.format(get());
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigurationValue<?> that = (ConfigurationValue<?>) o;
        return this.getKey().equals(that.getKey()) && Objects.equals(this.defaultValue, that.defaultValue);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getKey(), defaultValue);
    }

}
