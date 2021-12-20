package pl.by.fentisdev.portalgun.utils.nbt.versions;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import pl.by.fentisdev.portalgun.utils.ItemCreator;
import pl.by.fentisdev.portalgun.utils.nbt.NBTTagCompound;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class v118 extends NBTTagCompound{

    public v118(){
        super();
    }

    public v118(Object nbtTagCompound) {
        super(nbtTagCompound);
    }

    public v118(ItemCreator itemCreator) {
        super(itemCreator);
    }

    public v118(ItemStack itemStack) {
        super(itemStack);
    }

    public void createNewNBTTag(){
        try {
            Class<?> tagCompoundClass = Class.forName("net.minecraft.nbt.NBTTagCompound");
            nbtTagCompound=tagCompoundClass.newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void converter(){
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> craftItemstackClass = Class.forName("org.bukkit.craftbukkit."+version+".inventory.CraftItemStack");
            Method asNMSCopy = craftItemstackClass.getMethod("asNMSCopy", ItemStack.class);
            asNMSCopy.setAccessible(true);
            Object itemstack = asNMSCopy.invoke(null,itemStack);
            asNMSCopy.setAccessible(false);
            Method getTag = itemstack.getClass().getMethod("t");
            getTag.setAccessible(true);
            nbtTagCompound = getTag.invoke(itemstack);
            getTag.setAccessible(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ItemStack save(){
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> craftItemstackClass = Class.forName("org.bukkit.craftbukkit."+version+".inventory.CraftItemStack");
            Method asNMSCopy = craftItemstackClass.getMethod("asNMSCopy", ItemStack.class);
            Object itemstack = asNMSCopy.invoke(null,itemStack);
            Class<?> tagCompoundClass = Class.forName("net.minecraft.nbt.NBTTagCompound");
            Class<?> nmsItemstackClass = Class.forName("net.minecraft.world.item.ItemStack");
            Method setTag = itemstack.getClass().getMethod("c",tagCompoundClass);
            setTag.invoke(itemstack,nbtTagCompound);

            Method asBukkitCopy = craftItemstackClass.getMethod("asBukkitCopy",nmsItemstackClass);
            itemStack = (ItemStack) asBukkitCopy.invoke(null,itemstack);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        //net.minecraft.nbt.NBTTagCompound
        return itemStack;
    }

    public NBTTagCompound set(String key, Object object) {
        insert(nbtTagCompound,"a",key,object.getClass(),object);
        return this;
    }

    public NBTTagCompound setByte(String key, byte value) {
        insert(nbtTagCompound,"a",key,byte.class,value);
        return this;
    }

    public NBTTagCompound setShort(String key, short value) {
        insert(nbtTagCompound,"a",key,short.class,value);
        return this;
    }

    public NBTTagCompound setInt(String key, int value) {
        insert(nbtTagCompound,"a",key,int.class,value);
        return this;
    }

    public NBTTagCompound setLong(String key, long value) {
        insert(nbtTagCompound,"a",key,long.class,value);
        return this;
    }

    public NBTTagCompound setFloat(String key, float value) {
        insert(nbtTagCompound,"a",key,float.class,value);
        return this;
    }

    public NBTTagCompound setDouble(String key, double value) {
        insert(nbtTagCompound,"a",key,double.class,value);
        return this;
    }

    public NBTTagCompound setString(String key, String string) {
        insert(nbtTagCompound,"a",key,String.class,string);
        return this;
    }

    public NBTTagCompound setByteArray(String key, byte[] values) {
        insert(nbtTagCompound,"a",key,byte[].class,values);
        return this;
    }

    public NBTTagCompound setIntArray(String key, int[] values) {
        insert(nbtTagCompound,"a",key,int[].class,values);
        return this;
    }

    public NBTTagCompound setBoolean(String key, boolean value) {
        insert(nbtTagCompound,"a",key,boolean.class,value);
        return this;
    }

    public Object get(String key){
        return get(nbtTagCompound,"c",key);
    }

    public byte getByte(String key) {
        return (byte)get(nbtTagCompound,"f",key);
    }

    public short getShort(String key) {
        return (short)get(nbtTagCompound,"g",key);
    }

    public int getInt(String key) {
        return (int)get(nbtTagCompound,"h",key);
    }

    public long getLong(String key) {
        return (long)get(nbtTagCompound,"i",key);
    }

    public float getFloat(String key) {
        return (float)get(nbtTagCompound,"j",key);
    }

    public double getDouble(String key) {
        return (double)get(nbtTagCompound,"k",key);
    }

    public String getString(String key){
        return (String)get(nbtTagCompound,"l",key);
    }

    public byte[] getByteArray(String key) {
        return (byte[]) get(nbtTagCompound,"m",key);
    }

    public int[] getIntArray(String key) {
        return (int[]) get(nbtTagCompound,"n",key);
    }

    public boolean getBoolean(String key) {
        return (boolean)get(nbtTagCompound,"b",key);
    }

    public void remove(String key){
        get(nbtTagCompound,"r",key);
    }

    public boolean isEmpty(){
        return (boolean)get(nbtTagCompound,"f");
    }

    public NBTTagCompound getCompound(String key){
        return new v118(get(nbtTagCompound,"p",key));
    }

    public Set<String> getKeys(){
        return (Set<String>)get(nbtTagCompound,"b");
    }

    public Map<String,Object> getMap(){
        Map<String,Object> map = new HashMap<>();
        getKeys().forEach(k -> map.put(k,get(k)));
        return map;
    }

    public boolean hasKey(String key) {
        return (boolean)get(nbtTagCompound,"e",key);
    }
}
