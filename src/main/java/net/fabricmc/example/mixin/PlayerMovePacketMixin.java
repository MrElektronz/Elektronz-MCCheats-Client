package net.fabricmc.example.mixin;

import net.fabricmc.example.ElektronzMod;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
@Mixin(PlayerMoveC2SPacket.class)
public class PlayerMovePacketMixin {


    //@Shadow protected double x;
    //@Shadow protected double z;

    /*
    @Redirect(at = @At(value = "INVOKE", target=), method = "<init>")
    private static void constructorTail(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean changePosition, boolean changeLook, CallbackInfo ci){
        this.x =  Math.floor(x * 100) / 100;
        this.z =  Math.floor(z * 100) / 100;
        ElektronzMod.LOGGER.info("X is "+this.x);
        ci.cancel();
    }*/

    //@Redirect(method="getX", at = @At())

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static double injectedX(double x) {
        if(ElektronzMod.BOT_MOVEMENT.isEnabled()) return Math.floor(x * 100) / 100;
        return x;
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 2)
    private static double injectedZ(double x) {
        if(ElektronzMod.BOT_MOVEMENT.isEnabled()) return Math.floor(x * 100) / 100;
        return x;
    }

}
