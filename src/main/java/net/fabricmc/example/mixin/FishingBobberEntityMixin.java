package net.fabricmc.example.mixin;

import net.fabricmc.example.ElektronzMod;
import net.fabricmc.example.ModuleName;
import net.fabricmc.example.modules.AutoFishModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {

    @Shadow private boolean caughtFish;

    @Inject(at = @At("TAIL"), method = "onTrackedDataSet")
    public void onTrackedDataSet(TrackedData<?> data, CallbackInfo info){

        MinecraftClient client = MinecraftClient.getInstance();

        if(caughtFish && ElektronzMod.getInstance().getModule(ModuleName.AUTO_FISH).isEnabled()){
            ((AutoFishModule)ElektronzMod.getInstance().getModule(ModuleName.AUTO_FISH)).setRecastRod(20);
            client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
        }
    }

}
