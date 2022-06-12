package pl.by.fentisdev.portalgun.portalgun;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.util.BoundingBox;
import pl.by.fentisdev.portalgun.utils.PortalUtils;

import java.util.ArrayList;
import java.util.List;

public class Portal {

    private Location loc1,loc2;
    private int chunk1x,chunk1z,chunk2x,chunk2z;
    private PortalColors color;
    private BlockFace face;
    private ItemFrame up;
    private ItemFrame down;
    private BlockFace direction;

    public Portal(PortalColors color){
        this.color = color;
    }

    public void setColor(PortalColors color) {
        this.color = color;
        if (up!=null){
            up.setItem(PortalUtils.getInstance().getPortalMapItem(PortalSide.UP,color));
        }
        if (down!=null){
            down.setItem(PortalUtils.getInstance().getPortalMapItem(PortalSide.DOWN,color));
        }
    }

    public BlockFace getPortalFace() {
        return face;
    }

    public boolean hasPortal(){
        return loc1!=null;
    }

    public Location getLoc1() {
        return loc1;
    }

    public Location getLoc2() {
        return loc2;
    }

    public boolean inChunk(Chunk chunk){
        return (chunk.getX()==chunk1x&&chunk.getZ()==chunk1z) || (chunk.getX()==chunk2x&&chunk.getZ()==chunk2z);
    }

    public Location getLocTeleport(Entity entity){
        BoundingBox box = entity.getBoundingBox();

        if (face== BlockFace.DOWN){
            return loc1.getBlock().getLocation().add(0.5,(-box.getHeight())+0.7,0.5);
        }else if (face== BlockFace.UP){
            return loc1.getBlock().getLocation().add(0.5,0.5,0.5);
        }else{
            if (face == BlockFace.EAST){
                return loc1.getBlock().getLocation().add(box.getWidthX(),0,0.5);
            }
            if (face == BlockFace.WEST){
                return loc1.getBlock().getLocation().add(1+(-box.getWidthX()),0,0.5);
            }
            if (face == BlockFace.SOUTH){
                return loc1.getBlock().getLocation().add(0.5,0,box.getWidthZ());
            }
            if (face == BlockFace.NORTH){
                return loc1.getBlock().getLocation().add(0.5,0,1+(-(box.getWidthZ())));
            }
            return loc1.getBlock().getLocation().add(0.5,0.0,0.5);
        }
    }

    public Location getLocTeleport(){
        if (face== BlockFace.DOWN){
            return loc1.getBlock().getLocation().add(0.5,-1,0.5);
        }else if (face== BlockFace.UP){
            return loc1.getBlock().getLocation().add(0.5,0.5,0.5);
        }else{
            return loc1.getBlock().getLocation().add(0.5,0.0,0.5);
        }
    }

    public boolean isPortalLocation(Location loc){
        return loc1!=null&&(loc.getBlock().getLocation().distance(loc1.getBlock().getLocation())<1 ||
                loc.getBlock().getLocation().distance(loc2.getBlock().getLocation())<1);
    }

    public boolean isPortalLocation(Location loc, BlockFace face){
        return up!=null&&down!=null&&loc1!=null&&loc2!=null&&
                ((loc.getWorld()==loc1.getWorld()&&(loc.getBlock().getLocation().distance(loc1.getBlock().getLocation())<1 && down.getFacing()==face)) ||
                        (loc.getWorld()==loc2.getWorld()&&(loc.getBlock().getLocation().distance(loc2.getBlock().getLocation())<1 && up.getFacing()==face)));
    }

    public boolean isPortalItemFrame(ItemFrame itemFrame){
        return isPortalItemFrameUp(itemFrame) ||
                isPortalItemFrameDown(itemFrame);
    }

    public boolean isPortalItemFrameUp(ItemFrame itemFrame){
        return up!=null&&itemFrame.getUniqueId()==up.getUniqueId();
    }

    public boolean isPortalItemFrameDown(ItemFrame itemFrame){
        return down!=null&&itemFrame.getUniqueId()==down.getUniqueId();
    }

    public ItemFrame getUp() {
        return up;
    }

    public ItemFrame getDown() {
        return down;
    }

    public List<Entity> getEntityNearby(){
        List<Entity> en = new ArrayList<>();
        double x = face== BlockFace.EAST||face== BlockFace.WEST?0.045:0.1;
        double y = face== BlockFace.UP||face== BlockFace.DOWN?0.045:0.1;
        double z = face== BlockFace.SOUTH||face== BlockFace.NORTH?0.034:0.1;
        if (up!=null){
            for (Entity nearbyEntity : up.getNearbyEntities(x, y, z)) {
                if (nearbyEntity instanceof LivingEntity || nearbyEntity instanceof Item){
                    en.add(nearbyEntity);
                }
            }
        }
        if (down!=null){
            for (Entity nearbyEntity : down.getNearbyEntities(x, y, z)) {
                if ((nearbyEntity instanceof LivingEntity || nearbyEntity instanceof Item) && !en.contains(nearbyEntity)){
                    en.add(nearbyEntity);
                }
            }
        }
        return en;
    }

    public void resetPortal(){
        clearPortal();
        this.loc1=null;
        this.loc2=null;
        this.face=null;
        this.direction=null;
    }

    public void setPortal(Location loc1, Location loc2, BlockFace face, BlockFace direction){
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.chunk1x = loc1.getChunk().getX();
        this.chunk1z = loc1.getChunk().getZ();
        this.chunk2x = loc2.getChunk().getX();
        this.chunk2z = loc2.getChunk().getZ();
        this.face = face;
        this.direction = direction;
        renderPortal();
    }

    public void renderPortal(){
        clearPortal();
        color.getTeleportSound().playSound(loc1,1,1);
        Rotation rotation = face== BlockFace.DOWN?(direction==BlockFace.SOUTH? Rotation.CLOCKWISE:direction==BlockFace.WEST? Rotation.CLOCKWISE_45:direction==BlockFace.EAST? Rotation.CLOCKWISE_135: Rotation.NONE):
                face== BlockFace.UP?(direction==BlockFace.SOUTH? Rotation.CLOCKWISE:direction==BlockFace.WEST? Rotation.CLOCKWISE_135:direction==BlockFace.EAST? Rotation.CLOCKWISE_45: Rotation.NONE): Rotation.NONE;
        try {
            Class<? extends ItemFrame> c;
            Class<? extends ItemFrame> c2;

            if (PortalUtils.getInstance().isGlowItemFrame()){
                c=GlowItemFrame.class;
                c2=GlowItemFrame.class;
            }else{
                c=ItemFrame.class;
                c2=ItemFrame.class;
            }
            down = loc1.getWorld().spawn(loc1, c);
            down.setFacingDirection(face);
            down.setRotation(rotation);
            down.setItem(PortalUtils.getInstance().getPortalMapItem(PortalSide.DOWN,color));
            down.setInvulnerable(true);

            up = loc2.getWorld().spawn(loc2, c2);
            up.setFacingDirection(face);
            up.setRotation(rotation);
            up.setItem(PortalUtils.getInstance().getPortalMapItem(PortalSide.UP,color));
            up.setInvulnerable(true);

            if (PortalUtils.getInstance().isInvisibleItemFrame()){
                down.setVisible(false);
                up.setVisible(false);
            }
        } catch (IllegalArgumentException e) {
            resetPortal();
        }
    }

    public void unRenderPortal(){
        clearPortal();
    }

    private void clearPortal(){
        if (up!=null){
            PortalSound.PORTAL_CLOSE.playSound(up.getLocation(),1,1);
            up.remove();
            up=null;
        }
        if (down!=null){
            down.remove();
            down=null;
        }
    }

    public JsonObject toJson(){
        JsonObject json = new JsonObject();
        if (loc1!=null){
            json.add("loc1",PortalUtils.getInstance().getJsonLocation(loc1));
        }
        if (loc2!=null){
            json.add("loc2",PortalUtils.getInstance().getJsonLocation(loc2));
        }
        if (face!=null){
            json.addProperty("face",face.toString());
        }
        if (direction!=null){
            json.addProperty("direction",direction.toString());
        }
        return json;
    }
}
