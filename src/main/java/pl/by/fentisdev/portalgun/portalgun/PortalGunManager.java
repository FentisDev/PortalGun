package pl.by.fentisdev.portalgun.portalgun;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import pl.by.fentisdev.portalgun.PortalGunMain;
import pl.by.fentisdev.portalgun.events.EntityTeleportInPortalEvent;
import pl.by.fentisdev.portalgun.utils.PortalConfig;
import pl.by.fentisdev.portalgun.utils.PortalUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PortalGunManager {

    private static PortalGunManager instance = new PortalGunManager();

    public static PortalGunManager getInstance() {
        return instance;
    }

    private UUID portalFileUUID = UUID.randomUUID();
    private int portalScheduler;
    private List<PortalGun> portalGuns = new ArrayList<>();
    private JsonObject portal_players = new JsonObject();

    public UUID getPortalFileUUID() {
        return portalFileUUID;
    }

    public List<PortalGun> getPortalGuns() {
        return portalGuns;
    }

    public PortalGun getPortalGun(int id){
        return getPortalGuns().stream().filter(p -> p.getId()==id).findAny().orElse(null);
    }

    public PortalGun getPortalGun(ItemFrame itemFrame){
        return getPortalGuns().stream().filter(p -> (p.getPortal1()!=null&&p.getPortal1().isPortalItemFrame(itemFrame))||(p.getPortal2()!=null&&p.getPortal2().isPortalItemFrame(itemFrame))).findAny().orElse(null);
    }

    public void addPortalGun(PortalGun pg){
        portalGuns.add(pg);
    }

    public void removePortalGun(PortalGun pg){
        portalGuns.remove(pg);
    }

    public PortalGun createPortalGun(PortalModel portalModel){
        PortalGun portalGun = new PortalGun(getNextPortalGunId(),portalModel);
        addPortalGun(portalGun);
        return portalGun;
    }

    public int getNextPortalGunId(){
        int v = 0;
        for (PortalGun portalGun : getPortalGuns()) {
            if (portalGun.getId()>v){
                v=portalGun.getId();
            }
        }
        return v+1;
    }

    public List<PortalGun> getPlayerPortalGuns(Player p){
        List<PortalGun> pg = new ArrayList<>();
        JsonObject jsonPlayer = new JsonObject();
        if (portal_players.has(p.getUniqueId().toString())){
            jsonPlayer = portal_players.getAsJsonObject(p.getUniqueId().toString());
        }
        if (jsonPlayer.has("PortalGunsId")){
            for (JsonElement portalGunsId : jsonPlayer.getAsJsonArray("PortalGunsId")) {
                pg.add(getPortalGun(portalGunsId.getAsInt()));
            }
        }
        return pg;
    }

    public void setPlayerPortalGuns(Player p, List<PortalGun> portalGuns){
        JsonObject jsonPlayer = new JsonObject();
        if (portal_players.has(p.getUniqueId().toString())){
            jsonPlayer = portal_players.getAsJsonObject(p.getUniqueId().toString());
        }
        jsonPlayer.add("PortalGunsId",new GsonBuilder().create().toJsonTree(portalGuns.stream().map(PortalGun::getId).collect(Collectors.toList())));
        portal_players.add(p.getUniqueId().toString(),jsonPlayer);
    }

    public void addPlayerPortalGun(Player p, PortalGun portalGun){
        JsonObject jsonPlayer = new JsonObject();
        if (portal_players.has(p.getUniqueId().toString())){
            jsonPlayer = portal_players.getAsJsonObject(p.getUniqueId().toString());
        }
        JsonArray portalList = new JsonArray();
        if (jsonPlayer.has("PortalGunsId")){
            portalList = jsonPlayer.getAsJsonArray("PortalGunsId");
        }
        portalList.add(portalGun.getId());
        jsonPlayer.add("PortalGunsId",portalList);
        portal_players.add(p.getUniqueId().toString(),jsonPlayer);
    }

    public void removePlayerPortalGun(Player p, PortalGun portalGun){
        JsonObject jsonPlayer = new JsonObject();
        if (portal_players.has(p.getUniqueId().toString())){
            jsonPlayer = portal_players.getAsJsonObject(p.getUniqueId().toString());
        }
        JsonArray portalList = new JsonArray();
        if (jsonPlayer.has("PortalGunsId")){
            portalList = jsonPlayer.getAsJsonArray("PortalGunsId");
        }
        JsonArray newPortalList = new JsonArray();
        for (JsonElement jsonElement : portalList) {
            if (jsonElement.getAsInt()!=portalGun.getId()){
                newPortalList.add(jsonElement.getAsInt());
            }
        }
        jsonPlayer.add("PortalGunsId",newPortalList);
        portal_players.add(p.getUniqueId().toString(),jsonPlayer);
    }

    public void startPortalScheduler(){
        portalScheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(PortalGunMain.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size()==0||getPortalGuns().size()==0){
                    return;
                }
                getPortalGuns().forEach(portalGun -> {
                    if (portalGun.isOnline()){
                        if (!PortalConfig.getInstance().isInterdimensional()&&portalGun.getPortal1().getLoc1().getWorld()!=portalGun.getPortal2().getLoc1().getWorld()){
                            return;
                        }
                        for (Entity entity : portalGun.getPortal1().getEntityNearby()) {
                            EntityTeleportInPortalEvent event = new EntityTeleportInPortalEvent(portalGun,portalGun.getPortal1(),portalGun.getPortal2(),entity);
                            if (!event.isCancelled()){
                                Location nloc = portalGun.getPortal2().getLocTeleport(entity).clone();
                                nloc.setYaw(entity.getLocation().getYaw());
                                nloc.setPitch(entity.getLocation().getPitch());
                                PortalSound.PORTAL_ENTER.playSound(entity.getLocation(),1,1);
                                PortalUtils.getInstance().portalTeleport(entity,nloc,portalGun.getPortal2().getPortalFace(),portalGun.getPortal2().getPortalFace()==BlockFace.UP);
                                PortalSound.PORTAL_EXIT.playSound(entity.getLocation(),1,1);
                            }
                        }
                        for (Entity entity : portalGun.getPortal2().getEntityNearby()) {
                            EntityTeleportInPortalEvent event = new EntityTeleportInPortalEvent(portalGun,portalGun.getPortal2(),portalGun.getPortal1(),entity);
                            if (!event.isCancelled()){
                                Location nloc = portalGun.getPortal1().getLocTeleport(entity).clone();
                                nloc.setYaw(entity.getLocation().getYaw());
                                nloc.setPitch(entity.getLocation().getPitch());
                                PortalSound.PORTAL_ENTER.playSound(entity.getLocation(),1,1);
                                PortalUtils.getInstance().portalTeleport(entity,nloc,portalGun.getPortal1().getPortalFace(), portalGun.getPortal1().getPortalFace()==BlockFace.UP);
                                PortalSound.PORTAL_EXIT.playSound(entity.getLocation(),1,1);
                            }
                        }
                    }
                });
            }
        },0,5);
    }

    public void stopPortalScheduler(){
        Bukkit.getScheduler().cancelTask(portalScheduler);
    }

    public void savePortals(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("PortalFileUUID",portalFileUUID.toString());
        JsonObject portal_guns = new JsonObject();
        for (PortalGun portalGun : getPortalGuns()) {
            JsonObject po = new JsonObject();
            po.addProperty("PortalModel",portalGun.getPortalModel().toString().toLowerCase());
            po.add("Portal1",portalGun.getPortal1().toJson());
            po.add("Portal2",portalGun.getPortal2().toJson());
            portal_guns.add(""+portalGun.getId(),po);
        }
        jsonObject.add("PortalGuns",portal_guns);
        jsonObject.add("PortalPlayers",portal_players);
        try {
            File file = new File(PortalGunMain.getInstance().getDataFolder(),"portals.json");
            FileWriter writer = new FileWriter(file);
            writer.write(jsonObject.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registryPortals(){
        File file = new File(PortalGunMain.getInstance().getDataFolder(),"portals.json");
        if (!file.exists()){
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        try {
            JsonElement jsonElement = new JsonParser().parse(new FileReader(file));
            JsonObject jsonObject = null;
            try {
                jsonObject = (JsonObject) jsonElement;
            } catch (Exception e) {

            }
            if (jsonObject != null) {
                portalFileUUID = UUID.fromString(jsonObject.get("PortalFileUUID").getAsString());
                for (Map.Entry<String, JsonElement> guns : jsonObject.getAsJsonObject("PortalGuns").entrySet()) {
                    int id = Integer.parseInt(guns.getKey());
                    JsonObject portalguninfo = (JsonObject) guns.getValue();
                    PortalModel po = PortalModel.valueOf(portalguninfo.get("PortalModel").getAsString().toUpperCase());
                    JsonObject portal1 = portalguninfo.getAsJsonObject("Portal1");
                    JsonObject portal2 = portalguninfo.getAsJsonObject("Portal2");
                    Portal p1 = new Portal(po.getPortalColor1());
                    if (portal1.has("loc1")){
                        p1.setPortal(PortalUtils.getInstance().getLocationJson(portal1.getAsJsonObject("loc1")),
                                PortalUtils.getInstance().getLocationJson(portal1.getAsJsonObject("loc2")),
                                BlockFace.valueOf(portal1.get("face").getAsString()),
                                BlockFace.valueOf(portal1.get("direction").getAsString()));
                    }
                    Portal p2 = new Portal(po.getPortalColor2());
                    if (portal2.has("loc1")){
                        p2.setPortal(PortalUtils.getInstance().getLocationJson(portal2.getAsJsonObject("loc1")),
                                PortalUtils.getInstance().getLocationJson(portal2.getAsJsonObject("loc2")),
                                BlockFace.valueOf(portal2.get("face").getAsString()),
                                BlockFace.valueOf(portal2.get("direction").getAsString()));
                    }
                    addPortalGun(new PortalGun(id,po,p1,p2));
                }
                portal_players = jsonObject.getAsJsonObject("PortalPlayers");
            }
        }catch (FileNotFoundException e){

        }
    }
}
