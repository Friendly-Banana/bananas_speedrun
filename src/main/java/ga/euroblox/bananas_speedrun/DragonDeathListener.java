package ga.euroblox.bananas_speedrun;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Server;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.time.LocalTime;
import java.util.stream.Collectors;

public record DragonDeathListener(BananasSpeedrun plugin) implements Listener {

    @EventHandler
    public void onEnderDragonDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof EnderDragon) {
            plugin.speedrunState = State.NoSpeedrunnersOnline;
            String time = LocalTime.ofSecondOfDay(plugin.timer / 20).toString();
            Server server = e.getEntity().getServer();
            for (Player player : server.getOnlinePlayers())
                player.showTitle(Title.title(Component.text("Game Finished"), Component.text("Dragon has been slain in " + time)));

            server.broadcast(Utils.TextWithCommands("Dragon has been slain in", time, "by\n", plugin.activeRunner.stream().map(uuid -> server.getPlayer(uuid).getName()).collect(Collectors.joining(" ")), "\n", Utils.Command("Reset the world", "/reset")));
        }
    }
}
