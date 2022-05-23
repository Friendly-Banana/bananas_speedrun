package ga.euroblox.bananas_speedrun;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Server;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.Date;

public record DragonDeathListener(BananasSpeedrun plugin) implements Listener {

    @EventHandler
    public void onEnderDragonDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof EnderDragon && plugin.speedrunState == State.Running && plugin.tillDragon) {
            plugin.speedrunState = State.DragonSlain;
            plugin.highscores.add(new Score(true, new Date().getTime(), plugin.timer, plugin.speedrunner));
            plugin.highscores.sort(Comparator.comparingLong(Score::ticks));
            String time = LocalTime.ofSecondOfDay(plugin.timer / 20).toString();
            Server server = e.getEntity().getServer();
            for (Player player : server.getOnlinePlayers())
                player.showTitle(Title.title(Component.text("Game Finished"), Component.text("Dragon has been slain in " + time)));

            server.broadcast(Utils.SpaceJoin("Dragon has been slain in", time, "by", Utils.ComponentList(plugin.speedrunner, uuid -> Utils.PlayerText(server, uuid))));
        }
    }

    @EventHandler
    public void onPlayerEndPortal(PlayerPortalEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL && plugin.speedrunState == State.Running && !plugin.tillDragon) {
            plugin.speedrunState = State.Finished;
            plugin.highscores.add(new Score(false, new Date().getTime(), plugin.timer, plugin.speedrunner));
            plugin.highscores.sort(Comparator.comparingLong(Score::ticks));
            String time = LocalTime.ofSecondOfDay(plugin.timer / 20).toString();
            Server server = e.getPlayer().getServer();
            for (Player player : server.getOnlinePlayers())
                player.showTitle(Title.title(Component.text("Game Finished"), Component.text("Finishing Minecraft took " + time)));

            server.broadcast(Utils.SpaceJoin("Minecraft was played through in", time, "by", Utils.ComponentList(plugin.speedrunner, uuid -> Utils.PlayerText(server, uuid))));
            server.broadcast(Utils.Command("Reset the world", "/reset"));
        }
    }
}
