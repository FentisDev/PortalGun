package pl.by.fentisdev.portalgun.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import pl.by.fentisdev.portalgun.PortalGunMain;

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

    public void addRecipe(){
        if (Bukkit.getRecipe(recipe.getKey())!=null){
            return;
        }
        Bukkit.addRecipe(recipe);
    }

}
