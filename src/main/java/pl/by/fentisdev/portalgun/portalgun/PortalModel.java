package pl.by.fentisdev.portalgun.portalgun;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import pl.by.fentisdev.itemcreator.ItemCreator;
import pl.by.fentisdev.portalgun.utils.PortalConfig;

@Getter
public enum PortalModel {

    CHELL(0,PortalColors.BLUE,PortalColors.ORANGE),
    P_BODY(1,PortalColors.AQUA,PortalColors.PURPLE),
    ATLAS(2,PortalColors.YELLOW,PortalColors.RED),
    POTATOS(3,PortalColors.BLUE,PortalColors.ORANGE);

    private int id;
    private PortalColors portalColor1,portalColor2;

    PortalModel(int id, PortalColors portalColor1, PortalColors portalColor2) {
        this.id = id;
        this.portalColor1 = portalColor1;
        this.portalColor2 = portalColor2;
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

    public boolean hasCustomModelData(int customModelData){
        return getCustomModelDataNormal()==customModelData||getCustomModelDataShoot1()==customModelData||getCustomModelDataShoot2()==customModelData;
    }

    public String getName() {
        return ChatColor.translateAlternateColorCodes('&',PortalConfig.getInstance().portalGunName(this));
    }

    public ItemCreator createItem(){
        return new ItemCreator(getMaterialPortal()).setCustomModelData(getCustomModelDataNormal()).setDisplayName(getName()).setUnbreakable(true);
    }

    public static PortalModel getPortalModelByMaterialAndCustomModelData(Material material, int customModelData){
        PortalModel model = null;
        for (PortalModel value : values()) {
            if (value.getMaterialPortal()==material&&value.hasCustomModelData(customModelData)){
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
