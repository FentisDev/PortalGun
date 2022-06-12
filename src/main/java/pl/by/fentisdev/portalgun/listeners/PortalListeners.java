package pl.by.fentisdev.portalgun.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pl.by.fentisdev.portalgun.PortalGunMain;
import pl.by.fentisdev.portalgun.portalgun.PortalGun;
import pl.by.fentisdev.portalgun.portalgun.PortalGunManager;
import pl.by.fentisdev.portalgun.portalgun.PortalModel;
import pl.by.fentisdev.portalgun.portalgun.PortalSound;
import pl.by.fentisdev.portalgun.utils.ItemCreator;
import pl.by.fentisdev.portalgun.utils.PortalConfig;
import pl.by.fentisdev.portalgun.utils.PortalUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PortalListeners implements Listener {

    private final List<UUID> cd = new ArrayList<>();

    @EventHandler
    public void onClickInEntity(PlayerInteractEntityEvent e){
        Entity entity = e.getRightClicked();
        if (entity instanceof ItemFrame){
            for (PortalGun portalGun : PortalGunManager.getInstance().getPortalGuns()) {
                if (portalGun.getPortal1().isPortalItemFrame(((ItemFrame) entity))||
                        portalGun.getPortal2().isPortalItemFrame(((ItemFrame) entity))){
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onClickNewPortalGun(PlayerInteractEvent e){
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                item.hasItemMeta() &&
                item.getItemMeta().hasCustomModelData()){
            PortalModel po = PortalModel.getPortalModelByMaterial(item.getType());
            if (po==null){
                return;
            }
            NBTItem nbt = new NBTItem(item);
            if (nbt.hasKey("PortalID")){
                return;
            }
            if (po.getCustomModelDataNormal()==item.getItemMeta().getCustomModelData()){
                PortalGun pg = null;
                switch (PortalConfig.getInstance().getPortalGunMode()) {
                    case INFINITY:
                        pg = PortalGunManager.getInstance().createPortalGun(po);
                        break;
                    case ONE_TYPE_PER_PLAYER:
                        List<PortalGun> portalGuns = PortalGunManager.getInstance().getPlayerPortalGuns(p).stream().filter(portalGun -> portalGun.getPortalModel()==po).collect(Collectors.toList());
                        pg=portalGuns.size()!=0?portalGuns.get(0):PortalGunManager.getInstance().createPortalGun(po);
                        PortalGunManager.getInstance().addPlayerPortalGun(p,pg);
                        break;
                    case ONE_PORTAL_PER_PLAYER:
                        if (PortalGunManager.getInstance().getPlayerPortalGuns(p).size()!=0){
                            pg=PortalGunManager.getInstance().getPlayerPortalGuns(p).get(0);
                            pg.setPortalModel(po);
                        }else{
                            pg=PortalGunManager.getInstance().createPortalGun(po);
                            PortalGunManager.getInstance().addPlayerPortalGun(p,pg);
                        }
                        break;
                }
                if (pg!=null){
                    p.getInventory().setItemInMainHand(pg.getPortalItem());
                    PortalSound.PORTAL_GUN_PICKUP.playSound(p.getLocation(),1,1);
                }
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e){
        for (PortalGun portalGun : PortalGunManager.getInstance().getPortalGuns()) {
            if ((portalGun.getPortal1().getLoc1()!=null&&e.getWorld()==portalGun.getPortal1().getLoc1().getWorld() && portalGun.getPortal1().inChunk(e.getChunk())) ||
                    portalGun.getPortal1().getLoc1()!=null&&e.getWorld()==portalGun.getPortal1().getLoc2().getWorld() && portalGun.getPortal1().inChunk(e.getChunk())){
                portalGun.getPortal1().renderPortal();
            }
            if ((portalGun.getPortal2().getLoc1()!=null&&e.getWorld()==portalGun.getPortal2().getLoc1().getWorld() && portalGun.getPortal2().inChunk(e.getChunk())) ||
                    portalGun.getPortal2().getLoc2()!=null&&e.getWorld()==portalGun.getPortal2().getLoc2().getWorld() && portalGun.getPortal2().inChunk(e.getChunk())){
                portalGun.getPortal2().renderPortal();
            }
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent e){
        for (PortalGun portalGun : PortalGunManager.getInstance().getPortalGuns()) {
            if ((portalGun.getPortal1().getLoc1()!=null&&e.getWorld()==portalGun.getPortal1().getLoc1().getWorld() && portalGun.getPortal1().inChunk(e.getChunk())) ||
                    portalGun.getPortal1().getLoc1()!=null&&e.getWorld()==portalGun.getPortal1().getLoc2().getWorld() && portalGun.getPortal1().inChunk(e.getChunk())){
                portalGun.getPortal1().unRenderPortal();
            }
            if ((portalGun.getPortal2().getLoc1()!=null&&e.getWorld()== portalGun.getPortal2().getLoc1().getWorld() && portalGun.getPortal2().inChunk(e.getChunk())) ||
                    portalGun.getPortal2().getLoc2()!=null&&e.getWorld()== portalGun.getPortal2().getLoc2().getWorld() && portalGun.getPortal2().inChunk(e.getChunk())){
                portalGun.getPortal2().unRenderPortal();
            }
        }
    }

    @EventHandler
    public void onClickPortalGun(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (cd.contains(p.getUniqueId())){
            cd.remove(p.getUniqueId());
            return;
        }
        PortalGun portalGun;
        if (e.getHand() == EquipmentSlot.HAND &&
                (e.getAction() == Action.RIGHT_CLICK_BLOCK ||
                        e.getAction() == Action.RIGHT_CLICK_AIR ||
                        e.getAction() == Action.LEFT_CLICK_BLOCK ||
                        e.getAction() == Action.LEFT_CLICK_AIR) &&
                (portalGun=PortalUtils.getInstance().getPortalGun(e.getPlayer(),p.getInventory().getItemInMainHand()))!=null){
            e.setCancelled(true);
            if (e.getAction()==Action.RIGHT_CLICK_BLOCK||e.getAction()==Action.RIGHT_CLICK_AIR){
                portalGun.shootPortalBlue(p.getEyeLocation(),p);
            }else{
                portalGun.shootPortalOrange(p.getEyeLocation(),p);
            }
        }
    }

    @EventHandler
    public void onItemFrame(HangingBreakEvent e){
        if (e.getEntity() instanceof ItemFrame){
            ItemFrame itemFrame = (ItemFrame)e.getEntity();
            ItemStack item = itemFrame.getItem();
            if (!itemFrame.isVisible()&&item.getType()==Material.FILLED_MAP&&item.hasItemMeta()&&item.getItemMeta().hasLore()&&item.getItemMeta().getLore().get(0).equalsIgnoreCase("Portal")){
                e.setCancelled(true);
                if (e.getCause()!=HangingBreakEvent.RemoveCause.ENTITY){
                    for (PortalGun portalGun : PortalGunManager.getInstance().getPortalGuns()) {
                        if (portalGun.getPortal1().isPortalItemFrame(itemFrame)){
                            portalGun.getPortal1().resetPortal();
                        }
                        if (portalGun.getPortal2().isPortalItemFrame(itemFrame)){
                            portalGun.getPortal2().resetPortal();
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPortalHit(EntityDamageEvent e){
        if (e.getEntity() instanceof ItemFrame){
            for (PortalGun portalGun : PortalGunManager.getInstance().getPortalGuns()) {
                if (portalGun.getPortal1().isPortalItemFrame(((ItemFrame) e.getEntity()))||portalGun.getPortal2().isPortalItemFrame(((ItemFrame) e.getEntity()))){
                    e.setCancelled(true);
                }
            }
            ItemFrame itemFrame = (ItemFrame)e.getEntity();
            ItemStack item = itemFrame.getItem();
            if (!e.isCancelled()){
                if (!itemFrame.isVisible()&&item.getType()==Material.FILLED_MAP&&item.hasItemMeta()&&item.getItemMeta().hasLore()&&item.getItemMeta().getLore().get(0).equalsIgnoreCase("Portal")){
                    e.getEntity().remove();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDropFromInv(InventoryClickEvent e){
        if (e.isCancelled()){
            return;
        }
        Player p = (Player)e.getWhoClicked();
        if (e.getAction() == InventoryAction.DROP_ALL_CURSOR ||
                e.getAction() == InventoryAction.DROP_ONE_CURSOR){
            if (PortalUtils.getInstance().getPortalGun(p,e.getCursor())!=null) {
                ItemCreator item = new ItemCreator(e.getCursor());
                item.getNBTItem().setBoolean("Drop",true);
                p.setItemOnCursor(item.build());
                Bukkit.getScheduler().runTaskLater(PortalGunMain.getInstance(),r->p.updateInventory(),1);
                /*
                NBTItem nbtItem = new NBTItem(e.getCursor());
                nbtItem.setBoolean("Drop",true);
                p.setItemOnCursor(nbtItem.getItem());
                 */
                //p.updateInventory();
            }
        }
        if (e.getClick() == ClickType.DROP){
            if (PortalUtils.getInstance().getPortalGun(p,e.getCurrentItem())!=null) {
                ItemCreator item = new ItemCreator(e.getCurrentItem());
                item.getNBTItem().setBoolean("Drop",true);
                e.setCurrentItem(item.build());
                /*NBTItem nbtItem = new NBTItem(e.getCurrentItem());
                nbtItem.setBoolean("Drop",true);
                e.setCurrentItem(nbtItem.getItem());*/
                //p.updateInventory();
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDrop(PlayerDropItemEvent e){
        PortalGun portalGun;
        if ((portalGun = PortalUtils.getInstance().getPortalGun(e.getPlayer(),e.getItemDrop().getItemStack()))!=null) {
            ItemCreator itemDrop = new ItemCreator(e.getItemDrop().getItemStack());
            if (itemDrop.getNBTItem().hasKey("Drop")){
                itemDrop.getNBTItem().removeKey("Drop");
                e.getItemDrop().setItemStack(itemDrop.getNBTItem().getItem());
                return;
            }
            e.getItemDrop().remove();
            portalGun.resetPortals();
            cd.add(e.getPlayer().getUniqueId());
            ItemStack item = portalGun.getPortalItem();
            e.getPlayer().getInventory().setItemInMainHand(item);
            Bukkit.getScheduler().scheduleSyncDelayedTask(PortalGunMain.getInstance(), () -> {
                cd.remove(e.getPlayer().getUniqueId());
            }, 3);
        }
    }

    @EventHandler
    public void onWorldSave(WorldSaveEvent e){
        PortalGunManager.getInstance().savePortals();
    }
}
