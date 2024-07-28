package pl.by.fentisdev.portalgun.events;

import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import pl.by.fentisdev.portalgun.portalgun.Portal;
import pl.by.fentisdev.portalgun.portalgun.PortalGun;

public class EntityTeleportInPortalEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private PortalGun portalGun;
    @Getter
    private Portal portalIn;
    @Getter
    private Portal portalOut;
    @Getter
    private Entity entity;
    private boolean cancelled = false;

    public EntityTeleportInPortalEvent(PortalGun portalGun, Portal portalIn, Portal portalOut, Entity entity) {
        this.portalGun = portalGun;
        this.portalIn = portalIn;
        this.portalOut = portalOut;
        this.entity = entity;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
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
