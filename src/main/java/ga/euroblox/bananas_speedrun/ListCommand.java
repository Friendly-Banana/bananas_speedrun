package ga.euroblox.bananas_speedrun;

import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ListCommand extends CustomCommand {
    public ListCommand(BananasSpeedrun plugin) {
        super(plugin, false);
    }

    @Override
    public boolean Run(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(Component.text("Speedrunners: ").append(Utils.ComponentList(plugin.speedrunner,
                uuid -> {
                    OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(uuid);
                    if (offlinePlayer.isOnline())
                        return Component.text(offlinePlayer.getName()).color(Utils.GREEN);
                    else
                        return Component.text(offlinePlayer.getName());
                })));
        return true;
    }
}
