public class ScoreNumber {
	private int number;
	public String snumber;
	public static int record = 0;
	public int nPatos = 0;
	
	public static int[][] recortesNumero = {
			{3, 9},
			{49, 9},
			{94, 9},
			{139, 9},
			{184, 9},
			{229, 9},
			{275, 9},
			{320, 9},
			{365, 9},
			{410, 9}
	};
	
	public int espaco_entre_numeros = 23;
	
	public ScoreNumber(int n){
		this.number = n;
		setSNumber();
	}
	
	public void setSNumber(){
		snumber = String.valueOf(number);
	}
	
	public void setScore(int n){
		number = n;
		setSNumber();
	}
	public int getScore(){
		return number;
	}
	
	public void modifyScore(int dn){
		number += dn;
		setSNumber();
	}
	
	public void drawScore(Tela t, int x, int y){
		for(int i=0; i<snumber.length(); i++){
			number(t, Integer.parseInt(snumber.substring(i, i+1)), x + espaco_entre_numeros*i, y);
		}
	}
	
	public void drawRecord(Tela t, int x, int y){
		String srecord = String.valueOf(ScoreNumber.record);
		for(int i=0; i<srecord.length(); i++){
			number(t, Integer.parseInt(srecord.substring(i, i+1)), x + espaco_entre_numeros*i, y);
		}
	}
	
	public void number(Tela t, int number, int x, int y){
		t.imagem("sprites/numeros.png", recortesNumero[number][0], recortesNumero[number][1], 25, 28, x, y);
	}
	
	public void drawJustNumber(Tela t, int number, int x, int y){
		String srecord = String.valueOf(number);
		for(int i=0; i<srecord.length(); i++){
			number(t, Integer.parseInt(srecord.substring(i, i+1)), x + espaco_entre_numeros*i, y);
		}
	}
}
