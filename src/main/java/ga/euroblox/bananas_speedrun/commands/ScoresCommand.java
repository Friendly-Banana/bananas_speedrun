package ga.euroblox.bananas_speedrun.commands;

import ga.euroblox.bananas_speedrun.BananasSpeedrun;
import ga.euroblox.bananas_speedrun.Score;
import ga.euroblox.bananas_speedrun.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScoresCommand extends CustomCommand {
    public ScoresCommand(BananasSpeedrun plugin) {
        super(plugin, false);
    }

    @Override
    public boolean Run(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            SendFormattedHighscores(sender, "Highscores:", plugin.highscores);
            return true;
        }
        Player player;
        if (args.length == 1 && (player = sender.getServer().getPlayer(args[0])) != null) {
            List<Score> playersScores = new ArrayList<>();
            for (Score score : plugin.highscores) {
                if (score.players().contains(player.getUniqueId())) {
                    playersScores.add(score);
                }
            }
            SendFormattedHighscores(sender, "Highscores of " + player.getName() + ":", playersScores);
            return true;
        }
        return false;
    }

    private void SendFormattedHighscores(CommandSender sender, String title, Iterable<Score> scores) {
        ArrayList<TextComponent> componentList = new ArrayList<>();
        componentList.add(Component.text(title));
        int index = 0;
        for (Score score : scores) {
            index++;
            TextComponent facts = Component.text(index + ". " + LocalTime.ofSecondOfDay(score.ticks() / 20).toString() + ", " + Date.from(Instant.ofEpochMilli(score.date())) + ": ");
            componentList.add(facts.append(Utils.ComponentList(score.players(), uuid -> Utils.PlayerText(sender.getServer(), uuid))));
        }
        sender.sendMessage(Component.join(JoinConfiguration.separator(Component.newline()), componentList));
    }

}
