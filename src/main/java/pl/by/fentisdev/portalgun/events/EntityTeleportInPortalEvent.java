package pl.by.fentisdev.portalgun.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import pl.by.fentisdev.portalgun.portalgun.Portal;
import pl.by.fentisdev.portalgun.portalgun.PortalGun;

public class EntityTeleportInPortalEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    private PortalGun portalGun;
    private Portal portalIn;
    private Portal portalOut;
    private Entity entity;
    private boolean cancelled = false;

    public EntityTeleportInPortalEvent(PortalGun portalGun, Portal portalIn, Portal portalOut, Entity entity) {
        this.portalGun = portalGun;
        this.portalIn = portalIn;
        this.portalOut = portalOut;
        this.entity = entity;
    }

    public PortalGun getPortalGun() {
        return portalGun;
    }

    public Portal getPortalIn() {
        return portalIn;
    }

    public Portal getPortalOut() {
        return portalOut;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
