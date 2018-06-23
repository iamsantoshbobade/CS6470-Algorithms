package source;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;
import java.util.Scanner;

public class DiceRollerTrunk {

	char S[];
	int N[];
	static int input;
	double fairToFair,fairToLoaded,loadedToLoaded,loadedToFair;
	double bToF,bToL;

	public DiceRollerTrunk(int n) {
		input = n;
		S = new char[input];
		N = new int[input];
		
		String MODEL_PATH = "E:\\Study\\PADS_Workspace\\CasinoDice\\src\\source\\model.txt";
		String temp = new String("");
		String stringSplits[] = new String[3];
		
		Scanner scanner = null;
		try {
			scanner = new Scanner(new FileReader(MODEL_PATH));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		temp = scanner.nextLine();
		stringSplits = temp.split(":");
		bToF = Double.parseDouble(stringSplits[1]) * 100;
		bToL = Double.parseDouble(stringSplits[2]) * 100;

		temp = scanner.nextLine();
		stringSplits = temp.split(":");
		fairToFair = Double.parseDouble(stringSplits[1]) * 100;
		fairToLoaded = Double.parseDouble(stringSplits[2]) * 100;

		temp = scanner.nextLine();
		stringSplits = temp.split(":");
		loadedToLoaded = Double.parseDouble(stringSplits[1]) * 100;
		loadedToFair = Double.parseDouble(stringSplits[2]) * 100;

	}

	private int RandNoGenF() {
		Random r = new Random();
		int n = r.nextInt(6) + 1;
		return n;
	}

	private int RandNoGen() {
		Random r = new Random();
		int n = r.nextInt(10) + 1;
		return n;
	}

	private int RandNoGenS() {
		Random r = new Random();
		int n = r.nextInt(100) + 1;
		return n;
	}

	private void SequenceGenerator(int i) {
		int j = i;
		if (S[j] == 'F') {
			N[j] = RandNoGenF();
			// System.out.println("********RanGenF "+N[j]);
		}
		if (S[j] == 'L') {
			int r = RandNoGen();
			// System.out.println("********RanGen "+r);
			if (r > 5 && r < 11) {
				N[j] = 6;
			}
			if (r == 1) {
				N[j] = 1;
			}
			if (r == 2) {
				N[j] = 2;
			}
			if (r == 3) {
				N[j] = 3;
			}
			if (r == 4) {
				N[j] = 4;
			}
			if (r == 5) {
				N[j] = 5;
			}

		}

	}

	private void StateOne() {
		int r = RandNoGenS();
		// System.out.println("********RanGen "+r);
		if (r > bToF) {
			S[0] = 'L';
			SequenceGenerator(0);
		}
		if (r <= bToF) {
			S[0] = 'F';
			SequenceGenerator(0);
		}
	}

	private void StateGenerator() {
		for (int i = 1; i < input; i++) {
			if (S[i - 1] == 'F') {
				int r = RandNoGenS();
				if (r <= fairToFair) {
					S[i] = 'F';
					SequenceGenerator(i);
				} else {
					S[i] = 'L';
					SequenceGenerator(i);

				}
			}
			if (S[i - 1] == 'L') {
				int r = RandNoGenS();
				if (r <= loadedToLoaded) {
					S[i] = 'L';
					SequenceGenerator(i);
				} else {
					S[i] = 'F';
					SequenceGenerator(i);
				}
			}

		}
	}

	public static void main(String[] args) throws Exception {

		String toWrite = new String();
		System.out.println("Enter the number of sequence to be generated:");
		Scanner s = new Scanner(System.in);
		int input = s.nextInt();
		DiceRollerTrunk diceRoller = new DiceRollerTrunk(input);
		diceRoller.StateOne();
		diceRoller.StateGenerator();
		// System.out.println(Arrays.toString(diceRoller.S));
		// System.out.println(Arrays.toString(diceRoller.N));
		File file = new File("E:\\Study\\PADS_Workspace\\CasinoDice\\src\\source\\RollerGeneratedStatesSequence.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());

		BufferedWriter writer = new BufferedWriter(fw);
		toWrite = new String(diceRoller.S);
		writer.write(toWrite);

		writer.close();

		writer = null;
		file = null;
		file = new File("E:\\Study\\PADS_Workspace\\CasinoDice\\src\\source\\RollerGeneratedNumberSequence.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		fw = null;
		fw = new FileWriter(file.getAbsoluteFile());

		writer = new BufferedWriter(fw);
		// toWrite = new String(diceRoller.N.);
		// writer.write(
		toWrite = new String("");
		char c[] = new char[input];
		for (int i = 0; i < input; i++) {
			int k = diceRoller.N[i];
			c[i] = (char) (k + 48);
			toWrite = toWrite + c[i];
			// writer.write(k);
		}

		writer.write(toWrite);

		writer.close();

	}
}