package pl.by.fentisdev.portalgun.utils;

import org.bukkit.NamespacedKey;
import pl.by.fentisdev.portalgun.PortalGunMain;

public class PortalGunNameSpacedKeys {

    public static final NamespacedKey PORTAL_ID_KEY = new NamespacedKey(PortalGunMain.getInstance(),"portal_id");
    public static final NamespacedKey PORTAL_FILE_ID_KEY = new NamespacedKey(PortalGunMain.getInstance(),"portal_file_id");
    public static final NamespacedKey PORTAL_ITEM_DROP_KEY = new NamespacedKey(PortalGunMain.getInstance(),"portal_item_drop");
}
