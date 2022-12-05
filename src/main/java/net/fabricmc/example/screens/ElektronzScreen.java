package net.fabricmc.example.screens;

import net.fabricmc.example.ElektronzMod;
import net.fabricmc.example.modules.Module;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public class ElektronzScreen extends Screen {

    private final Screen parent;
    private final GameOptions gameOptions;

    public ElektronzScreen(Screen parent, GameOptions gameOptions) {
        super(Text.of("ElektronzClient"));
        this.parent = parent;
        this.gameOptions = gameOptions;
    }

    protected Text moduleText(Module module){
        if (module.isEnabled()) return Text.of(module.getClass().getSimpleName().replace("Module","") + " enabled");
        return Text.of(module.getClass().getSimpleName().replace("Module","") + " disabled");
    }

    protected Text enabledText(){
        if(ElektronzMod.getInstance().isEnabled()) return Text.of("Enabled");
        return Text.of("Disabled");
    }

    protected void init(){
        // Back button
        this.addDrawableChild(new ButtonWidget(20, 20, 50, 20, Text.of("Back"), (btn)->{
            this.client.setScreen(this.parent);
        }));

        // Enabled button
        this.addDrawableChild(new ButtonWidget(20, 44, 50, 20, enabledText(), (btn)->{
            ElektronzMod.getInstance().setEnabled(!ElektronzMod.getInstance().isEnabled());
           btn.setMessage(enabledText());
        }));

        // Module buttons
        int moduleId = 1;
        for (Module module : ElektronzMod.getInstance().getModules().values()) {
            this.addDrawableChild(new ButtonWidget(this.width/2-100, this.height/14+((moduleId*20)+4), 200, 20, moduleText(module), (btn)->{
                if(ElektronzMod.getInstance().isEnabled()){
                    module.setEnabled(!module.isEnabled());
                    btn.setMessage(moduleText(module));
                }
            }));
            moduleId++;
        }
    }
}
