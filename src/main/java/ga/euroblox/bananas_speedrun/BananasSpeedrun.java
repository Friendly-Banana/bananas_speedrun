package ga.euroblox.bananas_speedrun;

import ga.euroblox.bananas_speedrun.commands.*;
import net.kyori.adventure.text.Component;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public final class BananasSpeedrun extends JavaPlugin {
    private static final String TIMER_PATH = "timer";
    private static final String TILL_DRAGON_PATH = "till_dragon";
    private static final String STATE_PATH = "state";
    private static final String RESET_PATH = "reset";
    private static final String SPEEDRUNNER_PATH = "runner";
    private static final String SCORES_PATH = "highscores";

    public long timer = 0;
    public boolean tillDragon = true;
    public State speedrunState = State.NotStarted;
    public Set<UUID> speedrunner = new HashSet<>();
    public Set<UUID> activeRunner = new HashSet<>();
    public List<Score> highscores = new ArrayList<>();

    @Override
    public void onEnable() {
        // load previous run
        if (getConfig().getBoolean(RESET_PATH, false)) {
            Reset();
            return;
        }
        timer = getConfig().getInt(TIMER_PATH);
        tillDragon = getConfig().getBoolean(TILL_DRAGON_PATH);
        speedrunState = State.valueOf(getConfig().getString(STATE_PATH, State.NotStarted.toString()));
        speedrunner = getConfig().getStringList(SPEEDRUNNER_PATH).stream().map(UUID::fromString).collect(Collectors.toSet());
        ConfigurationSection section = getConfig().getConfigurationSection(SCORES_PATH);
        if (section == null)
            section = getConfig().createSection(SCORES_PATH);
        for (String key : section.getKeys(false))
            highscores.add(Score.Load(getConfig(), key));
        highscores.sort(Comparator.comparingLong(Score::ticks));
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
        getCommand("list").setExecutor(new ListCommand(this));
        getCommand("score").setExecutor(new ScoresCommand(this));
    }

    @Override
    public void onLoad() {
        if (speedrunState == State.NotStarted) {
            getServer().setDefaultGameMode(GameMode.ADVENTURE);
            for (World world : getServer().getWorlds()) {
                world.setGameRule(GameRule.MOB_GRIEFING, false);
                world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
                world.setDifficulty(Difficulty.PEACEFUL);
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getConfig().set(TIMER_PATH, timer);
        getConfig().set(TILL_DRAGON_PATH, tillDragon);
        getConfig().set(STATE_PATH, speedrunState.toString());
        getConfig().set(SPEEDRUNNER_PATH, speedrunner.stream().map(UUID::toString).toList());
        ConfigurationSection section = getConfig().getConfigurationSection(SCORES_PATH);
        if (section == null)
            section = getConfig().createSection(SCORES_PATH);
        for (int i = 0; i < highscores.size(); i++)
            highscores.get(i).Save(getConfig(), section.getCurrentPath() + "." + i);
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
        speedrunner.add(uuid);
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
