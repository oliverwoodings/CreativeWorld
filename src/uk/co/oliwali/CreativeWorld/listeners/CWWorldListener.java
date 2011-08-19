package uk.co.oliwali.CreativeWorld.listeners;

import java.util.Arrays;

import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldListener;

import uk.co.oliwali.CreativeWorld.Config;

public class CWWorldListener extends WorldListener {
	
	public void onChunkLoad(ChunkLoadEvent event) {
		if (event.isNewChunk() && event.getWorld().getName().equalsIgnoreCase(Config.CreativeWorld) && Config.DisableChunkGen) {
			CraftChunk chunk = (CraftChunk)event.getChunk();
			chunk.getHandle().b = new byte[chunk.getHandle().b.length];
			Arrays.fill(chunk.getHandle().b, (byte)0);
			event.getWorld().save();
		}
	}

}
