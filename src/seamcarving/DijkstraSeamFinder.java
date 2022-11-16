package seamcarving;

import graphs.Edge;
import graphs.Graph;
import graphs.shortestpaths.DijkstraShortestPathFinder;
import graphs.shortestpaths.ShortestPath;
import graphs.shortestpaths.ShortestPathFinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DijkstraSeamFinder implements SeamFinder {
    private final ShortestPathFinder<Graph<Vertex, Edge<Vertex>>, Vertex, Edge<Vertex>> pathFinder;

    public DijkstraSeamFinder() {
        this.pathFinder = createPathFinder();
    }

    protected <G extends Graph<V, Edge<V>>, V> ShortestPathFinder<G, V, Edge<V>> createPathFinder() {
        /*
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
        */
        return new DijkstraShortestPathFinder<>();
    }

    @Override
    public List<Integer> findHorizontalSeam(double[][] energies) {
        List<Integer> list = new ArrayList<>();

        MyGraph graph = new MyGraph(energies, false);
        DijkstraShortestPathFinder<MyGraph, Vertex, Edge<Vertex>> shortestPathFinder = new
                                                                        DijkstraShortestPathFinder<>();
        ShortestPath<Vertex, Edge<Vertex>> traversal =
                            shortestPathFinder.findShortestPath(graph, graph.dummyFirst, graph.dummyLast);
        for (Edge<Vertex> idk: traversal.edges()) {
            if (idk.to() == graph.dummyLast) {
                continue;
            }
            list.add(idk.to().row);
        }

        return list;
    }

    @Override
    public List<Integer> findVerticalSeam(double[][] energies) {
        List<Integer> list = new ArrayList<>();

        MyGraph graph = new MyGraph(energies, true);
        DijkstraShortestPathFinder<MyGraph, Vertex, Edge<Vertex>> shortestPathFinder =
                                                                new DijkstraShortestPathFinder<>();
        ShortestPath<Vertex, Edge<Vertex>> traversal =
                        shortestPathFinder.findShortestPath(graph, graph.dummyFirst, graph.dummyLast);
        for (Edge<Vertex> idk: traversal.edges()) {
            if (idk.to() == graph.dummyLast) {
                continue;
            }
            list.add(idk.to().row);
        }

        return list;
    }


    private static class Vertex {

        double energy;
        int row;
        int col;
        ArrayList<Edge<Vertex>> outgoingEdges = new ArrayList<>();
        public Vertex(double energy, int col, int row) {
            this.energy = energy;
            this.row = row;
            this.col = col;
        }
        public Vertex(double energy) {
            this.energy = energy;
        }

    }

    private class MyGraph implements Graph<Vertex, Edge<Vertex>> {
        int dimensionCol;
        int dimensionRow;
        Vertex dummyFirst = new Vertex(-1.0);
        Vertex dummyLast = new Vertex(-2.0);
        boolean isVerticalSeam;

        @Override
        public Collection<Edge<Vertex>> outgoingEdgesFrom(Vertex vertex) {
            return vertex.outgoingEdges;
        }

        public MyGraph(double[][] table, boolean horOrVer) {
            this.isVerticalSeam = horOrVer;
            if (isVerticalSeam) {
                this.dimensionRow = table.length;
                this.dimensionCol = table[0].length;
            } else {
                this.dimensionCol = table.length;
                this.dimensionRow = table[0].length;
            }

            workDone(table);
        }

        private void workDone(double[][] table) {

            //vertex<Double> current = dummyFirst;
            Vertex[][] motherload = new Vertex[dimensionRow][dimensionCol];
            if (isVerticalSeam) {
                for (int i = 0; i < dimensionRow; i++) {
                    for (int j = 0; j < dimensionCol; j++) {
                        motherload[i][j] = new Vertex(table[i][j], j, i);
                    }
                }
            } else {
                for (int i = 0; i < table.length; i++) {
                    for (int j = 0; j < table[0].length; j++) {
                        motherload[j][i] = new Vertex(table[i][j], i, j);
                    }
                }
            }


            for (int i = 0; i < dimensionRow; i++) {
                Vertex addVertex= motherload[i][0];
                dummyFirst.outgoingEdges.add(new Edge<>(dummyFirst, addVertex, addVertex.energy));

            }

            for (Vertex[] outerLoop: motherload) {

                for (Vertex element: outerLoop) {

                    if (element.col == dimensionCol-1) {
                        element.outgoingEdges.add(new Edge<>(element, dummyLast, 0));
                        continue;
                    }

                    int tempX = element.row;
                    int tempY = element.col;
                    if (tempX == 0) {
                        element.outgoingEdges.add(new Edge<>(element, motherload[tempX][tempY+1],
                                                                motherload[tempX][tempY+1].energy));
                        element.outgoingEdges.add(new Edge<>(element, motherload[tempX+1][tempY+1],
                                                                motherload[tempX+1][tempY+1].energy));
                    } else if (tempX == dimensionRow-1) {
                        element.outgoingEdges.add(new Edge<>(element, motherload[tempX-1][tempY+1],
                                                                motherload[tempX-1][tempY+1].energy));
                        element.outgoingEdges.add(new Edge<>(element, motherload[tempX][tempY+1],
                                                                motherload[tempX][tempY+1].energy));
                    } else {
                        element.outgoingEdges.add(new Edge<>(element, motherload[tempX-1][tempY+1],
                                                                motherload[tempX-1][tempY+1].energy));
                        element.outgoingEdges.add(new Edge<>(element, motherload[tempX][tempY+1],
                                                                motherload[tempX][tempY+1].energy));
                        element.outgoingEdges.add(new Edge<>(element, motherload[tempX+1][tempY+1],
                                                                motherload[tempX+1][tempY+1].energy));
                    }
                }
            }


        }

    }
}
