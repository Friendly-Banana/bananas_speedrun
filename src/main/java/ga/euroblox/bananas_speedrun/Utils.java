package ga.euroblox.bananas_speedrun;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public final class Utils {
    public static final TextColor GOLD = TextColor.color(0xFFF200);
    public static final TextColor GREEN = TextColor.color(0x00FF00);
    public static final TextColor RED = TextColor.color(0xA80000);
    public static final TextColor BLUE = TextColor.color(0x0000A8);
    public static final TextColor LIGHT_BLUE = TextColor.color(0x0088CC);
    public static final TextColor COMMAND_COLOR = LIGHT_BLUE;

    public static <T> Component ComponentList(T[] array, Function<T, Component> converter) {
        return ComponentList(Arrays.stream(array).toList(), converter);
    }

    public static <T> Component ComponentList(Iterable<T> list, Function<T, Component> converter) {
        List<Component> componentList = new ArrayList<>();
        for (T element : list)
            componentList.add(converter.apply(element));
        return Component.join(JoinConfiguration.separator(Component.space()), componentList);
    }

    public static Component NewlineJoin(Object... parts) {
        return Join(Component.newline(), parts);
    }

    public static Component SpaceJoin(Object... parts) {
        return Join(Component.space(), parts);
    }

    public static Component Join(Component joiner, Object... parts) {
        List<Component> componentList = new ArrayList<>();
        for (Object element : parts) {
            if (element instanceof String string) componentList.add(Component.text(string));
            else if (element instanceof Component component) componentList.add(component);
        }
        return Component.join(JoinConfiguration.separator(joiner), componentList);
    }

    public static TextComponent Command(String text, String command) {
        if (!command.startsWith("/")) command = "/" + command;
        return Command(text, command, COMMAND_COLOR);
    }

    public static TextComponent Command(String text, String command, TextColor color) {
        if (!command.startsWith("/")) command = "/" + command;
        return Component.text(text).color(color).decoration(TextDecoration.UNDERLINED, true).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)).hoverEvent(HoverEvent.showText(Component.text(command)));
    }

    public static TextComponent PlayerText(Server server, UUID uuid) {
        OfflinePlayer offlinePlayer = server.getOfflinePlayer(uuid);
        if (offlinePlayer.isOnline())
            return Component.text(offlinePlayer.getName()).color(GREEN);
        else
            return Component.text(offlinePlayer.getName());
    }
}
