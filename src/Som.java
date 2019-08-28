import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Som {
	private Clip clip = null;
	public int state = 0;//[0->Start Game] [1->Transicao de Round] [2->Game Over Single Player] [3->Credits]
	public String[] musica = {"Music/introDuck.wav", "Music/Round.wav", "Music/Failed.wav", "Music/Creditos.wav"};//Aqui eh onde sera colocado os nomes dos arquivos para cada estado do jogo
	public int number_Music = 4;
	public File[] fileMusic = new File[number_Music];/*Arquivos de musicas*/
	
	public Som() {/*Usado para tocar musica de fundo*/
        try {
        	clip = AudioSystem.getClip();
        	for(int i = 0; i < number_Music; i++)//Abre os arquivos de todas as musicas de fundo
        		fileMusic[i] = new File(musica[i]);
        } catch(Exception e) {
        	;
        }
	}
	
	public void trocaSom(int state_Song) {/*Troca Som de fundo*/
		if(clip.isRunning())//Verifica se a musica esta tocando, se estiver tocando ela eh desativada
			clip.stop();
		try {
			clip.loop(clip.LOOP_CONTINUOUSLY);
		}catch (Exception e){
			;
		}
	}
	
	public void enableSom(int state) {
		if(!clip.isRunning()) {//Se nao tiver tocando nenhuma musica, apenas ativa a musica desejada
			this.state = state;
			try {
				clip.open(AudioSystem.getAudioInputStream(fileMusic[state]));
				clip.loop(clip.LOOP_CONTINUOUSLY);
			}catch(Exception e) {
				;
			}
		}else if(clip.isRunning() == true && state != this.state){/*Se tiver Tocando Musica mas desejo ativar uma outra musica diferente*/
			clip.close();
			this.state = state;
			try {
				clip.open(AudioSystem.getAudioInputStream(fileMusic[state]));
				clip.loop(clip.LOOP_CONTINUOUSLY);
			}catch(Exception e) {
				;
			}
		}//Se for ativar uma musica que ja se encontra ativa então não é feito nada e ela permanece em loop apenas
	}
	
	public void disableSom() {
		if(clip.isRunning())//Verifica se a musica esta tocando, se estiver tocando ela eh desativada
			clip.close();//Precisa fechar o arquivo de musica, se nao a reativacao com clip.open nao funciona
	}
	
    public static void tocarSom(String filename) {/*Toca um som simples, com um determinado intervalo de tempo*/
        try {
            Clip simpleClip = AudioSystem.getClip();
            simpleClip.open(AudioSystem.getAudioInputStream(new File(filename)));
            simpleClip.start();
        } catch(Exception e) {
        	;
        }
    }
}
