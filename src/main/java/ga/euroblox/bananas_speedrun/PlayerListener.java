package ga.euroblox.bananas_speedrun;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

public record PlayerListener(BananasSpeedrun plugin) implements Listener {
    public static String motd = "";

    @EventHandler
    public void onPing(ServerListPingEvent pingEvent) {
        pingEvent.motd(Component.text(motd));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.IsSpeedrunner(player.getUniqueId())) {
            plugin.activeRunner.add(player.getUniqueId());
            plugin.UpdateState();
            if (plugin.speedrunState == State.NotStarted) {
                player.setGameMode(GameMode.ADVENTURE);
                player.showTitle(Title.title(Component.text("Speedrunner"), Component.text("/start the run or /add other speedrunners")));
            } else {
                player.setGameMode(GameMode.SURVIVAL);
                player.showTitle(Title.title(Component.text("Speedrunner!").color(Utils.GREEN), Component.text("Defeat the enderdragon as fast as possible.")));
            }
        } else {
            player.setGameMode(GameMode.SPECTATOR);
            if (player.isOp())
                player.showTitle(Title.title(Component.text("Operator"), Component.text("/start the run or /add other speedrunners")));
        }

        motd = "ingame";
        /*if (player.getUniqueId().equals(plugin.DEV_UUID)) {
            player.displayName(player.displayName().color(TextColor.color(plugin.YELLOW)));
        }*/
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        plugin.activeRunner.remove(event.getPlayer().getUniqueId());
        plugin.UpdateState();
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        plugin.activeRunner.remove(event.getPlayer().getUniqueId());
        plugin.UpdateState();
    }
}