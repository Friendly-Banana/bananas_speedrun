package ga.euroblox.bananas_speedrun;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand extends CustomCommand {
    public ListCommand(BananasSpeedrun plugin) {
        super(plugin);
    }

    @Override
    public boolean Run(CommandSender sender, Command command, String label, String[] args) {
        for (String arg : args) {
            Player player = sender.getServer().getPlayer(arg);
            if (player != null && !plugin.IsSpeedrunner(player.getUniqueId())) {
                plugin.AddSpeedrunner(player.getUniqueId());
                player.setGameMode(GameMode.ADVENTURE);
                player.teleport(player.getWorld().getSpawnLocation());
            }
        }
        plugin.UpdateState();
        return args.length > 0;
    }
}
