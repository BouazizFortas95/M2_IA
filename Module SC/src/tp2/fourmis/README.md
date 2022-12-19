# TP Simulation de fourmis


## About

<b>Sujet</b>
    <p>L’objectif de ce sujet est de simuler une colonie de fourmis partant à la recherche de nourriture.</p>
    <p>Une fourmi possède 4 états : chercher la nourriture, prendre la nourriture et faire demi-tour, se diriger vers le nid en déposant de la phéromone, déposer la nourriture dans le nid et faire demi-tour pour revenir à l’état initial.</p>

    <p>Une fourmi possède une réserve de phéromone : à chaque étape du retour vers le nid, la fourmi dépose une dose de phéromone, de plus en plus petite, sur la case qu’elle occupe.</p>

    <p>Une fourmi évolue dans un terrain, ce terrain comporte, outre les fourmis, une matrice de cellules, dans laquelle se trouve le nid des fourmis et des zones de nourriture.</p>

    <p>La fourmi est donc identifiée par ses coordonnées dans la matrice, sa direction, son état, sa dose de phéromone.</p>

    <p>Une cellule possède entre autres des coordonnées, éventuellement une dose de phéromone, la trace d’un nid, de la nourriture, des fourmis…</p>

    <p>La classe cellule contient le taux d’évaporation des phéromones ainsi que le taux de diffusion.
    Une interface graphique sommaire permet de représenter le terrain et de suivre l’évolution des fourmis et des traces de phéromones grâce à un Timer.</p>
<b>Description de l’application</b>
    <p>L’application est décomposée en 2 parties : une partie Modèle et une partie Interface Graphique (gui pour graphic user interface). Il n’y aura pas de partie contrôle nécessaire pour répondre au sujet.</p>

    <p>Dans le package modele se trouvent les classes Terrain composé d’un tableau à 2 dimensions de Cellule, classe également située dans le Modèle.</p>
    <p>Dans le Modèle se situe également la classe Fourmi.</p>
    <p>Les Cellule peuvent diffuser de la phéromone et la faire évaporer.</p>
    <p>Les fourmis agissent selon leurs états.</p>

## Getting Started


### Prerequisites


### Installing


## Usage

Add notes about how to use the system.
