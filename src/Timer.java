public class Timer {
    double tempo;
    double limite;
    boolean repete;
    boolean fim;
    
    public Timer(double limite, boolean repete) {
        this.limite = limite;
        this.repete = repete;
    }

    public void tique(double dt) {
        if(fim) return;
        tempo += dt;
        if(tempo > limite) {
            if(repete) {
                tempo -= limite;
            } else {
                fim = true;
            }
        }
    }
}