package ga.euroblox.bananas_speedrun;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record Score(long date, long ticks, Set<UUID> players) {
    private static transient final String dateKey = ".date";
    private static transient final String ticksKey = ".ticks";
    private static final transient String playersKey = ".players";

    public static Score load(FileConfiguration config, String key) {
        return new Score(config.getLong(key + dateKey), config.getLong(key + ticksKey), config.getStringList(key + playersKey).stream().map(UUID::fromString).collect(Collectors.toSet()));
    }

    public void save(FileConfiguration config, String key) {
        config.set(key + dateKey, date);
        config.set(key + ticksKey, ticks);
        config.set(key + playersKey, players.stream().map(UUID::toString));
    }
}
