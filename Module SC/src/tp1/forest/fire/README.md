<h3>Module System Complex </h3>
<h4>Travaux Pratique  N°1 : Simulation de feux de forêt </h4>

Un automate cellulaire fait évoluer un plan en soumettant chaque position qui le constitue à des lois de transition. Ces lois se fondent sur le voisinage des positions entre elles, nombre de voisins, qualité des voisins, etc. La propagation du feu d’arbres dans une forêt entre dans cette logique et se simule assez bien avec un automate cellulaire. 

<h4>1. Principe : </h4>
            La forêt est modélisée par une matrice. Chaque cellule de la matrice représente l'entité spatiale du modèle. Celle-ci peut être dans un des quatre états: 
        arbre (vert/1); 
        vide (blanc/2); 
        Feu (rouge/3) 
        cendre (gris/4).

        Au départ, la matrice est vide et toutes les positions sont à zéro. Des arbres sont ajouté, c’est-à-dire que des cellules prennent une valeur, selon une probabilité p, une forêt, et selon une probabilité 1-p, vide. On met le feu à une cellule et on suit la diffusion du feu de proche en proche à travers la grille spatiale selon la fonction de transition suivante: 
            * une cellule en arbre prend feu au temps t si l'une au moins de ses 4 voisines (nord, est, sud, ouest) est en feu au temps t-1. 
            * Les cellules en feu passeront en cendres au temps suivant, 
            * les cellules en cendres deviendront vides au temps suivant. 
        Pour la mise en œuvre du processus qui est très simple : toutes les positions de la matrice sont parcourues, si la position est un arbre et qu’il y a une position voisine sur feu, elle passe à la valeur feu ; en revanche, si la position est à feu, et bien elle passe à cendre. Si la position est à 
        cendre, elle passe à vide. 
        Pour éviter les parcours régulier des cellules, on doit sélectionner au préalable les cellules en feu, puis activer leur diffusion.
        Finalement, on doit ajouter la direction et la vitesse de vent dans ce modèle.

<h4>2. PreParing Envirenement : </h4>
    <h5>a. Tools For Praparing JavaFX to runing in your env : </h5>
            - JDK 11 OR later (Recommended 17 LTS)
            - FX-SDK  (Recommended 19 'latest')
            - Eclipse Compatibility of Release 4.17 with 4.16 (Or later for JDK 11 or Later)
    <h5>b. Configuration for execution the application : </h5>
            - Import project into your worksapace
            - Right click in project TP1-SC and select properites
            - Go to java build path and add external jars from 'Your_folder_existing_path\FX-SDK\lib' select all to import
            - Open Main and got to configuration running app select class main and go to Arguments tab
            - In Arguments tab click in VM arguments and add '--module-path "Your_folder_existing_path\FX-SDK\lib" --add-modules javafx.controls,javafx.fxml' next step is aplly and  run the project.

