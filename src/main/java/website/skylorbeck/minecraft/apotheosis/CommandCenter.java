package website.skylorbeck.minecraft.apotheosis;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.github.apace100.origins.command.LayerArgumentType;
import io.github.apace100.origins.command.OriginArgumentType;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

public class CommandCenter {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("apotheosis").requires(cs -> cs.hasPermissionLevel(2))
                        .then(literal("set")
                                .then(argument("target", EntityArgumentType.player())
                                        .then(argument("level", IntegerArgumentType.integer())
                                                .executes((command) -> {
                                                    // Sets the origins of several people in the given layer.
                                                    int i = IntegerArgumentType.getInteger(command, "level");
                                                    ServerPlayerEntity target = EntityArgumentType.getPlayer(command, "target");
                                                    APOXP.get(target).setLevel(i);
                                                    APOXP.sync(target);
                                                    command.getSource().sendFeedback(Text.of("Set "+ target.getDisplayName().getString()+(" Lv:" + i)), true);
                                                    return i;
                                                }))))
        );
    }
}