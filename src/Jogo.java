/*	Interface para implementacao da classe principal do jogo.*/
public interface Jogo {
    String getTitulo();
    int getLargura();
    int getAltura();
    void tique(double dt);
    void tecla(String tecla);
    void mouse(int x, int y);
    void desenhar(Tela tela);
}