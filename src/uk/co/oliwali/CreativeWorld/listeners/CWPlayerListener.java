package uk.co.oliwali.CreativeWorld.listeners;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

import uk.co.oliwali.CreativeWorld.Config;
import uk.co.oliwali.CreativeWorld.Permission;
import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.BlockEntry;

public class CWPlayerListener extends PlayerListener {
	
	public void onPlayerInteract(PlayerInteractEvent event) {
	
		if (event.isCancelled()) return;
		if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getPlayer().getItemInHand().getTypeId() == Config.InstaTool && Permission.instaBreak(event.getPlayer()) && Permission.inGroup(event.getPlayer(), Config.CreativeGroup)) {
			event.setCancelled(true);
			event.getClickedBlock().setTypeId(0);
			DataManager.addEntry(new BlockEntry(event.getPlayer(), DataType.BLOCK_BREAK, event.getClickedBlock()));
		}
		
	}
	
}