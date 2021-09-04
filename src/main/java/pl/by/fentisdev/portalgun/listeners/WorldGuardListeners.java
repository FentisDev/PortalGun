package pl.by.fentisdev.portalgun.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.by.fentisdev.portalgun.events.PlayerPortalShotEvent;
import pl.by.fentisdev.portalgun.utils.PortalWorldGuard;

public class WorldGuardListeners implements Listener {

    @EventHandler
    public void onUsePortalGun(PlayerPortalShotEvent e){
        if (!PortalWorldGuard.getInstance().verify(e.getPlayer(),e.getPlayer().getLocation(),PortalWorldGuard.getInstance().getPortalGunUseFlag())){
            e.setCancelled(true);
        }
        if (!PortalWorldGuard.getInstance().verify(e.getPlayer(),e.getBlock().getLocation(),PortalWorldGuard.getInstance().getPortalOpenFlag())){
            e.setCancelled(true);
        }
    }
}
