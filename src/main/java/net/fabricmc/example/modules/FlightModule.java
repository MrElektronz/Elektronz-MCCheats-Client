package net.fabricmc.example.modules;

import net.fabricmc.example.ElektronzMod;
import net.fabricmc.example.listeners.DeathListener;
import net.fabricmc.example.packets.PacketHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public class FlightModule extends Module implements DeathListener {

    public FlightModule(boolean startEnabled){
        super(startEnabled);
    }

    public int floatingTickCount = 0;
    public boolean updateFlyingAfterRespawn = false;

    @Override
    public void onEnabled(){
        super.onEnabled();
        ElektronzMod.EVENTS.add(DeathListener.class, this);
        MinecraftClient.getInstance().player.getAbilities().allowFlying = true;
        MinecraftClient.getInstance().player.getAbilities().setFlySpeed(0.1f);
        floatingTickCount = 0;
        ElektronzMod.LOGGER.info("fly enabled");
    }

    @Override
    public void onDisabled(){
        ElektronzMod.LOGGER.info("fly disabled");
        ElektronzMod.EVENTS.remove(DeathListener.class, this);
        if(MinecraftClient.getInstance().player.getAbilities().flying){
            MinecraftClient.getInstance().player.getAbilities().flying = false;
            ElektronzMod.LOGGER.info("flying false");
        }
        MinecraftClient.getInstance().player.getAbilities().allowFlying = false;
        MinecraftClient.getInstance().player.getAbilities().setFlySpeed(0.1f);


    }

    @Override
    public void onPlayerTick(MinecraftClient client){
        floatingTickCount++;
        if(floatingTickCount > 20 && updateFlyingAfterRespawn){
            updateFlyingAfterRespawn = false;
            setEnabled(false);
            setEnabled(true);
        }
        if(client.player != null && floatingTickCount>25 && client.player.world.getBlockState(new BlockPos(client.player.getPos().subtract(0,0.0433D,0))).isAir()){
            floatingTickCount = 0;
            MinecraftClient.getInstance().player.getAbilities().setFlySpeed((MinecraftClient.getInstance().player.getInventory().selectedSlot+1)*0.022f);
            Vec3d newPos = client.player.getPos().subtract(0,0.0433D,0);
            PacketHelper.sendPosition(newPos);
            //client.player.sendMessage(Text.of("Sent new location packet of "+newPos.x+" "+newPos.y+" "+newPos.z));
        }
    }



    public void setUpdateFlyingAfterRespawn(){
        if(isEnabled()){
           updateFlyingAfterRespawn = true;
           floatingTickCount = 0;
        }
    }


    @Override
    public void onDeath() {
        setUpdateFlyingAfterRespawn();
    }
}
