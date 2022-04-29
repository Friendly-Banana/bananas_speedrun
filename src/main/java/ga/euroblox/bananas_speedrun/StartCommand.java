package ga.euroblox.bananas_speedrun;

import org.bukkit.GameMode;
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
        if (plugin.activeRunner.isEmpty()) return false;
        for (UUID uuid : plugin.activeRunner) {
            Player player = sender.getServer().getPlayer(uuid);
            player.teleport(player.getWorld().getSpawnLocation());
            player.setGameMode(GameMode.SURVIVAL);
        }
        plugin.speedrunState = State.Running;
        return true;
    }
}
