package pl.by.fentisdev.portalgun.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import pl.by.fentisdev.portalgun.portalgun.PortalGun;

public class PortalGunDropEntityEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    private PortalGun portalGun;
    private Player player;
    private Entity entity;
    private boolean cancelled = false;

    public PortalGunDropEntityEvent(PortalGun portalGun, Player player, Entity entity) {
        this.portalGun = portalGun;
        this.player = player;
        this.entity = entity;
    }

    public PortalGun getPortalGun() {
        return portalGun;
    }

    public Player getPlayer() {
        return player;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
