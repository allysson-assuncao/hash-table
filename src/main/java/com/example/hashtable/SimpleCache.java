package com.example.hashtable;

import com.github.javafaker.Faker;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class SimpleCache {

    private final Map<String, Aluno> cache = new HashMap<>();

    // fonte de dados original e lenta
    private final Vector<Aluno> fonteDeDadosAlunos = new Vector<>();

    // simula a busca de uma api com alta latencia
    private Aluno buscarNaFonteDeDadosLenta(String matricula) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        for (Aluno aluno : fonteDeDadosAlunos) {
            if (aluno.matricula().equals(matricula)) {
                System.out.println("   -> Aluno encontrado na fonte de dados. Armazenando no cache...");
                // buscas recentes são adicionadas na cache
                cache.put(matricula, aluno);
                return aluno;
            }
        }
        return null;
    }

    // encontra o aluno pela chave (matrícula unica)
    public Aluno getAluno(String matricula) {
        // caso esteja na cache, bem mais rápido
        if (cache.containsKey(matricula)) {
            System.out.println("✅ Cache HIT! Retornando '" + matricula + "' do cache rápido.");
            return cache.get(matricula);
        }

        // caso não esteja na cache, faa a busca lenta
        System.out.println("❌ Cache MISS! Matrícula '" + matricula + "' não encontrada no cache.");
        return buscarNaFonteDeDadosLenta(matricula);
    }

    // pupula a "base" com alunos aleatórios
    public void popularFonteDeDados(int quantidade) {
        Faker faker = new Faker(new Locale("pt-BR"));
        System.out.println("Populando a fonte de dados com " + quantidade + " alunos aleatórios...");
        for (int i = 0; i < quantidade; i++) {
            String matricula = faker.number().digits(8);
            String nome = faker.name().fullName();
            String email = faker.internet().emailAddress(nome.split(" ")[0].toLowerCase());
            fonteDeDadosAlunos.add(new Aluno(matricula, nome, email));
        }
        System.out.println("Fonte de dados populada com sucesso.\n");
    }

    public static void main(String[] args) {
        SimpleCache cacheSistema = new SimpleCache();
        final int TOTAL_ALUNOS_NO_BANCO = 5000;

        cacheSistema.popularFonteDeDados(TOTAL_ALUNOS_NO_BANCO);

        List<String> matriculasExistentes = new ArrayList<>();
        for (Aluno aluno : cacheSistema.fonteDeDadosAlunos) {
            matriculasExistentes.add(aluno.matricula());
        }

        Random random = new Random();
        final int NUMERO_DE_BUSCAS = 5;

        System.out.println("--- INICIANDO SIMULAÇÃO DE BUSCAS ALEATÓRIAS ---");

        for (int i = 0; i < NUMERO_DE_BUSCAS; i++) {
            // escolhe uma chave aleatoria para buscar
            String matriculaAleatoria = matriculasExistentes.get(random.nextInt(TOTAL_ALUNOS_NO_BANCO));

            System.out.println("\n=======================================================");
            System.out.println("BUSCA #" + (i + 1) + " - Procurando pela matrícula: " + matriculaAleatoria);
            System.out.println("=======================================================");

            // --- Primeira busca ("Cache Miss") ---
            long startTime = System.nanoTime();
            Aluno aluno1 = cacheSistema.getAluno(matriculaAleatoria);
            long endTime = System.nanoTime();
            System.out.println("  -> DADO RECEBIDO: " + aluno1);
            System.out.println("  -> Tempo de busca: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + " ms");

            System.out.println("-------------------------------------------------------");

            // --- Segunda busca (para o mesmo dado, "Cache Hit") ---
            System.out.println("Repetindo a busca pela mesma matrícula...");
            startTime = System.nanoTime();
            Aluno aluno2 = cacheSistema.getAluno(matriculaAleatoria);
            endTime = System.nanoTime();
            System.out.println("  -> DADO RECEBIDO: " + aluno2);
            // sempre 0ms
            System.out.println("  -> Tempo de busca: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + " ms");
        }
    }

}