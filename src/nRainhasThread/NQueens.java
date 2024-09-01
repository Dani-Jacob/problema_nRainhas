package nRainhasThread;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NQueens {

	private int N;							 // Tamanho do tabuleiro (N x N)
	private int solutionCount;				 // Contador de soluções encontradas
	private boolean printSolutions; 		 // Indica se é necessário printar/armazenar todas as soluções
	private StringBuilder sb;				 // Armazena as soluções para printar/salvar em lote
	private int MAX_OUTPUT = 10000; 		 // Número máximo de soluções armazenadas antes de salvar no arquivo
    private String FILE_NAME = "output.txt"; // Nome do arquivo de saída

	// Construtor para inicializar a classe com o tamanho do tabuleiro e se deve printar as soluções
	public NQueens(int N, boolean printSolutions) {
		this.N = N;
		this.solutionCount = 0;
		this.printSolutions = printSolutions;
		this.sb = new StringBuilder();
	}

	// Getter para obter o número total de soluções encontradas
	public int getSolutionCount() {
		return this.solutionCount;
	}

	// Método público para iniciar a resolução do problema N-Rainhas
	public boolean solve() {
		// Limpa o arquivo de saída antes de começar a resolver o problema
		cleanOutput();
	    
	    // Cria um ExecutorService com um pool de threads fixo de tamanho N
	    ExecutorService executor = Executors.newFixedThreadPool(N);
	    
	    // Arrays booleanos para acompanhar as colunas e diagonais já ocupadas por rainhas
	    boolean[] columns = new boolean[N];
	    boolean[] diag1 = new boolean[2 * N - 1];
	    boolean[] diag2 = new boolean[2 * N - 1];

	    // Inicia uma tarefa para cada coluna da primeira linha do tabuleiro
	    for (int col = 0; col < N; col++) {
	        // Executa uma nova tarefa NQueensTask em uma thread separada
	        executor.execute(new NQueensTask(this, N, 0, col, columns, diag1, diag2));
	    }

	    // Encerra o ExecutorService, não aceita novas tarefas, mas continua executando as atuais
	    executor.shutdown();
	    
	    // Aguarda a conclusão de todas as threads antes de continuar
	    while (!executor.isTerminated()) {
	    }

	    // Retorna verdadeiro se ao menos uma solução foi encontrada
	    return solutionCount > 0;
	}

	// Método sincronizado para incrementar a variável contadora de soluções
	public synchronized void incrementSolutionCount() {
		solutionCount++;
	}

	// Método sincronizado para adicionar a solução encontrada à StringBuilder
	public synchronized void pushSolution(int[] board) {	
			// Adiciona a solução ao StringBuilder
			sb.append(solutionCount + " - Solução encontrada:\n");
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (board[i] == j) {
						sb.append(" Q "); // Representa uma rainha
					} else {
						sb.append(" . "); // Representa uma célula vazia
					}
				}
				sb.append("\n");
			}
			sb.append("\n");
			
			// Se o número de soluções acumuladas atingir o limite, salva no arquivo e limpa o StringBuilder
			if (solutionCount % MAX_OUTPUT == 0) {
				saveAllSolutions();
				sb.setLength(0);
			}
	}

	// Método para salvar todas as soluções armazenadas no StringBuilder no arquivo de saída
	public void saveAllSolutions() {   
        try (
            FileWriter fileWriter = new FileWriter(FILE_NAME, true);
            PrintWriter out = new PrintWriter(fileWriter)
        ) {
                out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	// Método recursivo para buscar possíveis soluções para o problema N-Rainhas
	public void solve(int row, int[] board, boolean[] columns, boolean[] diag1, boolean[] diag2) {
	    // Caso base: todas as rainhas já foram posicionadas, solução completa
	    if (row == N) {
	    	incrementSolutionCount(); // Incrementa o contador de soluções
			if (printSolutions) {
	        	// Armazena a solução encontrada
	        	pushSolution(board);
			}
	        return;
	    }

	    // Tenta posicionar uma rainha em cada coluna da linha atual
	    for (int col = 0; col < N; col++) {
	        // Verifica se é seguro colocar uma rainha na posição (row, col)
	        if (isSafe(row, col, columns, diag1, diag2)) {
	            // Posiciona a rainha na coluna atual
	            board[row] = col;
	            columns[col] = true; // Marca a coluna como ocupada
	            diag1[row - col + N - 1] = true; // Marca a diagonal principal como ocupada
	            diag2[row + col] = true; // Marca a diagonal secundária como ocupada

	            // Chama recursivamente para tentar posicionar a próxima rainha
	            solve(row + 1, board, columns, diag1, diag2);

	            // Desmarca as posições após retornar da recursão (backtracking)
	            columns[col] = false;
	            diag1[row - col + N - 1] = false;
	            diag2[row + col] = false;
	        }
	    }
	}

    // Método privado para verificar se a posição da rainha a ser inserida no tabuleiro é segura
	private boolean isSafe(int row, int col, boolean[] columns, boolean[] diag1, boolean[] diag2) {
		// Verifica se a coluna e as diagonais não estão ocupadas
		return !columns[col] && !diag1[row - col + N - 1] && !diag2[row + col];
	}
	
    // Método privado para limpar o arquivo de saída antes de começar a resolver o problema
    private void cleanOutput() {
	        try {
	            // Cria um novo FileWriter com o parâmetro false para limpar o arquivo
	            new FileWriter(FILE_NAME, false).close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
    }
	
}
