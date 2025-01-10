package pl.by.fentisdev.portalgun.utils;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import pl.by.fentisdev.portalgun.PortalGunMain;
import pl.by.fentisdev.portalgun.portalgun.PortalColors;
import pl.by.fentisdev.portalgun.portalgun.PortalGunMode;
import pl.by.fentisdev.portalgun.portalgun.PortalModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PortalConfig {

    @Getter
    private static PortalConfig instance = new PortalConfig();

    private final FileConfiguration cfg = PortalGunMain.getInstance().getConfig();
    private final List<Material> whitelistBlocks = new ArrayList<>();
    @Getter
    private final List<EntityType> grabEntities = new ArrayList<>();

    public void createConfig(){
        cfg.setInlineComments("PortalGunMode",Arrays.asList("[INFINITY: Each Portal Gun created has its own portal. ONE_TYPE_PER_PLAYER: The player can only have one Portal Gun of each type. ONE_PORTAL_PER_PLAYER: The Player can only have one Portal Gun, if he gets another Portal Gun the previous portals will be disabled.]"));
        cfg.addDefault("PortalGunMode","INFINITY");
        cfg.addDefault("Interdimensional",true);
        cfg.addDefault("GrabEntity",false);
        cfg.addDefault("CameraChangeDirection",true);
        cfg.addDefault("WhiteList",true);
        cfg.addDefault("WhiteListBlocks", Arrays.asList(Material.WHITE_CONCRETE.toString(),Material.WHITE_WOOL.toString(),Material.QUARTZ_BLOCK.toString(),Material.SMOOTH_QUARTZ.toString()));

        cfg.setInlineComments("GrabEntitiesListMode",Arrays.asList("You can switch to modes: BLACKLIST, WHITELIST and DISABLED"));
        cfg.addDefault("GrabEntitiesListMode","BLACKLIST");
        cfg.setInlineComments("GrabEntitiesList",Arrays.asList("EntityType List in: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/EntityType.html"));
        cfg.addDefault("GrabEntitiesList",Arrays.asList(EntityType.ARMOR_STAND.toString()));

        cfg.setInlineComments("PortalGunResources",Arrays.asList("Material List in: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html"));
        cfg.addDefault("PortalGunResources."+ PortalModel.CHELL.toString().toLowerCase()+".Material",Material.WOODEN_HOE.toString());
        cfg.addDefault("PortalGunResources."+ PortalModel.CHELL.toString().toLowerCase()+".CustomModelData.Normal",1);
        cfg.addDefault("PortalGunResources."+ PortalModel.CHELL.toString().toLowerCase()+".CustomModelData.Shoot1",2);
        cfg.addDefault("PortalGunResources."+ PortalModel.CHELL.toString().toLowerCase()+".CustomModelData.Shoot2",3);

        cfg.addDefault("PortalGunResources."+ PortalModel.ATLAS.toString().toLowerCase()+".Material",Material.IRON_HOE.toString());
        cfg.addDefault("PortalGunResources."+ PortalModel.ATLAS.toString().toLowerCase()+".CustomModelData.Normal",1);
        cfg.addDefault("PortalGunResources."+ PortalModel.ATLAS.toString().toLowerCase()+".CustomModelData.Shoot1",2);
        cfg.addDefault("PortalGunResources."+ PortalModel.ATLAS.toString().toLowerCase()+".CustomModelData.Shoot2",3);

        cfg.addDefault("PortalGunResources."+ PortalModel.P_BODY.toString().toLowerCase()+".Material",Material.STONE_HOE.toString());
        cfg.addDefault("PortalGunResources."+ PortalModel.P_BODY.toString().toLowerCase()+".CustomModelData.Normal",1);
        cfg.addDefault("PortalGunResources."+ PortalModel.P_BODY.toString().toLowerCase()+".CustomModelData.Shoot1",2);
        cfg.addDefault("PortalGunResources."+ PortalModel.P_BODY.toString().toLowerCase()+".CustomModelData.Shoot2",3);

        cfg.addDefault("PortalGunResources."+ PortalModel.POTATOS.toString().toLowerCase()+".Material",Material.GOLDEN_HOE.toString());
        cfg.addDefault("PortalGunResources."+ PortalModel.POTATOS.toString().toLowerCase()+".CustomModelData.Normal",1);
        cfg.addDefault("PortalGunResources."+ PortalModel.POTATOS.toString().toLowerCase()+".CustomModelData.Shoot1",2);
        cfg.addDefault("PortalGunResources."+ PortalModel.POTATOS.toString().toLowerCase()+".CustomModelData.Shoot2",3);

        cfg.addDefault("PortalCraftable",true);

        cfg.addDefault("PortalGunCrafts."+ PortalModel.CHELL.toString().toLowerCase()+".Name","&fPortal Gun");
        cfg.addDefault("PortalGunCrafts."+ PortalModel.CHELL.toString().toLowerCase()+".Craft",true);
        cfg.addDefault("PortalGunCrafts."+ PortalModel.CHELL.toString().toLowerCase()+".Shape",Arrays.asList("AIG","WNI","WWA"));
        cfg.addDefault("PortalGunCrafts."+ PortalModel.CHELL.toString().toLowerCase()+".Ingredients",Arrays.asList("I:IRON_INGOT","G:GLASS_PANE","W:WHITE_CONCRETE","N:NETHER_STAR"));

        cfg.addDefault("PortalGunCrafts."+ PortalModel.ATLAS.toString().toLowerCase()+".Name","&fAtlas Portal Gun");
        cfg.addDefault("PortalGunCrafts."+ PortalModel.ATLAS.toString().toLowerCase()+".Craft",true);
        cfg.addDefault("PortalGunCrafts."+ PortalModel.ATLAS.toString().toLowerCase()+".Shape",Arrays.asList("AIG","YNI","WWA"));
        cfg.addDefault("PortalGunCrafts."+ PortalModel.ATLAS.toString().toLowerCase()+".Ingredients",Arrays.asList("I:IRON_INGOT","G:GLASS_PANE","W:WHITE_CONCRETE","Y:YELLOW_CONCRETE","N:NETHER_STAR"));

        cfg.addDefault("PortalGunCrafts."+ PortalModel.P_BODY.toString().toLowerCase()+".Name","&fP-Body Portal Gun");
        cfg.addDefault("PortalGunCrafts."+ PortalModel.P_BODY.toString().toLowerCase()+".Craft",true);
        cfg.addDefault("PortalGunCrafts."+ PortalModel.P_BODY.toString().toLowerCase()+".Shape",Arrays.asList("AIG","LNI","WWA"));
        cfg.addDefault("PortalGunCrafts."+ PortalModel.P_BODY.toString().toLowerCase()+".Ingredients",Arrays.asList("I:IRON_INGOT","G:GLASS_PANE","W:WHITE_CONCRETE","L:LIGHT_BLUE_CONCRETE","N:NETHER_STAR"));

        cfg.addDefault("PortalGunCrafts."+ PortalModel.POTATOS.toString().toLowerCase()+".Name","&fPotatOS Portal Gun");
        cfg.addDefault("PortalGunCrafts."+ PortalModel.POTATOS.toString().toLowerCase()+".Craft",true);
        cfg.addDefault("PortalGunCrafts."+ PortalModel.POTATOS.toString().toLowerCase()+".Shape",Arrays.asList("PIG","WNI","WWA"));
        cfg.addDefault("PortalGunCrafts."+ PortalModel.POTATOS.toString().toLowerCase()+".Ingredients",Arrays.asList("I:IRON_INGOT","G:GLASS_PANE","W:WHITE_CONCRETE","P:POTATO","N:NETHER_STAR"));

        cfg.setInlineComments("PortalMapID",Arrays.asList("Don't Change! But if the portals don't render correctly, replace all values with -1"));
        for (PortalColors value : PortalColors.values()) {
            cfg.addDefault("PortalMapID."+ value.toString().toLowerCase()+".up",-1);
            cfg.addDefault("PortalMapID."+ value.toString().toLowerCase()+".down",-1);
        }
        cfg.options().copyHeader(true);
        cfg.options().copyDefaults(true);
        PortalGunMain.getInstance().saveDefaultConfig();
        PortalGunMain.getInstance().saveConfig();
    }

    public boolean hasGrabEntitiesListMode(){
        return !cfg.getString("GrabEntitiesListMode").equalsIgnoreCase("DISABLED");
    }

    public boolean cameraChangeDirection(){
        return cfg.getBoolean("CameraChangeDirection");
    }

    public void setCameraChangeDirection(boolean cameraChangeDirection){
        cfg.set("CameraChangeDirection",cameraChangeDirection);
    }

    public boolean whitelistGrabEntitiesListMode(){
        return cfg.getString("GrabEntitiesListMode").equalsIgnoreCase("WHITELIST");
    }

    public List<EntityType> getGrabEntitiesList(){
        if (grabEntities.isEmpty()){
            for (String grabEntity : cfg.getStringList("GrabEntitiesList")) {
                grabEntities.add(EntityType.valueOf(grabEntity.toUpperCase()));
            }
        }
        return grabEntities;
    }

    public boolean isInterdimensional(){
        return cfg.getBoolean("Interdimensional");
    }

    public void setInterdimensional(boolean interdimensional){
        cfg.set("Interdimensional",interdimensional);
    }

    public PortalGunMode getPortalGunMode(){
        return Optional.ofNullable(PortalGunMode.valueOf(cfg.getString("PortalGunMode").toUpperCase())).orElse(PortalGunMode.INFINITY);
    }

    public boolean portalCraftable(){
        return cfg.getBoolean("PortalCraftable");
    }

    public void setPortalCraftable(boolean portalCraftable){
        cfg.set("PortalCraftable",portalCraftable);
    }

    public int getPortalShootRange(){
        return cfg.getInt("PortalShootRange");
    }

    public void setPortalShootRange(int portalShootRange){
        cfg.set("PortalShootRange",portalShootRange);
    }

    public boolean whiteList(){
        return cfg.getBoolean("WhiteList");
    }

    public void setWhiteList(boolean whiteList){
        cfg.set("WhiteList",whiteList);
    }

    public void setWhiteListBlocks(List<Material> whiteList){
        cfg.set("WhiteListBlocks",whiteList.stream().map(Enum::toString).collect(Collectors.toList()));
        whitelistBlocks.clear();
        whitelistBlocks.addAll(whiteList);
    }

    public List<Material> getWhiteListBlocks(){
        if (whitelistBlocks.isEmpty()){
            for (String whiteListBlocks : cfg.getStringList("WhiteListBlocks")) {
                whitelistBlocks.add(Material.valueOf(whiteListBlocks.toUpperCase()));
            }
        }
        return whitelistBlocks;
    }

    public Material getPortalGunMaterial(PortalModel model){
        return Material.getMaterial(cfg.getString("PortalGunResources."+model.toString().toLowerCase()+".Material"));
    }

    public int getPortalGunCustomModelDataNormal(PortalModel model){
        return cfg.getInt("PortalGunResources."+model.toString().toLowerCase()+".CustomModelData.Normal");
    }

    public int getPortalGunCustomModelDataShoot1(PortalModel model){
        return cfg.getInt("PortalGunResources."+model.toString().toLowerCase()+".CustomModelData.Shoot1");
    }

    public int getPortalGunCustomModelDataShoot2(PortalModel model){
        return cfg.getInt("PortalGunResources."+model.toString().toLowerCase()+".CustomModelData.Shoot2");
    }

    public boolean canGrabEntity(){
        return cfg.getBoolean("GrabEntity");
    }

    public void setCanGrabEntity(boolean canGrabEntity){
        cfg.set("GrabEntity",canGrabEntity);
    }

    public String portalGunName(PortalModel pm){
        return cfg.getString("PortalGunCrafts."+ pm.toString().toLowerCase()+".Name");
    }

    public boolean canCraft(PortalModel pm){
        return cfg.getBoolean("PortalGunCrafts."+ pm.toString().toLowerCase()+".Craft");
    }

    public void setCanCraft(PortalModel pm, boolean canCraft){
        cfg.set("PortalGunCrafts."+ pm.toString().toLowerCase()+".Craft",canCraft);
    }
    public void setRecipe(PortalModel pm, List<String> shape, List<String> ingredients){
        cfg.set("PortalGunCrafts."+ pm.toString().toLowerCase()+".Shape",shape);
        cfg.set("PortalGunCrafts."+ pm.toString().toLowerCase()+".Ingredients",ingredients);
    }

    public void saveConfig(){
        PortalGunMain.getInstance().saveConfig();
    }

}
