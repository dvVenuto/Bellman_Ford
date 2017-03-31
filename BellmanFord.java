import java.util.*;

public class BellmanFord{

    private int[] distances = null;
    private int[] predecessors = null;
    private int source;

    BellmanFord(WGraph g, int source) throws Exception{
        
        // update pubic source
        this.source = source;
        // init dist and predecessors
        this.distances = new int[g.getNbNodes()];
        this.predecessors = new int[g.getNbNodes()];
        //wgraph is private, we dont know if nodes are in order so we must iterate and add
        HashSet<Integer> nodes = new HashSet<Integer>(g.getNbNodes());
        for(Edge e : g.getEdges()) {
            nodes.add(e.nodes[0]);
            nodes.add(e.nodes[1]);
        }
        //Init distances to infinity as per lessons (INT MAX VALUE used for infinity)
        for(Integer node :nodes) {
            if(node == source) {

                this.distances[node] = 0;
            } else {
                this.distances[node] = Integer.MAX_VALUE;
            }

            this.predecessors[node] = -1;
        }

        // relaxing nodes
        for(int i = 1; i < nodes.size(); i++) {
            for(Edge e : g.getEdges()) {
                int weight = distances[e.nodes[0]] +e.weight;
                if(weight <distances[e.nodes[1]]) {
                    distances[e.nodes[1]] = weight;
                    predecessors[e.nodes[1]] = e.nodes[0];
                }
            }
        }
        // check cycles that have negative weight nodes
        for(Edge e : g.getEdges()) {
            if(distances[e.nodes[0]] + e.weight < distances[e.nodes[1]]) {
                throw new Exception("Contains negative weight cycle");
            }
        }

    }

    public int[] shortestPath(int destination) throws Exception{
       
        // init path variable, preedeseccor (total linked list) is maxed size
        int[] path = new int[this.predecessors.length];
        // start path at dest (as per class)
        path[0] = destination;
        // init propoer states for looping
        int predecessor = destination;
        // init length(currently 1)
        int path_length = 1;
        // loop until we get a node that has no predecessor
        while((predecessor = this.predecessors[predecessor]) != -1) {
            // cur predecessor appended to the path, path length therefore increases by 1
            path[path_length] = predecessor;
            path_length++;
        }
        // make sure that the path goes to the source
        if(path[path_length - 1] != this.source) {
            throw new Exception("Source to dest path DNE");
        }

        // reverse the path so that it goes from the source to the destination
        int[] reversedPath = new int[path_length];
        for(int i = 0; i < path_length; i++) {
            reversedPath[i] = path[path_length - i - 1];
        }

        return reversedPath;
    }

    public void printPath(int destination){
        /*Print the path in the format s->n1->n2->destination
         *if the path exists, else catch the Error and 
         *prints it
         */
        try {
            int[] path = this.shortestPath(destination);
            for (int i = 0; i < path.length; i++){
                int next = path[i];
                if (next == destination){
                    System.out.println(destination);
                }
                else {
                    System.out.print(next + "-->");
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){

        String file = args[0];
        WGraph g = new WGraph(file);
        try{
            BellmanFord bf = new BellmanFord(g, g.getSource());
            bf.printPath(g.getDestination());
        }
        catch (Exception e){
            System.out.println(e);
        }

    }
}
