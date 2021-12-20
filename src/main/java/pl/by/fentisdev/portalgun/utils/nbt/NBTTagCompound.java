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

public abstract class NBTTagCompound implements NBTBase{

    public ItemStack itemStack;
    public Object nbtTagCompound;

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


    public abstract void createNewNBTTag();

    public abstract void converter();

    public ItemStack save(ItemStack itemStack){
        this.itemStack = itemStack;
        return save();
    }

    public abstract ItemStack save();

    public abstract NBTTagCompound set(String key, Object object);

    public abstract NBTTagCompound setByte(String key, byte value);

    public abstract NBTTagCompound setShort(String key, short value);

    public abstract NBTTagCompound setInt(String key, int value);

    public abstract NBTTagCompound setLong(String key, long value);

    public abstract NBTTagCompound setFloat(String key, float value);

    public abstract NBTTagCompound setDouble(String key, double value);

    public abstract NBTTagCompound setString(String key, String string);

    public abstract NBTTagCompound setByteArray(String key, byte[] values);

    public abstract NBTTagCompound setIntArray(String key, int[] values);

    public abstract NBTTagCompound setBoolean(String key, boolean value);

    public abstract Object get(String key);

    public abstract byte getByte(String key);

    public abstract short getShort(String key);

    public abstract int getInt(String key);

    public abstract long getLong(String key);

    public abstract float getFloat(String key);

    public abstract double getDouble(String key);

    public abstract String getString(String key);

    public abstract byte[] getByteArray(String key);

    public abstract int[] getIntArray(String key);

    public abstract boolean getBoolean(String key);

    public abstract void remove(String key);

    public abstract boolean isEmpty();

    public abstract NBTTagCompound getCompound(String key);

    public abstract Set<String> getKeys();

    public abstract Map<String,Object> getMap();

    public abstract boolean hasKey(String key);

    public ItemStack getItemStack(){
        return itemStack;
    };

    public JsonObject toJson(){
        return new GsonBuilder().create().toJsonTree(getMap()).getAsJsonObject();
    }

    public void insert(Object nbtTagCompound, String method, String key,Class<?> clazz, Object obj) {
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

    public Object get(Object nbtTagCompound, String method){
        return invokeMethod(method,nbtTagCompound);
    }

    public Object get(Object nbtTagCompound, String method, String key){
        return invokeMethodWithArgs(method,nbtTagCompound,key);
    }

    public Class<?>[] toPrimitiveTypeArray(Class<?>[] classes) {
        int a = classes != null ? classes.length : 0;
        Class<?>[] types = new Class<?>[a];
        for (int i = 0; i < a; i++)
            types[i] = classes[i];
        return types;
    }

    public boolean equalsTypeArray(Class<?>[] a, Class<?>[] o) {
        if (a.length != o.length)
            return false;
        for (int i = 0; i < a.length; i++)
            if (!a[i].equals(o[i]) && !a[i].isAssignableFrom(o[i]))
                return false;
        return true;
    }

    public Method getMethod(String name, Class<?> clazz,
                                    Class<?>... paramTypes) {
        Class<?>[] t = toPrimitiveTypeArray(paramTypes);
        for (Method m : clazz.getMethods()) {
            Class<?>[] types = toPrimitiveTypeArray(m.getParameterTypes());
            if (m.getName().equals(name) && equalsTypeArray(types, t))
                return m;
        }
        return null;
    }

    public Object invokeMethod(String method, Object obj) {
        try {
            return getMethod(method, obj.getClass()).invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object invokeMethodWithArgs(String method, Object obj, Object... args) {
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
