import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class NagamochiIbaraki {

    int V, _V;
    int E;
    int[][] adj;
    boolean[] exist;
    Random random;
    int sVertex, tVertex;

    public static void main(String[] args){

        NagamochiIbaraki obj = new NagamochiIbaraki();
        // initializes the graph with random edges
        obj.initializeGraph();
        obj.runNagamochiIbarakiAlgo();


        System.out.println();
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.println();
        System.out.println();
        // creates sample graph given in slides
        obj.createSampleGraph();
        obj.runNagamochiIbarakiAlgo();
    }

    /**
     * calculates mincut for the given graph
     * returns mincut value for a properly connected graph
     * If the graph is disconnected, it returns -1 value for mincut
     */
    private void runNagamochiIbarakiAlgo(){


        printGraph();
        int minCut = calculateMinCut();
        if(minCut == -1){
            System.out.println("Graph is Disconnected");
        }
        else{
            System.out.println("Mincut of the given undirected graph: "+minCut);
        }
    }

    /**
     * initializes the graph with V = 25, E = random value between 50 and 550
     * Randomly creates edges between vertices with edge weight random
     * exist array : keeps track whether the current vertex is present in the graph (i.e. not deleted while merging)
     * sVertex = second last vertex entering the cut set
     * tVertex = last vertex entering the cut set
     **/
    private void initializeGraph(){

        this.V = 25;
        this._V = 25;
        random = new Random();
        adj = new int[_V][_V];
        exist = new boolean[_V];
        this.E = getRandomEdges();
        generateRandomEdges();
        Arrays.fill(exist, true);
        sVertex = tVertex = 0;
    }

    /**
     * @return random number of edges ranging from 50 to 550
     */
    private int getRandomEdges(){

        return 5 * random.nextInt(110 - 10 + 1) + 10;
    }

    /**
     * returns randomly generated edges (parallel edges allowed but no self loops)
     */
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

    /**
     * calculates mincut by executing various rounds of calculating the stphasecut and merging vertices
     * @return mincut
     */
    private int calculateMinCut(){

        int minCut = Integer.MAX_VALUE;
        int stPhaseCut;
        while(V > 1){

            //calculates stPhaseCut for this round
            stPhaseCut = minCutPhase();

            //stPhaseCut = -1 when the graph is disconnected
            if(stPhaseCut == -1){
                return -1;
            }
            // keeps track of minCit out of all stPhaseCuts
            if(stPhaseCut < minCut){
                minCut = stPhaseCut;
            }
            // merges sVertex and tVertex into sVertex
            merge(sVertex, tVertex);
        }
        return  minCut;
    }

    /**
     * calculates mincut for one round of
     * @return
     */
    private int minCutPhase(){

        int phaseCut = 0;
        // set to track visited vertices for this iteration
        // LinkedHashSet maintains the order of insertion of vertices in the set
        Set<Integer> visited = new LinkedHashSet<>();

        // gets an artitrary vertex out of existing (not deleted while merging) vertices
        int vertex = getArbitraryVertex();
        visited.add(vertex);
        while (visited.size() != V){

            // most tightly connected vertex is the vertex whose sum of edge weights into set visited is maximum
            int tightVertex = getMostTightlyConnected(visited);

            // If the graph is disconnected
            if(tightVertex == -1){
                return -1;
            }

            // add the tightVertex to visited
            visited.add(tightVertex);
        }


        int index = 0;
        for(int visitedVertex:visited){

            // getting the second last inserted vertex in visited set
            if(index == visited.size() - 2){
                sVertex = visitedVertex;
                index++;
            }
            // getting the last inserted vertex in visited set
            else if(index == visited.size() - 1){
                tVertex = visitedVertex;
            }
            else{
                index++;
            }
        }

        for(int i = 0; i < _V; i++){
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

    /**
     *
     * @param visited is the set of visited vertices for this current iteration for calculation of mincut
     * @return returns the vertex having maximum sum of edge weights out of all vertices in visited
     */
    private int getMostTightlyConnected(Set<Integer> visited){

        int tightVertex = -1;
        int strength[] = new int[_V];
        Arrays.fill(strength, 0);
        for(int vertex:visited){

            for(int c = 0; c < _V; c++){
                if(!visited.contains(c) && adj[vertex][c] != 0){
                    strength[c] += adj[vertex][c];
                }
            }
        }

        int maxVal = 0;
        for(int i = 0; i < _V; i++){
            if(maxVal < strength[i]){
                maxVal = strength[i];
                tightVertex = i;
            }
        }
        return tightVertex;
    }

    private int getArbitraryVertex(){

        int vertexArr[] = new int[_V];
        int index = 0;

        // get all unvisited vertices in  vertexArr
        for(int i = 0; i < _V; i++){
            if(exist[i]){
                vertexArr[index++] = i;
            }
        }
        // pick random vertex which is not visited
        return vertexArr[random.nextInt(index)];
    }

    // picks the next available vertex from the graph
    private int getNextVertex(){

        for(int i = 0; i < _V; i++){
            if(exist[i]){
                return i;
            }
        }
        return -1;
    }

    private void createSampleGraph() {

        this.V = 6;
        this._V = 6;
        this.E = 7;
        adj =   new int[][] {
                {0, 6, 0, 0, 0, 0},
                {6, 0, 8, 3, 0, 0},
                {0, 8, 0, 0, 1, 0},
                {0, 3, 0, 0, 20, 5},
                {0, 0, 1, 20, 0, 2},
                {0, 0, 0, 5, 2, 0}
                };

        random = new Random();
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

    // prints the graph - number of vertices, edges and the adjacency matrix
    private void printGraph() {
        if (adj == null) {
            System.out.println("create graph by calling initializeGraph() or createSampleGraph()");
            return;
        }
        System.out.println("#Vertices: " + V + "; #Edges: " + E);
        System.out.println("*************************************");
        for (int i = 0; i < _V; i++) {
            System.out.print("{");
            for (int j = 0; j < _V; j++) {
                System.out.print(adj[i][j] + ",\t");
            }
            System.out.print("},\n");
        }
        System.out.println("**************************************");
    }

}
