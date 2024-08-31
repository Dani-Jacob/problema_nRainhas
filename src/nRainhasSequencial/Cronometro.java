package nRainhasSequencial;

import java.util.Date;

public class Cronometro{
	
	private Date start;

	//Contrututor do timer, já salvando a hora atual no start
	public Cronometro(){
		reset();
	}

	//Pega o tempo em mls, com base no start
	public long getTime(){
		Date now = new Date();
		//Tempo percorrido = tempo atual - inicial
		long millis = now.getTime() - start.getTime();
		return millis;
	}

	//Reseta o start para a hora atual
	public void reset(){
		start = new Date(); 
	}
}