import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    static String ALPHABETIC_ORDER = "abcdefghijklmnopqrstuvwxyz";

    public static void main(final String[] args) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String numberCasesStr = reader.readLine();
        try {
            int numberCases = Integer.parseInt(numberCasesStr);
            List<Graph> cases = new ArrayList<>();
            for (int i = 0; i < numberCases; i++) {
                // init graph case
                Graph graph = new Graph();
                // get veritices and edges size from input
                String verticeEdgeCount = reader.readLine();
                //generate vertices on graph
                generateVertices(graph, Integer.parseInt(verticeEdgeCount.split(" ")[0]));
                // get edges
                int numberOfEdges = Integer.parseInt(verticeEdgeCount.split(" ")[1]);
                for (int e = 0; e < numberOfEdges; e++) {
                    String verticesEdge = reader.readLine();
                    graph.addEdge(verticesEdge.split(" ")[0], verticesEdge.split(" ")[1]);
                }
                cases.add(graph);
            }
            for (int i = 0; i < cases.size(); i++) {
                //print case number
                System.out.println(String.format("Case #%s:", (i + 1)));
                // print graph case
                joinAndPrintComponents(cases.get(i));
                //adding blank line
                System.out.println("");
            }
        } catch (NumberFormatException err) {
            System.out.println(err.getMessage());
        }
    }

    private static void generateVertices(Graph graph, int size) {
        List<Vertice> verticeList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            verticeList.add(new Vertice(getVerticeNameOfIndex(i)));
        }
        verticeList.forEach(graph::addVertice);
    }

    private static String getVerticeNameOfIndex(int i) {
        return String.valueOf(ALPHABETIC_ORDER.charAt(i));
    }

    static void  joinAndPrintComponents(Graph graph) {
        List<SortedSet<Vertice>> components = new ArrayList<>();
        graph.getVerticeMap().values().forEach(vertice -> {
            // if group already existing in the other group
            SortedSet<Vertice> vertices = new TreeSet<>();
            //add connections of vertice
            addVerticesConnectionsSet(vertice, vertices);
            vertices.forEach(v -> {
                List<SortedSet<Vertice>> comps = components
                        .stream()
                        .filter(component -> component.contains(v))
                        .collect(Collectors.toList());
                components.removeAll(comps);
                Optional<SortedSet<Vertice>> joinedSetOp = comps.stream().reduce((vertices1, vertices2) -> {
                    vertices1.addAll(vertices2);
                    return vertices1;
                });
                if (joinedSetOp.isPresent()) {
                    joinedSetOp.get().addAll(vertices);
                    components.add(joinedSetOp.get());
                } else {
                    components.add(vertices);
                }
            });
        });
        //print a disconected graphs
        components.stream()
                .sorted(Comparator.comparing(SortedSet::first))
                .forEach(vertices -> {
                    StringBuilder verticesStr = new StringBuilder();
                    vertices.forEach(vertice -> verticesStr.append(vertice.getName()).append(","));
                    System.out.println(verticesStr.toString());
                });
        System.out.println(String.format("%s connected components", components.size()));
    }

    static void addVerticesConnectionsSet(Vertice v, SortedSet<Vertice> verticesComponent) {
        if (Objects.nonNull(v.getEdge())) {
            verticesComponent.addAll(v.getEdge());
        }
        verticesComponent.add(v);
    }
}

class Graph {
    private HashMap<String, Vertice> verticeMap;

    public Graph() {
        this.verticeMap = new HashMap<>();
    }

    public void addVertice(Vertice vertice) {
        this.verticeMap.put(vertice.getName(), vertice);
    }

    public void addEdge(String verticeNameFrom, String verticeNameTo) {
        Vertice from = verticeMap.get(verticeNameFrom);
        Vertice to = verticeMap.get(verticeNameTo);
        if (to != null && from != null) {
            from.addEdgeWith(to);
        }
    }

    public HashMap<String, Vertice> getVerticeMap() {
        return verticeMap;
    }
}

class Vertice implements Comparable<Vertice> {
    private String name;
    private Set<Vertice> edge;

    Vertice(String name) {
        this.name = name;
        this.edge = new HashSet<>();
    }

    public void addEdgeWith(Vertice vertice) {
        this.edge.add(vertice);
    }

    public Set<Vertice> getEdge() {
        return edge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertice vertice = (Vertice) o;
        return Objects.equals(name, vertice.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Vertice o) {
        int v1 = Main.ALPHABETIC_ORDER.indexOf(getName());
        int v2 = 0;
        if (o != null) {
            v2 = Main.ALPHABETIC_ORDER.indexOf(o.getName());
        }
        return Integer.compare(v1, v2);
    }
}