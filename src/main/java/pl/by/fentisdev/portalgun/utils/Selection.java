package pl.by.fentisdev.portalgun.utils;

import org.bukkit.Location;
import org.bukkit.World;

public class Selection {
    Location pos1,pos2;

    public Selection() {
    }

    public Selection(Location pos1, Location pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public World getWorld(){
        if (pos1!=null){
            return pos1.getWorld();
        }else if (pos2!=null){
            return pos2.getWorld();
        }
        return null;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public boolean hasCubeSelection(){
        return pos1!=null&&pos2!=null;
    }

    public Location getPosMin(){
        int xmin = Math.min(pos1.getBlockX(),pos2.getBlockX()),
                ymin = Math.min(pos1.getBlockY(),pos2.getBlockY()),
                zmin = Math.min(pos1.getBlockZ(),pos2.getBlockZ());
        return new Location(pos1.getWorld(),xmin,ymin,zmin);
    }

    public Location getPosMax(){
        int xmax = Math.max(pos1.getBlockX(),pos2.getBlockX()),
                ymax = Math.max(pos1.getBlockY(),pos2.getBlockY()),
                zmax = Math.max(pos1.getBlockZ(),pos2.getBlockZ());
        return new Location(pos1.getWorld(),xmax,ymax,zmax);
    }

    public int getXSize(){
        return getPosMax().getBlockX()-getPosMin().getBlockX();
    }

    public int getYSize(){
        return getPosMax().getBlockY()-getPosMin().getBlockY();
    }
    public int getZSize(){
        return getPosMax().getBlockZ()-getPosMin().getBlockZ();
    }

    public boolean inSelection(Location loc){
        Location min = getPosMin();
        Location max = getPosMax();
        return loc.getX()>=min.getX()&&loc.getX()<=max.getX() &&
                loc.getY()>=min.getY()&&loc.getY()<=max.getY() &&
                loc.getZ()>=min.getZ()&&loc.getZ()<=max.getZ();
    }

    public boolean posBlockEquals(){
        return pos1.getBlockX()==pos2.getBlockX()&&pos1.getBlockY()==pos2.getBlockY()&&pos1.getBlockZ()==pos2.getBlockZ();
    }

    public boolean posEquals(){
        return pos1.getX()==pos2.getX()&&pos1.getY()==pos2.getY()&&pos1.getZ()==pos2.getZ();
    }

    public Selection clone(){
        Selection sel = new Selection();
        sel.setPos1(pos1);
        sel.setPos2(pos2);
        return sel;
    }
}