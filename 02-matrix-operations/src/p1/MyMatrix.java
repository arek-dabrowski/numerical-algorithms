package p1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import types.DefinedOperations;
import types.MyType;
import types.MyFloat;
import types.MyDouble;

@SuppressWarnings("unchecked")
public class MyMatrix<T extends Number & DefinedOperations<T>> {
    private Class<T> type;
    private T[][] matrix;		//A
    private T[] vector;			//B
    private T[] resultVector;	//X
    private int size;
    private List<Integer> order;

    public MyMatrix(Class<T> type, int size) {
        this.type = type;
        this.size = size;
        this.matrix = (T[][]) Array.newInstance(type, size, size);
        this.resultVector = (T[]) Array.newInstance(type, size);
        this.vector = (T[]) Array.newInstance(type, size);
        loadData();
        multiplyByVector();
    }

    public void printMatrix(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printResultVector(){
        for (int i = 0; i < size; i++) {
            System.out.print(resultVector[i] + " ");
        }
    }

    public void printVector(){
        for (int i = 0; i < size; i++) {
            System.out.print(vector[i] + " ");
        }
    }

    private void multiplyByVector(){

        for (int i = 0; i < size; i++) {
            T result = null;
            try {
                result = type.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            result.zero();
            for (int j = 0; j < size; j++) {
                result = (T) result.add(matrix[i][j].mul(resultVector[j]));
            }
            vector[i] = result;
        }
    }
    
    private void initializeOrderArray() {
    	order = new ArrayList<>();
    	
    	for(int i = 0; i < size; i++) {
    		order.add(i);
    	}
    }


    private void loadData() {
        FileReader fileReader;
        BufferedReader bufferedReader;
        try {
            fileReader = new FileReader("matrix.txt");

            bufferedReader = new BufferedReader(fileReader);

            String line;

            for (int i = 0; i < size; i++) {
                line = bufferedReader.readLine();
                String number[] = line.split(" ");
                for (int j = 0; j < size; j++) {
                    if (type.equals(MyDouble.class)) {
                        MyDouble a = new MyDouble(Double.parseDouble(number[j])/65536);
                        matrix[i][j] = (T) a;
                    } else if (type.equals(MyFloat.class)) {
                    	MyFloat a = new MyFloat(Float.parseFloat(number[j])/65536);
                        matrix[i][j] = (T) a;
                    } else {
                        MyType a = new MyType(BigInteger.valueOf(Integer.parseInt(number[j])),BigInteger.valueOf(65536));
                        matrix[i][j] = (T) a;

                    }
                }
            }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            fileReader = new FileReader("vector.txt");

            bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();
                String number[] = line.split(" ");
                for (int i = 0; i < size; i++) {
                    if (type.equals(MyDouble.class)) {
                        MyDouble a = new MyDouble(Double.parseDouble(number[i])/65536);
                        resultVector[i] = (T) a;
                    } else if (type.equals(MyFloat.class)) {
                        MyFloat a = new MyFloat(Float.parseFloat(number[i])/65536);
                        resultVector[i] = (T) a;
                    } else {
                        MyType a = new MyType(BigInteger.valueOf(Integer.parseInt(number[i])),BigInteger.valueOf(65536));
                        resultVector[i] = (T) a;

                    }
                }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public T[] gauss() {

        for (int i = 0; i < size-1; i++) {
            for (int j = i; j < size-1; j++) {
                zeroSubmatrix(i, j);
            }
        }
        return calculateResultVector(false);
        
    }
    
    public T[] gaussPartialChoice() {
    	
        for (int i = 0; i < size-1; i++) {
        	findAndSwapBiggestRow(i, matrix[i][i]);
            for (int j = i; j < size-1; j++) {
                zeroSubmatrix(i, j);
            }
        }
    	return calculateResultVector(false);
    }
    
    public T[] gaussFullChoice() {
    	initializeOrderArray();
    	
        for (int i = 0; i < size-1; i++) {
        	findAndSwapBiggestElement(i, matrix[i][i]);
            for (int j = i; j < size-1; j++) {
                zeroSubmatrix(i, j);
            }
        }
    	return calculateResultVector(true);
    }

    private void zeroSubmatrix(int i, int j) {
    	T temp  = (T) matrix[j+1][i].div(matrix[i][i]);
        temp.changeSign();
        for (int k = 0; k < size; k++) {
            T temp2  = (T) temp.mul(matrix[i][k]);
            matrix[j+1][k] = (T) matrix[j+1][k].add(temp2);
        }
        T temp2  = (T) temp.mul(vector[i]);
        vector[j+1] = (T) vector[j+1].add(temp2);
    }
    
    private T[] calculateResultVector(boolean toSort) {
    	
        T[] resultVector = (T[]) Array.newInstance(type, size);
    	
    	T sum = null;
        try {
            sum = type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for(int i = size - 1; i >= 0; i--){
            sum.zero();
            for(int j = i+1; j < size; j++){
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
        T[] newResultVector = (T[]) Array.newInstance(type, size);
        
        for (int i = 0; i < order.size(); i++) newResultVector[order.get(i)] = resultVector[i];
    	
    	return newResultVector;
    }

/* Swapping methods */
    
    private void findAndSwapBiggestRow(int index, T value) {
    	T maxValue = value;
    	int maxIndex = index;

        for(int i = index + 1; i < size - 1; i++) {
        	if(maxValue.lessThan(matrix[index][i])) {
        		maxValue = matrix[index][i];
        		maxIndex = i;
        	}
        }
        
        if(maxIndex != index) swapRows(index, maxIndex);
        
    }
    
    private void findAndSwapBiggestElement(int index, T value) {
    	T maxValue = value;
    	int maxIndexI = index;
    	int maxIndexJ = index;

        for(int i = index; i < size - 1; i++) {
        	for(int j = index; j < size - 1; j++) {
        		if(maxValue.lessThan(matrix[i][j])) {
        			maxValue = matrix[i][j];
        			maxIndexI = i;
        			maxIndexJ = j;
        		}
        	}
        }
        
        if(maxIndexI != index) swapRows(index, maxIndexI);
        if(maxIndexJ != index) swapColumns(index, maxIndexJ);
        
    }
    
    private void swapRows(int index, int maxIndexI) {
    	for(int i = index; i < size; i ++) {
        	T temp = matrix[index][i];
        	matrix[index][i] = matrix[maxIndexI][i];
        	matrix[maxIndexI][i] = temp;
    	}

    	T temp = vector[index];
    	vector[index] = vector[maxIndexI];
    	vector[maxIndexI] = temp;
    }
    
    private void swapColumns(int index, int maxIndexJ) {
    	for(int i = 0; i < size; i ++) {
    		T temp = matrix[i][index];
    		matrix[i][index] = matrix[i][maxIndexJ];
    		matrix[i][maxIndexJ] = temp;
    	}

    	Collections.swap(order, index, maxIndexJ);
    }

    public T calculateError(T[] v){
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

        for (int i = 0; i < size; i++) {
            temp = v[i].sub(resultVector[i]);
            temp.abs();
            if(max.lessThan(temp)){
                max = temp;
            }
        }
        return max;
    }

    public Class<T> getType() {
        return type;
    }
}

