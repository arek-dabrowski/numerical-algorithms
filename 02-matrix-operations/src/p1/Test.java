package p1;

import types.MyDouble;
import types.MyFloat;
import types.MyType;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Test {

	/* E1 */
    public void timeEfficiencyTest(int size) {
        MyMatrix<MyFloat> x1 = new MyMatrix<>(MyFloat.class, size);
        MyMatrix<MyDouble> y1 = new MyMatrix<>(MyDouble.class, size);
        MyMatrix<MyType> z1 = new MyMatrix<>(MyType.class, size);

        MyMatrix<MyFloat> x2 = new MyMatrix<>(MyFloat.class, size);
        MyMatrix<MyDouble> y2 = new MyMatrix<>(MyDouble.class, size);
        MyMatrix<MyType> z2 = new MyMatrix<>(MyType.class, size);

        MyMatrix<MyFloat> x3 = new MyMatrix<>(MyFloat.class, size);
        MyMatrix<MyDouble> y3 = new MyMatrix<>(MyDouble.class, size);
        MyMatrix<MyType> z3 = new MyMatrix<>(MyType.class, size);

        long start = System.nanoTime();
        x1.gauss();
        double elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("Float:\tGauss:\t\t" + elapsedTimeMillis + "ms");

        start = System.nanoTime();
        x2.gaussPartialChoice();
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("Float:\tGauss Partial Choice:\t" + elapsedTimeMillis + "ms");

        start = System.nanoTime();
        x3.gaussFullChoice();
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("Float:\tGauss Full Choice:\t" + elapsedTimeMillis + "ms");

        start = System.nanoTime();
        y1.gauss();
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("Double:\tGauss:\t\t" + elapsedTimeMillis + "ms");

        start = System.nanoTime();
        y2.gaussPartialChoice();
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("Double:\tGauss Partial Choice:\t" + elapsedTimeMillis + "ms");

        start = System.nanoTime();
        y3.gaussFullChoice();
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("Double:\tGauss Full Choice:\t" + elapsedTimeMillis + "ms");

        start = System.nanoTime();
        z1.gauss();
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("MyType:\tGauss:\t\t" + elapsedTimeMillis + "ms");

        start = System.nanoTime();
        z2.gaussPartialChoice();
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("MyType:\tGauss Partial Choice:\t" + elapsedTimeMillis + "ms");

        start = System.nanoTime();
        z3.gaussFullChoice();
        elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
        System.out.println("MyType:\tGauss Full Choice:\t" + elapsedTimeMillis + "ms");
    }

    public void methodsTest(int size, StringBuilder resultsErrorsBuilder){
        MyMatrix<MyFloat> x1 = new MyMatrix<>(MyFloat.class, size);
        MyMatrix<MyFloat> y1 = new MyMatrix<>(MyFloat.class, size);
        MyMatrix<MyFloat> z1 = new MyMatrix<>(MyFloat.class, size);

        MyMatrix<MyDouble> x2 = new MyMatrix<>(MyDouble.class, size);
        MyMatrix<MyDouble> y2 = new MyMatrix<>(MyDouble.class, size);
        MyMatrix<MyDouble> z2 = new MyMatrix<>(MyDouble.class, size);

        MyFloat floatResultsGauss[] = x1.gauss();
        MyFloat errorFloatResultsGauss = x1.calculateError(floatResultsGauss);
        MyFloat floatResultsGaussPartial[] = y1.gaussPartialChoice();
        MyFloat errorFloatResultsGaussPartial = y1.calculateError(floatResultsGaussPartial);
        MyFloat floatResultsGaussFull[] = z1.gaussFullChoice();
        MyFloat errorFloatResultsGaussFull = z1.calculateError(floatResultsGaussFull);

        resultsErrorsBuilder.append(size);
        resultsErrorsBuilder.append(';');
        resultsErrorsBuilder.append(errorFloatResultsGauss);
        resultsErrorsBuilder.append(';');
        resultsErrorsBuilder.append(errorFloatResultsGaussPartial);
        resultsErrorsBuilder.append(';');
        resultsErrorsBuilder.append(errorFloatResultsGaussFull);
        resultsErrorsBuilder.append(';');

		MyDouble doubleResultsGauss[] = x2.gauss();
        MyDouble errorDoubleResultsGauss = x2.calculateError(doubleResultsGauss);
        MyDouble doubleResultsGaussPartial[] = y2.gaussPartialChoice();
        MyDouble errorDoubleResultsGaussPartial = y2.calculateError(doubleResultsGaussPartial);
        MyDouble doubleResultsGaussFull[] = z2.gaussFullChoice();
        MyDouble errorDoubleResultsGaussFull = z2.calculateError(doubleResultsGaussFull);

        resultsErrorsBuilder.append(errorDoubleResultsGauss);
        resultsErrorsBuilder.append(';');
        resultsErrorsBuilder.append(errorDoubleResultsGaussPartial);
        resultsErrorsBuilder.append(';');
        resultsErrorsBuilder.append(errorDoubleResultsGaussFull);
        resultsErrorsBuilder.append(';');

        resultsErrorsBuilder.append("\r\n");
    }

    public void doubleTest(int size, StringBuilder resultsErrorsBuilder){
        MyMatrix<MyDouble> x = new MyMatrix<>(MyDouble.class, size);
        MyMatrix<MyDouble> y = new MyMatrix<>(MyDouble.class, size);

        MyDouble doubleResultsGauss[] = x.gauss();
        MyDouble errorDoubleResultsGauss = x.calculateError(doubleResultsGauss);
        MyDouble doubleResultsGaussFull[] = y.gaussFullChoice();
        MyDouble errorDoubleResultsGaussFull = y.calculateError(doubleResultsGaussFull);

        resultsErrorsBuilder.append(size);
        resultsErrorsBuilder.append(';');
        resultsErrorsBuilder.append(errorDoubleResultsGauss);
        resultsErrorsBuilder.append(';');
        resultsErrorsBuilder.append(errorDoubleResultsGaussFull);
        resultsErrorsBuilder.append(';');
        resultsErrorsBuilder.append("\r\n");

    }

    public void myTypeTest(int size){
        MyMatrix<MyType> x = new MyMatrix<>(MyType.class, size);
        MyMatrix<MyType> y = new MyMatrix<>(MyType.class, size);
        MyMatrix<MyType> z = new MyMatrix<>(MyType.class, size);

        MyType myTypeResultsGauss[] = x.gauss();
        System.out.print("N=" + size + " Gauss: "+x.calculateError(myTypeResultsGauss));
        MyType myTypeResultsGaussPartial[] = y.gaussFullChoice();
        System.out.print(" Partial Gauss: "+y.calculateError(myTypeResultsGaussPartial));
        MyType myTypeResultsGaussFull[] = z.gaussFullChoice();
        System.out.print(" Full Gauss: "+z.calculateError(myTypeResultsGaussFull));
        System.out.println();
    }

    public void errorsTest(){
        try
        {
            PrintWriter resultsErrors = new PrintWriter(new FileWriter("resultsErrors.csv", false));
            StringBuilder resultsErrorsBuilder = new StringBuilder();
            PrintWriter doubleErrors = new PrintWriter(new FileWriter("doubleErrors.csv", false));
            StringBuilder doubleErrorsBuilder = new StringBuilder();

            for (int i = 10; i <= 500; i=i+10) {
                methodsTest(i, resultsErrorsBuilder);
            }
            resultsErrors.write(resultsErrorsBuilder.toString());
            resultsErrors.close();

			for (int i = 10; i <= 500; i=i+10) {
				doubleTest(i, doubleErrorsBuilder);
			}
			doubleErrors.write(doubleErrorsBuilder.toString());
			doubleErrors.close();

			/*for (int i = 10; i <= 100; i=i+10) {
				myTypeTest(i);
			}*/
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
    
    /* H1 & Q2 */
    public void chosenMethodTimeTest(int size) {
    	long start;
        double elapsedTimeMillis;
        
        System.out.println("Execution time test for partial choice");
        
        for(int i = 10; i <= size; i += 10) {
            MyMatrix<MyFloat> x = new MyMatrix<>(MyFloat.class, i);
            start = System.nanoTime();
            x.gaussPartialChoice();
            elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
            System.out.println("Float:\tN=" + i + "\t" + elapsedTimeMillis + "ms");
        }
        
        for(int i = 10; i <= size; i += 10) {
        	MyMatrix<MyDouble> x = new MyMatrix<>(MyDouble.class, i);
            start = System.nanoTime();
            x.gaussPartialChoice();
            elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
            System.out.println("Double:\tN=" + i + "\t" + elapsedTimeMillis + "ms");
        }
        
        for(int i = 10; i <= 150; i += 10) {
        	MyMatrix<MyType> x = new MyMatrix<>(MyType.class, i);
            start = System.nanoTime();
            x.gaussPartialChoice();
            elapsedTimeMillis = (double) (System.nanoTime() - start) / 1000000;
            System.out.println("MyType:\tN=" + i + "\t" + elapsedTimeMillis + "ms");
        }
    }

}
