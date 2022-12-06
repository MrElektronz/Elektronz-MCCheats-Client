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
public class BotMovementModule extends Module{





    public BotMovementModule(boolean startEnabled){
        super(startEnabled);
    }

    @Override
    public void onEnabled() {
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
    }

    @Override
    public void onPlayerTick(MinecraftClient client) {

    }

}
