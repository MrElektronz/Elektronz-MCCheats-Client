package net.fabricmc.example.mixin;

import net.fabricmc.example.ElektronzMod;
import net.fabricmc.example.ModuleName;
import net.fabricmc.example.modules.FlightModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
@Mixin(MinecraftClient.class)
public abstract class PlayerTickMixin {

    @Inject(at = @At("TAIL"), method = "tick")
    public void tick(CallbackInfo info){
        ElektronzMod.getInstance().getModules().values().forEach((module) -> {
            if(module.isEnabled()) module.onPlayerTick(MinecraftClient.getInstance());
        });

    }



}
