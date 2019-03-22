package p1;

import types.DefinedOperations;
import types.MyDouble;

public class GaussSeidel<T extends Number & DefinedOperations<T>> {
    MyMatrix<T> matrix;
    T[] vector;
    int numberOfIterations = 5;
    
    public GaussSeidel(MyMatrix<T> matrix, T[] vector){
        this.matrix = matrix;
        this.vector = vector;
    }

    public MyDouble[] doAlgorithm(){
        MyDouble[] v = new MyDouble[matrix.getSize()];

                for (int i = 0; i < matrix.getSize(); i++) {
            v[i] = new MyDouble(0.0);
        }
        for (int i = 0; i < numberOfIterations; i++) {
            v = gaussSeidelIteration(v);
        }
        return v;
    }

    public MyDouble[] doAlgorithm(double precision){
        boolean isPrecision = false;
        MyDouble[] v = new MyDouble[matrix.getSize()];
        MyDouble[] previousV = new MyDouble[matrix.getSize()];

        for (int i = 0; i < matrix.getSize(); i++) {
            v[i] = new MyDouble(0.0);
            previousV[i] = new MyDouble(0.0);
        }
        while(!isPrecision) {
            v = gaussSeidelIteration(v);
            if (calculateError(v,previousV).lessThan(new MyDouble(precision))){
                isPrecision = true;
            }
            for (int i = 0 ; i < v.length; i++) {
                previousV[i] = v[i];
            }
        }
        return v;
    }

    @SuppressWarnings("unchecked")
	public MyDouble[] gaussSeidelIteration(MyDouble[] v){
        MyDouble sumFirst = null;
        MyDouble sumSecond = null;
        MyDouble sumAll = null;
        MyDouble temp = null;
        try {
        	sumFirst = MyDouble.class.newInstance();
        	sumSecond = MyDouble.class.newInstance();
            sumAll = MyDouble.class.newInstance();
            temp = MyDouble.class.newInstance();
            sumFirst.zero();
            sumSecond.zero();
            sumAll.zero();
            temp.zero();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        MyDouble[] resultVector = new MyDouble[matrix.getSize()];
        for (int i = 0; i < resultVector.length; i++) {
            resultVector[i] = new MyDouble(0.0);
        }

        for (int i = 0; i < matrix.getSize(); i++) {
        	sumFirst.zero();
        	sumSecond.zero();
            sumAll.zero();
            for (int j = 0; j < i; j++) {
                if(i!=j) {
                    temp = (MyDouble) matrix.getValueAt(i, j).mul((T) resultVector[j]);
                    sumSecond = sumSecond.add(temp);
                }
            }
            for (int j = (i + 1); j < matrix.getSize(); j++) {
                if(i!=j) {
                    temp = (MyDouble) matrix.getValueAt(i, j).mul((T) v[j]);
                    sumFirst = sumFirst.add(temp);
                }
            }
            sumFirst.changeSign();
            sumSecond.changeSign();
            sumAll = sumAll.add(sumSecond);
            sumAll = sumAll.add(sumFirst);
            sumAll = sumAll.add((MyDouble) this.vector[i]);
            sumAll = sumAll.div((MyDouble) matrix.getValueAt(i,i));
            resultVector[i].setVal(sumAll);
        }

        return resultVector;
    }

    public MyDouble calculateError(MyDouble[] v, MyDouble[] previousV){
        MyDouble sum = new MyDouble();
        MyDouble temp;
        MyDouble average;
        sum.zero();

        for (int i = 0; i < v.length; i++) {
            temp = v[i].sub(previousV[i]);
            temp = temp.abs();
            sum = sum.add(temp);
        }
        average = sum.div(new MyDouble((double)v.length));
        return average;
    }
}
