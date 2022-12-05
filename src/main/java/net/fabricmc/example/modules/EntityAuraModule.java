package net.fabricmc.example.modules;

import net.fabricmc.example.ElektronzMod;
import net.fabricmc.example.RayCast;
import net.fabricmc.example.listeners.LeftClickListener;
import net.fabricmc.example.packets.PacketHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public class EntityAuraModule extends Module implements LeftClickListener {




    private int tickCounter = 0;
    public EntityAuraModule(boolean startEnabled){
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
        tickCounter++;
        if(tickCounter>18 && MinecraftClient.getInstance().player != null){
            tickCounter=0;
            //HitResult hit = RayCast.raycastInDirection(MinecraftClient.getInstance(),0, MinecraftClient.getInstance().cameraEntity.getRotationVector());//MinecraftClient.getInstance().getCameraEntity().raycast(100,0,false);

            //Use this for auto aura
            HitResult hit = RayCast.raycastInDirection(MinecraftClient.getInstance(),0, MinecraftClient.getInstance().cameraEntity.getRotationVector());
            if(hit != null && hit.getType().equals(HitResult.Type.ENTITY) && ((EntityHitResult)hit).getEntity().isLiving()){
                PlayerInteractEntityC2SPacket attackPacket = PlayerInteractEntityC2SPacket.attack(((EntityHitResult)hit).getEntity(),false);
                PacketHelper.sendPacketImmediately(attackPacket);
            }

        }
    }


    public EntityHitResult getNearestLivingEntity(ClientPlayerEntity player) {
        Box area = player.getBoundingBox().expand(40);
        LivingEntity entity = null;
        double nearest = Double.MAX_VALUE;
        for(Entity ent : player.getWorld().getOtherEntities(player, area)){
            double distance = ent.getPos().distanceTo(player.getPos());
            if(ent instanceof LivingEntity && ent.getPos().distanceTo(player.getPos())<nearest){
                nearest = distance;
                entity = (LivingEntity) ent;
            }
        }
        return entity==null ? null : new EntityHitResult(entity);
    }


    public EntityHitResult getPlayerPOVHitResult(ClientPlayerEntity player) {
        float playerRotX = player.getRotationClient().x;
        float playerRotY = player.getRotationClient().y;
        Vec3d startPos = player.getEyePos();
        float f2 = (float) Math.cos(-playerRotY * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = (float) Math.sin(-playerRotY * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = (float) -Math.cos(-playerRotX * ((float)Math.PI / 180F));
        float additionY = (float) Math.sin(-playerRotX * ((float)Math.PI / 180F));
        float additionX = f3 * f4;
        float additionZ = f2 * f4;
        double d0 = 200;
        Vec3d endVec = startPos.add((double)additionX * d0, (double)additionY * d0, (double)additionZ * d0);
        Box startEndBox = new Box(startPos, endVec);
        Entity entity = null;
        double nearest = Double.MAX_VALUE;
        for(Entity entity1 : player.getWorld().getOtherEntities(player,startEndBox, (val) -> true)) {
            Box aabb = entity1.getBoundingBox();//.expand(1);
            boolean intersects = aabb.intersects(startPos, endVec);
            if(intersects){
                entity = entity1;
                double currentDist = startPos.squaredDistanceTo(entity1.getPos());
                if(currentDist<nearest){
                    entity = entity1;
                    nearest = currentDist;
                }
            }

        }

        return (entity == null) ? null:new EntityHitResult(entity);
    }


    @Override
    public void onLeftClick(LeftClickEvent event) {

        if(isEnabled()){
            PacketHelper.sendPosition(player.getPos().add(2,0,0));
            //Attack enemy
            //Send back

            //event.cancel();
        }
    }
}
