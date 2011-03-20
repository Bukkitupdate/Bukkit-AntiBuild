package net.sradonia.bukkit.antibuild;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BListener extends BlockListener {
	private final AntiBuild plugin;
	private final String message;

	public BListener(AntiBuild instance, String message) {
		this.plugin = instance;
		this.message = message;
	}

	public void onBlockDamage(BlockDamageEvent event) {
		Player player = event.getPlayer();
		if (!plugin.canBuild(player)) {
			event.setCancelled(true);
			if (message != null)
				player.sendMessage(message);
		}
	}

	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (!plugin.canBuild(player)) {
			event.setCancelled(true);
			if (message != null)
				player.sendMessage(message);
		}
	}

}
