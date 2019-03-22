package p1;

import types.DefinedOperations;
import types.MyDouble;

public class Jacobian<T extends Number & DefinedOperations<T>> {
    MyMatrix<T> matrix;
    T[] vector;
    int numberOfIterations = 10;
    
    public Jacobian(MyMatrix<T> matrix, T[] vector){
        this.matrix = matrix;
        this.vector = vector;
    }

    public MyDouble[] doAlgorithm(){
        MyDouble[] v = new MyDouble[matrix.getSize()];

        //pierwszy wektor jest zerowy
        for (int i = 0; i < matrix.getSize(); i++) {
            v[i] = new MyDouble(0.0);
        }
        for (int i = 0; i < numberOfIterations; i++) {
            v = jacobianIteration(v);
            /*for (int j = 0; j < v.length; j++) {
                System.out.print(v[j]+ " ");
            }
            System.out.println();*/
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
            v = jacobianIteration(v);
            if (calculateError(v,previousV).lessThan(new MyDouble(precision))){
                isPrecision = true;
            }
            for (int i = 0 ; i < v.length; i++) {
                previousV[i] = v[i];
            }
        }

        return v;
    }

    public MyDouble[] jacobianIteration(MyDouble[] v){
        MyDouble sum = null;
        MyDouble sumAll = null;
        MyDouble temp = null;
        try {
            sum = MyDouble.class.newInstance();
            sumAll = MyDouble.class.newInstance();
            temp = MyDouble.class.newInstance();
            sum.zero();
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
            sum.zero();
            sumAll.zero();
            for (int j = 0; j < matrix.getSize(); j++) {
                if(i!=j) {
                    temp = (MyDouble) matrix.getValueAt(i, j).mul((T) v[j]);
                    sum = sum.add(temp);
                }
            }
            sum.changeSign();
            sumAll = sumAll.add(sum);
            sumAll = sumAll.add((MyDouble) this.vector[i]);
            sumAll = sumAll.div((MyDouble) matrix.getValueAt(i,i));
            resultVector[i].setVal(sumAll);
        }

        return resultVector;
    }

    public MyDouble calculateError(MyDouble[] v, MyDouble[] previousV) {
        MyDouble max = new MyDouble();
        MyDouble temp = new MyDouble();

        max.zero();

        for (int i = 0; i < v.length; i++) {
            temp = v[i].sub(previousV[i]);
            temp = temp.abs();
            if (max.lessThan(temp)) {
                max = temp;
            }
        }
        return max;
    }
}
