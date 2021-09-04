package pl.by.fentisdev.portalgun.portalgun;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public enum PortalSound {

    PORTAL_GUN_PICKUP("portal_gun_pickup",Sound.BLOCK_BELL_RESONATE),
    PORTAL_GUN_SHOOT("portal_gun_shoot",Sound.ENTITY_CHICKEN_EGG),
    PORTAL_OPEN_ORANGE("portal_open_orange",Sound.ENTITY_PLAYER_SPLASH),
    PORTAL_OPEN_BLUE("portal_open_blue",Sound.ENTITY_PLAYER_SPLASH_HIGH_SPEED),
    PORTAL_CLOSE("portal_close",Sound.BLOCK_LAVA_EXTINGUISH),
    PORTAL_ENTER("portal_enter",Sound.ENTITY_ENDERMAN_TELEPORT),
    PORTAL_EXIT("portal_exit",Sound.ENTITY_ENDERMAN_TELEPORT),
    PORTAL_INVALID_SURFACE("portal_invalid_surface",Sound.ENTITY_BLAZE_HURT);

    private String name;
    private Sound noResource;

    PortalSound(String name, Sound noResource) {
        this.name = name;
        this.noResource = noResource;
    }

    public String getName() {
        return "portal."+name;
    }

    public void playSound(Location loc, float a, float s, Player... p){
        for (Player player : p) {
            //player.playSound(loc,getName(),a,s);
            player.playSound(loc,noResource,a,s);
        }
    }

    public void playSound(Location loc, float a, float s){
        //loc.getWorld().playSound(loc,getName(),a,s);
        loc.getWorld().playSound(loc,noResource,a,s);
    }
}
