package pairwisealignment;

import java.io.DataInputStream;

public class PairwiseAlignment {

	int seqXLength;
	int seqYLength;
	String seqX;
	String seqY;
	int M[][];
	int I[][];
	int D[][];
	final int match = 5;
	final int mis_match = -2;
	final int op = -6;
	final int ext = -1;
	final int NEGATIVE_INFINITY = -12345;

	public PairwiseAlignment(String seqX, String seqY) {

		this.seqX = seqX;
		this.seqY = seqY;
		seqXLength = seqX.length();
		seqYLength = seqY.length();

		M = new int[seqXLength + 1][seqYLength + 1];
		M[0][0] = 0;
		for (int i = 1; i <= seqXLength; i++) {
			M[i][0] = NEGATIVE_INFINITY;

		}
		for (int i = 1; i < seqYLength; i++) {
			M[0][i] = NEGATIVE_INFINITY;
		}
		I = new int[seqXLength + 1][seqYLength + 1];
		I[0][0] = 0;
		I[0][1] = op;
		for (int i = 1; i <= seqXLength; i++) {
			I[i][0] = NEGATIVE_INFINITY;
		}
		for (int i = 2; i <= seqYLength; i++) {
			I[0][i] = I[0][i - 1] - 1;
		}
		D = new int[seqXLength + 1][seqYLength + 1];
		D[0][0] = 0;
		D[1][0] = op;
		for (int i = 1; i <= seqYLength; i++) {
			D[0][i] = NEGATIVE_INFINITY;

		}
		for (int i = 2; i <= seqXLength; i++) {
			D[i][0] = D[i - 1][0] - 1;
		}
	}

	int scoreCheck(int i, int j) {
		if (seqX.charAt(i) == seqY.charAt(j)) {
			return match;

		} else {
			return mis_match;
		}

	}

	void computeScore() {
		for (int i = 1; i <= seqXLength; i++) {
			for (int j = 1; j <= seqYLength; j++) {
				M[i][j] = scoreCheck(i - 1, j - 1)
						+ Math.max(M[i - 1][j - 1], Math.max(I[i - 1][j - 1], D[i - 1][j - 1]));
				I[i][j] = Math.max(I[i][j - 1] + ext, M[i][j - 1] + op);
				D[i][j] = Math.max(D[i - 1][j] + ext, M[i - 1][j] + op);
			}
		}

	}

	void printMatrix(int matrix[][]) {
		for (int i = 0; i < matrix.length; i++) {
			System.out.println();
			for (int j = 0; j < matrix[i].length; j++)
				System.out.print(matrix[i][j] + "\t");
		}
	}

	public static void main(String[] args) {

		String tempString1 = new String();
		String tempString2 = new String();

		try {
			DataInputStream in = new DataInputStream(System.in);
			System.out.println("Enter first sequence:");
			tempString1 = in.readLine();
			System.out.println("Enter second sequence:");
			tempString2 = in.readLine();
			PairwiseAlignment pairwiseAlignment = new PairwiseAlignment(tempString1, tempString2);
			pairwiseAlignment.computeScore();
			System.out.println("\nMatrix M: ");
			pairwiseAlignment.printMatrix(pairwiseAlignment.M);
			System.out.println("\nMatrix I: ");
			pairwiseAlignment.printMatrix(pairwiseAlignment.I);
			System.out.println("\nMatrix D: ");
			pairwiseAlignment.printMatrix(pairwiseAlignment.D);

		} catch (Exception e) {
			System.out.println("Exception occured.. : " + e.getMessage());
		}

	}

}
