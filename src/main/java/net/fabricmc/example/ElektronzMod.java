package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.example.listeners.EventManager;
import net.fabricmc.example.modules.*;
import net.fabricmc.example.modules.Module;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class ElektronzMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	private static ElektronzMod instance;
	private HashMap<ModuleName, Module> modules;
	public static final EventManager EVENTS = new EventManager();
	private boolean enabled = true;

	// includes all previously enabled modules if client is DISABLED
	private ArrayList<ModuleName> storedEnabledModules = new ArrayList<>();


	@Override
	public void onInitialize() {
		instance = this;
		modules = new HashMap<>();
		modules.put(ModuleName.AUTO_FISH, new AutoFishModule(true));
		modules.put(ModuleName.FLIGHT, new FlightModule(false));
		modules.put(ModuleName.REACH, new ReachModule(true));
		modules.put(ModuleName.ENTITY_AURA, new EntityAuraModule(false));


		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if(enabled){
			for(ModuleName mn : modules.keySet()){
				if(!modules.get(mn).isEnabled() && storedEnabledModules.contains(mn)){
					modules.get(mn).setEnabled(true);
				}
			}
		}else{
			storedEnabledModules.clear();
			for(ModuleName mn : modules.keySet()){
				if(modules.get(mn).isEnabled()){
					storedEnabledModules.add(mn);
					modules.get(mn).setEnabled(false);
				}
			}
		}
	}

	public static ElektronzMod getInstance() {
		return instance;
	}

	public HashMap<ModuleName, Module> getModules() {
		return modules;
	}

	public Module getModule(ModuleName module){
		return modules.get(module);
	}
}
