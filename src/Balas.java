
public class Balas {
	public int balas;

	public Balas(int i){
		balas = i;
		
	}
	
	public void desenhar(Tela t) {
		for(int i = 0; i < balas; i++) {
			t.imagem("sprites/Bala.png", 0, 0, 17, 26, 48 + 28 * i, 826);
		}
	}
}
