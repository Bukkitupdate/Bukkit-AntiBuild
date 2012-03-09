package net.sradonia.bukkit.antibuild;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PListener implements Listener {
	private final AntiBuild plugin;
	private final MessageSender message;

	public PListener(AntiBuild instance, MessageSender message) {
		this.plugin = instance;
		this.message = message;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (!plugin.canInteract(player)) {
			event.setCancelled(true);
			if (message != null)
				message.sendMessage(player);
		}
	}
}
