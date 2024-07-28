package pl.by.fentisdev.portalgun.events;

import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import pl.by.fentisdev.portalgun.portalgun.PortalGun;

public class PortalGunGrabEntityEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private PortalGun portalGun;
    @Getter
    private Player player;
    @Getter
    private Entity entity;
    private boolean cancelled = false;

    public PortalGunGrabEntityEvent(PortalGun portalGun, Player player, Entity entity) {
        this.portalGun = portalGun;
        this.player = player;
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
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
