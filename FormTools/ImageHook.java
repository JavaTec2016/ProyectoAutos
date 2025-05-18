package FormTools;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class ImageHook extends JPanel {
    private Image imagen;

    public ImageHook(Image im){
        imagen = im;
        setPreferredSize(new Dimension(im.getWidth(this), im.getHeight(this)));

    }
    public ImageHook(String ruta){
        setImagen(ruta);
        if(imagen == null) return;
        setPreferredSize(new Dimension(imagen.getWidth(this), imagen.getHeight(this)));
    }
    public void setImagen(Image i){
        imagen = i;
    }
    public void setImagen(String ruta){
        URL l = this.getClass().getResource(ruta);
        if(l == null){
            System.out.println("IMAGE HOOK ruta nula: " + ruta);
            return;
        }
        imagen = new ImageIcon(l).getImage();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawImage(imagen, 0, 0, this);
    }
}
