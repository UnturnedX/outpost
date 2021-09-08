package me.unturned.outpost.objects;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Config {
    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File file;

    public Config(JavaPlugin instance, String name) {
        this.plugin = instance;
        this.file = new File(instance.getDataFolder(), name);

        final YamlConfiguration yamlConfiguration = new YamlConfiguration();

        if (!exists())
            return;

        try {
            yamlConfiguration.load(file);
            this.config = yamlConfiguration;
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public Config set(String path, Object value) {
        config.set(path, value);
        return this;
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return config.getConfigurationSection(path);
    }

    public ConfigurationSection createSection(String path) {
        return config.createSection(path);
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public boolean isList(String path) {
        return config.isList(path);
    }

    public boolean isInt(String path) {
        return config.isInt(path);
    }

    public boolean isDouble(String path) {
        return config.isDouble(path);
    }

    public boolean isLong(String path) {
        return config.isLong(path);
    }

    public boolean isItemStack(String path) {
        return config.isItemStack(path);
    }

    public List<Integer> getIntegerList(String path) {
        return config.getIntegerList(path);
    }

    public boolean isString(String path) {
        return config.isString(path);
    }

    public boolean exists(String path) {
        return config.get(path) != null;
    }

    public Object get(String path) {
        return config.get(path);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public Integer getInt(String path) {
        return config.getInt(path);
    }

    public Double getDouble(String path) {
        return config.getDouble(path);
    }

    public Long getLong(String path) {
        return config.getLong(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public float getFloat(String path) {
        return (float) config.getDouble(path);
    }

    public void save() {
        try {
            this.config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean exists() {
        return file.exists() || config != null;
    }

    public void create() {
        if (file != null && config != null) return;

        config = new YamlConfiguration();

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        this.file = new File(plugin.getDataFolder(), file.getName());
        YamlConfiguration yamlConfiguration = new YamlConfiguration();

        try {
            yamlConfiguration.load(file);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public Config getAndSet(String path, int increaseBy) {
        config.set(path, getOrDefault(path, 0) + increaseBy);
        return this;
    }

    public Config getAndSet(String path, double increaseBy) {
        config.set(path, getOrDefault(path, 0) + increaseBy);
        return this;
    }

    public Config getAndSet(String path, long increaseBy) {
        config.set(path, getOrDefault(path, 0) + increaseBy);
        return this;
    }

    public int getOrDefault(String path, int defaultValue) {
        return exists(path)
                ? config.getInt(path)
                : defaultValue;
    }

    public double getOrDefault(String path, double defaultValue) {
        return exists(path)
                ? config.getDouble(path)
                : defaultValue;
    }

    public long getOrDefault(String path, long defaultValue) {
        return exists(path)
                ? config.getLong(path)
                : defaultValue;
    }

    public String getOrDefault(String path, String defaultValue) {
        return exists(path)
                ? config.getString(path)
                : defaultValue;
    }

    public void delete() {
        if (exists()) {
            file.delete();
        }
    }
}