package net.fabricmc.example.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public class AutoFishModule extends Module{

    public AutoFishModule(boolean startEnabled){
        super(startEnabled);
    }

    private int recastRod = -1;

    @Override
    public void onEnabled() {
        recastRod = -1;
    }

    @Override
    public void onPlayerTick(MinecraftClient client){
        if(recastRod>0) recastRod--;
        if(recastRod==0){
            client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
            recastRod=-1;
        }
    }

    public void setRecastRod(int countdown){
        recastRod = countdown;
    }

}
