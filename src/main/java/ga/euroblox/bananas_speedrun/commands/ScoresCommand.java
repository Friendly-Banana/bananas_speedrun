package ga.euroblox.bananas_speedrun.commands;

import ga.euroblox.bananas_speedrun.BananasSpeedrun;
import ga.euroblox.bananas_speedrun.Score;
import ga.euroblox.bananas_speedrun.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        ArrayList<Component> componentList = new ArrayList<>();
        componentList.add(Component.text(title));
        int index = 0;
        for (Score score : scores) {
            index++;
            String date = ZonedDateTime.ofInstant(Instant.ofEpochMilli(score.date()),
                    ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            Component info = Utils.SpaceJoin(index + ".", Component.text(LocalTime.ofSecondOfDay(score.ticks() / 20).toString()).color(Utils.GOLD), "by", Utils.ComponentList(score.players(), uuid -> Utils.PlayerText(sender.getServer(), uuid)));
            componentList.add(info.hoverEvent(HoverEvent.showText(Utils.NewlineJoin(date, score.tillDragon() ? "Time till Dragon death" : "Time till entering Endportal"))));
        }
        sender.sendMessage(Component.join(JoinConfiguration.separator(Component.newline()), componentList));
    }

}
