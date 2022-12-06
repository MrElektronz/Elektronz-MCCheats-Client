package net.fabricmc.example.mixin;

import io.netty.channel.Channel;
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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.Future;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Shadow private Channel channel;

    /*
    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V", cancellable = true)
    public void sendHead(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
        if(ElektronzMod.getInstance().isEnabled()) {
            //Bot Movement
            //TODO: Working not 100% of the time, probably need to add the other PlayerMoveC2SPackets :)
            if (packet instanceof PlayerMoveC2SPacket && MinecraftClient.getInstance().player != null) {
                if (ElektronzMod.getInstance().getModule(ModuleName.BOT_MOVEMENT).isEnabled()) {
                    ClientPlayerEntity player = MinecraftClient.getInstance().player;
                    if(packet instanceof PlayerMoveC2SPacket.PositionAndOnGround) {
                        double x = ((PlayerMoveC2SPacket.PositionAndOnGround) packet).getX(player.getX());
                        double z = ((PlayerMoveC2SPacket.PositionAndOnGround) packet).getZ(player.getZ());
                        double newX = Math.floor(x * 100) / 100;
                        double newZ = Math.floor(z * 100) / 100;
                        if (x != newX || z != newZ) {
                            //ElektronzMod.LOGGER.info("NewX: " + newX + " NewZ: " + newZ);
                            PacketHelper.sendPacketImmediately(new PlayerMoveC2SPacket.PositionAndOnGround(newX, ((PlayerMoveC2SPacket.PositionAndOnGround) packet).getY(player.getY()), newZ, ((PlayerMoveC2SPacket.PositionAndOnGround) packet).isOnGround()));
                        }
                    }else if(packet instanceof PlayerMoveC2SPacket.LookAndOnGround){
                        double x = ((PlayerMoveC2SPacket.LookAndOnGround) packet).getX(player.getX());
                        double z = ((PlayerMoveC2SPacket.LookAndOnGround) packet).getZ(player.getZ());
                        double newX = Math.floor(x * 100) / 100;
                        double newZ = Math.floor(z * 100) / 100;
                        if (x != newX || z != newZ) {
                            //ElektronzMod.LOGGER.info("NewX: " + newX + " NewZ: " + newZ);
                        }
                    }

                    ci.cancel();
                }
            }
        }
    }

    public boolean isOpen() {
        return channel != null && channel.isOpen();
    }
     */


    @Inject(at = @At("TAIL"), method = "send(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V", cancellable = true)
    public void send(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
        //ElektronzMod.LOGGER.info(packet.getClass().getName());

        if(ElektronzMod.getInstance().isEnabled()) {
            //Bot Movement
            if (packet instanceof PlayerMoveC2SPacket.PositionAndOnGround && MinecraftClient.getInstance().player != null) {
                if (ElektronzMod.getInstance().getModule(ModuleName.BOT_MOVEMENT).isEnabled()) {
                    ClientPlayerEntity player = MinecraftClient.getInstance().player;
                    long x = ((long) (((PlayerMoveC2SPacket) packet).getX(player.getX()) * 1000));
                    double dx = (x - (x % 10)) / 1000.0d;

                    long z = ((long) (((PlayerMoveC2SPacket) packet).getZ(player.getZ()) * 1000));
                    double dz = (z - (z % 10)) / 1000.0d;
                    //ElektronzMod.LOGGER.info("Tail Changed X from " + (((PlayerMoveC2SPacket) packet).getX(player.getX())) + " to " + dx);
                    //packet = new PlayerMoveC2SPacket.PositionAndOnGround(dx, player.getY(), dz, ((PlayerMoveC2SPacket.PositionAndOnGround) packet).isOnGround());
                    packet = new PlayerMoveC2SPacket.PositionAndOnGround(0, player.getY(), 0, ((PlayerMoveC2SPacket.PositionAndOnGround) packet).isOnGround());
                }
            }
            //Flying
            if (packet instanceof PlayerMoveC2SPacket) {
                //     FLYCHECK                                                                                                                         |   NOFALL
                if ((ElektronzMod.getInstance().getModule(ModuleName.FLIGHT).isEnabled() && MinecraftClient.getInstance().player.getAbilities().flying) || MinecraftClient.getInstance().player.fallDistance > 1) {
                    if (Math.abs(MinecraftClient.getInstance().player.getVelocity().y) > 0.07 && !MinecraftClient.getInstance().player.world.getBlockState(new BlockPos(MinecraftClient.getInstance().player.getPos().subtract(0, 2, 0))).isAir()) {
                        ClientPlayerEntity player = MinecraftClient.getInstance().player;
                        PacketHelper.sendPosition(MinecraftClient.getInstance().player.getPos().add(player.getVelocity().x, 0.05D, player.getVelocity().z));
                    }
                }
            }else{
                ElektronzMod.LOGGER.info(packet.getClass().getName());
            }
        }
        }


    public void oldCode(Packet<?> packet){
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        long x = ((long) (((PlayerMoveC2SPacket) packet).getX(player.getX()) * 1000));
        double dx = (x - (x % 10)) / 1000.0d;

        long z = ((long) (((PlayerMoveC2SPacket) packet).getZ(player.getZ()) * 1000));
        double dz = (z - (z % 10)) / 1000.0d;
        if(x%10!=0 || z%10!=0){
            ElektronzMod.LOGGER.info("IN IF with x = "+x+" and z = "+z+" ");
            //ElektronzMod.LOGGER.info("Head Changed X from " + (((PlayerMoveC2SPacket) packet).getX(player.getX())) + " to " + dx);
            //packet = new PlayerMoveC2SPacket.PositionAndOnGround(dx, player.getY(), dz, ((PlayerMoveC2SPacket.PositionAndOnGround) packet).isOnGround());
            packet = new PlayerMoveC2SPacket.PositionAndOnGround(1, player.getY(), 1, ((PlayerMoveC2SPacket.PositionAndOnGround) packet).isOnGround());
            //player.setPosition(1, player.getY(), 1);
            PacketHelper.sendPacketImmediately(packet);
            //ci.cancel();
        }else{
            ElektronzMod.LOGGER.info("OUT IF: Z "+z/1000+" and dz "+dz+" and X "+x/1000+" and dx "+dx);
            //ElektronzMod.LOGGER.info("No need to edit: " + (((PlayerMoveC2SPacket) packet).getX(player.getX())) + " and "+ (((PlayerMoveC2SPacket) packet).getZ(player.getZ())));

        }
    }
    }

