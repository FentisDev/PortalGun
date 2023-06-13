package pl.by.fentisdev.portalgun.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import pl.by.fentisdev.itemcreator.ItemCreator;
import pl.by.fentisdev.portalgun.PortalGunMain;

import java.lang.reflect.InvocationTargetException;

public class RecipeCreator {

    private ShapedRecipe recipe;

    public RecipeCreator(String name, ItemCreator results) {
        NamespacedKey key = new NamespacedKey(PortalGunMain.getInstance(), name);
        recipe = new ShapedRecipe(key, results.build());
    }

    public RecipeCreator addShape(String... shape){
        recipe.shape(shape);
        return this;
    }

    public RecipeCreator setIngredient(char key, Material material){
        recipe.setIngredient(key,material);
        return this;
    }

    public void addRecipe() {
        String version = getVersion();
        boolean isLow = version.contains("v1_14")||version.contains("v1_15");
        if (!isLow){
            if (Bukkit.getRecipe(recipe.getKey())!=null){
                return;
            }
        }
        Bukkit.addRecipe(recipe);
    }

    private String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

}
