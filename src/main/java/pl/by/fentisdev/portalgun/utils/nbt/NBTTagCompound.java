package pl.by.fentisdev.portalgun.utils.nbt;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.by.fentisdev.portalgun.utils.ItemCreator;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NBTTagCompound implements NBTBase{

    private ItemStack itemStack;
    private Object nbtTagCompound;

    public NBTTagCompound() {
        itemStack = new ItemStack(Material.STICK);
        createNewNBTTag();
    }

    public NBTTagCompound(Object nbtTagCompound){
        itemStack = new ItemStack(Material.STICK);
        this.nbtTagCompound = nbtTagCompound;
    }

    public NBTTagCompound(ItemCreator itemCreator){
        this.itemStack = itemCreator.build();
        if (!itemStack.hasItemMeta()){
            createNewNBTTag();
        }else{
            converter();
        }
    }

    public NBTTagCompound(ItemStack itemStack) {
        this.itemStack = itemStack;
        if (!itemStack.hasItemMeta()){
            createNewNBTTag();
        }else{
            converter();
        }
    }

    private void createNewNBTTag(){
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> tagCompoundClass;
            if(Integer.parseInt(Bukkit.getServer().getVersion().split(":")[1].replace(")","").trim().replace(".",""))>=1170){
                tagCompoundClass = Class.forName("net.minecraft.nbt.NBTTagCompound");
            }else{
                String nmsPack = "net.minecraft.server."+version;
                tagCompoundClass = Class.forName(nmsPack+".NBTTagCompound");
            }
            nbtTagCompound=tagCompoundClass.newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void converter(){
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
            Class<?> tagCompoundClass;
            Class<?> nmsItemstackClass;
            if(Integer.parseInt(Bukkit.getServer().getVersion().split(":")[1].replace(")","").trim().replace(".",""))>=1170){
                tagCompoundClass = Class.forName("net.minecraft.nbt.NBTTagCompound");
                nmsItemstackClass = Class.forName("net.minecraft.world.item.ItemStack");
            }else{
                String nmsPack = "net.minecraft.server."+version;
                tagCompoundClass = Class.forName(nmsPack+".NBTTagCompound");
                nmsItemstackClass = Class.forName(nmsPack+".ItemStack");
            }
            Method setTag = itemstack.getClass().getMethod("setTag",tagCompoundClass);
            setTag.invoke(itemstack,nbtTagCompound);

            Method asBukkitCopy = craftItemstackClass.getMethod("asBukkitCopy",nmsItemstackClass);
            itemStack = (ItemStack) asBukkitCopy.invoke(null,itemstack);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return itemStack;
    }

    public void set(String key, Object object) {
        insert(nbtTagCompound,"set",key,object.getClass(),object);
    }

    public void setByte(String key, byte value) {
        insert(nbtTagCompound,"setByte",key,byte.class,value);
    }

    public void setShort(String key, short value) {
        insert(nbtTagCompound,"setShort",key,short.class,value);
    }

    public void setInt(String key, int value) {
        insert(nbtTagCompound,"setInt",key,int.class,value);
    }

    public void setLong(String key, long value) {
        insert(nbtTagCompound,"setLong",key,long.class,value);
    }

    public void setFloat(String key, float value) {
        insert(nbtTagCompound,"setFloat",key,float.class,value);
    }

    public void setDouble(String key, double value) {
        insert(nbtTagCompound,"setDouble",key,double.class,value);
    }

    public void setString(String key, String string) {
        insert(nbtTagCompound,"setString",key,String.class,string);
    }

    public void setByteArray(String key, byte[] values) {
        insert(nbtTagCompound,"setByteArray",key,byte[].class,values);
    }

    public void setIntArray(String key, int[] values) {
        insert(nbtTagCompound,"setIntArray",key,int[].class,values);
    }

    public void setBoolean(String key, boolean value) {
        insert(nbtTagCompound,"setBoolean",key,boolean.class,value);
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
        return new NBTTagCompound(get(nbtTagCompound,"getCompound",key));
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

    public ItemStack getItemStack() {
        return itemStack;
    }

    public JsonObject toJson(){
        return new GsonBuilder().create().toJsonTree(getMap()).getAsJsonObject();
    }




    private void insert(Object nbtTagCompound, String method, String key,Class<?> clazz, Object obj) {
        invoker(method,nbtTagCompound,key,clazz,obj);
    }

    public Object invoker(String method, Object obj, String key, Class<?> clazz, Object value){
        try {
            Method m = obj.getClass().getMethod(method,String.class,clazz);
            return m.invoke(obj,key,value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object get(Object nbtTagCompound, String method){
        return invokeMethod(method,nbtTagCompound);
    }

    private Object get(Object nbtTagCompound, String method, String key){
        return invokeMethodWithArgs(method,nbtTagCompound,key);
    }

    private Class<?>[] toPrimitiveTypeArray(Class<?>[] classes) {
        int a = classes != null ? classes.length : 0;
        Class<?>[] types = new Class<?>[a];
        for (int i = 0; i < a; i++)
            types[i] = classes[i];
        return types;
    }

    private boolean equalsTypeArray(Class<?>[] a, Class<?>[] o) {
        if (a.length != o.length)
            return false;
        for (int i = 0; i < a.length; i++)
            if (!a[i].equals(o[i]) && !a[i].isAssignableFrom(o[i]))
                return false;
        return true;
    }

    private Method getMethod(String name, Class<?> clazz,
                                    Class<?>... paramTypes) {
        Class<?>[] t = toPrimitiveTypeArray(paramTypes);
        for (Method m : clazz.getMethods()) {
            Class<?>[] types = toPrimitiveTypeArray(m.getParameterTypes());
            if (m.getName().equals(name) && equalsTypeArray(types, t))
                return m;
        }
        return null;
    }

    private Object invokeMethod(String method, Object obj) {
        try {
            return getMethod(method, obj.getClass()).invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object invokeMethodWithArgs(String method, Object obj, Object... args) {
        try {
            Class[] argsClass = new Class[args.length];
            int i = 0;
            for (Object o : args){
                argsClass[i++]=o.getClass();
            }
            return getMethod(method,obj.getClass(),argsClass).invoke(obj,args);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

}
