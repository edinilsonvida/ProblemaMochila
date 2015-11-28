import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

public class Mochila {

	private static int mochilaRec(int i, Integer W, int[] itemPeso,
			int[] itemValor) {
		if (i < 0) {
			return 0;
		}
		if (itemPeso[i] > W) {
			return mochilaRec(i - 1, W, itemPeso, itemValor);
		} else {
			return Math.max(mochilaRec(i - 1, W, itemPeso, itemValor),
					mochilaRec(i - 1, W - itemPeso[i], itemPeso, itemValor)
							+ itemValor[i]);
		}
	}

	public static int[][] mochilaPDSR(int[][] K, int capacidade, int n,
			int[] pesoItem, int[] lucroItem) {
		int cap = 0;
		for (int i = 0; i <= capacidade; i++) {
			K[i][0] = 0;
		}
		for (int i = 0; i <= n; i++) {
			K[0][i] = 0;
		}
		for (int j = 0; j < n; j++) {
			for (int w = 1; w <= capacidade; w++) {
				if (pesoItem[j] > w) {
					K[w][j + 1] = K[w][j];
					// matrizKeep[w][j+1] = 0;
				} else {
					cap = w - pesoItem[j];
					K[w][j + 1] = Math.max(K[w][j], K[cap][j] + lucroItem[j]);
					// matrizKeep[w][j+1] = 1;
				}
			}
		}

		// retornaSolucaoOtima(capacidade,K,pesoItem);
		/* int[] optimalSubset = */// getOptimalSubset(K, pesoItem);
		// printArray(optimalSubset, "Optimal Subset", pesoItem, lucroItem);
		// return K[capacidade][n];
		return K;
	}

	private static void printMatriz(int capacidade, int n, int[][] K) {
		System.out.println(" --- Matriz Solução Otima --- ");
		for (int w = 0; w < capacidade + 1; w++) {
			for (int j = 0; j < n + 1; j++)
				System.out.print(K[w][j] + "\t");
			System.out.println('\n');
		}
	}

	private static void getOptimalSubset(int[][] solutionMatrix, int[] weights) {
		// int[] subset = new int[weights.length];
		// int numItems = 0;
		int peso = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("\n").append("Solução Otima").append(":\n{");
		int i = solutionMatrix.length - 1;
		int k = solutionMatrix[0].length - 1;
		while (i >= 0 && k > 0) {
			if (solutionMatrix[i][k] != solutionMatrix[i][k - 1]) {
				// subset[numItems] = k;
				builder.append(k).append(" ");
				k = k - 1;
				i -= weights[k];
				// numItems++;
				peso += weights[k];
			} else {
				k = k - 1;
			}
		}

		builder.append("}");
		System.out.println(builder);
		System.out.println("Peso:" + peso);
		/*
		 * for (int j = (solutionMatrix[0].length - 1); i > 0 && j > 0; j--) {
		 * if (solutionMatrix[i][j] != solutionMatrix[i][j-1]) {
		 * subset[numItems] = i; i -= weights[j-1]; numItems++; } }
		 */
		// return Arrays.copyOfRange(subset, 0, numItems);
	}

	private static void printVetor(int[] itemPeso, int[] itemValor) {
		System.out.println(" Item\t Peso\t  Valor ");
		for (int i = 0; i < itemPeso.length; i++) {
			System.out.println(" " + (i + 1) + " \t " + itemPeso[i] + " \t  "
					+ itemValor[i]);

		}
	}

	public static void main(String[] args) throws IOException {
		Integer n = 90; /*  = 4;*/ /*= 5000;*/
		Integer capacidade = 150; /* =10;*/  /*= 1050;*/

		 int[] pesoItem = new int[n];
		 int[] lucroItem = new int[n];

		Random rand = new Random();

		//int[] pesoItem /*= { 2, 3, 4 };*/  = { 6, 3, 4, 2 }; 
															 /* =
															 * {50,34,70,39,81,
															 * 62,55,46,32,68};
															 */
		/* = {2,4,7,5}; */
		//int[] lucroItem /*= { 1, 2, 5 };*/  = { 30, 14, 16, 9 }; 
																/* =
																 * {37,28,53,32
																 * ,57
																 * ,44,33,55,30
																 * ,54};
																 */
		/* = {5,4,2,6}; */

		
		  for (int i = 0; i < pesoItem.length; i++) {
			  pesoItem[i] = rand.nextInt(40) + 5;
			  lucroItem[i] = rand.nextInt(50) + 5;
		  }
		 

		File arquivo = new File(
				"C:\\Users\\Andre\\workspace\\ProblemaMochila\\output.txt");
		if (arquivo.exists()) {
			arquivo.delete();
		} else {
			arquivo.createNewFile();
		}
		FileWriter fw = new FileWriter(arquivo, true);
		BufferedWriter bw = new BufferedWriter(fw);

		printVetor(pesoItem, lucroItem);
		System.out.println("\n");
		System.out.println("--- Algoritmo Recursivo ---");
		System.out.println("\n");
		long tempoInicialR = System.currentTimeMillis();
		int valorRec = mochilaRec(n - 1, capacidade, pesoItem, lucroItem);
		long tempoFinalR = System.currentTimeMillis();
		System.out.println("Lucro Rec: "
				+ valorRec);
		
		long delta = (tempoFinalR - tempoInicialR);		
		System.out.printf(" %d milisec, %f sec, %f min, %f horas", delta, ((double)delta / 1000) % 60, ((double)delta / 60000)% 60, ((double)delta / 3600000) %24);
		/*System.out.println("Tempo Algoritmo Recursivo: "
				+ (tempoFinalR - tempoInicialR));*/
		int[][] K = new int[capacidade + 1][n + 1];
		// int valor = mochilaPDSR(K, capacidade, n, pesoItem, lucroItem);
		System.out.println("\n");
		System.out.println("--- Algoritmo PD ---");
		long tempoInicialPD = System.currentTimeMillis();
		K = mochilaPDSR(K, capacidade, n, pesoItem, lucroItem);
		long tempoFinalPD = System.currentTimeMillis();
		long deltaPD = tempoFinalPD - tempoInicialPD;
		getOptimalSubset(K, pesoItem);
		System.out.println("Lucro PD:  " + K[capacidade][n]);
		System.out.printf(" %d milisec, %f sec, %f min, %f horas", deltaPD, ((double)deltaPD / 1000) % 60, ((double)deltaPD / 60000)% 60, ((double)deltaPD / 3600000) %24);
		/*System.out.println("Tempo Algoritmo PD: "
				+ (tempoFinalPD - tempoInicialPD));*/
		printMatriz(capacidade, n, K);

	}

}
