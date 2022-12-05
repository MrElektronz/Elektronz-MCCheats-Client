package net.fabricmc.example.mixin;

import net.fabricmc.example.ElektronzMod;
import net.fabricmc.example.ModuleName;
import net.fabricmc.example.listeners.EventManager;
import net.fabricmc.example.listeners.LeftClickListener;
import net.fabricmc.example.modules.ReachModule;
import net.fabricmc.example.packets.PacketHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin extends ReentrantThreadExecutor<Runnable> implements WindowEventHandler {

    private MinecraftClientMixin(String strg1){
        super(strg1);
    }

    @Inject(at = {@At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;crosshairTarget:Lnet/minecraft/util/hit/HitResult;", ordinal = 0)}, method = {"doAttack()Z"}, cancellable = true)
    private void onDoAttack(CallbackInfoReturnable<Boolean> cir)
    {
        LeftClickListener.LeftClickEvent event = new LeftClickListener.LeftClickEvent();
        EventManager.fire(event);
        ElektronzMod.LOGGER.info("left click 1");

        if(event.isCancelled())
            cir.setReturnValue(false);
    }


}