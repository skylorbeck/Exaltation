package website.skylorbeck.minecraft.exaltation;

import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.origin.OriginRegistry;
import io.github.apace100.origins.registry.ModComponents;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.impl.networking.ServerSidePacketRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.exaltation.blocks.screens.AltarHandledScreen;

import java.lang.reflect.Method;
import java.util.Objects;

import static website.skylorbeck.minecraft.exaltation.Declarar.MODID;
import static website.skylorbeck.minecraft.skylorlib.Registrar.*;

public class Registrar {
    public static void Register(){
        regBlock("stonealtar",Declarar.stonealtar,MODID);
        regItem("stonealtar",Declarar.stonealtarItem,MODID);
        regBlock("ironaltar",Declarar.ironaltar,MODID);
        regItem("ironaltar",Declarar.ironaltarItem,MODID);
        regBlock("goldaltar",Declarar.goldaltar,MODID);
        regItem("goldaltar",Declarar.goldaltarItem,MODID);
        regBlock("diamondaltar",Declarar.diamondaltar,MODID);
        regItem("diamondaltar",Declarar.diamondaltarItem,MODID);
        regBlock("netheritealtar",Declarar.netheritealtar,MODID);
        regItem("netheritealtar",Declarar.netheritealtarItem,MODID);
        regItem("bonefragment",Declarar.boneFragment,MODID);
        Registry.register(Registry.STATUS_EFFECT, Declarar.getIdentifier("wolfmark"),Declarar.WOLFMARK);
        Registry.register(Registry.STATUS_EFFECT, Declarar.getIdentifier("hemorrhaging"),Declarar.HEMORRHAGING);

        regServerSidePacket("getadvancementpacket",MODID,((context, buffer) ->{//buffer is the data
            Identifier identifier = buffer.readIdentifier();
            int slot = buffer.readInt();
            context.getTaskQueue().execute(() ->{
                    OriginLayer originLayer = OriginLayers.getLayer(Declarar.getIdentifier("class"));
                    Origin origin = ModComponents.ORIGIN.get(context.getPlayer()).getOrigin(originLayer);
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeIdentifier(origin.getUpgrade(context.getPlayer().getServer().getAdvancementLoader().get(identifier)).get().getUpgradeToOrigin());
                    buf.writeInt(slot);
                    ServerSidePacketRegistryImpl.INSTANCE.sendToPlayer(context.getPlayer(),Declarar.sendAdvancementPacket,buf);
                });
        }));

    }
    public static void ClientRegister(){
        ScreenRegistry.register(Declarar.ALTARSCREENHANDLER, AltarHandledScreen::new);
    }
}
