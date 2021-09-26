package website.skylorbeck.minecraft.exaltation;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.github.apace100.origins.command.OriginArgumentType;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static website.skylorbeck.minecraft.exaltation.cardinal.ExaltationComponents.APOXP;

public class CommandCenter {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("exaltation").requires(cs -> cs.hasPermissionLevel(2))
                        .then(literal("setlevel")
                                .then(argument("target", EntityArgumentType.player())
                                        .then(argument("level", IntegerArgumentType.integer())
                                                .executes((command) -> {
                                                    int i = IntegerArgumentType.getInteger(command, "level");
                                                    ServerPlayerEntity target = EntityArgumentType.getPlayer(command, "target");
                                                    APOXP.get(target).setLevel(i);
                                                    APOXP.sync(target);
                                                    command.getSource().sendFeedback(Text.of("Set "+ target.getDisplayName().getString()+(" Lv:" + i)), true);
                                                    return i;
                                                }))))
        );
        dispatcher.register(
                literal("exaltation").requires(cs -> cs.hasPermissionLevel(2))
                        .then(literal("setclass")
                                .then(argument("target", EntityArgumentType.player())
                                        .then(argument("class", OriginArgumentType.origin())
                                                .executes((command) -> {
                                                    ServerPlayerEntity target = EntityArgumentType.getPlayer(command, "target");
                                                    Origin origin = OriginArgumentType.getOrigin(command,"class");
                                                            setOrigin(target,origin);
                                                    command.getSource().sendFeedback(new TranslatableText("Set "+ target.getDisplayName().getString()+" to ").append(origin.getName()), true);
                                                    return 0;
                                                }))))
        );
    }
    private static void setOrigin(PlayerEntity player,Origin origin) {
        OriginComponent component = ModComponents.ORIGIN.get(player);
        component.setOrigin(OriginLayers.getLayer(Declarar.getIdentifier("class")), origin);
        OriginComponent.sync(player);
        boolean hadOriginBefore = component.hadOriginBefore();
        OriginComponent.partialOnChosen(player, hadOriginBefore, origin);
    }
}