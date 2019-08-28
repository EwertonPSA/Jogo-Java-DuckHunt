public class Passaro {
	public double x, y, vy = 0, vx = 250;
	public static double G = 1300;
	public static double FLAP = -350;
	public static double FLAP_M = -450;
	public int direcao = 1;
	public int passaroEmMovimento = 0;//Fica alternando entre as posicoes da imagem que sera imprimido o passaro
	public double maxX;/*limite da tela em X*/
	public double maxY;/*limite da tela em Y*/
	public boolean morto = false;
	public String cor;
	public int timerMorte;
	public int tempoBatidaAsas;
	public Random aleatorio;/*Gera numeros aleatorios*/
	public int state_Pato = 1;/*Informa se o pato esta em IA ou a pessoa controla, [1->IA], [2->Player], esta associada ao estado do jogo*/
	public int limiar_do_flap = 0;
	public int vel_flap = 250;
	public double G_IA = 1400;
	public int timer_sai_Mato = 0;
	
	public int limite_superior_voo = 0;/*Vai indicar ateh onde o pato voa, depois ele decai por um periodo de tempo aleatorio e retorna a voar*/
	public int timer_sem_Flap = 30;/*Tempo de caida ao alcançar o limite superior de voo*/
	public int count_timer = 0;
	public int posicao_Morto_PatoX, posicao_Morto_PatoY;
	public boolean pontuacaoMax = false;
	public boolean livre = false;
	
	public Passaro(double maxX, double maxY, String cor, int state_Pato){
		aleatorio = new Random();
		timerMorte = 0;
		this.cor = cor;
		this.x = aleatorio.getIntRand(maxX-45);/*Pato inicializa em uma posicao Aleatorio em X*/
		this.y = maxY - aleatorio.getIntRand(120) ;/*Depende */
		this.maxX = maxX;/*Repassa o limite da tela em X*/
		this.maxY = maxY;/*Repassa o limite da tela em Y*/
		limite_superior_voo = (int) aleatorio.getIntRand( 60, (maxY-200));
		timer_sem_Flap = (int) aleatorio.getIntRand(150, 200);
		this.state_Pato = state_Pato;
	}
	
	public void update(double dt, int state_Pato){/*Controla o deslocamento do passaro na imagem*/
		this.state_Pato = state_Pato;/*Indica se o Pato esta sendo controlado pela IA ou pelo usuario*/
		
		switch(state_Pato) {
			case 1:/*Limite do pato controlado pela IA*/
				vooIndefinido(vy);
				vy += G_IA*dt;
				y += vy*dt;
				x += vx*dt;
				
				if(y <= 0 && !livre) y = 0;/*Limite superior da tela*/
				
				if(y <= -80 && livre) {
					y = -80;
				}
				
				if(x >= maxX-50) { /*Limite inferior da tela - um limitante*/
					x = maxX-50;
					left();
				}
				if(x <= 0) {
					x = 0;
					right();
				}
				if(y >= maxY-120 && morto == false) y = maxY-120;
				
				if(y >= maxY && morto == true) {
					y = maxY + 200;
					
				}	
				break;
				
			case 2:/*Limite do pato controlado pelo Player*/
				vy += G*dt;
				y += vy*dt;
				x += vx*dt;
				
				if(y <= 0) y = 0;/*Limite superior da tela*/
				if(x >= maxX-45) x = maxX-45;/*Limite inferior da tela - um limitante*/
				
				if(x <= 0) x = 0;
				if(y >= maxY-230 && morto == false) y = maxY-230;/*Altura de chao maior pro player nao se esconder no mato*/
				if(y >= maxY && morto == true) {
					y = maxY + 20;
					reiniciar();
				}
				count_timer+=1;
				if(count_timer == 15) {
					passaroEmMovimento+=1;
					passaroEmMovimento = passaroEmMovimento%2;//Alterna entre 1 e 0 pra trocar as imagens do passaro
				}
				count_timer = count_timer % 15;
				break;
		}
	}
	
	public void desenhar(Tela t){
		if(morto) {
			if(timerMorte < 40) {
				t.imagem("sprites/Passaros_" + cor + ".png", 0, 254, 68, 61, x, y);
				timerMorte += 1;
			}
			else if(timerMorte >= 40) {
				t.imagem("sprites/Passaros_" + cor + ".png", 83 + (direcao % 2) * 49, 248, 40, 58, x, y);
				if(timerMorte % 40 < 20) direcao = 1;
				else direcao = 0;
				timerMorte += 1;
			}
		}else { /*Pato Vivo*/
			if(vy < 0) {
				t.imagem("sprites/Passaros_" + cor + ".png", 2, (passaroEmMovimento*63 + 4) + direcao*122, 66, 61, x, y);
			}else {
				t.imagem("sprites/Passaros_" + cor + ".png", 79, (passaroEmMovimento*58 + 4) + direcao*128, 70, 61, x, y);
			}
		}
	}
	
	public void desenhaPontuacao(Tela t, ScoreNumber usuario) {
		if(morto) {
			if(timerMorte < 40) {
				if(pontuacaoMax == true) {
					usuario.drawJustNumber(t, 1000, posicao_Morto_PatoX, posicao_Morto_PatoY);
				}else {
					usuario.drawJustNumber(t, 500, posicao_Morto_PatoX, posicao_Morto_PatoY);
				}
			}
		}
		usuario.drawScore( t, (int)maxX-120, (int)maxY-120);
	}
	
	public boolean hitbox(int mx, int my, ScoreNumber usuario) {
		if(mx >= x && mx <= (x+66) && my >= y && my <= (y+61) && !livre) {
			if(mx >= (x+10) && mx <= (x+66-10) && my >= (y+10) && my <= (y+61-10) && !morto) {
				usuario.modifyScore(1000);
				pontuacaoMax = true;
			}else if(!morto){//Acertou o alvo no hitbox mas o pato não está morto
				usuario.modifyScore(500);
				pontuacaoMax = false;
			}
			if(mx > maxX-100 && mx <= maxX)	posicao_Morto_PatoX = mx-100;
			else posicao_Morto_PatoX = mx;
			
			posicao_Morto_PatoY = my;
			morto = true;
			usuario.nPatos++;
			return true;
		}
		return false;
	}
	
	public void flap(){/*Batidas de assas*/
		if(morto == false) {
			passaroEmMovimento+=1;
			passaroEmMovimento = passaroEmMovimento%2;//Alterna entre 1 e 0 pra trocar as imagens do passaro
			if(state_Pato == 1)
				this.vy = FLAP;/*Gravidade eh invertida por um tempo pro passaro subir*/
			else 
				this.vy = FLAP_M;
		}
	}
	
	public void vooIndefinido(double vy) {
		if(livre) {
			if(vy > -200) flap();
		}
		
		else if(timer_sai_Mato <= 55) {/*Tempo pra levantar o voo*/
			if(timer_sai_Mato % 2 == 0) flap();/*Tempo a cada tick pra levantar voo*/
			timer_sai_Mato+=1;
			
			if(aleatorio.getIntRand(100) > 85 ) {/*Taxa de valores aleatoreos menores pra diminuir a taxa de variacao da direção*/
				if(aleatorio.getIntRand(100)>50) right();
				else left();
				vel_flap = (int)aleatorio.getIntRand(180, 400);/*Quando escolhe uma direcao eh alterado a velocidade do pato*/
			}
			
		} else if(vy > limiar_do_flap && !morto) {
			if(aleatorio.getIntRand(100) > 85 ) {/*Diminuir a taxa de mudança de direção*/
				if(aleatorio.getIntRand(100)>50) right();
				else left();
				vel_flap = (int)aleatorio.getIntRand(180, 400);/*Quando escolhe uma direcao eh alterado a velocidade do pato*/
			}

			if(y <= limite_superior_voo) {
				if(count_timer == timer_sem_Flap) {
					limite_superior_voo = (int) aleatorio.getIntRand( 60, (maxY-200)-y);/*Proximo limitante superior aleatorio de voo do pato*/
					timer_sem_Flap = (int) aleatorio.getIntRand(70, 100);/*Tempo aleatorio de caida do pato*/
					count_timer = 0;
				}else count_timer+=1;
			}else flap();		
		}
	}
	
	public void reiniciar() {
		timerMorte = 0;
		vy = 0;
		vx = 200;
		x = aleatorio.getIntRand(maxX-45);
		y =  maxY - aleatorio.getIntRand(120);
		direcao = 1;
		limiar_do_flap -= 10;
		vel_flap += 20;
		timer_sai_Mato = 0;
		if(limiar_do_flap < -350) limiar_do_flap = -350;
		
		morto = false;
		livre = false;
	}

	public void right() {
		vx = vel_flap;
		direcao = 1;
	}

	public void left() {
		vx = -vel_flap;
		direcao = 0;
	}
}
