package graphs.shortestpaths;

import priorityqueues.ExtrinsicMinPQ;
import priorityqueues.NaiveMinPQ;
import graphs.BaseEdge;
import graphs.Graph;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new NaiveMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }


    @Override
    //Returns a (a map from vertex to preceding edge) containing the shortest path from start to end in given graph.
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {

        ExtrinsicMinPQ<V> edges = createMinPQ();
        Map<V, E> edgeTo = new HashMap<>();

        if (start.equals(end)) {
            return edgeTo;
        }

        Map<V, Double> distTo = new HashMap<>();
        Set<V> known = new HashSet<>();
        //Set<V> visited = new HashSet<>();

        edges.add(start, 0.0);
        //Initiate starting vertext is start with a distance of 0.
        distTo.put(start, 0.0);
        //visited start
        //visited.add(start);

        //while(!edges.isEmpty())
        while (!edges.isEmpty()) {
            V startVertex = edges.removeMin();
            known.add(startVertex);
            for (E edge : graph.outgoingEdgesFrom(startVertex)) {

                V tempVertex = edge.to();
                if (!distTo.containsKey(tempVertex)) {
                    distTo.put(tempVertex, Double.POSITIVE_INFINITY);
                    edgeTo.put(tempVertex, edge);
                    //visited.add(tempVertex);
                    edges.add(tempVertex, Double.POSITIVE_INFINITY);
                }
                double oldDist = distTo.get(tempVertex);
                double newDist = distTo.get(edge.from()) + edge.weight();
                if (newDist < oldDist) {
                    distTo.put(edge.to(), newDist);
                    edgeTo.put(edge.to(), edge);
                    edges.changePriority(edge.to(), newDist);
                }
                if (known.contains(end)) {
                    return edgeTo;
                }
            }
        }
        return edgeTo;

    }


    //Extracts the shortest path from start to end from the given shortest paths tree.
    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {


        if (start.equals(end)) {
            return new ShortestPath.SingleVertex<V, E>(start);
        }
        List<E> list = new ArrayList<>();
        if (spt.containsKey(end)) {
            list.add(0, spt.get(end));
            V vertex = spt.get(end).from();
            while (!vertex.equals(start)) {
                E tempEdge = spt.get(vertex);
                list.add(tempEdge);
                vertex = tempEdge.from();
            }

            List<E> list2 = new ArrayList<>();
            for (int i = list.size()-1; i >= 0; i--) {
                list2.add(list.get(i));
            }

            return new ShortestPath.Success<>(list2);
        }
        return new ShortestPath.Failure<>();
    }

}
