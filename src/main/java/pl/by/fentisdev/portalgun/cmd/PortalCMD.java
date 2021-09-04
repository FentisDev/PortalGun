package pl.by.fentisdev.portalgun.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import pl.by.fentisdev.portalgun.portalgun.*;
import pl.by.fentisdev.portalgun.utils.PortalConfig;
import pl.by.fentisdev.portalgun.utils.PortalUtils;

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
                                    case UNIQUE:
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
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length==1){
            return Arrays.asList("give","reset");
        }else if (strings.length==2){
            if (strings[0].equalsIgnoreCase("give")){
                return Arrays.asList("chell","p_body","atlas","potatos");
            }
            if (strings[0].equalsIgnoreCase("reset")){
                List<String> players = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
                players.add("*");
                return players;
            }
        }else if (strings.length==3){
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        }
        return null;
    }
}
