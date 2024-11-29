package pl.by.fentisdev.portalgun.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import pl.by.fentisdev.inventorygui.*;
import pl.by.fentisdev.itemcreator.ItemCreator;
import pl.by.fentisdev.portalgun.PortalGunMain;
import pl.by.fentisdev.portalgun.portalgun.PortalModel;

import java.util.*;

public class PortalConfigGui {
    @Getter
    private static PortalConfigGui instance = new PortalConfigGui();

    private InventoryGUI gui,guiWhitelist,guiRecipe;

    public void setup(){
        if (gui==null){
            gui = new InventoryGUI("PortalGun Config", 3, new InventoryClickEventHandler() {
                @Override
                public void onInventoryClick(InventoryGUIClickEvent e) {
                    if (e.guiInventory()){
                        e.setCancelled(true);
                        switch (e.getSlot()){
                            case 10:
                                PortalConfig.getInstance().setInterdimensional(!PortalConfig.getInstance().isInterdimensional());
                                update();
                                break;
                            case 11:
                                PortalConfig.getInstance().setCanGrabEntity(!PortalConfig.getInstance().canGrabEntity());
                                update();
                                break;
                            case 12:
                                openWhitelist(e.getPlayer());
                                break;
                            case 13:
                                int sr = PortalConfig.getInstance().getPortalShootRange();
                                switch (e.getClickType()){
                                    case RIGHT:
                                        sr++;
                                        break;
                                    case LEFT:
                                        sr--;
                                        break;
                                    case SHIFT_RIGHT:
                                        sr=sr+10;
                                        break;
                                    case SHIFT_LEFT:
                                        sr=sr-10;
                                }
                                PortalConfig.getInstance().setPortalShootRange(Math.max(sr, 1));
                                update();
                                break;
                            case 14:
                                openPortalGunSelector(e.getPlayer());
                                break;
                            case 15:
                                PortalConfig.getInstance().setCameraChangeDirection(!PortalConfig.getInstance().cameraChangeDirection());
                                update();
                                break;
                        }
                    }
                }

                @Override
                public void onInventoryDrag(InventoryGUIDragEvent e) {
                    e.setCancelled(true);
                }

                @Override
                public void onInventoryClose(InventoryGUICloseEvent e) {
                    e.getPlayer().sendMessage("§aSaving PortalGun Config.");
                    PortalConfig.getInstance().saveConfig();
                }
            }, PortalGunMain.getInstance());
            gui.setDestroyOnClose(false);
        }

        if (guiRecipe==null){
            guiRecipe = new InventoryGUI("PortalGun Recipe", 3, new InventoryClickEventHandler() {
                @Override
                public void onInventoryClick(InventoryGUIClickEvent e) {
                    if (e.guiInventory()){
                        if (e.getSlot()<2||
                                e.getSlot()>4&&e.getSlot()<11||
                                e.getSlot()>13&&e.getSlot()<20||
                                e.getSlot()>22){
                            e.setCancelled(true);
                            if (e.getSlot()==0){
                                PortalModel pm = PortalModel.getPortalModelByItem(e.getInventoryGUI().getItem(15));
                                PortalConfig.getInstance().setCanCraft(pm,!PortalConfig.getInstance().canCraft(pm));
                                guiRecipe.setItem(0,PortalConfig.getInstance().canCraft(pm)?new ItemCreator(Material.GREEN_WOOL).setDisplayName("§aCan Craft"):new ItemCreator(Material.RED_WOOL).setDisplayName("§cCan Craft"));
                            }
                        }else{
                            e.setCancelled(true);
                            if (e.getItemCursor()!=null&&e.getItemCursor().getMaterial()!=Material.AIR){
                                e.getInventoryGUI().setItem(e.getSlot(),e.getItemCursor().setAmount(1));
                            }else {
                                if (e.getItem() != null && e.getItem().getMaterial() != Material.AIR) {
                                    e.getInventoryGUI().removeItem(e.getSlot());
                                }
                            }
                        }
                    }
                }

                @Override
                public void onInventoryDrag(InventoryGUIDragEvent e) {
                    e.setCancelled(true);
                }

                @Override
                public void onInventoryClose(InventoryGUICloseEvent e) {
                    e.getPlayer().sendMessage("§aSaving PortalGun Recipe.\n§cRestart required!");
                    List<Material> materialList = new ArrayList<>();
                    Map<Material,Character> materialMap = new HashMap<>();
                    ItemStack item = null;
                    materialList.add((item=e.getInventory().getItem(2))!=null?item.getType():Material.AIR);
                    materialList.add((item=e.getInventory().getItem(3))!=null?item.getType():Material.AIR);
                    materialList.add((item=e.getInventory().getItem(4))!=null?item.getType():Material.AIR);
                    materialList.add((item=e.getInventory().getItem(11))!=null?item.getType():Material.AIR);
                    materialList.add((item=e.getInventory().getItem(12))!=null?item.getType():Material.AIR);
                    materialList.add((item=e.getInventory().getItem(13))!=null?item.getType():Material.AIR);
                    materialList.add((item=e.getInventory().getItem(20))!=null?item.getType():Material.AIR);
                    materialList.add((item=e.getInventory().getItem(21))!=null?item.getType():Material.AIR);
                    materialList.add((item=e.getInventory().getItem(22))!=null?item.getType():Material.AIR);
                    char[] abc = {'A','B','C','D','E','F','G','H','J'};
                    int v = 0;
                    for (Material m : materialList) {
                        if (!materialMap.containsKey(m)){
                            materialMap.put(m,abc[v++]);
                        }
                    }
                    List<String> shape = new ArrayList<>();
                    v=0;
                    shape.add(""+materialMap.get(materialList.get(v++))+materialMap.get(materialList.get(v++))+materialMap.get(materialList.get(v++)));
                    shape.add(""+materialMap.get(materialList.get(v++))+materialMap.get(materialList.get(v++))+materialMap.get(materialList.get(v++)));
                    shape.add(""+materialMap.get(materialList.get(v++))+materialMap.get(materialList.get(v++))+materialMap.get(materialList.get(v++)));
                    List<String> ingredients = new ArrayList<>();
                    for (Map.Entry<Material, Character> mc : materialMap.entrySet()) {
                        if (mc.getKey()!=Material.AIR){
                            ingredients.add(""+mc.getValue()+":"+mc.getKey().toString());
                        }
                    }
                    PortalModel pm = PortalModel.getPortalModelByItem(e.getInventoryGUI().getItem(15));
                    PortalConfig.getInstance().setRecipe(pm,shape,ingredients);
                    Bukkit.getScheduler().runTaskLater(PortalGunMain.getInstance(), r->{
                        open(e.getPlayer());
                    },1);
                }
            }, PortalGunMain.getInstance());
            guiRecipe.setDestroyOnClose(false);
        }

        if (guiWhitelist==null){
            guiWhitelist = new InventoryGUI("Whitelist Blocks", 6, new InventoryClickEventHandler() {
                @Override
                public void onInventoryClick(InventoryGUIClickEvent e) {
                    if (e.guiInventory()){
                        e.setCancelled(true);
                        if (e.getSlot()==0)
                            PortalConfig.getInstance().setWhiteList(!PortalConfig.getInstance().whiteList());
                        else
                        if (e.getSlot()>8){
                            List<Material> list = PortalConfig.getInstance().getWhiteListBlocks();
                            if (e.getItemCursor()!=null&&e.getItemCursor().getMaterial()!=Material.AIR&&
                                    !list.contains(e.getItemCursor().getMaterial())&&
                                    e.getItemCursor().getMaterial().isBlock()&&
                                    e.getItemCursor().getMaterial().isSolid()){
                                list.add(e.getItemCursor().getMaterial());
                                PortalConfig.getInstance().setWhiteListBlocks(list);
                            }else{
                                if (e.getItem()!=null&&e.getItem().getMaterial()!=Material.AIR){
                                    if (list.contains(e.getItem().getMaterial())){
                                        list.remove(e.getItem().getMaterial());
                                        PortalConfig.getInstance().setWhiteListBlocks(list);
                                    }
                                }
                            }
                        }
                        updateWhitelist();
                    }
                }

                @Override
                public void onInventoryDrag(InventoryGUIDragEvent e) {
                    e.setCancelled(true);
                }

                @Override
                public void onInventoryClose(InventoryGUICloseEvent e) {
                    e.getPlayer().sendMessage("§aSaving WhitelistBlocks");
                    Bukkit.getScheduler().runTaskLater(PortalGunMain.getInstance(), r->{
                        open(e.getPlayer());
                    },1);
                }
            },PortalGunMain.getInstance());
            guiWhitelist.setDestroyOnClose(false);
        }
    }

    public void update(){
        if (gui==null){
            setup();
        }
        gui.clear();
        for (int i = 0; i < 3*9; i++) {
            gui.setItem(i,new ItemCreator(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("§a"));
        }
        gui.setItem(10,PortalConfig.getInstance().isInterdimensional()?new ItemCreator(Material.GREEN_WOOL).setDisplayName("§aInterdimensional"):new ItemCreator(Material.RED_WOOL).setDisplayName("§cInterdimensional"));
        gui.setItem(11,PortalConfig.getInstance().canGrabEntity()?new ItemCreator(Material.GREEN_WOOL).setDisplayName("§aCan Grab Entities"):new ItemCreator(Material.RED_WOOL).setDisplayName("§cCan Grab Entities"));
        gui.setItem(12,new ItemCreator(Material.FILLED_MAP).setDisplayName("§aWhitelist Blocks"));
        gui.setItem(13,new ItemCreator(Material.COMPASS).setDisplayName("§aShoot Range: <"+PortalConfig.getInstance().getPortalShootRange()+">"));
        gui.setItem(14,new ItemCreator(Material.CRAFTING_TABLE).setDisplayName("§aRecipes").setLore("§cRestart required for apply any changes"));
        gui.setItem(15,PortalConfig.getInstance().cameraChangeDirection()?new ItemCreator(Material.GREEN_WOOL).setDisplayName("§aCamera Change Direction"):new ItemCreator(Material.RED_WOOL).setDisplayName("§cCamera Change Direction"));
    }

    public void updatePortalGunRecipe(PortalModel po){
        if (guiRecipe==null){
            setup();
        }
        guiRecipe.clear();
        for (int i = 0; i < 3*9; i++) {
            guiRecipe.setItem(i,new ItemCreator(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("§a"));
        }
        guiRecipe.setItem(0,PortalConfig.getInstance().canCraft(po)?new ItemCreator(Material.GREEN_WOOL).setDisplayName("§aCan Craft"):new ItemCreator(Material.RED_WOOL).setDisplayName("§cCan Craft"));
        List<String> shape = PortalGunMain.getInstance().getConfig().getStringList("PortalGunCrafts."+po.toString().toLowerCase()+".Shape");
        HashMap<Character,Material> ingredients = new HashMap<>();
        for (String ingredient : PortalGunMain.getInstance().getConfig().getStringList("PortalGunCrafts."+po.toString().toLowerCase()+".Ingredients")) {
            String[] split = ingredient.split(":");
            char c = split[0].toCharArray()[0];
            Material m = Material.getMaterial(split[1]);
            ingredients.put(c,m);
        }
        int r = 0;
        for (String s : shape) {
            char[] ca = s.toCharArray();
            int sl = 0;
            for (char c : ca) {
                Material m = null;
                if ((m=ingredients.get(c))!=null){
                    guiRecipe.setItem(2+sl+(9*r),new ItemCreator(m));
                }else{
                    guiRecipe.removeItem(2+sl+(9*r));
                }
                sl++;
            }
            r++;
        }
        guiRecipe.setItem(15,po.createItem());
    }

    public void updateWhitelist(){
        if (guiWhitelist==null){
            setup();
        }
        guiWhitelist.clear();
        for (int i = 0; i < 9; i++) {
            guiWhitelist.setItem(i,new ItemCreator(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("§a"));
        }
        guiWhitelist.setItem(0,PortalConfig.getInstance().whiteList()?new ItemCreator(Material.LIME_WOOL).setDisplayName("§aWhitelist On"):new ItemCreator(Material.RED_WOOL).setDisplayName("§cWhitelist Off"));
        for (Material whiteListBlock : PortalConfig.getInstance().getWhiteListBlocks()) {
            guiWhitelist.addItem(new ItemCreator(whiteListBlock));
        }
    }

    public void open(Player p){
        update();
        if (gui.getInventory().getViewers().size()>0){
            p.sendMessage("§cAnother player is using the menu!");
            return;
        }
        p.closeInventory();
        gui.open(p);
    }

    public void openPortalGunSelector(Player p){
        InventoryGUI inv = new InventoryGUI("Select PortalGun", 3, new InventoryClickEventHandler() {
            @Override
            public void onInventoryClick(InventoryGUIClickEvent e) {
                e.setCancelled(true);
                switch (e.getSlot()){
                    case 0:
                        PortalConfig.getInstance().setPortalCraftable(!PortalConfig.getInstance().portalCraftable());
                        openPortalGunSelector(p);
                        break;
                    case 10: openRecipe(p,PortalModel.CHELL);
                        break;
                    case 12: openRecipe(p,PortalModel.ATLAS);
                        break;
                    case 14: openRecipe(p,PortalModel.P_BODY);
                        break;
                    case 16: openRecipe(p,PortalModel.POTATOS);
                        break;
                }
            }

            @Override
            public void onInventoryDrag(InventoryGUIDragEvent e) {
                e.setCancelled(true);
            }

            @Override
            public void onInventoryClose(InventoryGUICloseEvent e) {

            }
        },PortalGunMain.getInstance());
        inv.setDestroyOnClose(true);

        for (int i = 0; i < 3*9; i++) {
            inv.setItem(i,new ItemCreator(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("§a"));
        }
        inv.setItem(0,PortalConfig.getInstance().portalCraftable()?new ItemCreator(Material.GREEN_WOOL).setDisplayName("§aCan Craft Portal Guns"):new ItemCreator(Material.RED_WOOL).setDisplayName("§cCan Craft Portal Guns"));
        inv.setItem(10,PortalModel.CHELL.createItem());
        inv.setItem(12,PortalModel.ATLAS.createItem());
        inv.setItem(14,PortalModel.P_BODY.createItem());
        inv.setItem(16,PortalModel.POTATOS.createItem());

        p.closeInventory();
        inv.open(p);
    }

    public void openRecipe(Player p, PortalModel pm){
        updatePortalGunRecipe(pm);
        if (guiRecipe.getInventory().getViewers().size()>0){
            p.sendMessage("§cAnother player is using the menu!");
            return;
        }
        p.closeInventory();
        guiRecipe.open(p);
    }

    public void openWhitelist(Player p){
        updateWhitelist();
        if (guiWhitelist.getInventory().getViewers().size()>0){
            p.sendMessage("§cAnother player is using the menu!");
            return;
        }
        p.closeInventory();
        Bukkit.getScheduler().runTaskLater(PortalGunMain.getInstance(),r-> guiWhitelist.open(p),1);
    }
}
