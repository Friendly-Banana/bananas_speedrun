package ga.euroblox.bananas_speedrun.commands;

import ga.euroblox.bananas_speedrun.BananasSpeedrun;
import ga.euroblox.bananas_speedrun.PlayerListener;
import ga.euroblox.bananas_speedrun.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetCommand extends CustomCommand {
    private static boolean wantsReset;

    public ResetCommand(BananasSpeedrun plugin) {
        super(plugin);
    }

    @Override
    public boolean Run(CommandSender sender, Command command, String label, String[] args) {
        Server server = sender.getServer();
        if (!wantsReset) {
            wantsReset = true;
            server.broadcast(Utils.SpaceJoin(sender.getName(), "wants to reset the run. Please", Utils.Command("confirm", "/reset confirm"), "or", Utils.Command("cancel", "/reset cancel")));
        } else if (args[0].equalsIgnoreCase("cancel")) {
            wantsReset = false;
        } else if (args[0].equalsIgnoreCase("confirm")) {
            PlayerListener.motd = "";
            for (Player player : server.getOnlinePlayers())
                player.kick(Component.text("Resetting world").color(TextColor.color(0xFF0000)));
            plugin.PrepareReset();
        }
        return true;
    }
}
