package nRainhasThread;

public class App {

	public static void main(String args[]) {
		System.out.println("Programa com threads iniciado, aguarde...");
		
		//Inicializa o timer
		Cronometro timer = new Cronometro();
		
		//Instancia um tabuleiro de tamanho n, com true para salvar todas as solucoes
		NQueens tabuleiro = new NQueens(13,true);
		//Resolve o problema das n rainhas do tabuleiro instanciado 
		tabuleiro.solve();
		//Salva novamente as solucoes restantes da variavel, garantindo que todas estão armazenadas
		tabuleiro.saveAllSolutions();
		
		
		//Para o timer e pega os milissegundos da execução
		long mili = timer.getTime();
		
		//Imprime as informacoes pertinentes
		System.out.println("Quantidade de solucoes encontradas: " + tabuleiro.getSolutionCount());
		System.out.println("Tempo de execução (mls): " + mili);
		
	}
}
