package pl.by.fentisdev.portalgun;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pl.by.fentisdev.portalgun.cmd.PortalCMD;
import pl.by.fentisdev.portalgun.listeners.PortalListeners;
import pl.by.fentisdev.portalgun.listeners.WorldGuardListeners;
import pl.by.fentisdev.portalgun.portalgun.*;
import pl.by.fentisdev.portalgun.utils.PortalConfig;
import pl.by.fentisdev.portalgun.utils.PortalWorldGuard;
import pl.by.fentisdev.portalgun.utils.RecipeCreator;

import java.util.List;

public class PortalGunMain extends JavaPlugin {

    private static PortalGunMain instance;

    @Override
    public void onLoad() {
        registryWorldGuardFlags();
    }

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new PortalListeners(),this);
        getCommand("portal").setExecutor(new PortalCMD());
        PortalConfig.getInstance().createConfig();
        registryCraft();
        registryWorldGuard();
        PortalGunManager.getInstance().registryPortals();
        PortalGunManager.getInstance().startPortalScheduler();
        /*bukkitRunnable = new BukkitRunnable(){
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size()==0||getPortalGuns().size()==0){
                    return;
                }
                //List<PortalGun> pg = getPortalGuns().stream().filter(PortalGun::isOnline).collect(Collectors.toList());
                for (PortalGun portalGun : getPortalGuns().stream().filter(PortalGun::isOnline).collect(Collectors.toList())) {

                }
                getPortalGuns().forEach(portalGun -> {
                    if (portalGun.isOnline()){
                        for (Entity entity : portalGun.getPortal1().getEntityNearby()) {
                            Location nloc = portalGun.getPortal2().getLocTeleport().clone();
                            nloc.setYaw(entity.getLocation().getYaw());
                            nloc.setPitch(entity.getLocation().getPitch());
                            PortalSound.PORTAL_ENTER.playSound(entity.getLocation(),1,1);
                            teleport(entity,nloc,portalGun.getPortal2().getPortalFace(),portalGun.getPortal2().getPortalFace()==BlockFace.UP, PortalUtils.getInstance().getCardinalDirection(entity));
                            PortalSound.PORTAL_EXIT.playSound(entity.getLocation(),1,1);
                        }
                        for (Entity entity : portalGun.getPortal2().getEntityNearby()) {
                            Location nloc = portalGun.getPortal1().getLocTeleport().clone();
                            nloc.setYaw(entity.getLocation().getYaw());
                            nloc.setPitch(entity.getLocation().getPitch());
                            PortalSound.PORTAL_ENTER.playSound(entity.getLocation(),1,1);
                            teleport(entity,nloc,portalGun.getPortal1().getPortalFace(), portalGun.getPortal1().getPortalFace()==BlockFace.UP, PortalUtils.getInstance().getCardinalDirection(entity));
                            PortalSound.PORTAL_EXIT.playSound(entity.getLocation(),1,1);
                        }
                    }
                });
                for (PortalGun portalGun : getPortalGuns()) {

                }
            }
        }.runTaskTimer(this,0,10);*/
    }

    @Override
    public void onDisable() {
        PortalGunManager.getInstance().savePortals();
        PortalGunManager.getInstance().stopPortalScheduler();
        for (PortalGun pg : PortalGunManager.getInstance().getPortalGuns()){
            pg.resetPortals();
        }
    }

    public static PortalGunMain getInstance() {
        return instance;
    }

    public void registryWorldGuardFlags(){
        if ((Bukkit.getPluginManager().getPlugin("WorldGuard"))!=null){
            PortalWorldGuard.getInstance().load();
        }
    }

    public void registryWorldGuard(){
        Plugin plugin;
        if ((plugin=Bukkit.getPluginManager().getPlugin("WorldGuard"))!=null&&plugin.isEnabled()){
            Bukkit.getPluginManager().registerEvents(new WorldGuardListeners(),this);
        }
    }

    public void registryCraft(){
        if (getConfig().getBoolean("PortalCraftable")){
            if (getConfig().getBoolean("PortalGunCrafts."+ PortalModel.CHELL.toString().toLowerCase()+".Craft")){
                registryPortalRecipe(PortalModel.CHELL);
            }
            if (getConfig().getBoolean("PortalGunCrafts."+ PortalModel.ATLAS.toString().toLowerCase()+".Craft")){
                registryPortalRecipe(PortalModel.ATLAS);
            }
            if (getConfig().getBoolean("PortalGunCrafts."+ PortalModel.P_BODY.toString().toLowerCase()+".Craft")){
                registryPortalRecipe(PortalModel.P_BODY);
            }
            if (getConfig().getBoolean("PortalGunCrafts."+ PortalModel.POTATOS.toString().toLowerCase()+".Craft")){
                registryPortalRecipe(PortalModel.POTATOS);
            }
            //new RecipeCreator("portalgun_chell", PortalOwner.CHELL.createItem()).addShape("AIG","WNI","WWA").setIngredient('I', Material.IRON_INGOT).setIngredient('G', Material.GLASS_PANE).setIngredient('W',Material.WHITE_CONCRETE).setIngredient('N',Material.NETHER_STAR).addRecipe();
            //new RecipeCreator("portalgun_atlas", PortalOwner.ATLAS.createItem()).addShape("AIG","YNI","WWA").setIngredient('I', Material.IRON_INGOT).setIngredient('G', Material.GLASS_PANE).setIngredient('W',Material.WHITE_CONCRETE).setIngredient('Y',Material.YELLOW_CONCRETE).setIngredient('N',Material.NETHER_STAR).addRecipe();
            //new RecipeCreator("portalgun_p_body", PortalOwner.P_BODY.createItem()).addShape("AIG","LNI","WWA").setIngredient('I', Material.IRON_INGOT).setIngredient('G', Material.GLASS_PANE).setIngredient('W',Material.WHITE_CONCRETE).setIngredient('L',Material.LIGHT_BLUE_CONCRETE).setIngredient('N',Material.NETHER_STAR).addRecipe();
            //new RecipeCreator("portalgun_potatos", PortalOwner.POTATOS.createItem()).addShape("PIG","WNI","WWA").setIngredient('I', Material.IRON_INGOT).setIngredient('G', Material.GLASS_PANE).setIngredient('W',Material.WHITE_CONCRETE).setIngredient('P',Material.POTATO).setIngredient('N',Material.NETHER_STAR).addRecipe();
        }
    }

    public void registryPortalRecipe(PortalModel po){
        List<String> shape = getConfig().getStringList("PortalGunCrafts."+po.toString().toLowerCase()+".Shape");
        List<String> ingredients = getConfig().getStringList("PortalGunCrafts."+po.toString().toLowerCase()+".Ingredients");
        RecipeCreator rc = new RecipeCreator("portalgun_"+po.toString().toLowerCase(),po.createItem());
        rc.addShape(shape.toArray(new String[0]));
        for (String ingredient : ingredients) {
            String[] split = ingredient.split(":");
            char c = split[0].toCharArray()[0];
            Material m = Material.getMaterial(split[1]);
            rc.setIngredient(c,m);
        }
        rc.addRecipe();
    }
}
