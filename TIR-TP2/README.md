<h3>Module Technologie Internet et Reseaux </h3>
<h4>Travaux Pratique  N°2 : Le modèle Peer-to-Peer (P2P)</h4>

Le modèle P2P peut être considéré comme une variante du modèle client/serveur
où chaque entité a la possibilité de jouer les deux rôles (client et serveur) à la fois

<h4>1. Principe : </h4>
    * Modifier le code que vous avez déjà confectionné pour réaliser le 
    scénario client/serveur (la première partie du TP) afin d’en avoir le 
    modèle Peer-to-Peer, décrit dans la figure.
    - Supposant que le service 1 est la température actuelle et le service 2 
    est le taux d’humidité.

<h4>2. PreParing Envirenement : </h4>
    <h5>a. Tools For Praparing JavaFX to runing in your env : </h5>
            - JDK 11 OR later (Recommended 17 LTS)
            - FX-SDK  (Recommended 19 'latest')
            - Eclipse Compatibility of Release 4.17 with 4.16 (Or later for JDK 11 or Later)
    <h5>b. Configuration for execution the application : </h5>
            - Import project into your worksapace
            - Right click in project TIR-TP2 and select properites
            - Go to java build path and add external jar's from 'Your_folder_existing_path\FX-SDK\lib' select all to import
            - Open Main and got to configuration running app select class main and go to Arguments tab
            - In Arguments tab click in VM arguments and add '--module-path "Your_folder_existing_path\FX-SDK\lib" --add-modules javafx.controls,javafx.fxml' next step is aplly and  run the project for every HostMain(HostA, HostB, HostC).
