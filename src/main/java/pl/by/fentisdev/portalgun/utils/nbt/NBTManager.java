package pl.by.fentisdev.portalgun.utils.nbt;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import pl.by.fentisdev.portalgun.utils.nbt.versions.v116;
import pl.by.fentisdev.portalgun.utils.nbt.versions.v117;
import pl.by.fentisdev.portalgun.utils.nbt.versions.v118;

import java.lang.reflect.InvocationTargetException;

public class NBTManager {

    private static NBTManager instance = new NBTManager();

    public static NBTManager getInstance() {
        return instance;
    }

    Class compoundClass;

    public NBTManager() {
        int ver = mineVersion();
        if (ver<1170){
            compoundClass = v116.class;
        }else if(ver<1180){
            compoundClass = v117.class;
        }else{
            compoundClass = v118.class;
        }
    }

    public NBTTagCompound createNBTTagCompound(){
        NBTTagCompound compound = null;
        try {
            compound = (NBTTagCompound)compoundClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return compound;
    }

    public NBTTagCompound createNBTTagCompound(ItemStack itemStack){
        NBTTagCompound compound = null;
        try {
            compound = (NBTTagCompound)compoundClass.getConstructor(ItemStack.class).newInstance(itemStack);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return compound;
    }

    /*public NBTTagCompound createNBTTagCompound(){
        int ver = mineVersion();
        if (ver<1170){
            return new v116();
        }else if(ver<1180){
            return new v117();
        }else{
            return new v118();
        }
    }*/



    private int mineVersion(){
        return Integer.parseInt(Bukkit.getServer().getVersion().split(":")[1].replace(")","").trim().replace(".",""));
    }
}
