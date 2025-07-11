import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        int tamanhoArray = 1000;
        long seed = 42L;
        Random random = new Random(seed);
        int[] arrayA = new int[tamanhoArray];
        for (int i = 0; i < tamanhoArray; i++) {
            // Gera números entre 0 e 1999 para ter chance de não encontrar o elemento
            arrayA[i] = random.nextInt(tamanhoArray * 2);
        }

        int x = arrayA[tamanhoArray - 100];

        System.out.println("Iniciando análise com " + tamanhoArray + " elementos.");
        System.out.println("Procurando pelo número X = " + x + "\n");
        System.out.println("==========================================================");

        System.out.println("PASSO 2: BUSCA LINEAR NO ARRAY ORIGINAL");
        long inicioBuscaLinear = System.nanoTime();
        boolean encontradoLinear = buscaLinear(arrayA, x);
        long tempoBuscaLinear = System.nanoTime() - inicioBuscaLinear;

        System.out.println("Elemento X " + (encontradoLinear ? "encontrado." : "NÃO encontrado."));
        System.out.printf("Tempo de execução da busca linear: %d nanossegundos (%d microssegundos)\n\n",
                tempoBuscaLinear, TimeUnit.NANOSECONDS.toMicros(tempoBuscaLinear));
        System.out.println("==========================================================");

        System.out.println("PASSO 3: BUSCA BINÁRIA NO ARRAY ORDENADO");
        int[] arrayOrdenado = Arrays.copyOf(arrayA, arrayA.length);

        long inicioOrdenacao = System.nanoTime();
        Arrays.sort(arrayOrdenado);
        long tempoOrdenacao = System.nanoTime() - inicioOrdenacao;

        long inicioBuscaBinaria = System.nanoTime();
        int indiceBinario = Arrays.binarySearch(arrayOrdenado, x);
        long tempoBuscaBinaria = System.nanoTime() - inicioBuscaBinaria;

        System.out.println("Elemento X " + (indiceBinario >= 0 ? "encontrado." : "NÃO encontrado."));
        System.out.printf("Tempo de ordenação do array: %d nanossegundos (%d microssegundos)\n",
                tempoOrdenacao, TimeUnit.NANOSECONDS.toMicros(tempoOrdenacao));
        System.out.printf("Tempo de execução da busca binária: %d nanossegundos (%d microssegundos)\n",
                tempoBuscaBinaria, TimeUnit.NANOSECONDS.toMicros(tempoBuscaBinaria));
        System.out.printf("Tempo total (Ordenação + Busca): %d nanossegundos (%d microssegundos)\n\n",
                (tempoOrdenacao + tempoBuscaBinaria), TimeUnit.NANOSECONDS.toMicros(tempoOrdenacao + tempoBuscaBinaria));
        System.out.println("==========================================================");

        System.out.println("PASSO 4 & 5: BUSCA EM TABELA HASH (HashMap)");

        long inicioCriacaoHash = System.nanoTime();
        HashMap<Integer, Integer> mapa = new HashMap<>();
        for (int elemento : arrayA) {
            mapa.put(elemento, elemento);
        }
        long tempoCriacaoHash = System.nanoTime() - inicioCriacaoHash;

        long inicioBuscaHash = System.nanoTime();
        boolean encontradoHash = mapa.containsKey(x);
        long tempoBuscaHash = System.nanoTime() - inicioBuscaHash;

        System.out.println("Elemento X " + (encontradoHash ? "encontrado." : "NÃO encontrado."));
        System.out.printf("Tempo de inserção dos elementos na Tabela Hash: %d nanossegundos (%d microssegundos)\n",
                tempoCriacaoHash, TimeUnit.NANOSECONDS.toMicros(tempoCriacaoHash));
        System.out.printf("Tempo de execução da busca na Tabela Hash: %d nanossegundos (%d microssegundos)\n",
                tempoBuscaHash, TimeUnit.NANOSECONDS.toMicros(tempoBuscaHash));
        System.out.println("==========================================================");
    }

    public static boolean buscaLinear(int[] array, int alvo) {
        for (int elemento : array) {
            if (elemento == alvo) {
                return true;
            }
        }
        return false;
    }

}
