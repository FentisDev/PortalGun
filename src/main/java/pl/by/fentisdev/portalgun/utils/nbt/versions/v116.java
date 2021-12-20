package pl.by.fentisdev.portalgun.utils.nbt.versions;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import pl.by.fentisdev.portalgun.utils.ItemCreator;
import pl.by.fentisdev.portalgun.utils.nbt.NBTTagCompound;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class v116 extends NBTTagCompound{

    public v116(){
        super();
    }

    public v116(Object nbtTagCompound) {
        super(nbtTagCompound);
    }

    public v116(ItemCreator itemCreator) {
        super(itemCreator);
    }

    public v116(ItemStack itemStack) {
        super(itemStack);
    }

    public void createNewNBTTag(){
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> tagCompoundClass = Class.forName("net.minecraft.server."+version+".NBTTagCompound");
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
            Method getTag = itemstack.getClass().getMethod("getTag");
            getTag.setAccessible(true);
            nbtTagCompound = getTag.invoke(itemstack);
            getTag.setAccessible(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ItemStack save(ItemStack itemStack){
        this.itemStack = itemStack;
        return save();
    }

    public ItemStack save(){
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> craftItemstackClass = Class.forName("org.bukkit.craftbukkit."+version+".inventory.CraftItemStack");
            Method asNMSCopy = craftItemstackClass.getMethod("asNMSCopy", ItemStack.class);
            Object itemstack = asNMSCopy.invoke(null,itemStack);
            Class<?> tagCompoundClass = Class.forName("net.minecraft.server."+version+".NBTTagCompound");
            Class<?> nmsItemstackClass = Class.forName("net.minecraft.server."+version+".ItemStack");
            Method setTag = itemstack.getClass().getMethod("setTag",tagCompoundClass);
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
        insert(nbtTagCompound,"set",key,object.getClass(),object);
        return this;
    }

    public NBTTagCompound setByte(String key, byte value) {
        insert(nbtTagCompound,"setByte",key,byte.class,value);
        return this;
    }

    public NBTTagCompound setShort(String key, short value) {
        insert(nbtTagCompound,"setShort",key,short.class,value);
        return this;
    }

    public NBTTagCompound setInt(String key, int value) {
        insert(nbtTagCompound,"setInt",key,int.class,value);
        return this;
    }

    public NBTTagCompound setLong(String key, long value) {
        insert(nbtTagCompound,"setLong",key,long.class,value);
        return this;
    }

    public NBTTagCompound setFloat(String key, float value) {
        insert(nbtTagCompound,"setFloat",key,float.class,value);
        return this;
    }

    public NBTTagCompound setDouble(String key, double value) {
        insert(nbtTagCompound,"setDouble",key,double.class,value);
        return this;
    }

    public NBTTagCompound setString(String key, String string) {
        insert(nbtTagCompound,"setString",key,String.class,string);
        return this;
    }

    public NBTTagCompound setByteArray(String key, byte[] values) {
        insert(nbtTagCompound,"setByteArray",key,byte[].class,values);
        return this;
    }

    public NBTTagCompound setIntArray(String key, int[] values) {
        insert(nbtTagCompound,"setIntArray",key,int[].class,values);
        return this;
    }

    public NBTTagCompound setBoolean(String key, boolean value) {
        insert(nbtTagCompound,"setBoolean",key,boolean.class,value);
        return this;
    }

    public Object get(String key){
        return get(nbtTagCompound,"get",key);
    }

    public byte getByte(String key) {
        return (byte)get(nbtTagCompound,"getByte",key);
    }

    public short getShort(String key) {
        return (short)get(nbtTagCompound,"getShort",key);
    }

    public int getInt(String key) {
        return (int)get(nbtTagCompound,"getInt",key);
    }

    public long getLong(String key) {
        return (long)get(nbtTagCompound,"getLong",key);
    }

    public float getFloat(String key) {
        return (float)get(nbtTagCompound,"getFloat",key);
    }

    public double getDouble(String key) {
        return (double)get(nbtTagCompound,"getDouble",key);
    }

    public String getString(String key){
        return (String)get(nbtTagCompound,"getString",key);
    }

    public byte[] getByteArray(String key) {
        return (byte[]) get(nbtTagCompound,"getByteArray",key);
    }

    public int[] getIntArray(String key) {
        return (int[]) get(nbtTagCompound,"getIntArray",key);
    }

    public boolean getBoolean(String key) {
        return (boolean)get(nbtTagCompound,"getBoolean",key);
    }

    public void remove(String key){
        get(nbtTagCompound,"remove",key);
    }

    public boolean isEmpty(){
        return (boolean)get(nbtTagCompound,"isEmpty");
    }

    public NBTTagCompound getCompound(String key){
        return new v116(get(nbtTagCompound,"getCompound",key));
    }

    public Set<String> getKeys(){
        return (Set<String>)get(nbtTagCompound,"getKeys");
    }

    public Map<String,Object> getMap(){
        Map<String,Object> map = new HashMap<>();
        getKeys().forEach(k -> map.put(k,get(k)));
        return map;
    }

    public boolean hasKey(String key) {
        return (boolean)get(nbtTagCompound,"hasKey",key);
    }
}
