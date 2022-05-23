package ga.euroblox.bananas_speedrun.commands;

import ga.euroblox.bananas_speedrun.BananasSpeedrun;
import ga.euroblox.bananas_speedrun.State;
import ga.euroblox.bananas_speedrun.Utils;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StartCommand extends CustomCommand {
    public StartCommand(BananasSpeedrun plugin) {
        super(plugin);
    }

    @Override
    public boolean Run(CommandSender sender, Command command, String label, String[] args) {
        if (plugin.activeRunner.isEmpty() || plugin.speedrunState != State.NotStarted) return false;
        World world = sender.getServer().getWorld("world");
        for (UUID uuid : plugin.activeRunner) {
            Player player = sender.getServer().getPlayer(uuid);
            player.teleport(world.getSpawnLocation());
            player.setGameMode(GameMode.SURVIVAL);
        }
        world.setTime(12 * 1000);
        world.setGameRule(GameRule.MOB_GRIEFING, true);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, true);
        world.setDifficulty(Difficulty.NORMAL);
        sender.getServer().broadcast(Utils.SpaceJoin("Speedrun started by", sender.name()));
        plugin.speedrunState = State.Running;
        return true;
    }
}
