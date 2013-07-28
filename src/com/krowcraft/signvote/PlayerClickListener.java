package com.krowcraft.signvote;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerClickListener implements Listener{
	
	mainvote plugin;
	
	public PlayerClickListener(mainvote instance){
		plugin = instance;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		double x, y, z;
		String playerstr;
		Player player = e.getPlayer();
		Action eAction = e.getAction();
		if (eAction == Action.RIGHT_CLICK_BLOCK ) {
			mainvote.logger.info("Right click block");
			Block clickedblock = e.getClickedBlock();
			mainvote.logger.info("" + clickedblock.getType().getId());
			if(clickedblock.getTypeId() == 68 || clickedblock.getTypeId() ==  63){
				mainvote.logger.info("sign instance");
				Sign sign = (Sign)clickedblock.getState();
				x = sign.getLocation().getBlockX();
				y = sign.getLocation().getBlockY();
				z = sign.getLocation().getBlockZ();
				if(mainvote.selectplayers.containsKey(player.getDisplayName())){
					playerstr = sign.getLine(0).toLowerCase();
					if(plugin.getConfig().contains((int)x + ":" + (int)y + ":" + (int)z)){
						player.sendMessage("Sign is already regestered");
						
					} else {
					mainvote.votewrite((int) x,(int) y,(int) z,playerstr,mainvote.selectplayers.get(player.getName()));
					}
				} else {
					//mainvote.voteread(mainvote.instance.getConfig().contains(path));
					
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		int x = (int)e.getBlock().getX();
		int y = (int)e.getBlock().getY();
		int z = (int)e.getBlock().getZ();
		String cordstring = x + ":" + y + ":" + z;
		if(plugin.getConfig().contains(cordstring)){
			plugin.getLogger().info("Contains " + cordstring);
			//
			String signdata = plugin.getConfig().getString(cordstring);
			
			String[] splitdata = signdata.split(".", 1);
			
			plugin.getLogger().info(splitdata[0]);
			
			String poot = splitdata + ".owner";
			
			String owner = plugin.getConfig().getString(poot);
			//
			
			if ((e.getPlayer().getName().toLowerCase() == owner) || e.getPlayer().isOp() || e.getPlayer().hasPermission("signvote.admin")){
				plugin.getLogger().info("Can break");
				plugin.getConfig().set(signdata, null);
				return;
			} else { // no perms to break
				plugin.getLogger().info("no perms to break");
				e.setCancelled(true);
				return;
			}
		}
		
	}
}
