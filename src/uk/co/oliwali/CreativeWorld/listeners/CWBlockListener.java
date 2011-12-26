package uk.co.oliwali.CreativeWorld.listeners;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

import uk.co.oliwali.CreativeWorld.Config;
import uk.co.oliwali.CreativeWorld.Permission;

public class CWBlockListener extends BlockListener {
	
	public void onBlockBreak(BlockBreakEvent event) {
		
		if (event.isCancelled()) return;
		if ((!Permission.inGroup(event.getPlayer(), Config.CreativeGroup) || !Arrays.asList(new Material[]{ Material.DIAMOND_PICKAXE, Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE }).contains(event.getPlayer().getItemInHand().getType())) && event.getBlock().getWorld().getName().equals(Config.CreativeWorld)) {
			event.setCancelled(true);
		}
		
	}

}
