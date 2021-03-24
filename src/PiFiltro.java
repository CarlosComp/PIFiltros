import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PiFiltro {


    public BufferedImage abreImagem() throws IOException {
        BufferedImage image = ImageIO.read(new File("Imagens-imput\\3.jpg"));
        return image;

    }
    public int[][] Image_To_Matriz(BufferedImage ImagemCarregada){
        int altura, largura;
        altura = ImagemCarregada.getHeight();
        largura = ImagemCarregada.getWidth();
        int vetAuxPixel[] = new int[largura*altura];
        vetAuxPixel = ImagemCarregada.getRGB(0, 0, largura, altura, null, 0, largura);
        int matrizPixel[][] = new int[altura][largura];
        int count = 0;
        for (int i=0;i<altura;i++)
        {
            for (int j=0;j<largura;j++)
            {
                matrizPixel[i][j]= vetAuxPixel[count];
                count++;
            }
        }
        return matrizPixel;
    }

    public void Cria_Imagem_Alterada(int [][] MatrizPixel,String NomeImagem) throws IOException{
        int new_altura,new_largura;
        new_altura=MatrizPixel.length;
        new_largura=MatrizPixel[1].length;
        int[] vetAux= new int[new_largura*new_altura];
        int count=0;
        for (int i=0;i<new_altura;i++){
            for(int j=0;j<new_largura;j++){
                vetAux[count]=MatrizPixel[i][j];
                count++;
            }
        }
        BufferedImage new_image_altera = new BufferedImage(new_largura, new_altura,BufferedImage.TYPE_INT_RGB);
        new_image_altera.setRGB(0, 0, new_largura, new_altura, vetAux, 0, new_largura);
        ImageIO.write(new_image_altera,"JPG", new File(NomeImagem+".jpg"));
    }

    public void laplaciano(BufferedImage Imagem1) throws IOException {
        int altura = Imagem1.getHeight();
        int largura = Imagem1.getWidth();
        int count = 0;

        int[] VetRed = new int[largura*altura];
        int[] VetGreen = new int[largura*altura];
        int[] VetBlue = new int[largura*altura];

        for(int i=0;i<altura;i++){
            for(int j=0;j<largura;j++){
                Color color = new Color(Imagem1.getRGB(j, i));
                VetRed[count]=color.getRed();
                VetBlue[count]=color.getBlue();
                VetGreen[count]=color.getGreen();
                count++;
            }
        }
        count=0;
        int [][] MatrizRed = new int[altura][largura];
        int [][] MatrizGreen = new int[altura][largura];
        int [][] MatrizBlue = new int[altura][largura];
        int [][] MatrizRed2 = new int[altura][largura];
        int [][] MatrizGreen2 = new int[altura][largura];
        int [][] MatrizBlue2 = new int[altura][largura];
        for(int i=0;i<altura;i++){
            for(int j=0;j<largura;j++){
                MatrizRed[i][j]=VetRed[count];
                MatrizGreen[i][j]=VetGreen[count];
                MatrizBlue[i][j]=VetBlue[count];
                count++;
            }
        }

        for (int x = 1; x < altura - 1; x++) {
            for (int y = 1; y < largura - 1; y++) {
                int Red00 = MatrizRed[x-1][y-1];
                int Red01 = MatrizRed[x-1][y];
                int Red02 = MatrizRed[x-1][y+1];
                int Red10 = MatrizRed[x][y-1];
                int Red11 = MatrizRed[x][y];
                int Red12 = MatrizRed[x][y+1];
                int Red20 = MatrizRed[x+1][y-1];
                int Red21 = MatrizRed[x+1][y];
                int Red22 = MatrizRed[x+1][y+1];
                int Green00 = MatrizGreen[x-1][y-1];
                int Green01 = MatrizGreen[x-1][y];
                int Green02 = MatrizGreen[x-1][y+1];
                int Green10 = MatrizGreen[x][y-1];
                int Green11 = MatrizGreen[x][y];
                int Green12 = MatrizGreen[x][y+1];
                int Green20 = MatrizGreen[x+1][y-1];
                int Green21 = MatrizGreen[x+1][y];
                int Green22 = MatrizGreen[x+1][y+1];
                int Blue00 = MatrizBlue[x-1][y-1];
                int Blue01 = MatrizBlue[x-1][y];
                int Blue02 = MatrizBlue[x-1][y+1];
                int Blue10 = MatrizBlue[x][y-1];
                int Blue11 = MatrizBlue[x][y];
                int Blue12 = MatrizBlue[x][y+1];
                int Blue20 = MatrizBlue[x+1][y-1];
                int Blue21 = MatrizBlue[x+1][y];
                int Blue22 = MatrizBlue[x+1][y+1];

                int r = (-Red00 - Red01 - Red02 -
                        Red10 + (8*Red11) - Red12 -
                        Red20 -   Red21 - Red22);
                int g = (-Green00 - Green01 - Green02 -
                        Green10 + (8*Green11) - Green12 -
                        Green20 -   Green21 - Green22);
                int b = (-Blue00 - Blue01 - Blue02 -
                        Blue10 + (8*Blue11) - Blue12 -
                        Blue20 -   Blue21 - Blue22);
                r = Math.min(255, Math.max(0, r));
                g = Math.min(255, Math.max(0, g));
                b = Math.min(255, Math.max(0, b));
                MatrizRed2[x][y]=r;
                MatrizGreen2[x][y]=g;
                MatrizBlue2[x][y]=b;
            }
        }
        BufferedImage aux = new BufferedImage(Imagem1.getWidth(), Imagem1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int i=0;i<Imagem1.getWidth() ;i++){
            for(int j=0;j<Imagem1.getHeight();j++){
                Color color = new Color(MatrizRed2[j][i], MatrizGreen2[j][i], MatrizBlue2[j][i]);
                aux.setRGB(i, j, color.getRGB());
            }
        }
        Cria_Imagem_Alterada(Image_To_Matriz(aux), "Laplaciano");
    }

    public static void main(String[] args) throws IOException {
        PiFiltro Executar = new PiFiltro();
        Executar.laplaciano(Executar.abreImagem());
    }

}
