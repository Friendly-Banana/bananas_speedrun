package ga.euroblox.bananas_speedrun.commands;

import ga.euroblox.bananas_speedrun.BananasSpeedrun;
import ga.euroblox.bananas_speedrun.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ListCommand extends CustomCommand {
    public ListCommand(BananasSpeedrun plugin) {
        super(plugin, false);
    }

    @Override
    public boolean Run(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(Component.text("Speedrunners: ").append(Utils.ComponentList(plugin.speedrunner, uuid -> Utils.PlayerText(sender.getServer(), uuid))));
        return true;
    }
}
