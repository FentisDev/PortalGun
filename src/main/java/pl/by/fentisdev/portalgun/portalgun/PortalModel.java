package pl.by.fentisdev.portalgun.portalgun;

import org.bukkit.Material;
import pl.by.fentisdev.portalgun.utils.ItemCreator;

public enum PortalModel {

    CHELL(PortalColors.BLUE,PortalColors.ORANGE, Material.WOODEN_HOE,1,"§fPortal Gun"),
    P_BODY(PortalColors.AQUA,PortalColors.PURPLE, Material.STONE_HOE,1,"§fP-Body Portal Gun"),
    ATLAS(PortalColors.YELLOW,PortalColors.RED, Material.IRON_HOE,1,"§fAtlas Portal Gun"),
    POTATOS(PortalColors.BLUE,PortalColors.ORANGE, Material.GOLDEN_HOE,1,"§fPotatOS Portal Gun");

    private PortalColors portal1,portal2;
    private Material material;
    private int customModelData;
    private String name;

    PortalModel(PortalColors portal1, PortalColors portal2, Material material, int customModelData, String name) {
        this.portal1 = portal1;
        this.portal2 = portal2;
        this.material = material;
        this.customModelData = customModelData;
        this.name = name;
    }

    public PortalColors getPortalColor1() {
        return portal1;
    }

    public PortalColors getPortalColor2() {
        return portal2;
    }

    public Material getMaterialPortal() {
        return material;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public String getName() {
        return name;
    }

    public ItemCreator createItem(){
        return new ItemCreator(getMaterialPortal()).setCustomModelData(getCustomModelData()).setDisplayName(getName()).setUnbreakable(true);
    }

    public static PortalModel getPortalModelByMaterial(Material material){
        PortalModel model = null;
        for (PortalModel value : values()) {
            if (value.getMaterialPortal()==material){
                model=value;
            }
        }
        return model;
    }
}
