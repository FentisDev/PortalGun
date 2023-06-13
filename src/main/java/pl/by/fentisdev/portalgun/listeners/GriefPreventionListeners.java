package pl.by.fentisdev.portalgun.listeners;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.by.fentisdev.portalgun.events.PlayerPortalShotEvent;
import pl.by.fentisdev.portalgun.events.PortalGunGrabEntityEvent;

public class GriefPreventionListeners implements Listener {

    private GriefPrevention gp = GriefPrevention.instance;

    @EventHandler
    public void onUsePortalGun(PlayerPortalShotEvent e){
        if (gp.claimsEnabledForWorld(e.getBlock().getWorld())&&
                gp.allowBuild(e.getPlayer(), e.getBlock().getLocation()) != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onGrabEntity(PortalGunGrabEntityEvent e){
        if (gp.claimsEnabledForWorld(e.getEntity().getWorld())&&
                gp.allowBreak(e.getPlayer(), e.getEntity().getLocation().getBlock(), e.getEntity().getLocation()) != null) {
            e.setCancelled(true);
        }
    }
}
