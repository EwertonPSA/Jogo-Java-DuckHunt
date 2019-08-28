import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Tela para desenho.
 * 
 */
public class Tela{
    Graphics2D g;
    
    public static HashMap<String, BufferedImage> sprites = new HashMap<>();
    
    public Tela(Graphics2D g) {
        this.g = g;
        g.setColor(Color.white);
    }

    public void imagem(String arquivo, int xa, int ya, int larg, int alt, double x, double y) {
        if(!sprites.containsKey(arquivo)) {
            try {
                sprites.put(arquivo, ImageIO.read(new File(arquivo)));
            } catch(java.io.IOException ioex) {
                throw new RuntimeException(ioex);
            }
        }
        g.drawImage(sprites.get(arquivo), (int)Math.round(x), (int)Math.round(y), (int)Math.round(x) + larg, (int)Math.round(y) + alt,
                    xa, ya, xa + larg, ya + alt, null);
    }
}
