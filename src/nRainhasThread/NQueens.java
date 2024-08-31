package nRainhasThread;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NQueens {

	private int N;							 // Tamanho do tabuleiro (N x N)
	private int solutionCount;				 //Contador de solucoes encontradas
	private boolean printSolutions; 		 // Diz se é necessario printar/armazenar todas as solucoes
	private StringBuilder sb;				 // Armazena os outputs
	private int MAX_OUTPUT = 10000; 		 // Tamanho max do pacote de armazenamento de solucoes para o print
    private String FILE_NAME = "output.txt"; //Nome do arquivo de saida

	//Construtor
	public NQueens(int N, boolean printSolutions) {
		this.N = N;
		this.solutionCount = 0;
		this.printSolutions = printSolutions;
		this.sb = new StringBuilder();
	}

	//Getter
	public int getSolutionCount() {
		return this.solutionCount;
	}

	//Metodo publico onde iniciasse a resolucao do problema
	public boolean solve() {
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

	    // Encerra o ExecutorService, não aceita novas tarefas, mas continua com as atuais
	    executor.shutdown();
	    
	    // Aguarda a conclusão de todas as threads
	    while (!executor.isTerminated()) {
	    }

	    // Retorna verdadeiro se ao menos uma solução foi encontrada
	    return solutionCount > 0;
	}

	//Metodo para incrementar a variavel contadora de solucoes
	public synchronized void incrementSolutionCount() {
		solutionCount++;
	}

	//Metodo para adicionar a solucao encontrada na variavel
	public synchronized void pushSolution(int[] board) {	
			sb.append(solutionCount + " - Solução encontrada:\n");
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (board[i] == j) {
						sb.append(" Q ");
					} else {
						sb.append(" . ");
					}
				}
				sb.append("\n");
			}
			sb.append("\n");
			 //Caso tenha antingido o limite estabelecido de solucoes adicionadas na variavel, salva e reinicia a variavel
			if (solutionCount % MAX_OUTPUT == 0) {
				saveAllSolutions();
				sb.setLength(0);
			}
	}

	//Metodo para salvar todos as solucoes da variavel no arquivo de saida
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

	//Metodo para buscar possiveis solucoes para o problema
	public void solve(int row, int[] board, boolean[] columns, boolean[] diag1, boolean[] diag2) {
	    //Todas as rainhas ja foram colocadas
	    if (row == N) {
	    	incrementSolutionCount();
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

    //Metodo privado para verificar se a posicao da rainha a ser inserida no tabuleiro é segura
	private boolean isSafe(int row, int col, boolean[] columns, boolean[] diag1, boolean[] diag2) {
		return !columns[col] && !diag1[row - col + N - 1] && !diag2[row + col];
	}
	
    //Metodo privado para limpar o arquivo de saida
    private void cleanOutput() {
	        try {
	            new FileWriter(FILE_NAME, false).close();;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
    }
	
}


