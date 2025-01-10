package pl.by.fentisdev.portalgun.portalgun;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import pl.by.fentisdev.portalgun.PortalGunMain;
import pl.by.fentisdev.portalgun.events.PlayerPortalShotEvent;
import pl.by.fentisdev.itemcreator.ItemCreator;
import pl.by.fentisdev.portalgun.utils.PortalGunNameSpacedKeys;
import pl.by.fentisdev.portalgun.utils.PortalUtils;

public class PortalGun {

    @Getter
    private int id;
    @Getter
    private PortalModel portalModel;
    @Getter
    private Portal portal1, portal2;
    @Getter
    private boolean isOnline = false;

    public PortalGun(int id, PortalModel portalModel){
        this.id = id;
        this.portalModel = portalModel;
        this.portal1 = new Portal(portalModel.getPortalColor1());
        this.portal2 = new Portal(portalModel.getPortalColor2());
    }

    public PortalGun(int id, PortalModel portalModel, Portal portal1, Portal portal2) {
        this.id = id;
        this.portalModel = portalModel;
        this.portal1 = portal1;
        this.portal2 = portal2;
        updateStatus();
    }

    public void setPortalModel(PortalModel portalModel) {
        this.portalModel = portalModel;
        portal1.setColor(portalModel.getPortalColor1());
        portal2.setColor(portalModel.getPortalColor2());
    }

    public Portal getPortal(ItemFrame itemFrame){
        return portal1!=null && portal1.isPortalItemFrame(itemFrame)?portal1:portal2!=null && portal2.isPortalItemFrame(itemFrame)?portal2:null;
    }

    public void updateStatus(){
        isOnline = isActivated();
    }

    public boolean isActivated(){
        return getPortal1()!=null&&getPortal2()!=null &&
                getPortal1().hasPortal() && getPortal2().hasPortal();
    }

    public void verifyPortals(){
        if (getPortal1().verifyPortal()||getPortal2().verifyPortal()){
            updateStatus();
        }
    }

    public void resetPortals(){
        portal1.resetPortal();
        portal2.resetPortal();
        updateStatus();
    }

    public boolean isTeleportable(){
        return portal1!=null&&portal2!=null&&portal1.hasPortal()&&portal2.hasPortal();
    }

    public ItemStack getPortalItem(){
        return getPortalItem(null);
    }

    public ItemStack getPortalItem(PortalColors colors){
        ItemCreator item = new ItemCreator(getPortalModel().getMaterialPortal())
                .setDisplayName(getPortalModel().getName())
                .setCustomModelData(colors==null?getPortalModel().getCustomModelDataNormal():(colors.isShoot1()?getPortalModel().getCustomModelDataShoot1():getPortalModel().getCustomModelDataShoot2()));

        item.getPersistentDataContainer().set(PortalGunNameSpacedKeys.PORTAL_ID_KEY, PersistentDataType.INTEGER,getId());
        item.getPersistentDataContainer().set(PortalGunNameSpacedKeys.PORTAL_FILE_ID_KEY, PersistentDataType.STRING,PortalGunManager.getInstance().getPortalFileUUID().toString());

        return item.getItemStack();
    }

    public ItemStack updatePortalItem(ItemStack itemStack){
        return updatePortalItem(itemStack,null);
    }

    public ItemStack updatePortalItem(ItemStack itemStack, PortalColors colors){
        return new ItemCreator(itemStack).setCustomModelData(colors==null?getPortalModel().getCustomModelDataNormal():(colors.isShoot1()?getPortalModel().getCustomModelDataShoot1():getPortalModel().getCustomModelDataShoot2())).getItemStack();
    }

    public void shootPortal(Player player, PortalClick portalClick, EquipmentSlot hand){
        shootPortal(player.getEyeLocation(),portalClick,player,hand);
    }

    private void shootPortal(Location location, PortalClick portalClick , Player p, EquipmentSlot hand){
        RayTraceResult t = location.getWorld().rayTraceBlocks(location, location.getDirection(), PortalGunMain.getInstance().getConfig().getInt("PortalShootRange"));
        if (t==null){
            return;
        }
        Block b = t.getHitBlock();
        if (b!=null) {
            if (!canPutAPortal(b)){
                return;
            }
            Vector v = t.getHitPosition();
            BlockFace face = t.getHitBlockFace();
            BlockFace compareFace = null;
            Block nb = b.getRelative(face);
            Location up = null, down = null;

            BlockFace direction = PortalUtils.getInstance().getCardinalDirection(location);

            if (face.isCartesian() && face!=BlockFace.UP && face!=BlockFace.DOWN){
                if (Math.abs(v.getY() - ((int) v.getY())) >= 0.5){
                    if (canPutAPortal(b.getRelative(compareFace=BlockFace.UP)) &&
                            nb.getRelative(compareFace).isEmpty()){
                        up = nb.getRelative(compareFace).getLocation();
                        down = nb.getLocation();
                    }else if (canPutAPortal(b.getRelative(compareFace=BlockFace.DOWN)) &&
                            nb.getRelative(compareFace).isEmpty()){
                        down = nb.getRelative(compareFace).getLocation();
                        up = nb.getLocation();
                    }
                }else{
                    if (canPutAPortal(b.getRelative(compareFace=BlockFace.DOWN)) &&
                            nb.getRelative(compareFace).isEmpty()){
                        down = nb.getRelative(compareFace).getLocation();
                        up = nb.getLocation();
                    } else if (canPutAPortal(b.getRelative(compareFace=BlockFace.UP)) &&
                            nb.getRelative(compareFace).isEmpty()) {
                        up = nb.getRelative(compareFace).getLocation();
                        down = nb.getLocation();
                    }
                }
            } else {
                switch (direction){
                    case NORTH:
                        if (Math.abs(v.getZ() - ((int)v.getZ())) > 0.5){ //UP
                            if (canPutAPortal(b.getRelative(compareFace=BlockFace.SOUTH)) &&
                                    nb.getRelative(compareFace).isEmpty()){
                                up = nb.getLocation();
                                down = nb.getRelative(compareFace).getLocation();
                            } else if (canPutAPortal(b.getRelative(compareFace = BlockFace.NORTH)) &&
                                    nb.getRelative(compareFace).isEmpty()) {
                                down = nb.getLocation();
                                up = nb.getRelative(compareFace).getLocation();
                            }
                        } else { //DOWN
                            if (canPutAPortal(b.getRelative(compareFace=BlockFace.NORTH)) &&
                                    nb.getRelative(compareFace).isEmpty()){
                                down = nb.getLocation();
                                up = nb.getRelative(compareFace).getLocation();
                            }else if (canPutAPortal(b.getRelative(compareFace=BlockFace.SOUTH)) &&
                                    nb.getRelative(compareFace).isEmpty()){
                                up = nb.getLocation();
                                down = nb.getRelative(compareFace).getLocation();
                            }
                        }
                        break;
                    case SOUTH:
                        if (Math.abs(v.getZ() - ((int) v.getZ())) < 0.5){//UP
                            if (canPutAPortal(b.getRelative(compareFace=BlockFace.NORTH)) &&
                                    nb.getRelative(compareFace).isEmpty()){
                                up = nb.getLocation();
                                down = nb.getRelative(compareFace).getLocation();
                            }else if (canPutAPortal(b.getRelative(compareFace=BlockFace.SOUTH)) &&
                                    nb.getRelative(compareFace).isEmpty()){
                                down = nb.getLocation();
                                up = nb.getRelative(compareFace).getLocation();
                            }
                        }else{//DOWN
                            if (canPutAPortal(b.getRelative(compareFace=BlockFace.SOUTH)) &&
                                    nb.getRelative(compareFace).isEmpty()){
                                down = nb.getLocation();
                                up = nb.getRelative(compareFace).getLocation();
                            }else if (canPutAPortal(b.getRelative(compareFace=BlockFace.NORTH)) &&
                                    nb.getRelative(compareFace).isEmpty()){
                                up = nb.getLocation();
                                down = nb.getRelative(compareFace).getLocation();
                            }
                        }
                        break;
                    case EAST:
                        if (Math.abs(v.getX() - ((int) v.getX())) > 0.5){//UP
                            if (canPutAPortal(b.getRelative(compareFace=BlockFace.WEST)) &&
                                    nb.getRelative(compareFace).isEmpty()){
                                up = nb.getLocation();
                                down = nb.getRelative(compareFace).getLocation();
                            }else if (canPutAPortal(b.getRelative(compareFace=BlockFace.EAST)) &&
                                    nb.getRelative(compareFace).isEmpty()){
                                down = nb.getLocation();
                                up = nb.getRelative(compareFace).getLocation();
                            }
                        }else{//DOWN
                            if (canPutAPortal(b.getRelative(compareFace=BlockFace.EAST)) &&
                                    nb.getRelative(compareFace).isEmpty()){
                                down = nb.getLocation();
                                up = nb.getRelative(compareFace).getLocation();
                            }else if (canPutAPortal(b.getRelative(compareFace=BlockFace.WEST)) &&
                                    nb.getRelative(compareFace).isEmpty()){
                                up = nb.getLocation();
                                down = nb.getRelative(compareFace).getLocation();
                            }
                        }
                        break;
                    case WEST:
                        if (Math.abs(v.getX() - ((int) v.getX())) < 0.5){//UP
                            if (canPutAPortal(b.getRelative(compareFace=BlockFace.EAST)) &&
                                    nb.getRelative(compareFace).isEmpty()){
                                up = nb.getLocation();
                                down = nb.getRelative(compareFace).getLocation();
                            }else if (canPutAPortal(b.getRelative(compareFace=BlockFace.WEST)) &&
                                    nb.getRelative(compareFace).isEmpty()){
                                down = nb.getLocation();
                                up = nb.getRelative(compareFace).getLocation();
                            }
                        }else{//DOWN
                            if (canPutAPortal(b.getRelative(compareFace=BlockFace.WEST)) &&
                                    nb.getRelative(compareFace).isEmpty()){
                                down = nb.getLocation();
                                up = nb.getRelative(compareFace).getLocation();
                            }else if (canPutAPortal(b.getRelative(compareFace=BlockFace.EAST)) &&
                                    nb.getRelative(compareFace).isEmpty()){
                                up = nb.getLocation();
                                down = nb.getRelative(compareFace).getLocation();
                            }
                        }
                        break;
                }
            }

            if (up != null && down != null) {
                for (PortalGun portalGun : PortalGunManager.getInstance().getPortalGuns()) {
                    if (portalGun.getPortal1().isPortalLocation(up, face) ||
                            portalGun.getPortal1().isPortalLocation(down, face)) {
                        portalGun.getPortal1().resetPortal();
                    }
                    if (portalGun.getPortal2().isPortalLocation(up, face) ||
                            portalGun.getPortal2().isPortalLocation(down, face)) {
                        portalGun.getPortal2().resetPortal();
                    }
                }
                if (checkSurface(up,face)&&checkSurface(down,face)){
                    Portal portal = portalClick==PortalClick.RIGHT?this.getPortal1():this.getPortal2();
                    PlayerPortalShotEvent event = new PlayerPortalShotEvent(this,portal,t.getHitBlock(),p);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()){
                        return;
                    }
                    ItemStack portalItem = p.getInventory().getItem(hand);
                    if (portalItem.getType() != getPortalModel().getMaterialPortal()){
                        portalItem.setType(getPortalModel().getMaterialPortal());
                    }
                    p.getInventory().setItem(hand,this.updatePortalItem(portalItem,portalClick==PortalClick.RIGHT?this.getPortalModel().getPortalColor1():this.getPortalModel().getPortalColor2()));
                    if (t.getHitBlockFace() == BlockFace.DOWN) {
                        portal.setPortal(up, down, t.getHitBlockFace(), direction);
                    } else {
                        portal.setPortal(down, up, t.getHitBlockFace(), direction);
                    }
                    PortalSound.PORTAL_GUN_SHOOT.playSound(location, 1, 1);
                }else{
                    PortalSound.PORTAL_INVALID_SURFACE.playSound(t.getHitPosition().toLocation(location.getWorld()), 1, 1);
                }
            } else {
                PortalSound.PORTAL_INVALID_SURFACE.playSound(t.getHitPosition().toLocation(location.getWorld()), 1, 1);
            }
        }
        updateStatus();
    }

    private boolean checkSurface(Location loc, BlockFace face){
        boolean status = true;
        for (Entity entity : loc.getChunk().getEntities()) {
            if (entity instanceof Hanging && PortalUtils.getInstance().isLocation(entity.getLocation(),loc) && entity.getFacing()==face){
                status = false;
            }
        }
        return status;
    }

    private boolean canPutAPortal(Block block){
        return PortalUtils.getInstance().inBlockList(block);
    }
}
