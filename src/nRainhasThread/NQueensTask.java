package nRainhasThread;

public class NQueensTask implements Runnable {

    private int[] board;           // Representa o tabuleiro, onde o índice é a linha e o valor é a coluna da rainha
    private int row;               // A linha atual onde a próxima rainha será colocada
    private boolean[] columns;     // Marca quais colunas já estão ocupadas por rainhas
    private boolean[] diag1;       // Marca quais diagonais principais ("/") já estão ocupadas
    private boolean[] diag2;       // Marca quais diagonais secundárias ("\") já estão ocupadas
    private NQueens solver;        // Referência ao solver NQueens para continuar a solução

    // Construtor
    public NQueensTask(NQueens solver, int N, int row, int col, boolean[] columns, boolean[] diag1, boolean[] diag2) {
        this.solver = solver;    
        this.row = row;            
        this.columns = columns.clone();
        this.diag1 = diag1.clone();    
        this.diag2 = diag2.clone();  
        this.board = new int[N];  
        this.board[row] = col;
        this.columns[col] = true;
        this.diag1[row - col + N - 1] = true;
        this.diag2[row + col] = true;
    }

    @Override
    public void run() {
        // Inicia a resolução a partir da próxima linha, após a posição inicial (row, col)
        solver.solve(row + 1, board, columns, diag1, diag2);
    }
}
