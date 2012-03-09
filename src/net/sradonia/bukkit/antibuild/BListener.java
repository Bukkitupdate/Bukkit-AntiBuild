package net.sradonia.bukkit.antibuild;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BListener implements Listener {
	private final AntiBuild plugin;
	private final MessageSender message;

	public BListener(AntiBuild instance, MessageSender message) {
		this.plugin = instance;
		this.message = message;
	}

	@EventHandler
	public void onBlockDamage(BlockDamageEvent event) {
		Player player = event.getPlayer();
		if (!plugin.canBuild(player)) {
			event.setCancelled(true);
			if (message != null)
				message.sendMessage(player);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (!plugin.canBuild(player)) {
			event.setCancelled(true);
			if (message != null)
				message.sendMessage(player);
		}
	}

}
