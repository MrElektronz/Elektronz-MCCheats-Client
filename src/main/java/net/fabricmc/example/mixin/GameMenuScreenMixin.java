package net.fabricmc.example.mixin;

import net.fabricmc.example.screens.ElektronzScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"), method = "initWidgets")
    public void initWidgets(CallbackInfo ci){
        this.addDrawableChild(new ButtonWidget(10,10,90,20, Text.of("EClient"), (btn) -> {
            this.client.setScreen(new ElektronzScreen(this, this.client.options));
        }));
    }

}
