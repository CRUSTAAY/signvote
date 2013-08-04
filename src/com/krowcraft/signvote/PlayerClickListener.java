package com.krowcraft.signvote;

import org.bukkit.ChatColor;
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
			Block clickedblock = e.getClickedBlock();
			mainvote.logger.info("" + clickedblock.getType().getId());
			if(clickedblock.getTypeId() == 68 || clickedblock.getTypeId() ==  63){
				Sign sign = (Sign)clickedblock.getState();
				x = sign.getLocation().getBlockX();
				y = sign.getLocation().getBlockY();
				z = sign.getLocation().getBlockZ();
				if(mainvote.selectplayers.containsKey(player.getDisplayName())){ // signvote owner/admin
					playerstr = sign.getLine(0).toLowerCase();
					if(plugin.getConfig().contains((int)x + ":" + (int)y + ":" + (int)z)){
						player.sendMessage("Sign is already regestered");
						
					} else {
					mainvote.votewrite((int) x,(int) y,(int) z,playerstr,mainvote.selectplayers.get(player.getName()));
					sign.setLine(0, ChatColor.BLUE+"[Vote]");
					sign.setLine(3, playerstr);
					sign.update();
					}
				} else { // voter
					if (!plugin.getConfig().contains(player.getName())){
						String[] datastuff = plugin.getConfig().getString((int)x + ":" + (int)y + ":" + (int)z).split("\\.");
						plugin.getConfig().set(player.getName() + ".votecount", plugin.getConfig().get(datastuff[0] + ".maxvotesperplayer"));
						
					}
					//mainvote.voteread(mainvote.instance.getConfig().contains(path));
					
				}
			}
		}
		plugin.saveConfig();
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		int x = (int)e.getBlock().getX();
		int y = (int)e.getBlock().getY();
		int z = (int)e.getBlock().getZ();
		String cordstring = x + ":" + y + ":" + z;
		if(plugin.getConfig().contains(cordstring)){
			plugin.getLogger().info("Contains " + cordstring);
			String signdata = plugin.getConfig().getString(cordstring);
			String[] splitdata = signdata.split("\\.");
			String poot = splitdata[0] + ".owner";
			String owner = plugin.getConfig().getString(poot);
			e.getPlayer().sendMessage(owner);
			if ((e.getPlayer().getName().toLowerCase() == owner.toLowerCase()) || e.getPlayer().isOp() || e.getPlayer().hasPermission("signvote.admin")){
				plugin.getLogger().info("Can break");
				plugin.getConfig().set(cordstring, null);
				plugin.saveConfig();
				return;
			} else { // no perms to break
				plugin.getLogger().info("no perms to break");
				e.setCancelled(true);
				return;
			}
		}
		
	}
}
