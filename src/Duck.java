import java.util.Calendar;

/*	Classe que implementa a lógica do jogo, modificando os atributos e o que deve ser impresso na tela de acordo com
 * o estado atual do jogo.*/
public class Duck implements Jogo{
	public Passaro bird1;
	public Passaro bird2;
	public Passaro bird3;
	
	public Balas balas;
	public Random gerador = new Random();
	public int game_state = 0; //[0->Start Game] [1->Game Classico] [2->Game Mutiplayer] [3->Credits] [4->Tela de Espera Single Player][5->Game Over Single Player][6->Game Over Mult Player]
	
	public double scenario_offset = 0;
	public double ground_offset = 0;
	
	public int timer = 0;
	public int round = 1;
	public int wave = 0;
	public long multiTime;
	public int cronometer;
	public int remaining;
	public Som som;/*Usado para executar as musicas do jogo*/
	
	
	public ScoreNumber usuario1;/*Utilizado pra armazenar a pontuacao  do usuario (atirador)*/
	public ScoreNumber usuario2;/*Utilizado pra armazenar a pontuacao  do usuario (pato)*/
	
	public Duck() {
		som = new Som();/*Inicializa o estado*/
		balas = new Balas(4);
		usuario1 = new ScoreNumber(0);
		usuario2 = new ScoreNumber(0);
		som.enableSom(0);
	}

	public String getTitulo(){
		return "Duck Hunt";
	}
	
	public int getLargura(){
		return 1400;
	}
	
	public int getAltura(){
		return 927;
	}
	
	/*	Método que a atualização dos atributos que variam com o tempo, no caso, a posição e velocidade dos patos.*/
	public void tique(double dt){
		if(game_state == 1 ) {
			bird1.update( dt, game_state);/*Atualizacao da imagem do passaro ao longo dos ticks da imagem*/
			bird2.update( dt, game_state);
			bird3.update( dt, game_state);
		}else if(game_state == 2) {
			bird1.update( dt, game_state);/*Atualizacao da imagem do passaro ao longo dos ticks da imagem*/
		}
	}
	
	/*	Define como o clique do mouse deve interferir no jogo, dependendo do estado atual.*/
	public void mouse(int x, int y) {
		switch(game_state) {
			case 0:/*Menu*/
				if(x >= 142 && x <= 447 && y >= 323 && y <= 416 ) {
					game_state = 4;
					multiTime = Calendar.getInstance().getTimeInMillis();
					som.disableSom();
				}else if(x >= 142 && x <= 447 && y >= 466 && y <= 530) {/*Mutiplayer, rever ainda hitbox da imagem*/
					som.disableSom();
					game_state = 2;
					multiTime = Calendar.getInstance().getTimeInMillis();
					bird1.limiar_do_flap = 0;
				}
				else if(x >= 142 && x <= 447 && y >= 550 && y <= 637) {
					som.enableSom(3);//Ativa o som de creditos
					game_state = 3;
				}
				break;
				
			case 1:/*Jogo Single player*/
				if(balas.balas > 0) {
					Som.tocarSom("Music/tiro.wav");
					bird1.hitbox(x, y, usuario1);
					bird2.hitbox(x, y, usuario1);
					bird3.hitbox(x, y, usuario1);
				}
				
				balas.balas -= 1;
				if(balas.balas == 0){
					bird1.livre = true;
					bird2.livre = true;
					bird3.livre = true;
				}
				break;
				
			case 2:/*Jogo Multiplayer*/
				if(!bird1.hitbox(x, y, usuario1)) {
					usuario2.modifyScore(200);
				}

				Som.tocarSom("Music/tiro.wav");
				break;
				
			case 3: /*Creditos*/
				if(x >= 142 && x <= 447 && y >= 550 && y <= 637) {
					game_state = 0;
					som.enableSom(0);//Ativa o som de menu
				}
				break;
				
			case 5: /*Game Over Single player*/
				if(x >= 142 && x <= 447 && y >= 550 && y <= 637) {
					game_state = 0;
					som.enableSom(0);//Ativa o som de menu
				}
				
				break;
			case 6: /*Game Over Multiplayer*/
				if(x >= 142 && x <= 447 && y >= 550 && y <= 637) {
					game_state = 0;
					som.enableSom(0);//Ativa o som de menu
				}
				
				break;
		}
	}
	
	/*	Define como o jogo deve interagir com as interacoes com o teclado. So ocorre no modo multiplayer, quando
	 * um usuario controla o pato.*/
	public void tecla(String c){
		if(c.equals("right")) bird1.right();
		if(c.equals("left")) bird1.left();
		if(c.equals(" ")) bird1.flap();
	}

	/*	Metodo que realiza a impressao das imagens na tela de acordo com o estado atual do jogo.*/
	public void desenhar(Tela t){
		switch(game_state) {
			case 0:/*Menu*/
				t.imagem("sprites/DuckMenu.jpg", 0, 0, 1911, 927, 0, 0);
				round = 1;
				usuario1.setScore(0);
				usuario2.setScore(0);
				wave = 4;
				usuario1.nPatos = 10;
				
				bird1 = new Passaro(getLargura (), getAltura(), "Verde", game_state);
				sleep(10);
				bird2 = new Passaro(getLargura (), getAltura(), "Preto", game_state);
				sleep(10);
				bird3 = new Passaro(getLargura (), getAltura(), "Azul", game_state);
				sleep(10);
				break;
			case 1:/*Jogo Single Player*/
				t.imagem("sprites/DuckSegundoPlano.png", 0, 0, 1911, 927, 0, 0);//Desenha a tela de fundo atras do pato
				
				//Desenha o passaro
				bird1.desenhar(t);
				bird2.desenhar(t);
				bird3.desenhar(t);
				
				t.imagem("sprites/DuckPrimeiroPlanoSingle.png", 0, 0, 1911, 927, 0, 0);//Desenha a tela de fundo na frente do pato
				bird1.desenhaPontuacao(t, usuario1);
				bird2.desenhaPontuacao(t, usuario1);
				bird3.desenhaPontuacao(t, usuario1);
				balas.desenhar(t);
				
				if(bird1.y > bird1.maxY && bird2.y > bird2.maxY && bird3.y > bird3.maxY) { //Passaros mortos e passaram da parte inferior da tela
					game_state = 4;
					wave += 1;
					multiTime = Calendar.getInstance().getTimeInMillis();
				}
				
				else if(balas.balas <= 0 && (bird1.y < 0 || bird1.y > bird1.maxY) && (bird2.y < 0 || bird2.y > bird2.maxY) && (bird3.y < 0 || bird3.y > bird3.maxY)) { //Patos fugiram ou morreram, e acabaram as balas
					game_state = 4;
					wave += 1;
					multiTime = Calendar.getInstance().getTimeInMillis();
				}
				
				usuario1.drawJustNumber(t, usuario1.nPatos, 900, 809);
				remaining = 12 - 3 * (wave + 1);
				if(remaining <= 0) remaining = 0;
				
				usuario1.drawJustNumber(t, remaining, 580, 809);
				
				break;
			case 2: /*Jogo multiplayer*/
				t.imagem("sprites/DuckSegundoPlano.png", 0, 0, 1911, 927, 0, 0);//Desenha a tela de fundo atras do pato

				bird1.desenhar(t);	
				t.imagem("sprites/DuckPrimeiroPlanoMulti.png", 0, 0, 1911, 927, 0, 0);//Desenha a tela de fundo na frente do pato
				bird1.desenhaPontuacao(t, usuario1);
				
				usuario2.drawJustNumber(t, usuario2.getScore(), 50, (int)getAltura()-112);
				
				cronometer = 60 - (int)((Calendar.getInstance().getTimeInMillis() - multiTime)/1000);
				usuario1.drawJustNumber(t,cronometer, 1200, 50);
				
				if(cronometer <= 0) game_state = 6;
				
				break;
				
			case 3: /*Credits*/
				t.imagem("sprites/Credits.jpg", 0, 0, 1911, 927, 0, 0);
				break;
				
			case 4:/*Tela de espera do single player*/
				
				t.imagem("sprites/DuckSegundoPlano.png", 0, 0, 1911, 927, 0, 0);
				if(wave < 4){
					bird1.reiniciar();
					bird2.reiniciar();
					bird3.reiniciar();
					balas.balas = 4;
					game_state = 1;
				}
				
				else if(wave == 4 && usuario1.nPatos < 8){ // GAME OVER
					som.enableSom(2);//Ativa o som de Game Over
					bird1 = new Passaro(getLargura (), getAltura(), "Verde", game_state);
					bird2 = new Passaro(getLargura (), getAltura(), "Preto", game_state);
					bird3 = new Passaro(getLargura (), getAltura(), "Azul", game_state);
					game_state = 5;
				}
				
				else {
					som.enableSom(1);//Ativa a musica de transicao de round
					t.imagem("sprites/MensagemInicioRound.png",  0, 0, 1911, 927, 0, 0);
					usuario1.drawJustNumber(t, round, 866, 260);
	
					cronometer = 3 - (int)((Calendar.getInstance().getTimeInMillis() - multiTime)/1000);
					
					if(cronometer <= 0) {
						if(round != 1) {
							bird1.reiniciar();
							bird2.reiniciar();
							bird3.reiniciar();
						}
						balas.balas = 4;
						game_state = 1;
						round += 1;
						wave = 0;
						usuario1.nPatos = 0;
						som.disableSom();/*Desativa a musica de transicao de round*/
					}
				}
				break;
				
			case 5: /*Fim de jogo single player*/
				t.imagem("sprites/Game_Over.jpg", 0, 0, 1911, 927, 0, 0);
				t.imagem("sprites/MensagemGameOver.png", 0, 120, 340, 60, 740, 250);
				usuario1.drawJustNumber(t, usuario1.getScore(), 760, 340);

				break;
				
			case 6:/*Fim de jogo multiplayer*/
				t.imagem("sprites/Game_Over.jpg", 0, 0, 1911, 927, 0, 0);
				
				if(usuario1.getScore() == usuario2.getScore()) {
					t.imagem("sprites/MensagemGameOver.png", 442, 118, 340, 60, 750, 250);
					
				}
				else if(usuario1.getScore() > usuario2.getScore()) {
					//printa tela de atirador vencedor
					t.imagem("sprites/MensagemGameOver.png", 0, 55, 340, 60, 740, 250);
				}
				else {
					//printa tela de pato vencedor
					t.imagem("sprites/MensagemGameOver.png", 0, 0, 340, 60, 740, 250);
				}
				
				t.imagem("sprites/MensagemGameOver.png", 425, 52, 207, 60, 740, 355);
				t.imagem("sprites/MensagemGameOver.png", 425, 0, 207, 60, 740, 458);
				usuario1.drawJustNumber(t, usuario1.getScore(), 970, 370);
				usuario2.drawJustNumber(t, usuario2.getScore(), 970, 470);
				break;
		}
	}
	
	/*	Metodo que realiza uma pausa na execucao. */
	public void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch(Exception e) {};
	}
	
	
	public static void main(String[] args) {
		try{
    		new Motor(new Duck());
    	} catch(Exception e) {};
    }	
}
