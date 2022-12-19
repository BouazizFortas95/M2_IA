/**
 * 
 */
package tp2.fourmis;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Bouaziz Fortas
 *
 */
public class Main extends Application {
	
	/**terrain liee a cet objet graphique*/
	private Terrain terrain;
	/**nb de fourmis*/
	int nbFourmis = 30;
	/**vitesse de simulation*/
	double tempo = 50;
	/**taille de la terrain*/
	private int taille;
	/**taille d'une cellule en pixel*/
	private int espace = 10;
	private  static Rectangle [][] environnement; 
	public static Circle[]fourmis;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	/**initialisation de l'application graphique*/
	public void start(Stage primaryStage) {
		int tailleTerrain = 70;
		fourmis = new Circle[nbFourmis];
		terrain = new Terrain(tailleTerrain, nbFourmis);
		taille = terrain.getTaille();
		construireScenePourFourmis( primaryStage);

	}
	
	/**construction du th�atre et de la sc�ne */
	void construireScenePourFourmis(Stage primaryStage) 
	{
		//definir la scene principale
		Group root = new Group();
		Scene scene = new Scene(root, 2*espace + taille*espace, 2*espace + taille*espace, Color.BLACK);
		primaryStage.setTitle("TP Simulation de fourmis");
		primaryStage.setScene(scene);
		//definir les acteurs et les habiller
		Main.environnement = new Rectangle[taille][taille];
		dessinEnvironnement( root);

		//afficher le theatre
		primaryStage.show();

		//-----lancer le timer pour faire vivre les fourmis et l'environnement
		Timeline littleCycle = new Timeline(new KeyFrame(Duration.millis(tempo), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				terrain.animGrille();
				updateTerrain();
			}
			
		}));
		
		littleCycle.setCycleCount(Timeline.INDEFINITE);
		littleCycle.play();
	}

	
	
	/** 
	 *creation des cellules et de leurs habits
	 */
	void dessinEnvironnement(Group root)
	{
		Cellule[][] grille = terrain.getGrille();
		// chaque parcelle de l'environnement est verte
		for(int i=0; i<taille; i++)
			for(int j=0; j<taille; j++)
			{
				Main.environnement[i][j] = new Rectangle((i+1)*(espace), (j+1)*(espace), espace, espace);
				Main.environnement[i][j].setFill(Color.DARKGREEN);
				root.getChildren().add(Main.environnement[i][j]);
			}
		for(int i=0; i<taille; i++)
			for(int j=0; j<taille; j++)
			{
				Cellule cell = grille[i][j];
				if (cell.getNourriture()>0) // affichage des zones de nourritures 
				{
					Color colNouriture = Color.DARKGOLDENROD;
					double ratio = cell.getNourriture() / 50d; // pourcentage de pheromone par rapport au max possible (50)
					colNouriture = colNouriture.interpolate(Color.BISQUE, ratio);
					Main.environnement[i][j] = new Rectangle((i+1)*(espace), (j+1)*(espace), espace, espace);
					Main.environnement[i][j].setFill(colNouriture);
					// dessin des nourritures au dessus de l'environnement
					root.getChildren().add(Main.environnement[i][j]);
				}
				else
					if (cell.isNid()) // affichage de la zone du nid
					{
						Main.environnement[i][j] = new Rectangle((i+1)*(espace), (j+1)*(espace), espace, espace);
						Main.environnement[i][j].setFill(Color.BROWN);
						// dessin des nids � la place de l'environnement
						root.getChildren().remove(Main.environnement[i][j] );
						root.getChildren().add(Main.environnement[i][j]);
					}
			}

		//cr�ation des fourmis, rouges tomate
		for(Fourmi  f : terrain.getLesFourmis())
		{
			f.setDessin(new Circle(((taille+3)*espace)/2 , ((taille+3)*espace)/2, espace/2, Color.TOMATO));
			f.setPas(espace);
			root.getChildren().add(f.getDessin());
		}
		
		//petit effet de flou g�n�ral
		root.setEffect(new BoxBlur(2, 2, 5));
	}



	/**modification de la couleur des cellules en fonction de la dose de nourriture et de ph�romones*/
	private void updateTerrain()
	{
		Cellule[][] grille = terrain.getGrille();
		for(int i=0; i<taille; i++)
			for(int j=0; j<taille; j++)
			{
				Cellule cell = grille[i][j];
				if (cell.hasJustChanged && cell.getNourriture()>0) // affichage des zones de nourritures 
				{
					Color colNouriture = Color.DARKGOLDENROD;
					double ratio = cell.getNourriture() / 50d; // pourcentage de pheromone par rapport au max possible (50)
					colNouriture = colNouriture.interpolate(Color.BISQUE, ratio);
					Main.environnement[i][j].setFill(colNouriture);
				}
				if (cell.hasJustChanged && cell.isEmptyNow()  && !cell.isNid())
				{
					Main.environnement[i][j].setFill(Color.DARKGREEN );
					cell.setEmptyNow(false);
				}
				if (cell.hasJustChanged && cell.getPheromone()>0 && cell.getNourriture()==0  && !cell.isNid() ) // s'il y a une trace de ph�romone
				{
					double ratio = (cell.getPheromone() / Fourmi.dosePheroMax ); // pourcentage de pheromone par rapport au max
					int g = (int)(ratio* 255*10) +100; // au plus bas, la zone est grise (color(100,100,100)
					if(g>255) g=255; // ne pas depasser 255
					float transparence = (float)ratio + 0.2f; if (transparence>1f) transparence = 1f; // calcul du degr� de transparence
					Color c1 = Color.DARKGREEN;
					Color c = c1.interpolate(Color.rgb(g,g,g), transparence);
					Main.environnement[i][j].setFill(c);
				}
				if (cell.hasJustChanged && cell.getPheromone()==0 && cell.getNourriture()==0 && !cell.isNid()) // s'il y a une trace de ph�romone
				{
					Main.environnement[i][j].setFill(Color.DARKGREEN);
				}

			}
	}


}
