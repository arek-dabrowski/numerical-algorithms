package p1;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

public class Randomizer {
    private int max = 65535;
    private int min = -65536;
    private Random random;

    public Randomizer(){
        random = new Random(max-200);
    }

    public void setRandomMatrix(int size) {
        PrintWriter writer;
        try {
            writer = new PrintWriter("matrix.txt");

            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    writer.print(getRandomInt()+" ");
                }
                writer.println();
            }

            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setRandomVector(int size) {
        PrintWriter writer;
        try {
            writer = new PrintWriter("vector.txt");

            for(int i = 0; i < size; i++){
                writer.print(getRandomInt()+" ");
            }

            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



    private int getRandomInt(){
        int number = random.nextInt(max - min + 1) + min;
        return number;
    }

}
