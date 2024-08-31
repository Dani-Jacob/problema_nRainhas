package nRainhasSequencial;

import java.io.FileWriter;
import java.io.PrintWriter;

public class NQueens {

    private int[] board;  
    // board[i] representa a coluna da rainha na linha i
    // Exemplo:  board[2] = 4 ; na linha 3 a rainha esta na coluna 4
    
    private int N; 					// Tamanho do tabuleiro (N x N)
    private int solutionCount; 		//Contador de solucoes encontradas
    private boolean printSolutions; // Diz se é necessario printar/armazenar todas as solucoes
    private boolean[] columns; 		// Colunas
    private boolean[] diag1;   		// Diagonais principais
    private boolean[] diag2;   		// Diagonais secundárias
    private StringBuilder sb;  		// Armazena os outputs
    
    private int MAX_OUTPUT = 10000; // tamanho max do pacote de armazenamento de solucoes para o print
    private String FILE_NAME = "output.txt"; //Nome do arquivo de saida
    
    //Construtor
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
    
    //Getter
    public int getSolutionCount() {
    	return this.solutionCount;
    }

    //Metodo publico para iniciar a resolucao do problema
    public boolean solve() {
    	//limpa o txt de saida
    	cleanOutput();
        return solve(0, columns, diag1, diag2);
    }

    //Metodo privado onde ocorre a recursividade para buscar solucoes
    private boolean solve(int row, boolean[] columns, boolean[] diag1, boolean[] diag2) {
    	
        //Se todas as rainhas ja foram colocadas
    	if (row == N) {
        	solutionCount++;
            if(printSolutions) {
            	//Adiciona a solucao encontrada na variavel
            	pushSolution();
            }
            return true;
        }

    	//Será true se existir qualquer solucao para o tabuleiro
        boolean result = false;
        for (int col = 0; col < N; col++) {
            if (isSafe(row, col, columns, diag1, diag2)) {
                // Coloca a rainha e marca as colunas e diagonais como ocupadas
                board[row] = col;
                columns[col] = true;
                diag1[row - col + N - 1] = true;
                diag2[row + col] = true;

                // Tenta resolver a próxima linha
                result = solve(row + 1, columns, diag1, diag2) || result;

                // Remove a rainha e desmarca as colunas e diagonais             
                columns[col] = false;
                diag1[row - col + N - 1] = false;
                diag2[row + col] = false;
            }
        }
        return result;
    }

    //Metodo privado para verificar se a posicao da rainha a ser inserida no tabuleiro é segura
    private boolean isSafe(int row, int col, boolean[] columns, boolean[] diag1, boolean[] diag2) {
        return !columns[col] && !diag1[row - col + N - 1] && !diag2[row + col];
    }

    //Metodo privado para adicionar a solucao encontrada na variavel que armezena os resultados
    private void pushSolution() {
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
    	if(solutionCount % MAX_OUTPUT == 0) {
    		saveAllSolutions();
    		sb.setLength(0);
    	}
    }
    
    //Metodo privado para limpar o arquivo de saida
    private void cleanOutput() {
	        try {
	            new FileWriter(FILE_NAME, false).close();;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
    }
    
    //Metodo publico para pegar todas as solucoes armazedas e inserir no .txt de saida
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