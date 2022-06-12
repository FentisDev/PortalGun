package pl.by.fentisdev.portalgun.portalgun;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import pl.by.fentisdev.portalgun.PortalGunMain;
import pl.by.fentisdev.portalgun.events.PlayerPortalShotEvent;
import pl.by.fentisdev.portalgun.utils.ItemCreator;
import pl.by.fentisdev.portalgun.utils.PortalUtils;

import java.util.Arrays;

public class PortalGun {

    private int id;
    private PortalModel portalModel;
    private Portal portal1, portal2;
    private boolean cooldown = false;
    private boolean online = false;

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
        this.online = isActivated();
    }

    public PortalModel getPortalModel() {
        return portalModel;
    }

    public void setPortalModel(PortalModel portalModel) {
        this.portalModel = portalModel;
        portal1.setColor(portalModel.getPortalColor1());
        portal2.setColor(portalModel.getPortalColor2());
    }

    public Portal getPortal1() {
        return portal1;
    }

    public Portal getPortal2() {
        return portal2;
    }

    public Portal getPortal(ItemFrame itemFrame){
        return portal1!=null && portal1.isPortalItemFrame(itemFrame)?portal1:portal2!=null && portal2.isPortalItemFrame(itemFrame)?portal2:null;
    }

    public boolean isOnline() {
        return online;
    }

    public boolean isActivated(){
        return getPortal1()!=null&&getPortal2()!=null &&
                getPortal1().hasPortal() && getPortal2().hasPortal();
    }

    public void resetPortals(){
        portal1.resetPortal();
        portal2.resetPortal();
    }

    public boolean isTeleportable(){
        return portal1!=null&&portal2!=null&&portal1.hasPortal()&&portal2.hasPortal();
    }

    public int getId() {
        return id;
    }

    public ItemStack getPortalItem(){
        return getPortalItem(null);
    }

    public ItemStack getPortalItem(PortalColors colors){
        ItemCreator item = new ItemCreator(getPortalModel().getMaterialPortal())
                .setDisplayName(getPortalModel().getName())
                .setCustomModelData(colors==null?getPortalModel().getCustomModelDataNormal():(colors.isShoot1()?getPortalModel().getCustomModelDataShoot1():getPortalModel().getCustomModelDataShoot2()));
        NBTItem nbt = item.getNBTItem();
        nbt.setInteger("PortalID",getId());
        nbt.setString("PortalFileUUID",PortalGunManager.getInstance().getPortalFileUUID().toString());
        return item.build();
        /*ItemStack item = new ItemStack(getPortalModel().getMaterialPortal());
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(getPortalModel().getName());
        //im.setLore(Arrays.asList("§7#"+getId()));
        im.setCustomModelData(colors==null? getPortalModel().getCustomModelDataNormal():(colors.isShoot1()?getPortalModel().getCustomModelDataShoot1():getPortalModel().getCustomModelDataShoot2()));
        item.setItemMeta(im);
        NBTItem nbt = new NBTItem(item);
        nbt.setInteger("PortalID",getId());
        nbt.setString("PortalFileUUID",PortalGunManager.getInstance().getPortalFileUUID().toString());
        return nbt.getItem();*/
    }

    public void shootPortalBlue(Location location, Player p){
        shootPortal(location,true,p);
    }

    public void shootPortalOrange(Location location, Player p){
        shootPortal(location,false,p);
    }

    private void shootPortal(Location location, boolean portalBlue, Player p){
        if (cooldown){
            return;
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(PortalGunMain.getInstance(), new Runnable() {
            @Override
            public void run() {
                cooldown = false;
            }
        },5);
        /*new BukkitRunnable(){
            @Override
            public void run() {
                cooldown=false;
                cancel();
            }
        }.runTaskTimer(PortalGunMain.getInstance(),5,0);*/
        RayTraceResult t = location.getWorld().rayTraceBlocks(location, location.getDirection(), PortalGunMain.getInstance().getConfig().getInt("PortalShootRange"));
        if (t==null){
            return;
        }
        Block b = t.getHitBlock();
        if (b!=null) {
            Vector v = t.getHitPosition();
            BlockFace face = t.getHitBlockFace();
            Block nb = b.getRelative(face);
            Location up = null, down = null;

            BlockFace direction = PortalUtils.getInstance().getCardinalDirection(location);

            if (t.getHitBlockFace() == BlockFace.NORTH ||
                    t.getHitBlockFace() == BlockFace.SOUTH ||
                    t.getHitBlockFace() == BlockFace.EAST ||
                    t.getHitBlockFace() == BlockFace.WEST) {
                double rest = Math.abs(v.getY() - ((int) v.getY()));
                if (rest >= 0.5) {
                    if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.UP)) &&
                            PortalUtils.getInstance().inBlockList(b) &&
                            nb.getRelative(BlockFace.UP).getType() == Material.AIR) {
                        up = nb.getRelative(BlockFace.UP).getLocation();
                        down = nb.getLocation();
                    } else if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.DOWN)) &&
                            PortalUtils.getInstance().inBlockList(b) &&
                            nb.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                        down = nb.getRelative(BlockFace.DOWN).getLocation();
                        up = nb.getLocation();
                    }
                } else {
                    if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.DOWN)) &&
                            PortalUtils.getInstance().inBlockList(b) &&
                            nb.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                        down = nb.getRelative(BlockFace.DOWN).getLocation();
                        up = nb.getLocation();
                    } else if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.UP)) &&
                            PortalUtils.getInstance().inBlockList(b) &&
                            nb.getRelative(BlockFace.UP).getType() == Material.AIR) {
                        up = nb.getRelative(BlockFace.UP).getLocation();
                        down = nb.getLocation();
                    }
                }

            } else if (t.getHitBlockFace() == BlockFace.UP || t.getHitBlockFace() == BlockFace.DOWN) {
                double rest;
                if (direction == BlockFace.NORTH) {
                    rest = Math.abs(v.getZ() - ((int) v.getZ()));
                    if (rest > 0.5) {//up
                        if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.SOUTH)) &&
                                PortalUtils.getInstance().inBlockList(b) &&
                                nb.getRelative(BlockFace.SOUTH).getType() == Material.AIR) {
                            up = nb.getLocation();
                            down = nb.getRelative(BlockFace.SOUTH).getLocation();
                        } else if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.NORTH)) &&
                                PortalUtils.getInstance().inBlockList(b) &&
                                nb.getRelative(BlockFace.NORTH).getType() == Material.AIR) {
                            down = nb.getLocation();
                            up = nb.getRelative(BlockFace.NORTH).getLocation();
                        }
                    } else {//down
                        if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.NORTH)) &&
                                PortalUtils.getInstance().inBlockList(b) &&
                                nb.getRelative(BlockFace.NORTH).getType() == Material.AIR) {
                            down = nb.getLocation();
                            up = nb.getRelative(BlockFace.NORTH).getLocation();
                        } else if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.SOUTH)) &&
                                PortalUtils.getInstance().inBlockList(b) &&
                                nb.getRelative(BlockFace.SOUTH).getType() == Material.AIR) {
                            up = nb.getLocation();
                            down = nb.getRelative(BlockFace.SOUTH).getLocation();
                        }
                    }
                } else if (direction == BlockFace.SOUTH) {
                    rest = Math.abs(v.getZ() - ((int) v.getZ()));
                    if (rest < 0.5) {//up
                        if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.NORTH)) &&
                                PortalUtils.getInstance().inBlockList(b) &&
                                nb.getRelative(BlockFace.NORTH).getType() == Material.AIR) {
                            up = nb.getLocation();
                            down = nb.getRelative(BlockFace.NORTH).getLocation();
                        } else if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.SOUTH)) &&
                                PortalUtils.getInstance().inBlockList(b) &&
                                nb.getRelative(BlockFace.SOUTH).getType() == Material.AIR) {
                            down = nb.getLocation();
                            up = nb.getRelative(BlockFace.SOUTH).getLocation();
                        }
                    } else {//down
                        if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.SOUTH)) &&
                                PortalUtils.getInstance().inBlockList(b) &&
                                nb.getRelative(BlockFace.SOUTH).getType() == Material.AIR) {
                            down = nb.getLocation();
                            up = nb.getRelative(BlockFace.SOUTH).getLocation();
                        } else if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.NORTH)) &&
                                PortalUtils.getInstance().inBlockList(b) &&
                                nb.getRelative(BlockFace.NORTH).getType() == Material.AIR) {
                            up = nb.getLocation();
                            down = nb.getRelative(BlockFace.NORTH).getLocation();
                        }
                    }
                } else if (direction == BlockFace.EAST) {
                    rest = Math.abs(v.getX() - ((int) v.getX()));
                    if (rest > 0.5) {//up
                        if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.WEST)) &&
                                PortalUtils.getInstance().inBlockList(b) &&
                                nb.getRelative(BlockFace.WEST).getType() == Material.AIR) {
                            up = nb.getLocation();
                            down = nb.getRelative(BlockFace.WEST).getLocation();
                        } else if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.EAST)) &&
                                PortalUtils.getInstance().inBlockList(b) &&
                                nb.getRelative(BlockFace.EAST).getType() == Material.AIR) {
                            down = nb.getLocation();
                            up = nb.getRelative(BlockFace.EAST).getLocation();
                        }
                    } else {//down
                        if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.EAST)) &&
                                PortalUtils.getInstance().inBlockList(b) &&
                                nb.getRelative(BlockFace.EAST).getType() == Material.AIR) {
                            down = nb.getLocation();
                            up = nb.getRelative(BlockFace.EAST).getLocation();
                        } else if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.WEST)) &&
                                PortalUtils.getInstance().inBlockList(b) &&
                                nb.getRelative(BlockFace.WEST).getType() == Material.AIR) {
                            up = nb.getLocation();
                            down = nb.getRelative(BlockFace.WEST).getLocation();
                        }
                    }
                } else if (direction == BlockFace.WEST) {
                    rest = Math.abs(v.getX() - ((int) v.getX()));
                    if (rest < 0.5) {//up
                        if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.EAST)) &&
                                PortalUtils.getInstance().inBlockList(b) &&
                                nb.getRelative(BlockFace.EAST).getType() == Material.AIR) {
                            up = nb.getLocation();
                            down = nb.getRelative(BlockFace.EAST).getLocation();
                        } else if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.WEST)) &&
                                PortalUtils.getInstance().inBlockList(b) &&
                                nb.getRelative(BlockFace.WEST).getType() == Material.AIR) {
                            down = nb.getLocation();
                            up = nb.getRelative(BlockFace.WEST).getLocation();
                        }
                    } else {//down
                        if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.WEST)) &&
                                PortalUtils.getInstance().inBlockList(b) &&
                                nb.getRelative(BlockFace.WEST).getType() == Material.AIR) {
                            down = nb.getLocation();
                            up = nb.getRelative(BlockFace.WEST).getLocation();
                        } else if (PortalUtils.getInstance().inBlockList(b.getRelative(BlockFace.EAST)) &&
                                PortalUtils.getInstance().inBlockList(b) &&
                                nb.getRelative(BlockFace.EAST).getType() == Material.AIR) {
                            up = nb.getLocation();
                            down = nb.getRelative(BlockFace.EAST).getLocation();
                        }
                    }
                }
            }

            if (up != null && down != null) {
                for (PortalGun gun : PortalGunManager.getInstance().getPortalGuns()) {
                    if (gun.getPortal1().isPortalLocation(up, face) ||
                            gun.getPortal1().isPortalLocation(down, face)) {
                        gun.getPortal1().resetPortal();
                    }
                    if (gun.getPortal2().isPortalLocation(up, face) ||
                            gun.getPortal2().isPortalLocation(down, face)) {
                        gun.getPortal2().resetPortal();
                    }
                }
                if (checkSurface(up,face)&&checkSurface(down,face)){
                    Portal portal = portalBlue?this.getPortal1():this.getPortal2();
                    /*if (portalBlue) {
                        portal = this.getPortal1();
                        p.getInventory().setItemInMainHand(this.getPortalItem(this.getPortalModel().getPortalColor1()));
                    } else {
                        portal = this.getPortal2();
                        p.getInventory().setItemInMainHand(this.getPortalItem(this.getPortalModel().getPortalColor2()));
                    }*/
                    PlayerPortalShotEvent event = new PlayerPortalShotEvent(this,portal,t.getHitBlock(),p);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()){
                        return;
                    }
                    p.getInventory().setItemInMainHand(this.getPortalItem(portalBlue?this.getPortalModel().getPortalColor1():this.getPortalModel().getPortalColor2()));
                    if (t.getHitBlockFace() == BlockFace.DOWN) {
                        portal.setPortal(up, down, t.getHitBlockFace(), direction);
                    } else {
                        portal.setPortal(down, up, t.getHitBlockFace(), direction);
                    }
                    PortalSound.PORTAL_GUN_SHOOT.playSound(location, 1, 1);
                    online = isActivated();
                }else{
                    PortalSound.PORTAL_INVALID_SURFACE.playSound(t.getHitPosition().toLocation(location.getWorld()), 1, 1);
                }
            } else {
                PortalSound.PORTAL_INVALID_SURFACE.playSound(t.getHitPosition().toLocation(location.getWorld()), 1, 1);
            }
        }
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

}
