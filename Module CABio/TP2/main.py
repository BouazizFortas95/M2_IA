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

matches_index = listPairValeur("ADSTARYEMQSDQIYTQN", "AETSAQYDMQSDQEFTRD")
print(matches_index)


def dotPlot(matches):
    data = np.array(matches)
    # print(data)
    x, y = data.T
    # print("X : ",x)
    # print("Y : ",y)
    plt.title("Dot plot")
    plt.xlabel("sequence A")
    plt.ylabel("sequence B")
    plt.scatter(x, y, c='blue', marker='x', s=20)
    # plt.plot(x, y, color='red', linewidth=2)
    plt.show()

dotPlot(matches_index)

# **********************  1.2. Dot plot avec fenêtre glissante ****************
def dotplotWindow(seqA, seqB, window):
    """Create a dot matrix of the two DNA sequences specified by the user"""

    data = [[(seqA[i:i + window] != seqB[j:j + window])
            for j in range(len(seqA) - window + 1)]
            for i in range(len(seqB) - window + 1)]
    plt.gray()
    plt.imshow(data)
    plt.xlabel("%s (length %i bp)" % ("First sequence", len(seqA)))
    plt.ylabel("%s (length %i bp)" % ("Second sequence", len(seqB)))
    plt.title("Dot matrix using window size %i" % window)
    plt.show()

dotplotWindow("ADSTARYEMQSDQIYTQN", "AETSAQYDMQSDQEFTRD", 1)

# =============================================================================
# 2. Programmation dynamique
# =============================================================================

# **********************  2.1. Needleman et Wunsch ****************
Top = "\u2191"
left = "\u2190"
diag = "\u2196"
identité = 1
operant = 0
gap = 0



