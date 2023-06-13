package pl.by.fentisdev.portalgun.portalgun;

import org.bukkit.Material;
import pl.by.fentisdev.itemcreator.ItemCreator;
import pl.by.fentisdev.portalgun.utils.PortalConfig;

public enum PortalModel {

    CHELL(0,PortalColors.BLUE,PortalColors.ORANGE,"§fPortal Gun"),
    P_BODY(1,PortalColors.AQUA,PortalColors.PURPLE,"§fP-Body Portal Gun"),
    ATLAS(2,PortalColors.YELLOW,PortalColors.RED,"§fAtlas Portal Gun"),
    POTATOS(3,PortalColors.BLUE,PortalColors.ORANGE,"§fPotatOS Portal Gun");

    private int id;
    private PortalColors portal1,portal2;
    private String name;

    PortalModel(int id, PortalColors portal1, PortalColors portal2, String name) {
        this.id = id;
        this.portal1 = portal1;
        this.portal2 = portal2;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public PortalColors getPortalColor1() {
        return portal1;
    }

    public PortalColors getPortalColor2() {
        return portal2;
    }

    public Material getMaterialPortal() {
        return PortalConfig.getInstance().getPortalGunMaterial(this);
    }

    public int getCustomModelDataNormal() {
        return PortalConfig.getInstance().getPortalGunCustomModelDataNormal(this);
    }

    public int getCustomModelDataShoot1() {
        return PortalConfig.getInstance().getPortalGunCustomModelDataShoot1(this);
    }

    public int getCustomModelDataShoot2() {
        return PortalConfig.getInstance().getPortalGunCustomModelDataShoot2(this);
    }

    public String getName() {
        return name;
    }

    public ItemCreator createItem(){
        return new ItemCreator(getMaterialPortal()).setCustomModelData(getCustomModelDataNormal()).setDisplayName(getName()).setUnbreakable(true);
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

    public static PortalModel getPortalModelById(int id){
        PortalModel model = null;
        for (PortalModel value : values()) {
            if (value.getId()==id){
                model=value;
            }
        }
        return model;
    }

    public static PortalModel getPortalModelByItem(ItemCreator item){
        PortalModel model = null;
        for (PortalModel value : values()) {
            if (value.getMaterialPortal()==item.getMaterial()&&(
                            value.getCustomModelDataNormal()==item.getCustomModelData()||
                            value.getCustomModelDataShoot1()==item.getCustomModelData()||
                            value.getCustomModelDataShoot2()==item.getCustomModelData()
                    )){
                model=value;
            }
        }
        return model;
    }
}
