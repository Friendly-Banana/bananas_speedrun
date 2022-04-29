package ga.euroblox.bananas_speedrun;

import net.kyori.adventure.text.Component;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public final class BananasSpeedrun extends JavaPlugin {
    public static final String TIMER_PATH = "timer";
    public static final String STATE_PATH = "state";
    public static final String RESET_PATH = "reset";
    public static final String SPEEDRUNNER_PATH = "runner";
    public static final int YELLOW = 0xFCE94F;
    public static final UUID DEV_UUID = UUID.fromString("914d309b-9b39-4269-8698-69f3b569a563");

    public long timer = 0;
    public State speedrunState = State.NotStarted;
    public List<UUID> speedrunner = new ArrayList<>();
    public Set<UUID> activeRunner = new HashSet<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new DragonDeathListener(this), this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (speedrunState == State.Running) {
                timer++;
                if (timer % 20 == 0) {
                    String time = LocalTime.ofSecondOfDay(timer / 20).toString();
                    for (Player player : getServer().getOnlinePlayers())
                        player.sendActionBar(Component.text(time));
                }
            }
        }, 1, 1);
        getCommand("reset").setExecutor(new ResetCommand(this));
        getCommand("start").setExecutor(new StartCommand(this));
        getCommand("add").setExecutor(new AddCommand(this));
        // load previous run
        if (getConfig().getBoolean(RESET_PATH, false)) {
            Reset();
            return;
        }
        timer = getConfig().getInt(TIMER_PATH);
        speedrunState = State.valueOf(getConfig().getString(STATE_PATH, State.NotStarted.toString()));
        speedrunner = getConfig().getStringList(SPEEDRUNNER_PATH).stream().map(UUID::fromString).collect(Collectors.toList());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getConfig().set(TIMER_PATH, timer);
        getConfig().set(STATE_PATH, speedrunState.toString());
        getConfig().set(SPEEDRUNNER_PATH, speedrunner.stream().map(UUID::toString));
        saveConfig();
    }

    public boolean IsSpeedrunner(UUID uuid) {
        if (speedrunner.isEmpty()) {
            speedrunner.add(uuid);
            return true;
        }
        return speedrunner.contains(uuid);
    }

    public void AddSpeedrunner(UUID uuid) {
        if (!speedrunner.contains(uuid)) speedrunner.add(uuid);
        activeRunner.add(uuid);
    }

    public void UpdateState() {
        if (speedrunState == State.Running || speedrunState == State.NoSpeedrunnersOnline)
            speedrunState = activeRunner.isEmpty() ? State.NoSpeedrunnersOnline : State.Running;
    }

    public void PrepareReset() {
        getConfig().set(RESET_PATH, true);
        onDisable();
        Bukkit.spigot().restart();
    }

    void Reset() {
        getConfig().set(RESET_PATH, false);
        saveConfig();
        try {
            for (String worldName : new String[]{"world", "world_nether", "world_the_end"}) {
                File world = new File(getServer().getWorldContainer(), worldName);
                if (world.exists())
                    FileUtils.deleteDirectory(world);
            }
        } catch (IOException e) {
            getLogger().severe("Couldn't delete World folder.");
            e.printStackTrace();
        }
    }
}
