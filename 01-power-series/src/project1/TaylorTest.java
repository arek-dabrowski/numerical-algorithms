package project1;

public class TaylorTest {
	public void test() {
		TaylorLog log;
		TaylorArctg arctg;
//		int count = 0;
//		int countForward = 0;
//		int countBackward = 0;
//		int countBasic = 0;
//		int countPrevious = 0;
		int iterations = 150;
//		double[] countAbsoluteError = new double[15];
		double res = 0.0;
		double res1, res2, res3, res4;
		res1 = res2 = res3 = res4 = 0.0;
		for(double i = -0.999999; i <= 1; i += 0.000002) {
			log = new TaylorLog(i, iterations);
			arctg = new TaylorArctg(i, iterations);
			res = Math.log(1+i)*Math.atan(i);
			for(int j = 10; j <= iterations; j += 10) {
				log.sum(j);
				arctg.sum(j);
				res1 = log.resultSumForward() * arctg.resultSumForward();
				res2 = log.resultSumModifiedForward() * arctg.resultSumModifiedForward();
				res3 = log.resultSumBackward() * arctg.resultSumBackward();
				res4 = log.resultSumModifiedBackward() * arctg.resultSumModifiedBackward();
//Test pytania 1
//				countAbsoluteError[j/10 - 1] = Math.abs(res1-res);
				
//Test hipotezy 1
//				if(Math.abs(res1-res) < Math.abs(res3-res)) countForward++;
//				else if(Math.abs(res1-res) > Math.abs(res3-res)) countBackward++;
//				if(Math.abs(res2-res) < Math.abs(res4-res)) countForward++;
//				else if(Math.abs(res2-res) > Math.abs(res4-res)) countBackward++;

//Test hipotezy 3
//				if(Math.abs(res1-res) < Math.abs(res2-res)) countBasic++;
//				else if(Math.abs(res1-res) > Math.abs(res2-res)) countPrevious++;
//				if(Math.abs(res3-res) < Math.abs(res4-res)) countBasic++;
//				else if(Math.abs(res3-res) > Math.abs(res4-res)) countPrevious++;


			}
			System.out.println("ln(1+x)*arctg(x) for x = "+i+" at "+iterations+" elements");
			System.out.print("\nMATH library result:\t\t");
			System.out.format("%10.30f%n",Math.log(1+i)*Math.atan(i));
			System.out.print("\nTaylor forward:\t\t\t");
			System.out.format("%10.30f%n",res1);
			System.out.print("Taylor backward:\t\t");
			System.out.format("%10.30f%n",res2);
			System.out.print("\nPrevious element forward:\t");
			System.out.format("%10.30f%n",res3);
			System.out.print("Previous element backward:\t");
			System.out.format("%10.30f%n",res4);
			System.out.println("\n");
		}
	}
}
