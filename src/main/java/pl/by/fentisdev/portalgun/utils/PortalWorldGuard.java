package pl.by.fentisdev.portalgun.utils;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;

public class PortalWorldGuard {

    private static PortalWorldGuard instance = new PortalWorldGuard();

    public static PortalWorldGuard getInstance() {
        return instance;
    }

    public static BooleanFlag portalOpenFlag;
    public static BooleanFlag portalGunUseFlag;

    public void load(){
        portalOpenFlag = register("portalgun-portal-open");
        portalGunUseFlag = register("portalgun-portalgun-use");
        //System.out.println("PortalGun Plugin Flags registradas com sucesso!");
    }

    public BooleanFlag getPortalOpenFlag() {
        return portalOpenFlag;
    }

    public BooleanFlag getPortalGunUseFlag() {
        return portalGunUseFlag;
    }

    public boolean verify(Player player, Location loc,BooleanFlag flag){
        boolean state = true;
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionManager regions = container.get(lp.getWorld());
        for (Map.Entry<String, ProtectedRegion> regionsMap : regions.getRegions().entrySet()) {
            if (regionsMap.getValue().contains(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ())){
                if (regionsMap.getValue().getFlags().containsKey(flag)){
                    state = regionsMap.getValue().getFlag(flag);
                }
            }
        }
        return state;
    }

    public BooleanFlag register(String flagName){
        BooleanFlag flag = null;
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            flag = new BooleanFlag(flagName, RegionGroup.ALL);
            registry.register(flag);
        } catch (FlagConflictException e) {
            e.printStackTrace();
        }
        return flag;
    }


}
