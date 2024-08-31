package nRainhasSequencial;

public class App {

	public static void main(String args[]) {
		//Print para indicar o inicio do programa
		System.out.println("Programa sequencial iniciado, aguarde... \n");
	
		//Inicializa o timer
		Cronometro timer = new Cronometro();
		
		//Instancia um tabuleiro de tamanho n, com true para salvar todas as solucoes
		NQueens tabuleiro = new NQueens(14,true);
		//Resolve o problema das n rainhas do tabuleiro instanciado 
		tabuleiro.solve();
		//Salva novamente as solucoes restantes da variavel, garantindo que todas estão armazenadas
		tabuleiro.saveAllSolutions();
		
		//Pega os milissegundos da execução até o momento
		long mili = timer.getTime();
		
		//Imprime as informacoes pertinentes
		System.out.println("Quantidade de solucoes encontradas: " + tabuleiro.getSolutionCount());
		System.out.println("Tempo de execução (mls): " + mili);
		
	}
}
