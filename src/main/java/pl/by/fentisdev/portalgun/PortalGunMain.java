package pl.by.fentisdev.portalgun;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pl.by.fentisdev.itemcreator.DependenciesItemCreator;
import pl.by.fentisdev.portalgun.cmd.PortalCMD;
import pl.by.fentisdev.portalgun.listeners.GriefPreventionListeners;
import pl.by.fentisdev.portalgun.listeners.PortalListeners;
import pl.by.fentisdev.portalgun.listeners.WorldGuardListeners;
import pl.by.fentisdev.portalgun.portalgun.PortalGun;
import pl.by.fentisdev.portalgun.portalgun.PortalGunManager;
import pl.by.fentisdev.portalgun.portalgun.PortalModel;
import pl.by.fentisdev.portalgun.utils.*;

import java.util.List;

public class PortalGunMain extends JavaPlugin {

    private static PortalGunMain instance;

    @Override
    public void onLoad() {
        instance = this;
        new DependenciesItemCreator(this);
        registryWorldGuardFlags();
    }

    @Override
    public void onEnable() {
        instance = this;
        HoldingFile.getInstance().readHoldings();
        Bukkit.getPluginManager().registerEvents(new PortalListeners(),this);
        getCommand("portalgun").setExecutor(new PortalCMD());
        PortalConfig.getInstance().createConfig();
        registryCraft();

        registryWorldGuard();
        registryGriefPrevention();
        PortalGunManager.getInstance().registryPortals();
        PortalGunManager.getInstance().startPortalScheduler();
        Metrics metrics = new Metrics(PortalGunMain.getInstance(),15397);
        updateChecker();
    }

    @Override
    public void onDisable() {
        PortalGunManager.getInstance().savePortals();
        PortalGunManager.getInstance().stopPortalScheduler();
        for (PortalGun pg : PortalGunManager.getInstance().getPortalGuns()){
            pg.resetPortals();
        }
        for (Entity entity : PortalGunManager.getInstance().getHolding().values()) {
            if (entity!=null){
                entity.setGravity(true);
            }
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

    public void registryGriefPrevention(){
        Plugin plugin;
        if ((plugin=Bukkit.getPluginManager().getPlugin("GriefPrevention"))!=null&&plugin.isEnabled()){
            Bukkit.getPluginManager().registerEvents(new GriefPreventionListeners(),this);
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

    public void updateChecker(){
        new UpdateChecker(this, 96641).getVersion(version -> {
            Bukkit.getConsoleSender().sendMessage("§eChecking for updates to [Portal Gun]...");
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                Bukkit.getConsoleSender().sendMessage("§aThe plugin is already in the most updated version!");
            } else {
                Bukkit.getConsoleSender().sendMessage("§cA new version is available! You can download it at https://www.spigotmc.org/resources/portal-gun.96641/");
            }
        });
    }
}
