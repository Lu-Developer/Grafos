import model.Grafo;
import model.Vertice;
import java.io.*;
import java.util.*;

public class GerarGrafo {

    public static void main(String[] args) {
        List<Vertice> listaVertices = new ArrayList<>();
        Set<String> nodosDuplicados = new HashSet<>();
        Set<String> nodosNegativos = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("config.txt"))) {
            String linha;
            boolean primeiraLinha = true;
            String ultimaLinha = null;

            while ((linha = reader.readLine()) != null) {

                if (primeiraLinha) {
                    System.out.println(linha);
                    primeiraLinha = false;
                    continue;
                }

//                String nodo = linha.trim();
                String nodo = linha.split(":")[1].trim();
                boolean temDuplicata = false;
                boolean temNegativa = false;

                for (Vertice i : listaVertices) {
                    if (i.getNome().equals(nodo)) {
                        temDuplicata = true;
                        nodosDuplicados.add(linha);
                        break;
                    }
                }

                if (nodo.contains("-")){
                    temNegativa = true;
                    nodosNegativos.add(linha);
                }

                if (nodo.contains("TRAILER")){
                    ultimaLinha = nodo;
                }else{
                    if (!temDuplicata && !temNegativa && !nodo.equals(ultimaLinha)){
                       listaVertices.add(new Vertice(nodo));
                    }
                }
            }

            String ultLinha = ultimaLinha.split("TRAILER")[1];
            int numeroNodos = listaVertices.size();

            if (Integer.parseInt(ultLinha) == numeroNodos) {
                System.out.println("O número de nodos está correto: " + numeroNodos);
            } else {
                System.out.println("O número de nodos está incorreto: " + numeroNodos + " (linha " + ultLinha + ")");
            }

            Grafo g = new Grafo();
            for (Vertice v : listaVertices){
                g.addVertice(v.getNome());
            }

            for (Vertice vertOrigem : g.getListaVertices()){
                int origemIn = Integer.parseInt(vertOrigem.getNome().substring(0, 1));
                int origemUlt = Integer.parseInt(vertOrigem.getNome().substring(1, 3));
                for (Vertice vertDestino : g.getListaVertices()){
                    int destiIn = Integer.parseInt(vertDestino.getNome().substring(0, 1));
                    int destiUlt = Integer.parseInt(vertDestino.getNome().substring(1, 3));
                    if (!vertOrigem.equals(vertDestino) &&
                            (origemIn == destiIn || origemIn == destiIn -1 || origemIn == destiIn +1) &&
                            (origemUlt == destiUlt || origemUlt == destiUlt -1 || origemUlt == destiUlt +1)){
                        g.addAresta(vertOrigem, vertDestino);
                    }
                }
            }
            System.out.println(g);
            // Salvar a lista de adjacencia num arquivo
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("lista_adjacencia.txt"))) {
                writer.write(g.toString() + "\n");
                writer.write("Nodos duplicados: " + String.join(", ", nodosDuplicados) + "\n");
                writer.write("Nodos negativos: " + String.join(", ", nodosNegativos) + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
