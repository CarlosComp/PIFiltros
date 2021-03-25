import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PiFiltro {

    public BufferedImage abreImagem() throws IOException {
        BufferedImage image = ImageIO.read(new File("Imagens-imput\\4.jpg"));
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

    public int[][] Aplicar_Filtro(int altura, int largura, int [][]Matriz, int[][] filtro){
        int p1, p2, p3, p4, p5, p6, p7, p8, p9, p;
        int [][] Matriz2 = new int[altura][largura];
        for (int x = 1; x < altura - 1; x++) {
            for (int y = 1; y < largura - 1; y++) {
                p1 = Matriz[x-1][y-1]*filtro[0][0];
                p2 = Matriz[x-1][y]*filtro[0][1];
                p3 = Matriz[x-1][y+1]*filtro[0][2];
                p4 = Matriz[x][y-1]*filtro[1][0];
                p5 = Matriz[x][y]*filtro[1][1];
                p6 = Matriz[x][y+1]*filtro[1][2];
                p7 = Matriz[x+1][y-1]*filtro[2][0];
                p8 = Matriz[x+1][y]*filtro[2][1];
                p9 = Matriz[x+1][y+1]*filtro[2][2];
                p = p1+p2+p3+p4+p5+p6+p7+p8+p9;
                p = Math.min(255, Math.max(0, p));
                Matriz2[x][y] = 255 - p;
            }
        }
        return Matriz2;
    }

    public void Sobel(BufferedImage Imagem1) throws IOException {
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

        int [][] SobelHorizontal = new int [3][3];
        SobelHorizontal[0][0] = -1;
        SobelHorizontal[0][1] = -2;
        SobelHorizontal[0][2] = -1;
        SobelHorizontal[2][0] = 1;
        SobelHorizontal[2][1] = 2;
        SobelHorizontal[2][2] = 1;

        int [][] SobelVertical = new int [3][3];
        SobelVertical[0][0] = -1;
        SobelVertical[1][0] = -2;
        SobelVertical[2][0] = -1;
        SobelVertical[0][2] = 1;
        SobelVertical[1][2] = 2;
        SobelVertical[2][2] = 1;

        MatrizRed2 = Aplicar_Filtro(altura, largura, MatrizRed, SobelHorizontal);
        MatrizGreen2 = Aplicar_Filtro(altura, largura, MatrizGreen, SobelHorizontal);
        MatrizBlue2 = Aplicar_Filtro(altura, largura, MatrizBlue, SobelHorizontal);

        BufferedImage aux = new BufferedImage(Imagem1.getWidth(), Imagem1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int i=0;i<Imagem1.getWidth() ;i++){
            for(int j=0;j<Imagem1.getHeight();j++){
                Color color = new Color(MatrizRed2[j][i], MatrizGreen2[j][i], MatrizBlue2[j][i]);
                aux.setRGB(i, j, color.getRGB());
            }
        }
        Cria_Imagem_Alterada(Image_To_Matriz(aux), "sobelHorizontal");

        MatrizRed2 = Aplicar_Filtro(altura, largura, MatrizRed, SobelVertical);
        MatrizGreen2 = Aplicar_Filtro(altura, largura, MatrizGreen, SobelVertical);
        MatrizBlue2 = Aplicar_Filtro(altura, largura, MatrizBlue, SobelVertical);

        aux = new BufferedImage(Imagem1.getWidth(), Imagem1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int i=0;i<Imagem1.getWidth() ;i++){
            for(int j=0;j<Imagem1.getHeight();j++){
                Color color = new Color(MatrizRed2[j][i], MatrizGreen2[j][i], MatrizBlue2[j][i]);
                aux.setRGB(i, j, color.getRGB());
            }
        }
        Cria_Imagem_Alterada(Image_To_Matriz(aux), "sobelVertical");
    }

    public void laplaciano(BufferedImage Imagem1) throws IOException {
        int altura = Imagem1.getHeight();
        int largura = Imagem1.getWidth();
        int count = 0;

        int[] VetRed = new int[largura*altura];
        int[] VetGreen = new int[largura*altura];
        int[] VetBlue = new int[largura*altura];
        int [][] MatrizRed = new int[altura][largura];
        int [][] MatrizGreen = new int[altura][largura];
        int [][] MatrizBlue = new int[altura][largura];
        int [][] MatrizRed2 = new int[altura][largura];
        int [][] MatrizGreen2 = new int[altura][largura];
        int [][] MatrizBlue2 = new int[altura][largura];

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

        for(int i=0;i<altura;i++){
            for(int j=0;j<largura;j++){
                MatrizRed[i][j]=VetRed[count];
                MatrizGreen[i][j]=VetGreen[count];
                MatrizBlue[i][j]=VetBlue[count];
                count++;
            }
        }

        int [][] laplace1 = new int [3][3];
        laplace1[0][0] = -1;
        laplace1[0][1] = -1;
        laplace1[0][2] = -1;
        laplace1[1][0] = -1;
        laplace1[1][1] = 8;
        laplace1[1][2] = -1;
        laplace1[2][0] = -1;
        laplace1[2][1] = -1;
        laplace1[2][2] = -1;


        int [][] laplace2 = new int [3][3];
        laplace2[0][0] = 1;
        laplace2[0][1] = 1;
        laplace2[0][2] = 1;
        laplace2[1][0] = 1;
        laplace2[1][1] = -8;
        laplace2[1][2] = 1;
        laplace2[2][0] = 1;
        laplace2[2][1] = 1;
        laplace2[2][2] = 1;

        int [][] laplace3 = new int [3][3];
        laplace3[0][0] = 0;
        laplace3[0][1] = 1;
        laplace3[0][2] = 0;
        laplace3[1][0] = 1;
        laplace3[1][1] = -4;
        laplace3[1][2] = 1;
        laplace3[2][0] = 0;
        laplace3[2][1] = 1;
        laplace3[2][2] = 0;

        int [][] laplace4 = new int [3][3];
        laplace4[0][0] = 0;
        laplace4[0][1] = -1;
        laplace4[0][2] = 0;
        laplace4[1][0] = -1;
        laplace4[1][1] = 4;
        laplace4[1][2] = -1;
        laplace4[2][0] = 0;
        laplace4[2][1] = -1;
        laplace4[2][2] = 0;

        MatrizRed2 = Aplicar_Filtro(altura, largura, MatrizRed, laplace1);
        MatrizGreen2 = Aplicar_Filtro(altura, largura,MatrizGreen, laplace1);
        MatrizBlue2 = Aplicar_Filtro(altura, largura, MatrizBlue, laplace1);

        BufferedImage aux = new BufferedImage(Imagem1.getWidth(), Imagem1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int i=0;i<Imagem1.getWidth() ;i++){
            for(int j=0;j<Imagem1.getHeight();j++){
                Color color = new Color(MatrizRed2[j][i], MatrizGreen2[j][i], MatrizBlue2[j][i]);
                aux.setRGB(i, j, color.getRGB());
            }
        }
        Cria_Imagem_Alterada(Image_To_Matriz(aux), "laplaciano-1");

        MatrizRed2 = Aplicar_Filtro(altura, largura, MatrizRed, laplace2);
        MatrizGreen2 = Aplicar_Filtro(altura, largura,MatrizGreen, laplace2);
        MatrizBlue2 = Aplicar_Filtro(altura, largura, MatrizBlue, laplace2);

        aux = new BufferedImage(Imagem1.getWidth(), Imagem1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int i=0;i<Imagem1.getWidth() ;i++){
            for(int j=0;j<Imagem1.getHeight();j++){
                Color color = new Color(MatrizRed2[j][i], MatrizGreen2[j][i], MatrizBlue2[j][i]);
                aux.setRGB(i, j, color.getRGB());
            }
        }
        Cria_Imagem_Alterada(Image_To_Matriz(aux), "laplaciano-2");

        MatrizRed2 = Aplicar_Filtro(altura, largura, MatrizRed, laplace3);
        MatrizGreen2 = Aplicar_Filtro(altura, largura,MatrizGreen, laplace3);
        MatrizBlue2 = Aplicar_Filtro(altura, largura, MatrizBlue, laplace3);

        aux = new BufferedImage(Imagem1.getWidth(), Imagem1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int i=0;i<Imagem1.getWidth() ;i++){
            for(int j=0;j<Imagem1.getHeight();j++){
                Color color = new Color(MatrizRed2[j][i], MatrizGreen2[j][i], MatrizBlue2[j][i]);
                aux.setRGB(i, j, color.getRGB());
            }
        }
        Cria_Imagem_Alterada(Image_To_Matriz(aux), "laplaciano-3");

        MatrizRed2 = Aplicar_Filtro(altura, largura, MatrizRed, laplace4);
        MatrizGreen2 = Aplicar_Filtro(altura, largura,MatrizGreen, laplace4);
        MatrizBlue2 = Aplicar_Filtro(altura, largura, MatrizBlue, laplace4);

        aux = new BufferedImage(Imagem1.getWidth(), Imagem1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int i=0;i<Imagem1.getWidth() ;i++){
            for(int j=0;j<Imagem1.getHeight();j++){
                Color color = new Color(MatrizRed2[j][i], MatrizGreen2[j][i], MatrizBlue2[j][i]);
                aux.setRGB(i, j, color.getRGB());
            }
        }
        Cria_Imagem_Alterada(Image_To_Matriz(aux), "laplaciano-4");
    }

    public void Media(BufferedImage Imagem1) throws IOException {
        int largura = Imagem1.getWidth();
        int altura = Imagem1.getHeight();

        BufferedImage aux = new BufferedImage(largura, altura,
                BufferedImage.TYPE_INT_ARGB);
        int p1, p2, p3, p4, p5, p6, p7, p8, p9, p;
        for (int x = 1; x < altura - 1; x++) {
            for (int y = 1; y < largura - 1; y++) {
                p1 = Imagem1.getRGB(x-1,y-1);
                p2 = Imagem1.getRGB(x-1,y);
                p3 = Imagem1.getRGB(x-1,y+1);
                p4 = Imagem1.getRGB(x,y-1);
                p5 = Imagem1.getRGB(x,y);
                p6 = Imagem1.getRGB(x,y+1);
                p7 = Imagem1.getRGB(x+1,y-1);
                p8 = Imagem1.getRGB(x+1,y);
                p9 = Imagem1.getRGB(x+1,y+1);
                p = (p1+p2+p3+p4+p5+p6+p7+p8+p9)/9;
                p = Math.min(255, Math.max(0, p));
                aux.setRGB(x, y, p);
            }
        }

        Cria_Imagem_Alterada(Image_To_Matriz(aux), "media");
    }


    public static void main(String[] args) throws IOException {
        PiFiltro Executar = new PiFiltro();
        Executar.laplaciano(Executar.abreImagem());
        Executar.Sobel(Executar.abreImagem());
        Executar.Media(Executar.abreImagem());
    }

}
