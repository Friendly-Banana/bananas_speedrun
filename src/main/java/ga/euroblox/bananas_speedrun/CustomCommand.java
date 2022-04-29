package ga.euroblox.bananas_speedrun;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class CustomCommand implements CommandExecutor {
    protected final BananasSpeedrun plugin;
    protected final boolean needsPerms;

    public CustomCommand(BananasSpeedrun plugin) {
        this(plugin, true);
    }

    public CustomCommand(BananasSpeedrun plugin, boolean needsPerms) {
        this.plugin = plugin;
        this.needsPerms = needsPerms;
    }

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!needsPerms || sender.isOp() || sender instanceof Player p && plugin.IsSpeedrunner(p.getUniqueId())) {
            return Run(sender, command, label, args);
        } else {
            sender.sendMessage("You need to have OP rights or be a speedrunner to run this command.");
            return false;
        }
    }

    public abstract boolean Run(CommandSender sender, Command command, String label, String[] args);
}
