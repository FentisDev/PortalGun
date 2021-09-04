package pl.by.fentisdev.portalgun.utils;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import pl.by.fentisdev.portalgun.PortalGunMain;
import pl.by.fentisdev.portalgun.portalgun.PortalColors;
import pl.by.fentisdev.portalgun.portalgun.PortalGunMode;
import pl.by.fentisdev.portalgun.portalgun.PortalModel;

import java.util.Arrays;
import java.util.Optional;

public class PortalConfig {

    private static PortalConfig instance = new PortalConfig();

    public static PortalConfig getInstance() {
        return instance;
    }

    private FileConfiguration cfg = PortalGunMain.getInstance().getConfig();

    public void createConfig(){
        cfg.options().header("PortalGunMode:[INFINITY: Each Portal Gun created has its own portal. UNIQUE: The player can only have one Portal Gun of each type. ONE_PORTAL_PER_PLAYER: The Player can only have one Portal Gun, if he gets another Portal Gun the previous portals will be disabled.]");
        cfg.addDefault("PortalGunMode","INFINITY");
        cfg.addDefault("Interdimensional",true);
        cfg.addDefault("WhiteList",true);
        cfg.addDefault("WhiteListBlocks", Arrays.asList(Material.WHITE_CONCRETE.toString(),Material.WHITE_WOOL.toString(),Material.QUARTZ_BLOCK.toString(),Material.SMOOTH_QUARTZ.toString()));
        cfg.addDefault("PortalCraftable",true);

        cfg.addDefault("PortalGunCrafts."+ PortalModel.CHELL.toString().toLowerCase()+".Craft",true);
        cfg.addDefault("PortalGunCrafts."+ PortalModel.CHELL.toString().toLowerCase()+".Shape",Arrays.asList("AIG","WNI","WWA"));
        cfg.addDefault("PortalGunCrafts."+ PortalModel.CHELL.toString().toLowerCase()+".Ingredients",Arrays.asList("I:IRON_INGOT","G:GLASS_PANE","W:WHITE_CONCRETE","N:NETHER_STAR"));

        cfg.addDefault("PortalGunCrafts."+ PortalModel.ATLAS.toString().toLowerCase()+".Craft",true);
        cfg.addDefault("PortalGunCrafts."+ PortalModel.ATLAS.toString().toLowerCase()+".Shape",Arrays.asList("AIG","YNI","WWA"));
        cfg.addDefault("PortalGunCrafts."+ PortalModel.ATLAS.toString().toLowerCase()+".Ingredients",Arrays.asList("I:IRON_INGOT","G:GLASS_PANE","W:WHITE_CONCRETE","Y:YELLOW_CONCRETE","N:NETHER_STAR"));

        cfg.addDefault("PortalGunCrafts."+ PortalModel.P_BODY.toString().toLowerCase()+".Craft",true);
        cfg.addDefault("PortalGunCrafts."+ PortalModel.P_BODY.toString().toLowerCase()+".Shape",Arrays.asList("AIG","LNI","WWA"));
        cfg.addDefault("PortalGunCrafts."+ PortalModel.P_BODY.toString().toLowerCase()+".Ingredients",Arrays.asList("I:IRON_INGOT","G:GLASS_PANE","W:WHITE_CONCRETE","L:LIGHT_BLUE_CONCRETE","N:NETHER_STAR"));

        cfg.addDefault("PortalGunCrafts."+ PortalModel.POTATOS.toString().toLowerCase()+".Craft",true);
        cfg.addDefault("PortalGunCrafts."+ PortalModel.POTATOS.toString().toLowerCase()+".Shape",Arrays.asList("PIG","WNI","WWA"));
        cfg.addDefault("PortalGunCrafts."+ PortalModel.POTATOS.toString().toLowerCase()+".Ingredients",Arrays.asList("I:IRON_INGOT","G:GLASS_PANE","W:WHITE_CONCRETE","P:POTATO","N:NETHER_STAR"));

        for (PortalColors value : PortalColors.values()) {
            cfg.addDefault("PortalMapID."+ value.toString().toLowerCase()+".up",-1);
            cfg.addDefault("PortalMapID."+ value.toString().toLowerCase()+".down",-1);
        }
        cfg.options().copyHeader(true);
        cfg.options().copyDefaults(true);
        PortalGunMain.getInstance().saveDefaultConfig();
        PortalGunMain.getInstance().saveConfig();
    }

    public boolean isInterdimensional(){
        return cfg.getBoolean("Interdimensional");
    }

    public PortalGunMode getPortalGunMode(){
        return Optional.ofNullable(PortalGunMode.valueOf(cfg.getString("PortalGunMode").toUpperCase())).orElse(PortalGunMode.INFINITY);
    }


}
