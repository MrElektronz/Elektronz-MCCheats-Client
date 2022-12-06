package net.fabricmc.example.packets;

import net.fabricmc.example.mixin.invoker.ClientConnectionInvoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public class PacketHelper {

    public static void sendPosition(Vec3d pos){
        MinecraftClient client = MinecraftClient.getInstance();
        ClientConnection con = client.player.networkHandler.getConnection();
        pos = PacketHelper.fixCoords(pos);
        Packet packet = new PlayerMoveC2SPacket.PositionAndOnGround(pos.getX(), pos.getY(), pos.getZ(), false);
        ((ClientConnectionInvoker)con).callSendImmediately(packet, null);
    }

    public static void sendPositionSlowly(Vec3d pos, boolean ground){
        MinecraftClient client = MinecraftClient.getInstance();
        ClientConnection con = client.player.networkHandler.getConnection();
        pos = PacketHelper.fixCoords(pos);
        Packet packet = new PlayerMoveC2SPacket.PositionAndOnGround(pos.getX(), pos.getY(), pos.getZ(), false);
        ((ClientConnectionInvoker)con).callSendImmediately(packet, null);
        con.send(packet, null);
    }

    public static void sendPacketImmediately(Packet<?> packet){
        MinecraftClient client = MinecraftClient.getInstance();
        ClientConnection con = client.player.networkHandler.getConnection();
        ((ClientConnectionInvoker)con).callSendImmediately(packet, null);
    }



    private static Vec3d fixCoords(Vec3d pos) {
        return pos;
    }


}
