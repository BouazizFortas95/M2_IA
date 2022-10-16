# -*- coding: utf-8 -*-
"""
Created on Mon Oct 10 22:24:47 2022

@author: Bouaziz Fortas
"""
import matplotlib.pyplot as plt
import numpy as np

# =============================================================================
# 1. Exercices préliminaires : Dot plot
# =============================================================================

# **********************  1.1. Dot plot simple ********************************
def listPairValeur(list1, list2):
    return [(i, j) for i in range(len(list1)) for j in range(len(list2)) if list1[i] == list2[j]]

matches_index = listPairValeur("GTTUIP", "EGTP")
print(matches_index)


def dotPlot(matches):
    data = np.array(matches)
    # print(data)
    x, y = data.T
    # print(x)
    # print(y)
    plt.title("Dot plot")
    plt.scatter(x, y)
    plt.show()

dotPlot(matches_index)

# **********************  1.2. Dot plot avec fenêtre glissante ********************************

# =============================================================================
# 2. Programmation dynamique
# =============================================================================
