package p1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import types.DefinedOperations;
import types.MyDouble;

@SuppressWarnings("unchecked")
public class MyMatrix<T extends Number & DefinedOperations<T>> {
    private Class<T> type;
    private T[][] matrix;
    private int numberOfRows;
    private int numberOfColumns;
    private List<Integer> order;

    public MyMatrix(Class<T> type, int numberOfRows, int numberOfColumns) {
        this.type = type;
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.matrix = (T[][]) Array.newInstance(type, numberOfRows, numberOfColumns);
        loadData();
    }

    public void printMatrix(){
        for (int i = 0; i < numberOfColumns; i++) {
            for (int j = 0; j < numberOfRows; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public T[] multiplyByVector(T[] resultVector){
        T[] vector = (T[]) Array.newInstance(type, numberOfRows);
        for (int i = 0; i < numberOfRows; i++) {
            T result = null;
            try {
                result = type.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            result.zero();
            for (int j = 0; j < numberOfRows; j++) {
                result = (T) result.add(matrix[i][j].mul(resultVector[j]));
            }
            vector[i] = result;
        }

        return vector;
    }

    private void initializeOrderArray() {
        order = new ArrayList<>();

        for(int i = 0; i < numberOfRows; i++) {
            order.add(i);
        }
    }


    public void loadData() {
        FileReader fileReader;
        BufferedReader bufferedReader;
        try {
            fileReader = new FileReader("matrix.txt");

            bufferedReader = new BufferedReader(fileReader);

            String line;

            for (int i = 0; i < numberOfRows; i++) {
                line = bufferedReader.readLine();
                String number[] = line.split(" ");
                for (int j = 0; j < numberOfColumns; j++) {
                    if (type.equals(MyDouble.class)) {
                        MyDouble a = new MyDouble(Double.parseDouble(number[j]));
                        matrix[i][j] = (T) a;
                    }
                }
            }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public T[] gauss(T[] vector) {

        for (int i = 0; i < numberOfColumns-1; i++) {
            for (int j = i; j < numberOfRows-1; j++) {
                zeroSubmatrix(i, j, vector);
            }
        }
        return calculateResultVector(false, vector);

    }

    public T[] gaussPartialChoice(T[] vector) {

        for (int i = 0; i < numberOfColumns-1; i++) {
            findAndSwapBiggestRow(i, matrix[i][i], vector);
            for (int j = i; j < numberOfRows-1; j++) {
                zeroSubmatrix(i, j, vector);
            }
        }
        return calculateResultVector(false, vector);
    }
    
    public T[] optimizedGaussPartialChoice(T[] vector) {

        for (int i = 0; i < numberOfColumns-1; i++) {
            findAndSwapBiggestRow(i, matrix[i][i], vector);
            for (int j = i; j < numberOfRows-1; j++) {
            	if(matrix[j+1][i].isZero()) {
            		continue;
            	}
            	else {
            		zeroSubmatrix(i, j, vector);
            	}
            }
        }
        return calculateResultVector(false, vector);
    }

    public T[] gaussFullChoice(T[] vector) {
        initializeOrderArray();

        for (int i = 0; i < numberOfColumns-1; i++) {
            findAndSwapBiggestElement(i, matrix[i][i], vector);
            for (int j = i; j < numberOfRows-1; j++) {
                zeroSubmatrix(i, j, vector);
            }
        }
        return calculateResultVector(true, vector);
    }

    private void zeroSubmatrix(int i, int j, T[] vector) {
        T temp  = (T) matrix[j+1][i].div(matrix[i][i]);
        temp.changeSign();
        for (int k = 0; k < numberOfColumns; k++) {
            T temp2  = (T) temp.mul(matrix[i][k]);
            matrix[j+1][k] = (T) matrix[j+1][k].add(temp2);
        }
        T temp2  = (T) temp.mul(vector[i]);
        vector[j+1] = (T) vector[j+1].add(temp2);
    }

    private T[] calculateResultVector(boolean toSort, T[] vector) {

        T[] resultVector = (T[]) Array.newInstance(type, numberOfRows);

        T sum = null;
        try {
            sum = type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for(int i = numberOfColumns - 1; i >= 0; i--){
            sum.zero();
            for(int j = i+1; j < numberOfRows; j++){
                T temp = (T) matrix[i][j].mul(resultVector[j]);
                sum = (T) sum.add(temp);
            }
            T temp2 = (T) vector[i].sub(sum);
            resultVector[i] = (T) temp2.div(matrix[i][i]);

        }
        if(toSort) return sortResultVector(resultVector);
        else return resultVector;
    }

    private T[] sortResultVector(T[] resultVector) {
        T[] newResultVector = (T[]) Array.newInstance(type, numberOfRows);

        for (int i = 0; i < order.size(); i++) newResultVector[order.get(i)] = resultVector[i];

        return newResultVector;
    }

    /* Swapping methods */

    private void findAndSwapBiggestRow(int index, T value, T[] vector) {
        T maxValue = value.abs();
        int maxIndex = index;

        for(int i = index + 1; i < numberOfRows; i++) {
            if(maxValue.lessThan(matrix[i][index].abs())) {
                maxValue = matrix[i][index].abs();
                maxIndex = i;
            }
        }

        if(maxIndex != index) swapRows(index, maxIndex, vector);

    }

    private void findAndSwapBiggestElement(int index, T value, T[] vector) {
        T maxValue = value;
        int maxIndexI = index;
        int maxIndexJ = index;

        for(int i = index; i < numberOfColumns - 1; i++) {
            for(int j = index; j < numberOfRows - 1; j++) {
                if(maxValue.lessThan(matrix[i][j])) {
                    maxValue = matrix[i][j];
                    maxIndexI = i;
                    maxIndexJ = j;
                }
            }
        }

        if(maxIndexI != index) swapRows(index, maxIndexI, vector);
        if(maxIndexJ != index) swapColumns(index, maxIndexJ);

    }

    private void swapRows(int index, int maxIndexI, T[] vector) {
        for(int i = index; i < numberOfRows; i ++) {
            T temp = matrix[index][i];
            matrix[index][i] = matrix[maxIndexI][i];
            matrix[maxIndexI][i] = temp;
        }

        T temp = vector[index];
        vector[index] = vector[maxIndexI];
        vector[maxIndexI] = temp;
    }

    private void swapColumns(int index, int maxIndexJ) {
        for(int i = 0; i < numberOfColumns; i ++) {
            T temp = matrix[i][index];
            matrix[i][index] = matrix[i][maxIndexJ];
            matrix[i][maxIndexJ] = temp;
        }

        Collections.swap(order, index, maxIndexJ);
    }

    /*public T calculateError(T[] v){
        T max=null, temp=null;
        try {
            max = type.newInstance();
            temp = type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        max.zero();

        for (int i = 0; i < numberOfColumns; i++) {
            temp = v[i].sub(resultVector[i]);
            temp.abs();
            if(max.lessThan(temp)){
                max = temp;
            }
        }
        return max;
    }*/

    public Class<T> getType() {
        return type;
    }

    public int getSize() {
        return numberOfRows;
    }

    public T getValueAt(int i, int j){
        return matrix[i][j];
    }
}
