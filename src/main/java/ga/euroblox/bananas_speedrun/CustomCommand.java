package ga.euroblox.bananas_speedrun;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class CustomCommand implements CommandExecutor {
    protected final BananasSpeedrun plugin;

    public CustomCommand(BananasSpeedrun plugin) {
        this.plugin = plugin;
    }

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp() || sender instanceof Player p && plugin.IsSpeedrunner(p.getUniqueId())) {
            return Run(sender, command, label, args);
        } else {
            return false;
        }
    }

    public abstract boolean Run(CommandSender sender, Command command, String label, String[] args);
}
