import java.util.Calendar;

public class Random {
	private long p = 2147483648l;
	private long m = 843314861;
	private long a = 453816693;
	private double xi = 1023;
	
	/**
	 * Cria um objeto random com uma semente baseada no tempo
	 */
	public Random(){/*Com o nome da classe eu nao declaro nenhum retorno*/
		this.xi = Calendar.getInstance().getTimeInMillis();
	}
	
	/**
	 * Cria uma objeto random com uma semente baseada no parametro X
	 * @param X: Semente a ser digitada
	 */
	public Random(int X){/*Com o nome da classe eu nao declaro nenhum retorno*/
		this.xi = X;
	}
	
	/**
	 * Gera um valor aleat�rio, esse valor aleat�rio ser� incluido como semente
	 * E assim n�o haver� repeti��es de valores 
	 * @return
	 */
	public double getRand(){
		double k = (a + m*xi)%p;/*Valor aleatorio*/
		xi = k;/*Altero a semente com o valor atribuido a k para a proxima chamada*/
		return k;
	}

	/**
	 * Gera um valor aleat�rio entre min e max
	 * @param min: um valor minimo para gera��o do numero aleat�rio
	 * @param max: um valor maximo para gera��o do numero aleat�rio
	 * @return: Um valor aleat�rio entre o intervalo de min e max
	 */
	public double getIntRand(double min, double max){
		double d = getRand();
		return (int)(min + d % (max-min)) ;/*Obtendo o valor entre max e min*/
	}
	
	/**
	 * Gera um valor aleat�rio at� max
	 * @param max: um valor maximo para gera��o do numero aleat�rio
	 * @return um valor aleat�rio entre 0 at� max
	 */
	public double getIntRand(double max){
		double d = getRand();
		return (d % max) ;
	}
}