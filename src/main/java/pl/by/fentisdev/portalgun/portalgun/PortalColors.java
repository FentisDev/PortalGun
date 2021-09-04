package pl.by.fentisdev.portalgun.portalgun;

public enum PortalColors {
    BLUE(PortalSound.PORTAL_OPEN_BLUE,2),
    ORANGE(PortalSound.PORTAL_OPEN_ORANGE,3),
    AQUA(PortalSound.PORTAL_OPEN_BLUE,2),
    RED(PortalSound.PORTAL_OPEN_ORANGE,3),
    YELLOW(PortalSound.PORTAL_OPEN_ORANGE,2),
    PURPLE(PortalSound.PORTAL_OPEN_BLUE,3);

    private PortalSound teleportSound;
    private int customModelData;

    PortalColors(PortalSound teleportSound, int customModelData) {
        this.teleportSound = teleportSound;
        this.customModelData = customModelData;
    }

    public PortalSound getTeleportSound() {
        return teleportSound;
    }

    public int getCustomModelData() {
        return customModelData;
    }

}
