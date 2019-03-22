package p1;

import types.MyDouble;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.ejml.data.*;

public class EquationSet {
	private int N;
	private int n;
	private int matrixSize = newton(N+2,2);
	private Map<Integer,Integer> indexMap = new HashMap<>();
	private double[][] matrix;
	
	public EquationSet(int numberOfAgents) {
		this.N = numberOfAgents;
		this.n = newton(numberOfAgents,2);
		this.matrixSize = newton(numberOfAgents+2,2);
		initMap();
	}
	
	public int getMatrixSize() {
		return matrixSize;
	}
	
	public int cantorPairingFunction(int k1, int k2) {
		return (((k1 + k2)*(k1 + k2 + 1))/2 + k2);
	}
	
	public int newton(int n, int k) {
		int res = 1;
		int i;
		 
		for(i = 1; i <= k; i++){
			res = res * (n - i + 1) / i;
		}
		 
		return res;
	}
	
	private void initMap() {
		int i = 0;
		for(int yy = 0; yy <= N; yy++) {
			for(int nn = 0; nn <= N - yy; nn++) {
				indexMap.put(cantorPairingFunction(yy, nn), i);
				i++;
			}
		}
	}
	
	public void generateMatrix() {
		this.matrix = new double[matrixSize][matrixSize];
		int x = 0;
		for(int yy = 0; yy <= N; yy++) {
			for(int nn = 0; nn <= N - yy; nn++) {
				int uu = N - yy - nn;
				double w1 = (yy * uu)/(double)n;
				double w2 = (nn * uu)/(double)n;
				double w3 = (yy * nn)/(double)n;
				double w4;
				
				if((yy == 0 && nn == 0) || (yy == 0 && nn == N) || (yy == N && nn == 0)) {
					w4 = (n - (yy*uu + nn*uu + yy*nn))/(double)n;
				}
				else {
					w4 = (n - (yy*uu + nn*uu + yy*nn))/(double)n - 1;
				}
				
				if(w1 != 0.0) {
					matrix[x][indexMap.get(cantorPairingFunction(yy+1, nn))] = w1;
				}
				if(w2 != 0.0) {
					matrix[x][indexMap.get(cantorPairingFunction(yy, nn+1))] = w2;
				}
				if(w3 != 0.0) {
					matrix[x][indexMap.get(cantorPairingFunction(yy-1, nn-1))] = w3;
				}
				if(w4 != 0.0) {
					matrix[x][indexMap.get(cantorPairingFunction(yy, nn))] = w4;
				}
				
				x++;
			}
		}
		saveMatrixToFile();
		saveVectorToFile();
	}
	
	public DMatrixSparseCSC generateSparseMatrix() {
		
		DMatrixSparseCSC mtrx = new DMatrixSparseCSC(matrixSize, matrixSize, 0);
				
		
		int x = 0;
		for(int yy = 0; yy <= N; yy++) {
			for(int nn = 0; nn <= N - yy; nn++) {
				int uu = N - yy - nn;
				double w1 = (yy * uu)/(double)n;
				double w2 = (nn * uu)/(double)n;
				double w3 = (yy * nn)/(double)n;
				double w4;
				
				if((yy == 0 && nn == 0) || (yy == 0 && nn == N) || (yy == N && nn == 0)) {
					w4 = (n - (yy*uu + nn*uu + yy*nn))/(double)n;
				}
				else {
					w4 = (n - (yy*uu + nn*uu + yy*nn))/(double)n - 1;
				}
				
				if(w1 != 0.0) {
					mtrx.set(x, indexMap.get(cantorPairingFunction(yy+1, nn)), w1);
				}
				if(w2 != 0.0) {
					mtrx.set(x, indexMap.get(cantorPairingFunction(yy, nn+1)), w2);
				}
				if(w3 != 0.0) {
					mtrx.set(x, indexMap.get(cantorPairingFunction(yy-1, nn-1)), w3);
				}
				if(w4 != 0.0) {
					mtrx.set(x, indexMap.get(cantorPairingFunction(yy, nn)), w4);
				}
				
				x++;
			}
		}
		return mtrx;
	}
	
	public DMatrixSparseCSC generateSparseVector() {
		
		DMatrixSparseCSC vec = new DMatrixSparseCSC(matrixSize, 1, 0);
		vec.set(matrixSize-1, 0, 1.0);
		
		return vec;
	}

	
	private void saveMatrixToFile() {
		PrintWriter writer;
		try {
			writer = new PrintWriter("matrix.txt");
			for(int i = 0; i < matrixSize; i++) {
				for(int j = 0; j < matrixSize; j++) {
					writer.print(matrix[i][j]+" ");
				}
				writer.println();
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void saveVectorToFile() {
		PrintWriter writer;
		try {
			writer = new PrintWriter("vector.txt");
			for(int i = 0; i < matrixSize-1; i++) {
				writer.print(0.0+" ");
			}
			writer.print(1.0+" ");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public MyDouble[] loadVector(){
		FileReader fileReader;
		BufferedReader bufferedReader;
		MyDouble[] resultVector = new MyDouble[matrixSize];
		try {
			fileReader = new FileReader("vector.txt");

			bufferedReader = new BufferedReader(fileReader);

			String line = bufferedReader.readLine();
			String number[] = line.split(" ");
			for (int i = 0; i < matrixSize; i++) {
				MyDouble a = new MyDouble(Double.parseDouble(number[i]));
				resultVector[i] = a;
			}
			bufferedReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultVector;
	}

}
