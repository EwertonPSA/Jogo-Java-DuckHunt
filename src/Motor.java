import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Canvas;

/* Motor do jogo, gerencia a parte grafica e os eventos relacionados com teclado e mouse. */
public class Motor{
	
    public Jogo jogo;
    public BufferStrategy strategy;
    
    public Motor(Jogo j) {
        jogo = j;
        Canvas canvas = new Canvas();
        JFrame container = new JFrame(jogo.getTitulo());
        Cursor cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(
                jogo.getLargura(), jogo.getAltura()));
        panel.setLayout(null);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        Rectangle bounds = gs[gs.length-1].getDefaultConfiguration().getBounds();
        container.setResizable(false);
        container.setBounds(bounds.x+(bounds.width - jogo.getLargura())/2,
                            bounds.y+(bounds.height - jogo.getAltura())/2,
                            jogo.getLargura(),jogo.getAltura());
        canvas.setBounds(0,0,jogo.getLargura(),jogo.getAltura());
        panel.add(canvas);   
        canvas.setIgnoreRepaint(true);
        container.getContentPane().setCursor(cursor);
        container.pack();
        container.setVisible(true);
        container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent evt) {
            	jogo.tecla(keyString(evt));
            }
            @Override
            public void keyTyped(KeyEvent evt) {
                jogo.tecla(keyString(evt));
            }
            @Override
            public void keyPressed(KeyEvent evt) {
            	jogo.tecla(keyString(evt));
            }
        });
        canvas.addMouseListener(new MouseListener() {
        	@Override
        	public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {/*Identificar a posicao em que foi clicada na tela*/
				jogo.mouse(e.getX(), e.getY());
			}
			@Override
			public void mouseReleased(MouseEvent e) {}
        });
        canvas.createBufferStrategy(2);
        strategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        mainLoop();
    }
    
    /*Impressao de imagem a cada tique*/
    private void mainLoop() {
        Timer t = new Timer(5, new ActionListener() {
            public long t0;
            public void actionPerformed(ActionEvent evt) {
                long t1 = System.currentTimeMillis();
                if(t0 == 0)
                    t0 = t1;
                if(t1 > t0) {
                    double dt = (t1 - t0) / 1000.0;
                    t0 = t1;
                    jogo.tique(dt);/*Atualização */
                    Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
                    g.setColor(Color.black);
                    g.fillRect(0,0,jogo.getLargura(),
                          jogo.getAltura());
                    jogo.desenhar(new Tela(g));
                    strategy.show();
                }
            }
        }); 
        t.start();
    }
    
    /*	Definir uma chave para cada tecla pressionada. */
    private static String keyString(KeyEvent evt) {
        if(evt.getKeyChar() != KeyEvent.CHAR_UNDEFINED) {
            return String.valueOf(evt.getKeyChar()).toLowerCase();/*Retorna coisas do teclado*/
        } else {
            switch(evt.getKeyCode()) {
	        	case KeyEvent.VK_LEFT: return "left";  	
	        	case KeyEvent.VK_RIGHT: return "right";
	            case KeyEvent.VK_UP: return "up";
	            case KeyEvent.VK_DOWN: return "down";
	            case KeyEvent.VK_ENTER: return "enter";
	            case KeyEvent.VK_BACK_SPACE: return "backspace";
	            default: return "";
            }
        }
    }

    
}
