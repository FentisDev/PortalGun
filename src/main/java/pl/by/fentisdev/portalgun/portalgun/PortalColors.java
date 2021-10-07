package pl.by.fentisdev.portalgun.portalgun;

public enum PortalColors {
    BLUE(PortalSound.PORTAL_OPEN_BLUE,true),
    ORANGE(PortalSound.PORTAL_OPEN_ORANGE,false),
    AQUA(PortalSound.PORTAL_OPEN_BLUE,true),
    RED(PortalSound.PORTAL_OPEN_ORANGE,false),
    YELLOW(PortalSound.PORTAL_OPEN_ORANGE,true),
    PURPLE(PortalSound.PORTAL_OPEN_BLUE,false);

    private PortalSound teleportSound;
    private boolean shoot1;

    PortalColors(PortalSound teleportSound, boolean shoot1) {
        this.teleportSound = teleportSound;
        this.shoot1 = shoot1;
    }

    public PortalSound getTeleportSound() {
        return teleportSound;
    }

    public boolean isShoot1() {
        return shoot1;
    }
}
