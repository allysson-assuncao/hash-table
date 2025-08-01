package com.example.hashtable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ComparaAVLHash {

    public static void main(String[] args) {
        final int NUM_ELEMENTS = 1_000_000;
        final int MAX_VALUE = 10_000_000;

        System.out.println("Gerando " + String.format("%,d", NUM_ELEMENTS) + " números aleatórios...");
        int[] randomNumbers = new Random().ints(NUM_ELEMENTS, 1, MAX_VALUE).distinct().toArray();

        final int actualElements = randomNumbers.length;
        System.out.println("Dados gerados. Total de elementos únicos: " + String.format("%,d", actualElements));
        System.out.println("------------------------------------------------------");

        AVLTree avlTree = new AVLTree();
        Map<Integer, Integer> hashMap = new HashMap<>();

        System.out.println("\n--- Teste de Inserção ---");

        long startTime = System.nanoTime();
        for (int number : randomNumbers) {
            avlTree.insert(number);
        }
        long endTime = System.nanoTime();
        long avlInsertionTime = (endTime - startTime) / 1_000_000;
        System.out.println("Árvore AVL: " + avlInsertionTime + " ms para inserir " + String.format("%,d", actualElements) + " elementos.");

        startTime = System.nanoTime();
        for (int number : randomNumbers) {
            hashMap.put(number, number);
        }
        endTime = System.nanoTime();
        long hashInsertionTime = (endTime - startTime) / 1_000_000;
        System.out.println("Tabela Hash (HashMap): " + hashInsertionTime + " ms para inserir " + String.format("%,d", actualElements) + " elementos.");
        System.out.println("------------------------------------------------------");

        System.out.println("\n--- Teste de Busca ---");

        long avlSearchCount = 0;
        startTime = System.nanoTime();
        for (int number : randomNumbers) {
            if (avlTree.search(number) != null) {
                avlSearchCount++;
            }
        }
        endTime = System.nanoTime();
        long avlSearchTime = (endTime - startTime) / 1_000_000;
        System.out.println("Árvore AVL: " + avlSearchTime + " ms para buscar " + String.format("%,d", avlSearchCount) + " elementos.");

        long hashSearchCount = 0;
        startTime = System.nanoTime();
        for (int number : randomNumbers) {
            if (hashMap.get(number) != null) {
                hashSearchCount++;
            }
        }
        endTime = System.nanoTime();
        long hashSearchTime = (endTime - startTime) / 1_000_000;
        System.out.println("Tabela Hash (HashMap): " + hashSearchTime + " ms para buscar " + String.format("%,d", hashSearchCount) + " elementos.");
        System.out.println("------------------------------------------------------");
    }

}
