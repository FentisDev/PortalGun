package pl.by.fentisdev.portalgun.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import pl.by.fentisdev.portalgun.PortalGunMain;
import pl.by.fentisdev.portalgun.portalgun.PortalGunManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HoldingFile {

    @Getter
    private static HoldingFile instance = new HoldingFile();

    private List<UUID> holdingsList = new ArrayList<>();

    public List<UUID> getHoldingsList() {
        return holdingsList;
    }

    public void removeHolding(List<UUID> uuidList){
        uuidList.forEach(this::removeHolding);
    }

    public void removeHolding(UUID uuid){
        holdingsList.remove(uuid);
    }

    public void saveHoldings(){
        JsonObject jsonObject = new JsonObject();
        JsonArray holdings = new JsonArray();
        PortalGunManager.getInstance().getHolding().forEach((p,e) -> holdings.add(e.getUniqueId().toString()));
        jsonObject.add("HoldingEntities",holdings);
        try {
            File file = new File(PortalGunMain.getInstance().getDataFolder(),"holdings.json");
            FileWriter writer = new FileWriter(file);
            writer.write(jsonObject.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readHoldings(){
        File file = new File(PortalGunMain.getInstance().getDataFolder(),"holdings.json");
        if (!file.exists()){
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            JsonElement jsonElement = new JsonParser().parse(new FileReader(file));
            JsonObject jsonObject = null;
            try {
                jsonObject = (JsonObject) jsonElement;
            } catch (Exception e) {

            }
            if (jsonObject != null) {
                for (JsonElement h : jsonObject.getAsJsonArray("HoldingEntities")) {
                    holdingsList.add(UUID.fromString(h.getAsString()));
                }
            }
        }catch (FileNotFoundException e){

        }
    }
}
