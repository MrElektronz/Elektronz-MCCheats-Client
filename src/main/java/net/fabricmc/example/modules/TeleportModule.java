package net.fabricmc.example.modules;

import net.fabricmc.example.ElektronzMod;
import net.fabricmc.example.packets.PacketHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public class TeleportModule extends Module{





    public TeleportModule(boolean startEnabled){
        super(startEnabled);
    }

    @Override
    public void onEnabled() {
        super.onEnabled();
        ClientPlayerEntity p = MinecraftClient.getInstance().player;

        Vec3d newPos = p.getPos().add(MinecraftClient.getInstance().cameraEntity.getRotationVector().normalize().multiply(3));
        PacketHelper.sendPositionSlowly(newPos,false);
        p.setPosition(newPos);
        ElektronzMod.LOGGER.info("TELEPORTED");
        setEnabled(false);
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
    }

    @Override
    public void onPlayerTick(MinecraftClient client) {

    }

}
