# -*- coding: utf-8 -*-
"""
Created on Mon Oct  3 07:21:40 2022

@author: Bouaziz Fortas
"""


print('------------------------------------------------------------------------')

# =============================================================================
# Partie 1: nucléotides et brins d'ADN
# =============================================================================


def readDNA(filename):
    with open(filename, "r") as f:
        seq = f.readline()

    return seq

sequence = readDNA("sample_dna.txt")
print("Séquence : ", sequence)

def calculeOccurrence(seq):
   return seq.count('A'), seq.count('C'), seq.count('G'), seq.count('T')

occ_A, occ_C, occ_G, occ_T = calculeOccurrence(sequence)

print("Length of sequence : ", len(sequence))
print('Occurance of A : ', occ_A)
print('Occurance of C : ', occ_C)
print('Occurance of G : ', occ_G)
print('Occurance of T : ', occ_T)

def getBrinSeq(seq):
    seq = seq.replace('A', 't').replace('T', 'a').replace('C', 'g').replace('G', 'c')
    seq = seq.upper()
    return seq

brin = getBrinSeq(sequence)
print("Brin : ", brin)

print('------------------------------------------------------------------------')

# =============================================================================
# Partie 2 : Conversion de séquences d'ADN en séquences de protéine
# =============================================================================

# a. Transcription de l'ADN en ARN
def transDNA2RNA(seq):
    rna = seq.replace('T', 'U')
    return rna

rna = transDNA2RNA(sequence)
print("RNA : ", rna)

# b. Traduction de l'ARN en protéine

def read_amino_acid(data='amino_acid.csv'):

    aa_dict = {}
    with open(data) as f:
        f.readline()  # skip the first line
        for line in f:
            split_line = line.strip().split(',')
            if split_line[2] != 'Stop':
                aa_dict[split_line[0]] = split_line[2]
    return aa_dict


protine_codes = read_amino_acid()
print("\nProtines Items ({length}) Code : {vals}".format(length=len(protine_codes),vals=protine_codes))

def traductionRNA2Peptide(seq, codes_table):
    start = seq.find('AUG')
    peptide = ""
    i = start
    while i < len(seq)-2:
        code = seq[i:i+3]
        a = codes_table[code]
        i += 3
        peptide += a
    return peptide


result_protine = traductionRNA2Peptide(rna, protine_codes)
print("\nResult Protine : ", result_protine)





