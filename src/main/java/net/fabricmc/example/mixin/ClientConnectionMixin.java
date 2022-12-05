package net.fabricmc.example.mixin;

import net.fabricmc.example.ElektronzMod;
import net.fabricmc.example.ModuleName;
import net.fabricmc.example.modules.FlightModule;
import net.fabricmc.example.modules.ReachModule;
import net.fabricmc.example.packets.PacketHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.Future;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(at = @At("TAIL"), method = "send(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V", cancellable = true)
    public void send(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
        //ElektronzMod.LOGGER.info(packet.getClass().getName());
        if(ElektronzMod.getInstance().isEnabled()) {
            //Fyling
            if (packet instanceof PlayerMoveC2SPacket) {
                //     FLYCHECK                                                                                                                         |   NOFALL
                if ((ElektronzMod.getInstance().getModule(ModuleName.FLIGHT).isEnabled() && MinecraftClient.getInstance().player.getAbilities().flying) || MinecraftClient.getInstance().player.fallDistance > 1) {
                    if (Math.abs(MinecraftClient.getInstance().player.getVelocity().y) > 0.07 && !MinecraftClient.getInstance().player.world.getBlockState(new BlockPos(MinecraftClient.getInstance().player.getPos().subtract(0, 2, 0))).isAir()) {
                        ClientPlayerEntity player = MinecraftClient.getInstance().player;
                        PacketHelper.sendPosition(MinecraftClient.getInstance().player.getPos().add(player.getVelocity().x, 0.05D, player.getVelocity().z));
                    }
                }
                //needs to be executed when received from a server, because the client NEVER sends this.
            }

            //else{
              //  ElektronzMod.LOGGER.info(packet.getClass().getName());
            //}
        }
    }


}