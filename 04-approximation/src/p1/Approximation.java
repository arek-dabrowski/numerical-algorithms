package p1;

import types.MyDouble;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Approximation {
    public double numberOfEquations[] = new double[48];
    public double partialGauss[] = new double[48];
    public double optimizedGauss[] = new double[48];
    public double gaussSeidel[] = new double[48];
    public double sparseLULib[] = new double[48];

    public void loadData() {
        FileReader fileReader;
        BufferedReader bufferedReader;
        try {
            fileReader = new FileReader("timesTest.csv");

            bufferedReader = new BufferedReader(fileReader);

            String line;
            line = bufferedReader.readLine();


            for (int i = 0; i < 48; i++) {
                line = bufferedReader.readLine();
                String number[] = line.split(";");
                numberOfEquations[i] = Double.parseDouble(number[1]);
                partialGauss[i] = Double.parseDouble(number[4]);
                optimizedGauss[i] = Double.parseDouble(number[5]);
                gaussSeidel[i] = Double.parseDouble(number[6]);
                sparseLULib[i] = Double.parseDouble(number[7]);
            }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test() {
        loadData();
        doApproximation(partialGauss, 3);
        doApproximation(optimizedGauss, 2);
        doApproximation(gaussSeidel, 2);
        doApproximation(sparseLULib, 1);
    }

    private void doApproximation(double[] method, int num) {
        MyMatrix<MyDouble> matrix = new MyMatrix<>(MyDouble.class, num+1, num+1);
        double sum;

        for (int k = 0; k <= num; k++) {
            int pom = 0;
            for (int j = k; j <= num + k; j++) {
                sum = 0.0;

                for (int i = 0; i < 48; i++) {
                    sum += Math.pow(numberOfEquations[i], j);
                }

                matrix.setValue(k, pom, new MyDouble(sum));
                pom++;
            }

        }

        MyDouble vector[] = new MyDouble[num + 1];

        for (int i = 0; i <= num; i++) {
            double pom = 0.0;
            sum = 0.0;
            for (int j = 0; j < 48; j++) {
                pom = Math.pow(numberOfEquations[j], i);
                pom *= method[j];
                sum += pom;
            }
            vector[i] = new MyDouble(sum);
        }

        MyDouble[] res = matrix.gauss(vector);

        for (int i = 0; i <= num; i++) {
            System.out.print(res[i] + " ");
        }
        System.out.println();

    }
}
