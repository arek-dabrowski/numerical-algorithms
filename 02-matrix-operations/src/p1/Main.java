package p1;

/**
 * Algorytmy numeryczne - zadanie 2
 * Iwona Jaœtak 246849
 * Arkadiusz D¹browski 246859
 */

public class Main {

	public static void main(String[] args) {
		int size = 500;
		Randomizer randomizer = new Randomizer();
		randomizer.setRandomMatrix(size);
		randomizer.setRandomVector(size);
		Test test = new Test();

		//test.timeEfficiencyTest(size);
		//test.chosenMethodTimeTest(size);
		test.errorsTest();
	}

}
