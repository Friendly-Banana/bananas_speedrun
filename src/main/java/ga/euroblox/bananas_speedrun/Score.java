package ga.euroblox.bananas_speedrun;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record Score(boolean tillDragon, long date, long ticks, Set<UUID> players) {
    private static final String dragonKey = ".dragon";
    private static final String dateKey = ".date";
    private static final String ticksKey = ".ticks";
    private static final String playersKey = ".players";

    public static Score Load(FileConfiguration config, String key) {
        return new Score(config.getBoolean(key + dragonKey), config.getLong(key + dateKey), config.getLong(key + ticksKey), config.getStringList(key + playersKey).stream().map(UUID::fromString).collect(Collectors.toSet()));
    }

    public void Save(FileConfiguration config, String key) {
        config.set(key + dragonKey, tillDragon);
        config.set(key + dateKey, date);
        config.set(key + ticksKey, ticks);
        config.set(key + playersKey, players.stream().map(UUID::toString).toList());
    }
}
