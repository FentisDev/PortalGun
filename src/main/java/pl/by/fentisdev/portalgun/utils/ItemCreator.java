package pl.by.fentisdev.portalgun.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ItemCreator {

    private Material material;
    private int amount = 1;
    private String displayName = "";
    private List<String> lore = new ArrayList<>();
    private boolean unbreakable = false;
    private int customModelData = 0;
    private List<EnchantmentAndLevel> enchantments = new ArrayList<>();
    private Book book = new Book();
    private NBTItem nbt;

    public ItemCreator(ItemCreator copy){
        this.material = copy.getMaterial();
        this.amount = copy.getAmount();
        this.displayName = copy.getDisplayName();
        this.lore = copy.getLore();
        this.unbreakable = copy.isUnbreakable();
        this.customModelData = copy.getCustomModelData();
        this.nbt = copy.getNBTItem();
    }

    public ItemCreator(ItemStack stack){
        if (stack==null){
            this.material = Material.AIR;
            return;
        }
        this.material = stack.getType();
        this.amount = stack.getAmount();
        this.nbt = new NBTItem(stack);
        if (!stack.hasItemMeta()){
            return;
        }
        this.displayName = stack.getItemMeta().getDisplayName();
        this.lore = stack.getItemMeta().hasLore()?stack.getItemMeta().getLore():new ArrayList<>();
        this.unbreakable = stack.getItemMeta().isUnbreakable();
        this.customModelData = stack.getItemMeta().hasCustomModelData()?stack.getItemMeta().getCustomModelData():0;
    }

    public ItemCreator(Material material) {
        this.material = material;
        this.nbt = new NBTItem(new ItemStack(material));
    }

    public ItemCreator(Material material, int amount) {
        this.material = material;
        this.amount = amount;
        this.nbt = new NBTItem(new ItemStack(material,amount));
    }

    public ItemCreator(JsonObject json){
        Gson gson = new GsonBuilder().create();
        material = Material.getMaterial(json.get("Material").getAsString());
        amount = json.get("Amount").getAsInt();
        displayName = json.get("DisplayName").getAsString();
        lore = gson.fromJson(json.getAsJsonArray("Lore").toString(), ArrayList.class);
        unbreakable = json.get("Unbreakable").getAsBoolean();
        customModelData = json.get("CustomModelData").getAsInt();
        enchantments = gson.fromJson(json.getAsJsonArray("Enchantments").toString(),ArrayList.class);
        book = new Book(json.getAsJsonObject("Book"));
        nbt = new NBTItem(new ItemStack(material,amount));
        nbt.mergeCompound(new NBTContainer(json.get("NBTItem").getAsString()));
    }


    public Material getMaterial() {
        return material;
    }

    public ItemCreator setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public ItemCreator setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemCreator setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public List<String> getLore() {
        return lore;
    }

    public ItemCreator setLore(String... lore){
        this.lore = Arrays.asList(lore);
        return this;
    }

    public ItemCreator setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public ItemCreator setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public ItemCreator setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    public ItemCreator setEnchantment(Enchantment enchantment, int level) {
        if (enchantments.isEmpty()){
            enchantments.add(new EnchantmentAndLevel(enchantment,level));
        }else{
            boolean status = true;
            for (EnchantmentAndLevel enl : enchantments){
                if (enl.getEnchantment().getName() == enchantment.getName()){
                    enl.setLevel(level);
                    status = false;
                }
            }
            if (status){
                enchantments.add(new EnchantmentAndLevel(enchantment,level));
            }
        }
        return this;
    }

    public ItemCreator setEnchantment(List<EnchantmentAndLevel> enchantments){
        this.enchantments = enchantments;
        return this;
    }

    public Book getBook() {
        return book;
    }

    public ItemCreator setBook(Book book) {
        this.book = book;
        return this;
    }

    public NBTItem getNBTItem() {
        return nbt;
    }

    public ItemCreator setNBTItem(NBTItem nbt) {
        this.nbt = nbt;
        return this;
    }

    public ItemCreator setSkull(String playerName){

        return this;
    }

    public ItemStack build(){
        ItemStack itemStack = new ItemStack(material,amount);
        if (nbt!=null&&!nbt.getKeys().isEmpty()){
            itemStack = nbt.getItem();
        }
        ItemMeta meta = itemStack.getItemMeta();
        if (!displayName.isEmpty()){
            meta.setDisplayName(displayName);
        }
        if (lore!=null&&!lore.isEmpty()){
            meta.setLore(lore);
        }
        meta.setUnbreakable(unbreakable);
        for (EnchantmentAndLevel enl : enchantments){
            meta.addEnchant(enl.getEnchantment(),enl.getLevel(),true);
        }
        if (material == Material.WRITTEN_BOOK && !book.getBookPages().isEmpty()){
            BookMeta bm = (BookMeta) meta;
            bm.setAuthor(book.getOwner());
            bm.setTitle(book.getTitle());
            int max = 0;
            for (BookPage p : book.getBookPages()){
                if (p.getPage()>max){
                    max = p.getPage();
                }
            }
            for (int j = 0; j < max; j++) {
                bm.addPage(" ");
            }
            for (BookPage p : book.getBookPages()){
                bm.setPage(p.getPage(),p.getWritten());
            }
            //i.setItemMeta(bm);
        }
        if (customModelData!=0){
            meta.setCustomModelData(customModelData);
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public String toString(){
        Gson gson = new GsonBuilder().create();
        JsonObject json = new JsonObject();
        json.addProperty("Material",material.getData().getName());
        json.addProperty("Amount",amount);
        json.addProperty("DisplayName",displayName);
        json.add("Lore",gson.toJsonTree(lore).getAsJsonArray());
        json.addProperty("Unbreakable",unbreakable);
        json.addProperty("CustomModelData",customModelData);
        json.add("Enchantments", gson.toJsonTree(enchantments).getAsJsonArray());
        json.add("Book",book.getJson());
        json.addProperty("NBTItem",nbt.toString());
        return json.getAsString();
    }

    public class EnchantmentAndLevel{

        private Enchantment enchantment;
        private int level;

        public EnchantmentAndLevel(Enchantment enchantment, int level) {
            this.enchantment = enchantment;
            this.level = level;
        }

        public Enchantment getEnchantment() {
            return enchantment;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String toString(){
            JsonObject json = new JsonObject();
            json.addProperty("Enchantment",enchantment.toString());
            json.addProperty("Level",level);
            return json.getAsString();
        }
    }

    public class Book{
        private List<BookPage> bookPages = new ArrayList<>();
        private String title = "";
        private String owner = "Unknown";

        public Book() {
        }

        public Book(String title, String owner, List<BookPage> bookPages) {
            this.bookPages = bookPages;
            this.title = title;
            this.owner = owner;
        }

        public Book(JsonObject json) {
            title = json.get("Title").getAsString();
            owner = json.get("Owner").getAsString();
            for (JsonElement pages : json.getAsJsonArray("BookPages")) {
                bookPages.add(new BookPage(pages.getAsJsonObject()));
            }
        }

        public List<BookPage> getBookPages() {
            return bookPages;
        }

        public String getTitle() {
            return title;
        }

        public Book setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getOwner() {
            return owner;
        }

        public Book setOwner(String owner) {
            this.owner = owner;
            return this;
        }

        public Book setBookPages(List<BookPage> bookPages) {
            this.bookPages = bookPages;
            return this;
        }

        public Book addBookPage(int page, String... written){
            String convert = StringUtils.join(written,"\n");
            BookPage nbp = null;
            for (BookPage p : bookPages){
                if (p.getPage() == page){
                    nbp = p;
                }
            }
            if (nbp!=null){
                nbp.setWritten(convert);
            }else{
                bookPages.add(new BookPage(convert,page));
            }
            return this;
        }

        public BookPage getBookPage(int page){
            return bookPages.stream().filter(bp->bp.getPage()==page).findFirst().orElse(null);
        }

        public JsonObject getJson(){
            Gson gson = new GsonBuilder().create();
            JsonObject json = new JsonObject();
            json.addProperty("Title",title);
            json.addProperty("Owner",owner);
            json.add("BookPages",gson.toJsonTree(bookPages.stream().map(BookPage::toJson).collect(Collectors.toList())).getAsJsonArray());
            return json;
        }
    }
    public class BookPage{
        private String written;
        private int page;

        public BookPage(String written, int page) {
            this.written = written;
            this.page = page;
        }

        public BookPage(JsonObject json) {
            page = json.get("Page").getAsInt();
            written = json.get("Written").getAsString();
        }

        public String getWritten() {
            return written;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public void setWritten(String written) {
            this.written = written;
        }

        public JsonObject toJson(){
            JsonObject json = new JsonObject();
            json.addProperty("Page",page);
            json.addProperty("Written",written);
            return json;
        }
    }

    public class Skull{

        public Skull(String playerName) {

        }

        public Skull() {
        }
    }
}
