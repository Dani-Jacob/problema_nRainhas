package nRainhasSequencial;

import java.io.FileWriter;
import java.io.PrintWriter;

public class NQueens {

    private int[] board;  
    // board[i] representa a coluna da rainha na linha i
    // Exemplo: board[2] = 4 significa que na linha 3 a rainha está na coluna 4
    
    private int N; 					// Tamanho do tabuleiro (N x N)
    private int solutionCount; 		// Contador de soluções encontradas
    private boolean printSolutions; // Indica se é necessário printar/armazenar todas as soluções
    private boolean[] columns; 		// Array para marcar as colunas ocupadas por rainhas
    private boolean[] diag1;   		// Array para marcar as diagonais principais ocupadas por rainhas
    private boolean[] diag2;   		// Array para marcar as diagonais secundárias ocupadas por rainhas
    private StringBuilder sb;  		// StringBuilder para armazenar as soluções para impressão em lote
    
    private int MAX_OUTPUT = 10000; // Número máximo de soluções armazenadas antes de salvar no arquivo
    private String FILE_NAME = "output.txt"; // Nome do arquivo de saída
    
    // Construtor para inicializar o tabuleiro, variáveis de controle e arrays auxiliares
    public NQueens(int N, boolean printSolutions) {
        this.N = N;
        this.solutionCount = 0;
        board = new int[N];
        this.printSolutions = printSolutions;
        this.columns = new boolean[N];
        this.diag1 = new boolean[2 * N - 1];
        this.diag2 = new boolean[2 * N - 1];
        this.sb = new StringBuilder();
    }
    
    // Getter para obter o número total de soluções encontradas
    public int getSolutionCount() {
    	return this.solutionCount;
    }

    // Método público para iniciar a resolução do problema
    public boolean solve() {
    	// Limpa o arquivo de saída antes de começar a resolver o problema
    	cleanOutput();
        // Inicia a resolução a partir da primeira linha (row = 0)
        return solve(0, columns, diag1, diag2);
    }

    // Método privado recursivo onde ocorre a busca por soluções
    private boolean solve(int row, boolean[] columns, boolean[] diag1, boolean[] diag2) {
    	
        // Caso base: se todas as rainhas já foram colocadas (row == N)
    	if (row == N) {
        	solutionCount++; // Incrementa o contador de soluções
            if(printSolutions) {
            	// Adiciona a solução encontrada ao StringBuilder
            	pushSolution();
            }
            return true;
        }

    	// Será `true` se existir qualquer solução válida para o tabuleiro
        boolean result = false;
        for (int col = 0; col < N; col++) {
            // Verifica se é seguro colocar uma rainha na posição (row, col)
            if (isSafe(row, col, columns, diag1, diag2)) {
                // Coloca a rainha e marca as colunas e diagonais como ocupadas
                board[row] = col;
                columns[col] = true;
                diag1[row - col + N - 1] = true;
                diag2[row + col] = true;

                // Chama recursivamente para tentar resolver a próxima linha
                result = solve(row + 1, columns, diag1, diag2) || result;

                // Remove a rainha e desmarca as colunas e diagonais para backtracking
                columns[col] = false;
                diag1[row - col + N - 1] = false;
                diag2[row + col] = false;
            }
        }
        return result;
    }

    // Método privado para verificar se a posição da rainha a ser inserida no tabuleiro é segura
    private boolean isSafe(int row, int col, boolean[] columns, boolean[] diag1, boolean[] diag2) {
        // Verifica se a coluna e as diagonais não estão ocupadas
        return !columns[col] && !diag1[row - col + N - 1] && !diag2[row + col];
    }

    // Método privado para adicionar a solução encontrada ao StringBuilder
    private void pushSolution() {
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
    	if(solutionCount % MAX_OUTPUT == 0) {
    		saveAllSolutions();
    		sb.setLength(0);
    	}
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
    
    // Método público para salvar todas as soluções armazenadas no StringBuilder no arquivo de saída
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

}
