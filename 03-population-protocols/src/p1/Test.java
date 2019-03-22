package p1;

import simulation.MonteCarlo;
import types.MyDouble;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Test {

    public void monteCarloTest(){
        try
        {
            PrintWriter resultsErrors = new PrintWriter(new FileWriter("monteCarloErrors3.csv", false));
            StringBuilder resultsErrorsBuilder = new StringBuilder();

            resultsErrorsBuilder.append("number of Agents");
            resultsErrorsBuilder.append(';');
            resultsErrorsBuilder.append("Partial Gauss");
            resultsErrorsBuilder.append(';');
            resultsErrorsBuilder.append("Optimized partial Gauss");
            resultsErrorsBuilder.append(';');
            resultsErrorsBuilder.append("Jacobian");
            resultsErrorsBuilder.append(';');
            resultsErrorsBuilder.append("Gauss-Seidel");
            resultsErrorsBuilder.append(';');
            resultsErrorsBuilder.append("\r\n");

            for (int i = 3; i <= 15; i++) {
                monteCarlo(i, resultsErrorsBuilder);
            }
            resultsErrors.write(resultsErrorsBuilder.toString());
            resultsErrors.close();
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

    public void monteCarlo(int numberOfAgents, StringBuilder resultsErrorsBuilder){
        double precision = 0.000001;
        EquationSet equations = new EquationSet(numberOfAgents);
        equations.generateMatrix();
        int matrixSize = equations.getMatrixSize();

        MonteCarlo m = new MonteCarlo();
        MyDouble[] monteCarloResults = m.test(500000, numberOfAgents, matrixSize);

        MyMatrix<MyDouble> matrix1 = new MyMatrix<>(MyDouble.class, matrixSize, matrixSize);
        MyDouble[] vector1 = equations.loadVector();
        MyMatrix<MyDouble> matrix2 = new MyMatrix<>(MyDouble.class, matrixSize, matrixSize);
        MyDouble[] vector2 = equations.loadVector();
        MyMatrix<MyDouble> matrix3 = new MyMatrix<>(MyDouble.class, matrixSize, matrixSize);
        MyDouble[] vector3 = equations.loadVector();
        MyMatrix<MyDouble> matrix4 = new MyMatrix<>(MyDouble.class, matrixSize, matrixSize);
        MyDouble[] vector4 = equations.loadVector();

        MyDouble resultsGaussPartial[] = matrix1.gaussPartialChoice(vector1);
        MyDouble errorResultsGaussPartial = calculateMaxError(monteCarloResults, resultsGaussPartial);

        MyDouble resultsOptimizedGaussPartial[] = matrix2.optimizedGaussPartialChoice(vector2);
        MyDouble errorResultsOptimizedGaussPartial = calculateMaxError(monteCarloResults, resultsOptimizedGaussPartial);

        Jacobian jacobian = new Jacobian(matrix3, vector3);
        MyDouble resultsJacobian[] = jacobian.doAlgorithm(precision);
        MyDouble errorResultsJacobian = calculateMaxError(monteCarloResults, resultsJacobian);

        GaussSeidel gaussSeidel = new GaussSeidel(matrix4, vector4);
        MyDouble resultsGaussSeidel[] = gaussSeidel.doAlgorithm(precision);
        MyDouble errorResultsGaussSeidel = calculateMaxError(monteCarloResults, resultsGaussSeidel);

        resultsErrorsBuilder.append(numberOfAgents);
        resultsErrorsBuilder.append(';');
        resultsErrorsBuilder.append(errorResultsGaussPartial);
        resultsErrorsBuilder.append(';');
        resultsErrorsBuilder.append(errorResultsOptimizedGaussPartial);
        resultsErrorsBuilder.append(';');
        resultsErrorsBuilder.append(errorResultsJacobian);
        resultsErrorsBuilder.append(';');
        resultsErrorsBuilder.append(errorResultsGaussSeidel);
        resultsErrorsBuilder.append(';');
        System.out.println("Number of agents:" + numberOfAgents);

        resultsErrorsBuilder.append("\r\n");
    }

    public void errorsTest(){
        try
        {
            PrintWriter resultsErrors = new PrintWriter(new FileWriter("resultsErrors3.csv", false));
            StringBuilder resultsErrorsBuilder = new StringBuilder();

            resultsErrorsBuilder.append("number of Agents");
            resultsErrorsBuilder.append(';');
            resultsErrorsBuilder.append("Partial Gauss");
            resultsErrorsBuilder.append(';');
            resultsErrorsBuilder.append("Optimized partial Gauss");
            resultsErrorsBuilder.append(';');
            resultsErrorsBuilder.append("Jacobian");
            resultsErrorsBuilder.append(';');
            resultsErrorsBuilder.append("Gauss-Seidel");
            resultsErrorsBuilder.append(';');
            resultsErrorsBuilder.append("\r\n");

            for (int i = 3; i <= 35; i++) {
                methodsTest(i, resultsErrorsBuilder);
            }
            resultsErrors.write(resultsErrorsBuilder.toString());
            resultsErrors.close();
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

    private void methodsTest(int numberOfAgents, StringBuilder resultsErrorsBuilder) {
        double precision = 0.00000000000001;
        EquationSet equations = new EquationSet(numberOfAgents);
        equations.generateMatrix();
        int matrixSize = equations.getMatrixSize();

        MyMatrix<MyDouble> matrix1 = new MyMatrix<>(MyDouble.class, matrixSize, matrixSize);
        MyDouble[] vector1 = equations.loadVector();
        MyMatrix<MyDouble> matrix2 = new MyMatrix<>(MyDouble.class, matrixSize, matrixSize);
        MyDouble[] vector2 = equations.loadVector();
        MyMatrix<MyDouble> matrix3 = new MyMatrix<>(MyDouble.class, matrixSize, matrixSize);
        MyDouble[] vector3 = equations.loadVector();
        MyMatrix<MyDouble> matrix4 = new MyMatrix<>(MyDouble.class, matrixSize, matrixSize);
        MyDouble[] vector4 = equations.loadVector();

        MyDouble resultsGaussPartial[] = matrix1.gaussPartialChoice(vector1);
        MyDouble[] bVector1 = matrix1.multiplyByVector(resultsGaussPartial);
        MyDouble errorResultsGaussPartial = calculateMaxError(bVector1, vector1);

        MyDouble resultsOptimizedGaussPartial[] = matrix2.optimizedGaussPartialChoice(vector2);
        MyDouble[] bVector2 = matrix2.multiplyByVector(resultsOptimizedGaussPartial);
        MyDouble errorResultsOptimizedGaussPartial = calculateMaxError(bVector2, vector2);

        Jacobian jacobian = new Jacobian(matrix3, vector3);
        MyDouble resultsJacobian[] = jacobian.doAlgorithm(precision);
        MyDouble[] bVector3 = matrix3.multiplyByVector(resultsJacobian);
        MyDouble errorResultsJacobian = calculateMaxError(bVector3, vector3);

        GaussSeidel gaussSeidel = new GaussSeidel(matrix4, vector4);
        MyDouble resultsGaussSeidel[] = gaussSeidel.doAlgorithm(precision);
        MyDouble[] bVector4 = matrix4.multiplyByVector(resultsGaussSeidel);
        MyDouble errorResultsGaussSeidel = calculateMaxError(bVector4, vector4);

        resultsErrorsBuilder.append(numberOfAgents);
        resultsErrorsBuilder.append(';');
        resultsErrorsBuilder.append(errorResultsGaussPartial);
        resultsErrorsBuilder.append(';');
        resultsErrorsBuilder.append(errorResultsOptimizedGaussPartial);
        resultsErrorsBuilder.append(';');
        resultsErrorsBuilder.append(errorResultsJacobian);
        resultsErrorsBuilder.append(';');
        resultsErrorsBuilder.append(errorResultsGaussSeidel);
        resultsErrorsBuilder.append(';');
        System.out.println("Number of agents: " + numberOfAgents + " Partial Gauss " + errorResultsGaussPartial + " Optimized Partial Gauss "+ errorResultsGaussPartial + " Difference " + (errorResultsGaussPartial.sub(errorResultsOptimizedGaussPartial)));

        resultsErrorsBuilder.append("\r\n");
    }

    private MyDouble calculateMaxError(MyDouble[] v, MyDouble[] resultVector){
        MyDouble max = new MyDouble();
        MyDouble temp = new MyDouble();

        max.zero();

        for (int i = 0; i < resultVector.length; i++) {
            temp = v[i].sub(resultVector[i]);
            temp = temp.abs();
            if(max.lessThan(temp)){
                max = temp;
            }
        }
        return max;
    }

    private MyDouble calculateAverageError(MyDouble[] v, MyDouble[] resultVector){
        MyDouble max = new MyDouble();
        MyDouble sum = new MyDouble();
        MyDouble temp = new MyDouble();
        MyDouble average = new MyDouble();

        max.zero();
        sum.zero();
        average.zero();

        for (int i = 0; i < resultVector.length; i++) {
            temp = v[i].sub(resultVector[i]);
            temp = temp.abs();
            sum = sum.add(temp);
        }
        average = sum.div(new MyDouble((double)resultVector.length));
        return average;
    }
    
    public void timesTest(int N) {
        for (int i = 3; i <= N; i+=5) {
        	methodsTimeTest(i);
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void methodsTimeTest(int numberOfAgents) {
        double precision = 0.000001;
        EquationSet equations = new EquationSet(numberOfAgents);
        equations.generateMatrix();
        int matrixSize = equations.getMatrixSize();
        
        MyMatrix<MyDouble> matrix1 = new MyMatrix<>(MyDouble.class, matrixSize, matrixSize);
        MyDouble[] vector1 = equations.loadVector();
        MyMatrix<MyDouble> matrix2 = new MyMatrix<>(MyDouble.class, matrixSize, matrixSize);
        MyDouble[] vector2 = equations.loadVector();
        MyMatrix<MyDouble> matrix3 = new MyMatrix<>(MyDouble.class, matrixSize, matrixSize);
        MyDouble[] vector3 = equations.loadVector();
        MyMatrix<MyDouble> matrix4 = new MyMatrix<>(MyDouble.class, matrixSize, matrixSize);
        MyDouble[] vector4 = equations.loadVector();
        
        Jacobian jacobian = new Jacobian(matrix3, vector3);
        GaussSeidel gaussSeidel = new GaussSeidel(matrix4, vector4);
        
    	long start;
        double elapsedTimeMillis;
                
        start = System.nanoTime();
        matrix1.gaussPartialChoice(vector1);
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("PartialChoice:\t\tN=" + numberOfAgents + "\t" + elapsedTimeMillis + "ms");
        
        start = System.nanoTime();
        matrix2.gaussPartialChoice(vector2);
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("OptimizedPartialChoice:\tN=" + numberOfAgents + "\t" + elapsedTimeMillis + "ms");
        
        start = System.nanoTime();
        jacobian.doAlgorithm(precision);
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("Jacobian:\t\tN=" + numberOfAgents + "\t" + elapsedTimeMillis + "ms");
        
        start = System.nanoTime();
        gaussSeidel.doAlgorithm(precision);
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("GaussSeidel:\t\tN=" + numberOfAgents + "\t" + elapsedTimeMillis + "ms");
        System.out.println();
    }

}
