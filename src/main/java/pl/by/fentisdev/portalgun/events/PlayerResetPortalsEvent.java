package pl.by.fentisdev.portalgun.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import pl.by.fentisdev.portalgun.portalgun.PortalGun;

public class PlayerResetPortalsEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private PortalGun portalGun;
    @Getter
    private Player player;
    private boolean cancelled = false;

    public PlayerResetPortalsEvent(PortalGun portalGun, Player player) {
        this.portalGun = portalGun;
        this.player = player;
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
