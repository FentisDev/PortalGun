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
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;

public class PortalWorldGuard {

    @Getter
    private static PortalWorldGuard instance = new PortalWorldGuard();
    @Getter
    public static BooleanFlag portalOpenFlag;
    @Getter
    public static BooleanFlag portalGunUseFlag;
    @Getter
    public static BooleanFlag portalGunGrabFlag;

    public void load(){
        portalOpenFlag = register("portalgun-portal-open");
        portalGunUseFlag = register("portalgun-portalgun-use");
        portalGunGrabFlag = register("portalgun-portalgun-grab");
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
