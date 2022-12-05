package net.fabricmc.example.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public abstract class Module {

    private boolean isEnabled;
    protected ClientPlayerEntity player;
    public Module(boolean startEnabled){
        this.isEnabled = startEnabled;
    }

    public Module(){
        this.isEnabled = false;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
        if(enabled) onEnabled();
        else onDisabled();
    }

    public void onEnabled(){
        this.player = MinecraftClient.getInstance().player;
    };
    public void onDisabled(){};

    public abstract void onPlayerTick(MinecraftClient client);
}
