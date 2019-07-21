import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class NagamochiIbaraki {

    int V, V_;
    int E;
    int[][] adj;
    boolean[] exist;
    Random random;
    int sVertex, tVertex;

    public static void main(String[] args){

        NagamochiIbaraki obj = new NagamochiIbaraki();
        obj.runNagamochiIbarakiAlgo();

    }

    private void runNagamochiIbarakiAlgo(){

//        initializeGraph();
        createSampleGraph2();
        int minCut = calculateMinCut();
        if(minCut == -1){
            System.out.println("Disconnected");
        }

        System.out.println(minCut);
    }

    private void initializeGraph(){

//        this.V = 25;
//        this.V_ = 25;
        this.V = 5;
        this.V_ = 5;
        random = new Random();
        adj = new int[V][V];
        exist = new boolean[V];
//        this.E = getRandomEdges();
        this.E = 18;
        generateRandomEdges();
//        for(int i = 0; i < V_; i++){
//            for(int j = 0; j < V_; j++){
//                System.out.print(adj[i][j] + " ");
//            }
//            System.out.println();
//        }
        printGraph();
        Arrays.fill(exist, true);
        sVertex = tVertex = 0;
    }

    private int getRandomEdges(){

        return 5 * random.nextInt(110 - 10 + 1) + 10;
    }

    private void generateRandomEdges(){

        for(int i = 0; i < E; i++){
            int u = random.nextInt(V);
            int v = random.nextInt(V);
            while(u == v){
               u = random.nextInt(V);
               v = random.nextInt(V);
            }
            int weight = random.nextInt(100) + 1;
            adj[u][v] += weight;
            adj[v][u] += weight;
        }

    }

    private int calculateMinCut(){

        int minCut = Integer.MAX_VALUE;
        int stPhaseCut;
        while(V > 1){

            stPhaseCut = minCutPhase();
            if(stPhaseCut == -1){
                return -1;
            }
            if(stPhaseCut < minCut){
                minCut = stPhaseCut;
            }
            merge(sVertex, tVertex);
        }
        return  minCut;
    }

    private int minCutPhase(){

        int phaseCut = 0;
        Set<Integer> visited = new LinkedHashSet<>();

//        int vertex = getArbitraryVertex(V, visited);
        int vertex = getNextVertex();
        visited.add(vertex);
        while (visited.size() != V){

            int tightVertex = getMostTightlyConnected(visited);
            if(tightVertex == -1){
                return -1;
            }
            visited.add(tightVertex);
        }


        int index = 0;
        for(int visitedVertex:visited){

            if(index == visited.size() - 2){
                sVertex = visitedVertex;
                index++;
            }
            else if(index == visited.size() - 1){
                tVertex = visitedVertex;
            }
            else{
                index++;
            }
        }

        for(int i = 0; i < V; i++){
            phaseCut += adj[tVertex][i];
        }

        return phaseCut;
    }

    private void merge(int v1, int v2){

        // merge v2 into v1
        // remove edges between them to avoid self loops
        adj[v1][v2] = adj[v2][v1] = 0;
        for(int i = 0; i < V; i++){
            // add edges of v2 to v1
            adj[v1][i] += adj[v2][i];
            adj[i][v1] += adj[i][v2];

            // delete edges of v2
            adj[v2][i] = 0;
            adj[i][v2] = 0;
        }

        // delete vertex
        exist[v2] = false;
        this.V--;
    }

    private int getMostTightlyConnected(Set<Integer> visited){

        int tightVertex = -1;
        int strength[] = new int[V];
        Arrays.fill(strength, 0);
        for(int vertex:visited){

            for(int c = 0; c < V; c++){
                if(!visited.contains(c) && adj[vertex][c] != 0){
                    strength[c] += adj[vertex][c];
                }
            }
        }

        int maxVal = 0;
        for(int i = 0; i < V; i++){
            if(maxVal < strength[i]){
                maxVal = strength[i];
                tightVertex = i;
            }
        }
        return tightVertex;
    }

    private int getArbitraryVertex(int V, Set<Integer> visited){

        int vertexArr[] = new int[V];
        int index = 0;

        // get all unvisited vertices in  vertexArr
        for(int i = 0; i < V; i++){
            if(!visited.contains(i)){
                vertexArr[index++] = i;
            }
        }
        // pick random vertex which is not visited
        return vertexArr[random.nextInt(index)];
    }

    private int getNextVertex(){

        for(int i = 0; i < V_; i++){
            if(exist[i]){
                return i;
            }
        }
        return -1;
    }

    private void createSampleGraph() {

        this.V = 6;
        this.E = 7;
        adj =   new int[][] {
                        {0, 6, 0, 0, 0, 0},
                        {6, 0, 8, 3, 0, 0},
                        {0, 8, 0, 0, 1, 0},
                        {0, 3, 0, 0, 20, 5},
                        {0, 0, 1, 20, 0, 2},
                        {0, 0, 0, 5, 2, 0},
                };

        exist = new boolean[V];
        Arrays.fill(exist, true);
        sVertex = tVertex = 0;

    }

    public int getV() {
        return V;
    }

    public void setV(int v) {
        V = v;
    }

    public int getE() {
        return E;
    }

    public void setE(int e) {
        E = e;
    }

    private void createSampleGraph2() {

        this.V = 5;
        this.E = 8;
        this.V_ = 5;
        adj =
                new int[][] {
                        {0,	121,	32,	320,	183,	},
                        {121,	0,	57,	0,	0,	},
                        {32,	57,	0,	100,	52,	},
                        {320,	0,	100,	0,	170,	},
                        {183,	0,	52,	170,	0,	}};
        exist = new boolean[V_];
        Arrays.fill(exist, true);
    }

    private void printGraph() {
        if (adj == null) {
            System.out.println("create graph by calling createSampleGraph() or CreateGraph()");
            return;
        }
        System.out.println("#Vertices: " + V + "; #Edges: " + E);
        System.out.println("------------------------------------");
        for (int i = 0; i < V_; i++) {
            System.out.print("{");
            for (int j = 0; j < V_; j++) {
                System.out.print(adj[i][j] + ",\t");
            }
            System.out.print("},\n");
        }
        System.out.println("------------------------------------");
    }

}
