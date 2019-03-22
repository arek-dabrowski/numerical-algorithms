package p1;

import types.MyDouble;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.interfaces.linsol.LinearSolverSparse;
import org.ejml.sparse.ComputePermutation;
import org.ejml.sparse.FillReducing;
import org.ejml.sparse.csc.decomposition.lu.LuUpLooking_DSCC;
import org.ejml.sparse.csc.factory.FillReductionFactory_DSCC;
import org.ejml.sparse.csc.linsol.lu.*;

public class Test {

    public void timesTest(int N) {
        try
        {
        	warmUp(40);
        	
            PrintWriter timesTest = new PrintWriter(new FileWriter("timesTest.csv", false));
            StringBuilder timesTestBuilder = new StringBuilder();

            timesTestBuilder.append("number of agents");
            timesTestBuilder.append(';');
            timesTestBuilder.append("number of equations");
            timesTestBuilder.append(';');
            timesTestBuilder.append("building matrix time");
            timesTestBuilder.append(';');
            timesTestBuilder.append("building sparse matrix time");
            timesTestBuilder.append(';');
            timesTestBuilder.append("partial Gauss time");
            timesTestBuilder.append(';');
            timesTestBuilder.append("optimized partial Gauss time");
            timesTestBuilder.append(';');
            timesTestBuilder.append("Gauss-Seidel 10^-10 time");
            timesTestBuilder.append(';');
            timesTestBuilder.append("EJML sparseLU time");
            timesTestBuilder.append(';');
            timesTestBuilder.append("\r\n");

            for (int i = 3; i <= N; i++) {
            	methodsTimeTest(i, timesTestBuilder);
            }
            
            timesTest.write(timesTestBuilder.toString());
            timesTest.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("file not found exception");
            e.printStackTrace();
        }
        catch(IOException e)
        {
            System.out.println("ioexception");
            e.printStackTrace();
        }

    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void methodsTimeTest(int numberOfAgents, StringBuilder timesTestBuilder) {
        double precision = 0.0000000001; //10^-10
        
        EquationSet equations = new EquationSet(numberOfAgents);
        int matrixSize = equations.getMatrixSize();
        
        DMatrixSparseCSC mtrx;
    	DMatrixSparseCSC vec;
    	DMatrixSparseCSC res;
        ComputePermutation<DMatrixSparseCSC> cp = FillReductionFactory_DSCC.create(FillReducing.NONE);
        LuUpLooking_DSCC lu = new LuUpLooking_DSCC(cp);
        LinearSolverSparse<DMatrixSparseCSC, DMatrixRMaj> solver = new LinearSolverLu_DSCC(lu);
        
    	long start;
        double elapsedTimeMillis;

        timesTestBuilder.append(numberOfAgents);
        timesTestBuilder.append(';');
       
        timesTestBuilder.append(matrixSize);
        timesTestBuilder.append(';');
        
        start = System.nanoTime();
        equations.generateMatrix();
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("Dense matrix build time:\tN=" + numberOfAgents + "\t" + elapsedTimeMillis + "ms");
        timesTestBuilder.append(elapsedTimeMillis);
        timesTestBuilder.append(';'); 
        
        MyMatrix<MyDouble> matrix1 = new MyMatrix<>(MyDouble.class, matrixSize, matrixSize);
        MyDouble[] vector1 = equations.loadVector();
        MyMatrix<MyDouble> matrix2 = new MyMatrix<>(MyDouble.class, matrixSize, matrixSize);
        MyDouble[] vector2 = equations.loadVector();
        MyMatrix<MyDouble> matrix3 = new MyMatrix<>(MyDouble.class, matrixSize, matrixSize);
        MyDouble[] vector3 = equations.loadVector();
        
        GaussSeidel gaussSeidel = new GaussSeidel(matrix3, vector3);
        
        start = System.nanoTime();
        mtrx = equations.generateSparseMatrix();
        vec = equations.generateSparseVector();
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("Sparse matrix build time:\tN=" + numberOfAgents + "\t" + elapsedTimeMillis + "ms");
        timesTestBuilder.append(elapsedTimeMillis);
        timesTestBuilder.append(';'); 
                
        start = System.nanoTime();
        matrix1.gaussPartialChoice(vector1);
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("PartialChoice:\t\tN=" + numberOfAgents + "\t" + elapsedTimeMillis + "ms");
        timesTestBuilder.append(elapsedTimeMillis);
        timesTestBuilder.append(';');
        
        start = System.nanoTime();
        matrix2.optimizedGaussPartialChoice(vector2);
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("OptimizedPartialChoice:\tN=" + numberOfAgents + "\t" + elapsedTimeMillis + "ms");
        timesTestBuilder.append(elapsedTimeMillis);
        timesTestBuilder.append(';');
        
        start = System.nanoTime();
        gaussSeidel.doAlgorithm(precision);
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("GaussSeidel(10^-10):\tN=" + numberOfAgents + "\t" + elapsedTimeMillis + "ms");
        timesTestBuilder.append(elapsedTimeMillis);
        timesTestBuilder.append(';');
        
        solver.setA(mtrx);
        res = new DMatrixSparseCSC(mtrx.numRows, 1, 0);
        
        start = System.nanoTime();
        solver.solveSparse(vec, res);
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("EJML sparseLU:\t\tN=" + numberOfAgents + "\t" + elapsedTimeMillis + "ms");
        timesTestBuilder.append(elapsedTimeMillis);
        timesTestBuilder.append(';');
        
        timesTestBuilder.append("\r\n");
        
        System.out.println();
    }
    
	public void libraryMethodTest(int numberOfAgents) {
    	warmUp(35);
    	
        EquationSet equations = new EquationSet(numberOfAgents);
        
        DMatrixSparseCSC mtrx;
    	DMatrixSparseCSC vec;
    	DMatrixSparseCSC res;
        ComputePermutation<DMatrixSparseCSC> cp = FillReductionFactory_DSCC.create(FillReducing.NONE);
        LuUpLooking_DSCC lu = new LuUpLooking_DSCC(cp);
        LinearSolverSparse<DMatrixSparseCSC, DMatrixRMaj> solver = new LinearSolverLu_DSCC(lu);

    	long start;
        double elapsedTimeMillis;
        
        System.out.println("Number of Agents: " + numberOfAgents + " | Number of equations: " + equations.getMatrixSize());
        
        start = System.nanoTime();
        mtrx = equations.generateSparseMatrix();
        vec = equations.generateSparseVector();
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("Sparse matrix build time:\tN=" + numberOfAgents + "\t" + elapsedTimeMillis + "ms");
        
        solver.setA(mtrx);
        res = new DMatrixSparseCSC(mtrx.numRows, 1, 0);
        
        start = System.nanoTime();
        solver.solveSparse(vec, res);
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("EJML sparse LU:\t\tN=" + numberOfAgents + "\t" + elapsedTimeMillis + "ms");
        
        System.out.println();
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void warmUp(int numberOfAgents) {
    	System.out.println("Warm up started");
        double precision = 0.0000000001; //10^-10
        
        EquationSet equations = new EquationSet(numberOfAgents);
        int matrixSize = equations.getMatrixSize();
        
        DMatrixSparseCSC mtrx = equations.generateSparseMatrix();
    	DMatrixSparseCSC vec = equations.generateSparseVector();
    	DMatrixSparseCSC res = new DMatrixSparseCSC(mtrx.numRows, 1, 0);
        ComputePermutation<DMatrixSparseCSC> cp = FillReductionFactory_DSCC.create(FillReducing.NONE);
        LuUpLooking_DSCC lu = new LuUpLooking_DSCC(cp);
        LinearSolverSparse<DMatrixSparseCSC, DMatrixRMaj> solver = new LinearSolverLu_DSCC(lu);
        solver.setA(mtrx);
    
        equations.generateMatrix();
        
        MyMatrix<MyDouble> matrix1 = new MyMatrix<>(MyDouble.class, matrixSize, matrixSize);
        MyDouble[] vector1 = equations.loadVector();
        MyMatrix<MyDouble> matrix2 = new MyMatrix<>(MyDouble.class, matrixSize, matrixSize);
        MyDouble[] vector2 = equations.loadVector();
        MyMatrix<MyDouble> matrix3 = new MyMatrix<>(MyDouble.class, matrixSize, matrixSize);
        MyDouble[] vector3 = equations.loadVector();
        
        GaussSeidel gaussSeidel = new GaussSeidel(matrix3, vector3);
        
        mtrx = equations.generateSparseMatrix();
        vec = equations.generateSparseVector();
        
        matrix1.gaussPartialChoice(vector1);
        matrix2.optimizedGaussPartialChoice(vector2);
        gaussSeidel.doAlgorithm(precision);
        solver.solveSparse(vec, res);
        
    	System.out.println("Warm up finished");
    	System.out.println();
        
    }

}
