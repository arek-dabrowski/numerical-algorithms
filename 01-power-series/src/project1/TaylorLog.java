package project1;

public class TaylorLog {
	private static double[] basicArray;
	private static double[] modifiedArray;

	private double sumForward, sumBackward, sumModifiedForward, sumModifiedBackward = 0.0;

	TaylorLog(double x, int n){
		basicArray = new double[n+1];
		modifiedArray = new double[n+1];

		for(double k = 1.0; k<=n; k++) {
			basicArray[(int)k-1] = (power(-1.0,k+1) * power(x,k))/k;
			if (k == 1.0){
				modifiedArray[(int)k-1] = x;
			} else {
				modifiedArray[(int)k-1] = modifiedArray[(int) k-2] * (-(x*(k-1)))/k;
			}
		}	
	}



	public void sum(int n) {
		sumForward = sumModifiedForward = sumBackward = sumModifiedBackward = 0.0;

		for(double k = 0.0; k<n; k++) {
			sumForward += basicArray[(int)k];
			sumModifiedForward += modifiedArray[(int)k];
		}

		for(double k = n-1; k>=0; k--) {
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
