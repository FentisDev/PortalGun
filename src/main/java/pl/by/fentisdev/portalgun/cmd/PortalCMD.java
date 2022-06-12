package pl.by.fentisdev.portalgun.cmd;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import pl.by.fentisdev.portalgun.PortalGunMain;
import pl.by.fentisdev.portalgun.portalgun.*;
import pl.by.fentisdev.portalgun.utils.PortalConfig;
import pl.by.fentisdev.portalgun.utils.PortalUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PortalCMD implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player){
            Player p = (Player)commandSender;
            if (strings.length>0){
                if (strings[0].equalsIgnoreCase("give")){
                    if (strings.length>1){
                        PortalModel po = null;
                        if (strings[1].equalsIgnoreCase("CHELL")){
                            po= PortalModel.CHELL;
                        }else
                        if (strings[1].equalsIgnoreCase("P_BODY")){
                            po= PortalModel.P_BODY;
                        }else
                        if (strings[1].equalsIgnoreCase("ATLAS")){
                            po= PortalModel.ATLAS;
                        }else
                        if (strings[1].equalsIgnoreCase("POTATOS")){
                            po= PortalModel.POTATOS;
                        }
                        if (po!=null){
                            Player to = p;
                            if (strings.length>2&&Bukkit.getPlayer(strings[2])!=null){
                                to=Bukkit.getPlayer(strings[2]);
                            }else{
                                to=null;
                                p.sendMessage("§cPlayer not found!");
                            }
                            if (to!=null){
                                PortalGun pg = null;
                                switch (PortalConfig.getInstance().getPortalGunMode()){
                                    case INFINITY:
                                        pg = PortalGunManager.getInstance().createPortalGun(po);
                                        break;
                                    case ONE_TYPE_PER_PLAYER:
                                        for (PortalGun playerPortalGun : PortalGunManager.getInstance().getPlayerPortalGuns(to)) {
                                            if (playerPortalGun.getPortalModel()==po){
                                                pg = playerPortalGun;
                                            }
                                        }
                                        if (pg==null){
                                            pg = PortalGunManager.getInstance().createPortalGun(po);
                                            PortalGunManager.getInstance().addPlayerPortalGun(to,pg);
                                        }
                                        break;
                                    case ONE_PORTAL_PER_PLAYER:
                                        if (PortalGunManager.getInstance().getPlayerPortalGuns(to).size()>0){
                                            pg = PortalGunManager.getInstance().getPlayerPortalGuns(to).get(0);
                                            pg.setPortalModel(po);
                                        }else{
                                            pg = PortalGunManager.getInstance().createPortalGun(po);
                                            PortalGunManager.getInstance().addPlayerPortalGun(to,pg);
                                        }
                                }
                                to.getInventory().addItem(pg.getPortalItem());
                                PortalSound.PORTAL_GUN_PICKUP.playSound(to.getLocation(),1,1);
                            }
                        }else{
                            p.sendMessage("§cPortalGun name incorrect!");
                        }
                    }else{
                        p.sendMessage("§cPut a name of PortalGun (Chell, P_body, Atlas, PotatOS)");
                    }
                }
                if (strings[0].equalsIgnoreCase("reset")){
                    PortalGun pg = null;
                    if (strings.length==1){
                        p.sendMessage("§cPlease hold a PortalGun or insert Player name or PortalGun id or * to reset all PortalGuns");
                        return false;
                    }

                    if ((pg=PortalUtils.getInstance().getPortalGun(p.getInventory().getItemInMainHand()))==null){
                        if (strings[1].equalsIgnoreCase("*")){
                            PortalGunManager.getInstance().getPortalGuns().forEach(PortalGun::resetPortals);
                            p.sendMessage("§aReseted!");
                        }else{
                            Player to = Optional.of(Bukkit.getPlayer(strings[1])).orElse(null);
                            if (to!=null){
                                if (PortalConfig.getInstance().getPortalGunMode()==PortalGunMode.INFINITY){
                                    p.sendMessage("§cPortalGun don't have connection with player in PortalGun mode Infinity");
                                    return false;
                                }
                                PortalGunManager.getInstance().getPlayerPortalGuns(to).forEach(PortalGun::resetPortals);
                                p.sendMessage("§aReseted!");
                            }else{
                                int id = Optional.of(Integer.parseInt(strings[1])).orElse(-1);
                                if (id!=-1){
                                    pg = Optional.of(PortalGunManager.getInstance().getPortalGun(id)).orElse(null);
                                    if (pg!=null){
                                        pg.resetPortals();
                                        p.sendMessage("§aReseted!");
                                    }else{
                                        p.sendMessage("§cPlease hold a PortalGun or insert Player name or PortalGun id or * to reset all PortalGuns");
                                    }
                                }
                            }
                        }
                    }else{
                        pg.resetPortals();
                        p.sendMessage("§aReseted!");
                    }
                }
                if (strings[0].equalsIgnoreCase("id")){
                    if(strings.length==1){
                        PortalGun pg;
                        if ((pg = PortalUtils.getInstance().getPortalGun(p.getInventory().getItemInMainHand())) != null) {
                            p.sendMessage("§ePortalGun ID: " + pg.getId());
                        } else {
                            p.sendMessage("§cHold the PortalGun first.");
                        }
                    }else{
                        if (PortalConfig.getInstance().getPortalGunMode()==PortalGunMode.INFINITY){
                            p.sendMessage("§cPortalGun don't have connection with player in PortalGun mode Infinity");
                            return false;
                        }
                        Player to = Optional.of(Bukkit.getPlayer(strings[1])).orElse(null);
                        if (to!=null&&to.isOnline()){
                            StringBuilder ids = new StringBuilder("§aPlayer: "+to.getName()+" PortalGuns: ");
                            List<String> idsList = new ArrayList<>();
                            PortalGunManager.getInstance().getPlayerPortalGuns(to).forEach(pg -> idsList.add(""+pg.getId()));
                            ids.append(idsList.size()==0?"None":String.join(",",idsList));
                            ids.append(".");
                            p.sendMessage(ids.toString());
                        }else{
                            p.sendMessage("§cPlayer offline!");
                        }
                    }
                }
                if (strings[0].equalsIgnoreCase("whitelist")){
                    if (strings.length>1){
                        if (strings[1].equalsIgnoreCase("on")){
                            PortalGunMain.getInstance().getConfig().set("WhiteList",true);
                            PortalGunMain.getInstance().saveConfig();
                            p.sendMessage("§aWhiteList is on!");
                        }else if (strings[1].equalsIgnoreCase("off")) {
                            PortalGunMain.getInstance().getConfig().set("WhiteList",false);
                            PortalGunMain.getInstance().saveConfig();
                            p.sendMessage("§aWhiteList is off!");
                        }else if (strings[1].equalsIgnoreCase("add")||strings[1].equalsIgnoreCase("remove")){
                            if (strings.length==3){
                                try {
                                    Material material = Material.getMaterial(strings[2].toUpperCase());
                                    List<String> list = PortalGunMain.getInstance().getConfig().getStringList("WhiteListBlocks");
                                    if(strings[1].equalsIgnoreCase("add")){
                                        if (list.contains(material.toString())){
                                            p.sendMessage("§cThis Material is already on the WhiteList!");
                                        }else{
                                            list.add(material.toString());
                                            p.sendMessage("§aMaterial "+material+" has been added to the WhiteList!");
                                        }
                                    }else{
                                        if (list.contains(material.toString())){
                                            list.remove(material.toString());
                                            p.sendMessage("§aMaterial "+material+" has bem removed from WhiteList!");
                                        }else{
                                            p.sendMessage("§cThis Material is not on the WhiteList!");
                                        }
                                    }
                                    PortalGunMain.getInstance().getConfig().set("WhiteListBlocks",list);
                                    PortalGunMain.getInstance().saveConfig();

                                } catch (Exception e) {
                                    p.sendMessage("§cMaterial not found.");
                                }
                            }
                        }
                    }else{
                        p.sendMessage("§eWhitelist Blocks:");
                        p.sendMessage(String.join("\n- ",PortalGunMain.getInstance().getConfig().getStringList("WhiteListBlocks")).toLowerCase());
                    }
                }
            }
        }
        return false;
    }

    private List<String> possibleMaterials = null;

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> players = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        if (strings.length==1){
            return Arrays.asList("give","reset","id","whitelist");
        }else if (strings.length==2){
            if (strings[0].equalsIgnoreCase("give")){
                return Arrays.asList("chell","p_body","atlas","potatos");
            }
            if (strings[0].equalsIgnoreCase("reset")){
                players.add("*");
                return players;
            }
            if (strings[0].equalsIgnoreCase("id")){
                return players;
            }
            if (strings[0].equalsIgnoreCase("whitelist")){
                return Arrays.asList("add","remove","on","off");
            }
        }else if (strings.length==3){
            if (strings[0].equalsIgnoreCase("give")){
                return players;
            }
            if (strings[0].equalsIgnoreCase("whitelist")){
                if (strings[1].equalsIgnoreCase("add")){
                    if (possibleMaterials == null) {
                        possibleMaterials = new ArrayList<>();
                        for (Material m : Material.values()) {
                            if (m.isBlock() && m.isSolid()) {
                                possibleMaterials.add(m.toString().toLowerCase());
                            }
                        }
                    }
                    return possibleMaterials;
                }else if (strings[1].equalsIgnoreCase("remove")){
                    return PortalGunMain.getInstance().getConfig().getStringList("WhiteListBlocks").stream().map(String::toLowerCase).collect(Collectors.toList());
                }
            }
        }
        return null;
    }
}
