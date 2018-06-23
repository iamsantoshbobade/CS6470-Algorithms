package source;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.Scanner;

public class CasinoDiceDecoderTrunk {

	int n;
	double M[][];
	int P[][];
	String sequence;

	public CasinoDiceDecoderTrunk(int size) {
		n = size;
		M = new double[2][n];
		P = new int[2][n];
		sequence = new String();

	}

	public static void main(String[] args) {

		String MODEL_PATH = "E:\\Study\\PADS_Workspace\\CasinoDice\\src\\source\\model.txt";

		CasinoDiceDecoderTrunk roller = null;

		double fairToFair = 0.0;
		double fairToLoaded = 0.0;
		double loadedToLoaded = 0.0;
		double loadedToFair = 0.0;

		// Roll Chances Probability
		double fairDieRollProbability = 0.0;
		double loadedDieProbability1To5 = 0.0;
		double loadedDieProbability6 = 0.0;

		// starting dice Probability
		double chanceForLoadedStart = 0.0;
		double chanceForFairStart = 0.0;

		String temp;
		String stringSplits[] = new String[3];
		int ourArray[] = null;

		try {

			roller = new CasinoDiceDecoderTrunk(5000);

			Scanner scanner = new Scanner(new FileReader(MODEL_PATH));
			temp = scanner.nextLine();
			stringSplits = temp.split(":");
			chanceForFairStart = Double.parseDouble(stringSplits[1]);
			chanceForLoadedStart = Double.parseDouble(stringSplits[2]);

			temp = scanner.nextLine();
			stringSplits = temp.split(":");
			fairToFair = Double.parseDouble(stringSplits[1]);
			fairToLoaded = Double.parseDouble(stringSplits[2]);

			temp = scanner.nextLine();
			stringSplits = temp.split(":");
			loadedToLoaded = Double.parseDouble(stringSplits[1]);
			loadedToFair = Double.parseDouble(stringSplits[2]);

			temp = scanner.nextLine();
			stringSplits = temp.split(":");
			fairDieRollProbability = Double.parseDouble(stringSplits[1]);

			temp = scanner.nextLine();
			stringSplits = temp.split(":");
			loadedDieProbability1To5 = Double.parseDouble(stringSplits[1]);
			loadedDieProbability6 = Double.parseDouble(stringSplits[2]);

			ourArray = readFileIntoArray(
					"E:\\Study\\PADS_Workspace\\CasinoDice\\src\\source\\RollerGeneratedNumberSequence.txt");

			roller.processTheAssembly(ourArray, fairToFair, fairToLoaded, loadedToLoaded, loadedToFair,
					fairDieRollProbability, loadedDieProbability1To5, loadedDieProbability6, chanceForLoadedStart,
					chanceForFairStart);

			roller.printPathMain();

			//File file = new File("E:\\Study\\PADS_Workspace\\CasinoDice\\src\\source\\DecodedStatesSequence.txt");
			File file = new File("RelativeEx.txt");
			
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);

			BufferedWriter writer = new BufferedWriter(fw);
			
			//Write to text file
			writer.write(roller.sequence);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static int[] readFileIntoArray(String fileName) {

		int number = 0;

		try {
			FileReader fr = new FileReader(fileName);

			// BufferedReader br = new BufferedReader(fr);

			int[] diceSequence = new int[10];
			int i = 0;
			int arrayMax = 10;

			number = fr.read();

			char c = (char) number;

			while (number != -1) {
				if (c == ' ') {
					number = fr.read();
					c = (char) number;
					continue;
				}
				diceSequence[i] = c - 48;
				i++;
				if (i == arrayMax) {
					int[] newArray = new int[arrayMax * 2];
					arrayMax = arrayMax * 2;
					for (int j = 0; j < diceSequence.length; j++) {
						newArray[j] = diceSequence[j];
					}
					diceSequence = newArray;
				}
				number = fr.read();
				c = (char) number;
			} // end of reading the file and putting to the array

			// now we cut down the size of the array
			// first search for the first null value

			int usedIndicesOfTheArray = 0;

			for (;;) {
				if (diceSequence[usedIndicesOfTheArray] != 0) {// the element in
					// here is not
					// null){
					usedIndicesOfTheArray++;
				} else {
					break;
				}
			}

			int[] finalThing = new int[usedIndicesOfTheArray];

			for (int j = 0; j < usedIndicesOfTheArray; j++) {
				finalThing[j] = diceSequence[j];
			}
			diceSequence = finalThing;
			return diceSequence;
			// end of the program if u naw mean

		} catch (Exception e) {
			System.out.println("Houston have problemszzz..naw mea");
			// return null;
		}
		return null;
	}

	/*
	 * public void checkIfWeHaveCorrectArray(int[] diceRolls) {
	 * 
	 * for (int i = 0; i < diceRolls.length; i++) {
	 * System.out.print(diceRolls[i] + ""); // or println }
	 * 
	 * }
	 */
	
	private String getRelativeURL(String fileName){
		
		URL url = getClass().getResource(fileName);
		return url.getPath();
	}

	public void processTheAssembly(int[] ourArray, double fairToFair, double fairToLoaded, double loadedToLoaded,
			double loadedToFair, double fairDiceRollProbability, double loadedDieProbability1To5,
			double loadedDieProbability6, double chanceForLoadedStart, double chanceForFairStart) {

		// double[][] M = new double[2][ourArray.length];

		M[0][0] = chanceForFairStart;
		M[1][0] = chanceForLoadedStart;

		// int[][] P = new int[2][ourArray.length];

		double dicePercent;

		for (int j = 1; j < ourArray.length; j++) {

			// getting the percent for our array
			if (ourArray[j] == 6) {
				dicePercent = loadedDieProbability6;
			} else {
				dicePercent = loadedDieProbability1To5;
			}

			// filling up all 4 (really 2) arraize
			if ((M[0][j - 1] * fairToFair * (1.0 / 6)) > (M[1][j - 1] * loadedToFair * (1.0 / 6))) {
				M[0][j] = M[0][j - 1] * fairToFair * (1.0 / 6);
				P[0][j] = 1;
			} else {
				M[0][j] = M[1][j - 1] * loadedToFair * (1.0 / 6);
				P[0][j] = 2;
			}
			if ((M[1][j - 1] * loadedToLoaded * dicePercent) > (M[0][j - 1] * fairToLoaded * dicePercent)) {
				M[1][j] = M[1][j - 1] * loadedToLoaded * dicePercent;
				P[1][j] = 2;
			} else {
				M[1][j] = M[0][j - 1] * fairToLoaded * dicePercent;
				P[1][j] = 1;
			}
		}
		// return new Object[] { M, P };

	}

	public void printPathMain() {

		/*
		 * if (ourArray[0][0][ourArray[0][0].length] >
		 * ourArray[0][1][ourArray[0][0].length])
		 * 
		 * { printPath(ourArray[1][0], ourArray[0][0].length); } else {
		 * printPath(ourArray[1][1],ourArray[0][0].length); }
		 */

		// if (M[0][n - 1] > M[1][n - 1]) {
		printPath(P, n - 1);
		/*
		 * } else {
		 * 
		 * }
		 */
	}

	public void printPath(int[][] P, int j) {

		if (j >= 0) {

			printPath(P, j - 1);

			if (P[0][j] == 1) {
				// System.out.print("F");
				sequence = sequence + "F";

			} else if (P[1][j] == 2)

			{
				// System.out.print("L");
				sequence = sequence + "L";
			}
		}

	}
}
