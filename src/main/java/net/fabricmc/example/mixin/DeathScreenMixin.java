package net.fabricmc.example.mixin;

import net.fabricmc.example.listeners.DeathListener;
import net.fabricmc.example.listeners.EventManager;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.security.auth.callback.Callback;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
@Mixin(DeathScreen.class)
public class DeathScreenMixin extends Screen {

    protected DeathScreenMixin(Text title) {
        super(title);
    }
    @Inject(at = @At("TAIL"), method = "tick()V")
    private void onTick(CallbackInfo ci){
        EventManager.fire(DeathListener.DeathEvent.INSTANCE);
    }
}
