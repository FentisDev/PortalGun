package pl.by.fentisdev.portalgun.cmd;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.by.fentisdev.itemcreator.ItemCreator;
import pl.by.fentisdev.portalgun.PortalGunMain;
import pl.by.fentisdev.portalgun.portalgun.*;
import pl.by.fentisdev.portalgun.utils.PortalConfig;
import pl.by.fentisdev.portalgun.utils.PortalConfigGui;
import pl.by.fentisdev.portalgun.utils.PortalUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CommandAlias("portalgun|pg")
public class PortalCommands extends BaseCommand {

    @Subcommand("give")
    @Syntax("<portalModel> <player>")
    @CommandPermission("portalgun.admin")
    @CommandCompletion("@portalmodels @players")
    public void givePlayerPortal(CommandSender sender, PortalModel portalModel, @Optional OnlinePlayer onlinePlayer){
        if (onlinePlayer==null){
            sender.sendMessage("§cPlayer not found!");
            return;
        }
        Player toPlayer = onlinePlayer.getPlayer();
        PortalGun pg = null;
        switch (PortalConfig.getInstance().getPortalGunMode()){
            case INFINITY:
                pg = PortalGunManager.getInstance().createPortalGun(portalModel);
                break;
            case ONE_TYPE_PER_PLAYER:
                for (PortalGun playerPortalGun : PortalGunManager.getInstance().getPlayerPortalGuns(toPlayer)) {
                    if (playerPortalGun.getPortalModel()==portalModel){
                        pg = playerPortalGun;
                    }
                }
                if (pg==null){
                    pg = PortalGunManager.getInstance().createPortalGun(portalModel);
                    PortalGunManager.getInstance().addPlayerPortalGun(toPlayer,pg);
                }
                break;
            case ONE_PORTAL_PER_PLAYER:
                if (PortalGunManager.getInstance().getPlayerPortalGuns(toPlayer).size()>0){
                    pg = PortalGunManager.getInstance().getPlayerPortalGuns(toPlayer).get(0);
                    pg.setPortalModel(portalModel);
                }else{
                    pg = PortalGunManager.getInstance().createPortalGun(portalModel);
                    PortalGunManager.getInstance().addPlayerPortalGun(toPlayer,pg);
                }
        }
        toPlayer.getInventory().addItem(pg.getPortalItem());
        PortalSound.PORTAL_GUN_PICKUP.playSound(toPlayer.getLocation(),1,1);
    }

    @Subcommand("reset")
    @CommandPermission("portalgun.admin")
    public void reset(CommandSender sender){
        sender.sendMessage("§cPlease hold a PortalGun or insert Player name or PortalGun id or * to reset all PortalGuns");
    }

    @Subcommand("reset all")
    @CommandPermission("portalgun.admin")
    public void resetAll(CommandSender sender) {
        PortalGunManager.getInstance().getPortalGuns().forEach(PortalGun::resetPortals);
        sender.sendMessage("§aReseted!");
    }

    @Subcommand("reset hand")
    @CommandPermission("portalgun.admin")
    public void resetHand(Player player) {
        PortalGun pg = null;
        if ((pg= PortalUtils.getInstance().getPortalGun((player).getInventory().getItemInMainHand()))!=null){
            pg.resetPortals();
            player.sendMessage("§aReseted!");
        }
    }

    @Subcommand("reset player")
    @CommandPermission("portalgun.admin")
    @Syntax("<player>")
    public void resetPlayer(CommandSender sender, @Optional OnlinePlayer onlinePlayer){
        PortalGun pg = null;
        if (onlinePlayer==null){
            sender.sendMessage("§cPlayer not found!");
            return;
        }
        Player to = onlinePlayer.getPlayer();
        if (to!=null){
            if (PortalConfig.getInstance().getPortalGunMode()== PortalGunMode.INFINITY){
                sender.sendMessage("§cPortalGun don't have connection with player in PortalGun mode Infinity");
                return;
            }
            PortalGunManager.getInstance().getPlayerPortalGuns(to).forEach(PortalGun::resetPortals);
            sender.sendMessage("§aReseted!");
        }
    }

    @Subcommand("reset id")
    @CommandPermission("portalgun.admin")
    @Syntax("<portalGunId>")
    public void resetId(CommandSender sender, String[] args){
        PortalGun pg = null;
        int id = java.util.Optional.of(Integer.parseInt(args[0])).orElse(-1);
        if (id != -1) {
            pg = PortalGunManager.getInstance().getPortalGun(id);
            if (pg != null) {
                pg.resetPortals();
                sender.sendMessage("§aReseted!");
                return;
            }
            sender.sendMessage("§cPlease hold a PortalGun or insert Player name or PortalGun id or * to reset all PortalGuns");
        }
    }

    @Subcommand("config")
    @CommandPermission("portalgun.admin")
    public void openMenuConfig(Player player){
        PortalConfigGui.getInstance().open(player);
    }

    @Subcommand("id")
    @CommandPermission("portalgun.admin")
    public void getId(Player player){
        PortalGun pg;
        if ((pg = PortalUtils.getInstance().getPortalGun((player).getInventory().getItemInMainHand())) != null) {
            player.sendMessage("§ePortalGun ID: " + pg.getId());
            return;
        }
        player.sendMessage("§cHold the PortalGun first.");
    }

    @Subcommand("id")
    @CommandPermission("portalgun.admin")
    @Syntax("<player>")
    public void getId(CommandSender sender, OnlinePlayer onlinePlayer){
        if (onlinePlayer==null){
            sender.sendMessage("§cPlayer not found!");
            return;
        }
        Player to = onlinePlayer.getPlayer();
        if (PortalConfig.getInstance().getPortalGunMode()==PortalGunMode.INFINITY){
            sender.sendMessage("§cPortalGun don't have connection with player in PortalGun mode Infinity");
            sender.sendMessage("§aBut.. here is the list of IDs present in the player's inventory.");
            List<String> idList = new ArrayList<>();
            to.getInventory().forEach(itemStack -> {
                PortalGun pg = null;
                if ((pg=PortalUtils.getInstance().getPortalGun(itemStack))!=null){
                    idList.add(""+pg.getId());
                }
            });
            if (idList.isEmpty()){
                sender.sendMessage("§cNo PortalGun was found in the player's inventory.");
                return;
            }
            sender.sendMessage("§e"+String.join(",",idList)+".");
            return;
        }
        StringBuilder ids = new StringBuilder("§aPlayer: "+to.getName()+" PortalGuns: ");
        List<String> idsList = new ArrayList<>();
        PortalGunManager.getInstance().getPlayerPortalGuns(to).forEach(pg -> idsList.add(""+pg.getId()));
        ids.append(idsList.isEmpty() ?"None":String.join(",",idsList));
        ids.append(".");
        sender.sendMessage(ids.toString());
    }

    @Subcommand("whitelist")
    @CommandPermission("portalgun.admin")
    public void whitelist(CommandSender sender){
        sender.sendMessage("§aWhiteList is on!");
    }

    @Subcommand("whitelist on")
    @CommandPermission("portalgun.admin")
    public void whitelistOn(CommandSender sender){
        PortalConfig.getInstance().setWhiteList(true);
        PortalGunMain.getInstance().saveConfig();
        sender.sendMessage("§aWhiteList is on!");
    }

    @Subcommand("whitelist off")
    @CommandPermission("portalgun.admin")
    public void whitelistOff(CommandSender sender){
        PortalConfig.getInstance().setWhiteList(false);
        PortalGunMain.getInstance().saveConfig();
        sender.sendMessage("§aWhiteList is off!");
    }

    @Subcommand("whitelist add")
    @CommandPermission("portalgun.admin")
    public void whitelistAdd(CommandSender sender, String[] args){
        Material material = Material.getMaterial(args[0].toUpperCase());
        List<Material> list = PortalConfig.getInstance().getWhiteListBlocks();
        if (list.contains(material)){
            sender.sendMessage("§cThis Material is already on the WhiteList!");
        }else{
            if (!material.isBlock()||material.isAir()){
                sender.sendMessage("§cThis Material is not a block or is a not valid block!");
                return;
            }
            list.add(material);
            sender.sendMessage("§aMaterial "+material+" has been added to the WhiteList!");
            PortalConfig.getInstance().setWhiteListBlocks(list);
            PortalGunMain.getInstance().saveConfig();
        }
    }

    @Subcommand("whitelist remove")
    @CommandPermission("portalgun.admin")
    @CommandCompletion("@blockwhitelist")
    @Syntax("<blockMaterial>")
    public void whitelistRemove(CommandSender sender, Material material){
        if (material==null){
            sender.sendMessage("§cMaterial not found!");
            return;
        }
        List<Material> list = PortalConfig.getInstance().getWhiteListBlocks();
        if (list.contains(material)){
            list.remove(material);
            sender.sendMessage("§aMaterial "+material+" has bem removed from WhiteList!");
            PortalConfig.getInstance().setWhiteListBlocks(list);
            PortalGunMain.getInstance().saveConfig();
        }else{
            sender.sendMessage("§cThis Material is not on the WhiteList!");
        }
    }

    @Subcommand("whitelist list")
    @CommandPermission("portalgun.admin")
    public void whitelistList(CommandSender sender){
        sender.sendMessage("§eList: "+ PortalConfig.getInstance().getWhiteListBlocks().stream().map(material -> material.name().toLowerCase()).collect(Collectors.joining(", ")) +".");
    }
}
