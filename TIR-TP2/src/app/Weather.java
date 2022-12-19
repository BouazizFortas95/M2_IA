/**
 * 
 */
package app;

/**
 * @author Bouaziz Fortas
 *
 */
public class Weather {
	
	private int tempo, hum;

	/**
	 * 
	 */
	public Weather(int tem, int hum) {
		setTempo(tem);
		setHum(hum);
	}

	/**
	 * @return the tempo
	 */
	public int getTempo() {
		return tempo;
	}

	/**
	 * @param tempo the tempo to set
	 */
	public void setTempo(int tempo) {
		this.tempo = tempo;
	}

	/**
	 * @return the hum
	 */
	public int getHum() {
		return hum;
	}

	/**
	 * @param hum the hum to set
	 */
	public void setHum(int hum) {
		this.hum = hum;
	}

}
