package com.nijikokun.bukkit.AB;

import com.nijikokun.bukkit.AB.AntiBuild;
import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.event.player.PlayerListener;

public class PListener extends PlayerListener {
	public static AntiBuild plugin;

	public PListener(AntiBuild instance) {
		plugin = instance;
	}

	public void onPlayerItem(PlayerItemEvent event) {
		if (!plugin.canBuild(event.getPlayer()))
			event.setCancelled(true);
	}

}
