package pl.by.fentisdev.portalgun.portalgun;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class PortalRender extends MapRenderer {

    private BufferedImage image;

    public PortalRender(PortalSide side, PortalColors color) {
        try {
            URL url = getClass().getResource("/imgs/portal_"+color.toString().toLowerCase()+"_"+side.toString().toLowerCase()+".png");
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        if (image!=null){
            mapCanvas.drawImage(0,0,image);
        }
    }
}
