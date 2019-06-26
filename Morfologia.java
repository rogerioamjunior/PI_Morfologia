import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Morfologia {
    
    public static BufferedImage Dilatacao(BufferedImage image, int[][] mask){
        
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        int max;
        for(int i=1; i<image.getHeight()-1; i++){
         
                for(int j=1; j<image.getWidth()-1; j++){
                    max = 0;

                   
                   for (int l=0; l<3; l++ ) {
                        for (int k=0;k<3;k++){
                            int px = j + (k-1);
                            int py = i + (l-1);
                            int tone = new Color(image.getRGB(px, py)).getRed();
                            
                            if(mask[k][l] != 0){
                               if(tone >max)
                                   max = tone;
                            }
                        }
                        
       
                   } 
                    
                out.setRGB(j, i, new Color(max, max, max).getRGB());
                }
        }
        return out;
    }
    public static BufferedImage erosao(BufferedImage image, int[][] mask){
        
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        int min;
        for(int i=1; i<image.getHeight()-1; i++){
         
                for(int j=1; j<image.getWidth()-1; j++){
                    min = 255;

                   
                   for (int l=0; l<3; l++ ) {
                        for (int k=0;k<3;k++){
                            int px = j + (k-1);
                            int py = i + (l-1);
                            int tone = new Color(image.getRGB(px, py)).getRed();
                            
                            if(mask[k][l] != 0){
                               if(tone <min)
                                   min = tone;
                            }
                        }
                        
       
                   } 
                    
                out.setRGB(j, i, new Color(min, min, min).getRGB());
                }
        }
        return out;
    }
  
    public static BufferedImage fechamento(BufferedImage img, int [][] mask) {
        return erosao(Dilatacao(img, mask), mask);
    }
    public static BufferedImage abertura(BufferedImage img, int [][] mask) {
        return erosao(Dilatacao(img, mask), mask);
    }
    public int aux(float value) {
        int v = (int)value;
        if(v > 255)
            v = 255;
        else if(v < 0)
            v = 0;
        return v;
    }

    public static BufferedImage extracaoCont(BufferedImage normal, BufferedImage imgerod) {

        BufferedImage out = new BufferedImage(normal.getWidth(), normal.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < normal.getHeight(); y++) {
            for (int x = 0; x < normal.getWidth(); x++) {
                //Le o pixel
                Color p1 = new Color(normal.getRGB(x, y));
                Color p2 = new Color(imgerod.getRGB(x, y));

                //Faz a subtração, canal a canal
                int tone = p1.getRed() - p2.getRed();
                if(tone > 255)
                    tone = 255;
                else if(tone < 0)
                    tone = 0;

                out.setRGB(x, y, new Color(tone, tone, tone).getRGB());
            }
        }

        return out;
    }

    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new File("test.jpeg"));
        
        int width = image.getWidth();
	int height = image.getHeight();

        
        int mask[][] =	{	{0, 1, 0},
                                {1, 1, 1},
                                {0, 1, 0},
                                            };
        
        BufferedImage dilat = Dilatacao(image, mask);
        BufferedImage eros = erosao(image, mask);
        BufferedImage abertura= abertura(image, mask);
        BufferedImage fechamento= fechamento(image, mask);
        BufferedImage contorno = extracaoCont(image, eros);
        
        ImageIO.write(dilat, "jpg", new File("dil.jpg"));
        ImageIO.write(eros, "jpg", new File("eros.jpg"));
        ImageIO.write(abertura, "jpg", new File("abertura.jpg"));
        ImageIO.write(fechamento, "jpg", new File("fechamento.jpg"));
        ImageIO.write(contorno, "jpg", new File("contorno.jpg"));
    }
    
}
