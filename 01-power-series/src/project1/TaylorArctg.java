package project1;

public class TaylorArctg {
	private static double[] basicArray;
	private static double[] modifiedArray;

	private double sumForward, sumBackward, sumModifiedForward, sumModifiedBackward = 0.0;

	TaylorArctg(double x, int n){
		basicArray = new double[n+2];
		modifiedArray = new double[n+2];

		for(double k = 0.0; k<=n; k++) {
			basicArray[(int)k] = (power(-1.0,k) * power(x,1+2*k))/(1+2*k);
			if (k == 0.0){
				modifiedArray[(int)k] = x;
				modifiedArray[(int)k+1] = modifiedArray[(int) k] * -(power(x,2.0)*(2*k+1))/(2*k+3);
			} else {
				modifiedArray[(int)k+1] = modifiedArray[(int) k] * -(power(x,2.0)*(2*k+1))/(2*k+3);
			}
		}
		
	}

	public void sum(int n) {
		sumForward = sumBackward = sumModifiedForward = sumModifiedBackward = 0.0;
		for(double k = 0.0; k<=n; k++) {
			sumForward += basicArray[(int)k];
			sumModifiedForward += modifiedArray[(int)k];
		}

		for(double k = n; k>=0; k--) {
			sumBackward += basicArray[(int)k];
			sumModifiedBackward += modifiedArray[(int)k];
		}
	}
	
    private double power(double x, double n) {
        double result = x;
        
        if (n == 0) return 1;
        
        for (int i = 1; i<n; i++) {
        	result *= x;
        }

        return result;
    }

	public double resultSumForward() {
		return sumForward;
	}
	
	public double resultSumModifiedForward() {
		return sumModifiedForward;
	}
	
	public double resultSumBackward() {
		return sumBackward;
	}
	
	public double resultSumModifiedBackward() {
		return sumModifiedBackward;
	}
}
