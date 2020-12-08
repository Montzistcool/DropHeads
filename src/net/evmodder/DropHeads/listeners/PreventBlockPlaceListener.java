package net.evmodder.DropHeads.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import com.mojang.authlib.GameProfile;
import net.evmodder.DropHeads.DropHeads;
import net.evmodder.EvLib.extras.HeadUtils;
import net.evmodder.EvLib.extras.TextUtils;

public class PreventBlockPlaceListener implements Listener{
	final String PREVENT_PLACE_MSG;

	public PreventBlockPlaceListener(){
		PREVENT_PLACE_MSG = TextUtils.translateAlternateColorCodes('&', DropHeads.getPlugin().getConfig()
				.getString("prevent-head-placement-message", "&7[&6DropHeads&7]&c No permission to place head blocks"));
	}

	// This listener is only registered when 'prevent-head-placement' = true
	@EventHandler(ignoreCancelled = true)
	public void onBlockPlaceEvent(BlockPlaceEvent evt){
		if(!HeadUtils.isPlayerHead(evt.getBlockPlaced().getType())) return;
		ItemStack headItem = evt.getHand() == EquipmentSlot.HAND
				? evt.getPlayer().getInventory().getItemInMainHand()
				: evt.getPlayer().getInventory().getItemInOffHand();
		if(!headItem.hasItemMeta()) return;
		SkullMeta meta = (SkullMeta) headItem.getItemMeta();
		GameProfile profile = HeadUtils.getGameProfile(meta);
		if(profile == null) return;

		if(evt.getPlayer().hasPermission("dropheads.canplacehead")) return;
		evt.getPlayer().sendMessage(PREVENT_PLACE_MSG);
		evt.setCancelled(true);
	}
}