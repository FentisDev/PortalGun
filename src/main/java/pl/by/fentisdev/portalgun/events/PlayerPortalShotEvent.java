package pl.by.fentisdev.portalgun.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import pl.by.fentisdev.portalgun.portalgun.Portal;
import pl.by.fentisdev.portalgun.portalgun.PortalGun;

public class PlayerPortalShotEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    private PortalGun portalGun;
    private Portal portal;
    private Block block;
    private Player player;
    private boolean cancelled = false;

    public PlayerPortalShotEvent(PortalGun portalGun, Portal portal, Block block, Player player) {
        this.portalGun = portalGun;
        this.portal = portal;
        this.block = block;
        this.player = player;
    }

    public PortalGun getPortalGun() {
        return portalGun;
    }

    public Portal getPortal() {
        return portal;
    }

    public Block getBlock() {
        return block;
    }

    public Player getPlayer() {
        return player;
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
