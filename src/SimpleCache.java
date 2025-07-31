import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class SimpleCache {

    private static class Aluno {
        String chave, nome;

        public Aluno(String chave, String nome) {
            this.chave = chave;
            this.nome = nome;
        }
    }

    private final Map<String, String> cache = new HashMap<>();
    private static final Vector<Aluno> alunos = new Vector<>();

    private String fetchFromDatabase(String key) {
        System.out.println("❌ Cache Miss! Buscando '" + key + "' da fonte de dados lenta (ex: Banco de Dados)...");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String data = "Aluno não encontrado!";
        for(Aluno aluno : alunos) {
            if(aluno.chave.equals(key)) {
                data = aluno.nome;
            }
        }
        System.out.println("   -> Dado encontrado. Armazenando no cache para uso futuro.");
        cache.put(key, data);
        return data;
    }

    public String getData(String key) {
        if (cache.containsKey(key)) {
            System.out.println("✅ Cache Hit! Retornando '" + key + "' diretamente do cache rápido.");
            return cache.get(key);
        }
        return fetchFromDatabase(key);
    }

    public static void main(String[] args) {
        SimpleCache myCache = new SimpleCache();

        Aluno aluno1 = new Aluno("1", "Allysson");
        Aluno aluno2 = new Aluno("2", "Moisés");
        alunos.add(aluno1);
        alunos.add(aluno2);

        System.out.println("--- Primeira Requisição ---");
        System.out.println("DADO RECEBIDO: " + myCache.getData("1"));
        System.out.println("\n");

        System.out.println("--- Segunda Requisição (para o mesmo dado) ---");
        System.out.println("DADO RECEBIDO: " + myCache.getData("1"));
        System.out.println("\n");

        System.out.println("--- Terceira Requisição (para um dado diferente) ---");
        System.out.println("DADO RECEBIDO: " + myCache.getData("2"));
        System.out.println("\n");

        System.out.println("--- Quarta Requisição (para o segundo dado novamente) ---");
        System.out.println("DADO RECEBIDO: " + myCache.getData("2"));
        System.out.println("\n");
    }

}
