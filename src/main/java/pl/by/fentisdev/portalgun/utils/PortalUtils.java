package pl.by.fentisdev.portalgun.utils;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import pl.by.fentisdev.portalgun.PortalGunMain;
import pl.by.fentisdev.portalgun.portalgun.*;
import pl.by.fentisdev.portalgun.utils.nbt.NBTTagCompound;

import java.util.Arrays;

public class PortalUtils {

    private static PortalUtils instance = new PortalUtils();

    public static PortalUtils getInstance() {
        return instance;
    }

    private ItemStack blueUpMap;
    private ItemStack blueDownMap;
    private ItemStack orangeUpMap;
    private ItemStack orangeDownMap;
    private ItemStack aquaUpMap;
    private ItemStack aquaDownMap;
    private ItemStack redUpMap;
    private ItemStack redDownMap;
    private ItemStack yellowUpMap;
    private ItemStack yellowDownMap;
    private ItemStack purpleUpMap;
    private ItemStack purpleDownMap;

    public PortalUtils() {
        blueUpMap = createPortalMapItem(PortalSide.UP,PortalColors.BLUE);
        blueDownMap = createPortalMapItem(PortalSide.DOWN,PortalColors.BLUE);
        orangeUpMap = createPortalMapItem(PortalSide.UP,PortalColors.ORANGE);
        orangeDownMap = createPortalMapItem(PortalSide.DOWN,PortalColors.ORANGE);
        aquaUpMap = createPortalMapItem(PortalSide.UP,PortalColors.AQUA);
        aquaDownMap = createPortalMapItem(PortalSide.DOWN,PortalColors.AQUA);
        redUpMap = createPortalMapItem(PortalSide.UP,PortalColors.RED);
        redDownMap = createPortalMapItem(PortalSide.DOWN,PortalColors.RED);
        yellowUpMap = createPortalMapItem(PortalSide.UP,PortalColors.YELLOW);
        yellowDownMap = createPortalMapItem(PortalSide.DOWN,PortalColors.YELLOW);
        purpleUpMap = createPortalMapItem(PortalSide.UP,PortalColors.PURPLE);
        purpleDownMap = createPortalMapItem(PortalSide.DOWN,PortalColors.PURPLE);
        PortalGunMain.getInstance().saveConfig();
    }
    public ItemStack getPortalMapItem(PortalSide side, PortalColors color){
        ItemStack item = null;
        switch (color){
            case BLUE:
                item=side==PortalSide.UP?blueUpMap:blueDownMap;
                break;
            case ORANGE:
                item=side==PortalSide.UP?orangeUpMap:orangeDownMap;
                break;
            case AQUA:
                item=side==PortalSide.UP?aquaUpMap:aquaDownMap;
                break;
            case RED:
                item=side==PortalSide.UP?redUpMap:redDownMap;
                break;
            case YELLOW:
                item=side==PortalSide.UP?yellowUpMap:yellowDownMap;
                break;
            case PURPLE:
                item=side==PortalSide.UP?purpleUpMap:purpleDownMap;
                break;
        }
        return item;
    }

    private ItemStack createPortalMapItem(PortalSide side, PortalColors color){
        ItemStack item = new ItemStack(Material.FILLED_MAP);
        MapMeta mm = (MapMeta)item.getItemMeta();
        int mapid = -1;
        MapView mapView;
        if ((mapid=PortalGunMain.getInstance().getConfig().getInt("PortalMapID."+color.toString().toLowerCase()+"."+side.toString().toLowerCase()))==-1){
            mapView = Bukkit.createMap(Bukkit.getWorlds().get(0));
            PortalGunMain.getInstance().getConfig().set("PortalMapID."+color.toString().toLowerCase()+"."+side.toString().toLowerCase(),mapView.getId());
        }else{
            mapView = Bukkit.getMap(mapid);
        }
        mapView.getRenderers().clear();
        mapView.addRenderer(new PortalRender(side, color));
        mapView.setLocked(true);
        mm.setMapView(mapView);
        mm.setLore(Arrays.asList("Portal"));
        item.setItemMeta(mm);
        return item;
    }

    public PortalGun getPortalGun(ItemStack item) {
        PortalGun portalGun = null;
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData()) {
            NBTTagCompound nbt = new NBTTagCompound(item);
            if (nbt.hasKey("PortalID")&&nbt.hasKey("PortalFileUUID")&&nbt.getString("PortalFileUUID").equalsIgnoreCase(PortalGunManager.getInstance().getPortalFileUUID().toString())) {
                int id = nbt.getInt("PortalID");
                portalGun = PortalGunManager.getInstance().getPortalGun(id);
            }
        }
        return portalGun;
    }

    public PortalGun getPortalGun(Player p, ItemStack item){
        PortalGun portalGun = null;
        if (item!=null&&item.hasItemMeta()&&item.getItemMeta().hasCustomModelData()){
            NBTTagCompound nbt = new NBTTagCompound(item);
            if (nbt.hasKey("PortalID")){
                if (nbt.hasKey("PortalFileUUID")&&!nbt.getString("PortalFileUUID").equalsIgnoreCase(PortalGunManager.getInstance().getPortalFileUUID().toString())){
                    portalGun=PortalGunManager.getInstance().createPortalGun(PortalModel.getPortalModelByMaterial(item.getType()));
                    nbt.setInt("PortalID",portalGun.getId());
                    nbt.setString("PortalFileUUID",PortalGunManager.getInstance().getPortalFileUUID().toString());
                    nbt.save();
                }
                switch (PortalConfig.getInstance().getPortalGunMode()){
                    case INFINITY:
                        int id = nbt.getInt("PortalID");
                        portalGun = PortalGunManager.getInstance().getPortalGun(id);
                        break;
                    case ONE_TYPE_PER_PLAYER:
                        PortalGun pg = null;
                        for (PortalGun playerPortalGun : PortalGunManager.getInstance().getPlayerPortalGuns(p)) {
                            if (playerPortalGun.getPortalModel()==PortalModel.getPortalModelByMaterial(item.getType())){
                                pg = playerPortalGun;
                            }
                        }
                        if (pg==null){
                            portalGun=PortalGunManager.getInstance().createPortalGun(PortalModel.getPortalModelByMaterial(item.getType()));
                            PortalGunManager.getInstance().addPlayerPortalGun(p,portalGun);
                        }else{
                            portalGun = pg;
                        }
                        break;
                    case ONE_PORTAL_PER_PLAYER:
                        try {
                            portalGun=PortalGunManager.getInstance().getPlayerPortalGuns(p).get(0);
                            portalGun.setPortalModel(PortalModel.getPortalModelByMaterial(item.getType()));
                        }catch (Exception e){
                            PortalGunManager.getInstance().addPlayerPortalGun(p,portalGun=PortalGunManager.getInstance().createPortalGun(PortalModel.getPortalModelByMaterial(item.getType())));
                        }
                }
            }
        }
        return portalGun;
    }

    public boolean inBlockList(Block block){
        BoundingBox bb = block.getBoundingBox();
        return bb.getHeight()==1&&bb.getWidthX()==1&&bb.getWidthZ()==1&&inBlockList(block.getType());
    }

    public boolean inBlockList(Material material){
        if (material.isBlock() && material.isSolid()){
            if (PortalGunMain.getInstance().getConfig().getBoolean("WhiteList")){
                return PortalGunMain.getInstance().getConfig().getStringList("WhiteListBlocks").stream().anyMatch(l -> l.equalsIgnoreCase(material.toString()));
            }
            return true;
        }
        return false;
    }

    private final BlockFace[] axis = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

    public BlockFace getCardinalDirection(Location loc){
        return axis[Math.round(loc.getYaw() / 90f) & 0x3].getOppositeFace();
    }

    public Location getLocationJson(JsonObject jsonObject){
        World w = Bukkit.getWorld(jsonObject.get("world").getAsString());
        double x = jsonObject.get("x").getAsDouble();
        double y = jsonObject.get("y").getAsDouble();
        double z = jsonObject.get("z").getAsDouble();
        float yaw = jsonObject.get("yaw").getAsFloat();
        float pitch = jsonObject.get("pitch").getAsFloat();
        return new Location(w,x,y,z,yaw,pitch);
    }

    public JsonObject getJsonLocation(Location loc){
        JsonObject jo = new JsonObject();
        jo.addProperty("world",loc.getWorld().getName());
        jo.addProperty("x",loc.getX());
        jo.addProperty("y",loc.getY());
        jo.addProperty("z",loc.getZ());
        jo.addProperty("yaw",loc.getYaw());
        jo.addProperty("pitch",loc.getPitch());
        return jo;
    }

    public boolean isLocation(Location pos1, Location pos2){
        return pos1.getBlockX()==pos2.getBlockX()&&pos1.getBlockY()==pos2.getBlockY()&&pos1.getBlockZ()==pos2.getBlockZ();
    }

    public void portalTeleport(Entity entity, Location loc, BlockFace face, boolean down){
        Vector vec = entity.getVelocity().clone();
        double vecY = vec.getY();
        if (vecY<0){
            vecY *= -1;
        }
        if (!down){
            int yaw = 0;
            switch (face){
                case EAST:
                    yaw = -90;
                    vec = new Vector(vecY,0,0);
                    break;
                case WEST:
                    yaw = 90;
                    vec = new Vector(-vecY,0,0);
                    break;
                case NORTH:
                    yaw = 180;
                    vec = new Vector(0,0,-vecY);
                    break;
                default:
                    vec = new Vector(0,0,vecY);
                    break;
            }
            loc.setYaw(yaw);
        }else{
            vec = new Vector(0, vecY, 0);
        }
        entity.teleport(loc);
        entity.setVelocity(vec);
    }
}
