package net.fabricmc.example.modules;

import net.fabricmc.example.ElektronzMod;
import net.fabricmc.example.RayCast;
import net.fabricmc.example.listeners.LeftClickListener;
import net.fabricmc.example.packets.PacketHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public class ReachModule extends Module implements LeftClickListener {


    private int reach = 4;
    private int teleportBackIn = 0;
    private Vec3d oldPos;

    public ReachModule(boolean startEnabled){
        super(startEnabled);
    }

    @Override
    public void onEnabled() {
        super.onEnabled();
        ElektronzMod.EVENTS.add(LeftClickListener.class, this);
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
        ElektronzMod.EVENTS.remove(LeftClickListener.class, this);

    }

    @Override
    public void onPlayerTick(MinecraftClient client) {
        if(teleportBackIn>0){
            teleportBackIn--;
            PacketHelper.sendPosition(oldPos);
        }
    }

    public int getReach() {
        return reach;
    }
    public void teleportBackLater(Vec3d oldPos, int afterTicks){
        teleportBackIn=afterTicks;
        this.oldPos=oldPos;
    }

    public void onLeftClick2(LeftClickEvent event) {

        if(isEnabled()){
            HitResult hit = RayCast.raycastInDirection(MinecraftClient.getInstance(),0, MinecraftClient.getInstance().cameraEntity.getRotationVector());
            if(hit != null && hit instanceof EntityHitResult){
                EntityHitResult ehit = (EntityHitResult) hit;
                Vec3d oldPos = player.getPos();
                ArrayList<Integer> ports = getGodReachPorts(oldPos, ehit.getEntity().getPos());
                ElektronzMod.LOGGER.info("Player  "+oldPos+" tries hitting entity at "+ehit.getEntity().getPos());
                for(int i : ports){
                    oldPos = oldPos.add(MinecraftClient.getInstance().cameraEntity.getRotationVector().normalize().multiply(i));
                    PacketHelper.sendPosition(oldPos);
                    ElektronzMod.LOGGER.info("Port to: "+oldPos);
                }
                PlayerInteractEntityC2SPacket attackPacket = PlayerInteractEntityC2SPacket.attack(ehit.getEntity(),false);
                PacketHelper.sendPacketImmediately(attackPacket);
                for(int i : ports){
                    oldPos = oldPos.subtract(MinecraftClient.getInstance().cameraEntity.getRotationVector().normalize().multiply(i));
                    PacketHelper.sendPosition(oldPos);
                    ElektronzMod.LOGGER.info("Port back: "+oldPos);
                }
            }
        }
    }

    @Override
    public void onLeftClick(LeftClickEvent event) {

        if(isEnabled()) {
            HitResult hit = RayCast.raycastInDirection(MinecraftClient.getInstance(), 0, MinecraftClient.getInstance().cameraEntity.getRotationVector());
            if (hit != null && hit instanceof EntityHitResult) {
                EntityHitResult ehit = (EntityHitResult) hit;
                Vec3d playerPos = player.getPos();
                Vec3d entityPos = ehit.getPos();

                teleportFromTo(MinecraftClient.getInstance(), playerPos,entityPos);

                PlayerInteractEntityC2SPacket attackPacket = PlayerInteractEntityC2SPacket.attack(ehit.getEntity(),false);
                PacketHelper.sendPacketImmediately(attackPacket);

                teleportFromTo(MinecraftClient.getInstance(), entityPos, playerPos);
                MinecraftClient.getInstance().player.setPosition(playerPos);
            }
        }
    }


    public ArrayList<Integer> getGodReachPorts(Vec3d myPos, Vec3d enemyPos){
        boolean reached = false;
        ArrayList<Integer> ports = new ArrayList<>();
        while(!reached){
            double distance = myPos.distanceTo(enemyPos);
            if (distance<3) {
                reached = true;
                break;
            }
            myPos = myPos.add(MinecraftClient.getInstance().cameraEntity.getRotationVector().normalize().multiply(6));
            ports.add(6);
        }
        return ports;

    }


    /**
     *
     * @param client
     * @param from
     * @param to
     * Code taken from @LiveOverflow, need to evaluate why this code works and the method above does not.
     * Still need to fix bug when normal movement package gets send to the server, this disrupts the correct teleportation
     * Possible solutions: - Disable movement package sending to the server when blinking
     *                     - Store all teleported positions and teleport back to them (Will this completely fix the issue?)
     */
    void teleportFromTo(MinecraftClient client, Vec3d from, Vec3d to){
        double distancePerBlink = 8.5;
        Double targetDistance = Math.ceil(from.distanceTo(to) / distancePerBlink);
        for(int i = 1;i<=targetDistance;i++){
            Vec3d tempPos = from.lerp(to,i/targetDistance);
            ElektronzMod.LOGGER.info("Lerp Distance: "+(int)((i/targetDistance)*100)+"% -> ");
            PacketHelper.sendPosition(tempPos);
            if (i % 4 == 0) {
                try{
                    Thread.sleep((long)((1/20)*1000));
                }catch (Exception ex){

                }
            }
        }
    }
}
