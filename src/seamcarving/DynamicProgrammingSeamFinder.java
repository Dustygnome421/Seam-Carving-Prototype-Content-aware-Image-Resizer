package seamcarving;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 * @see SeamCarver
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {

    private static class Node {
        public int preRow;
        public int preCol;
        public double totalEnergy;
        public Node(int preRow, int preCol, double totalEnergy) {
            this.preRow = preRow;
            this.preCol = preCol;
            this.totalEnergy = totalEnergy;
        }
    }

    @Override
    public List<Integer> findHorizontalSeam(double[][] energies) {
        int numberOfCol = energies[0].length;
        int numberOfRow = energies.length;
        Node[][] table = new Node[numberOfCol][numberOfRow];
        for (int i = 0; i < numberOfCol; i++) {
            table[i][0] = new Node(i, 0, energies[0][i]);
        }

        for (int col = 1; col < numberOfRow; col++) {
            for (int row = 0; row < numberOfCol; row++) {
                double tempEnergy = energies[col][row];
                if (row == 0) {
                    Node leftNode = table[row][col - 1];
                    Node bottomLeftNode = table[row + 1][col - 1];
                    if (tempEnergy + leftNode.totalEnergy < tempEnergy + bottomLeftNode.totalEnergy) {
                        table[row][col] = new Node(row, col - 1, tempEnergy + leftNode.totalEnergy);
                    } else {
                        table[row][col] = new Node(row + 1, col - 1, tempEnergy + bottomLeftNode.totalEnergy);
                    }
                } else if (row == numberOfCol - 1) {
                    Node leftNode = table[row][col - 1];
                    Node upLeftNode = table[row - 1][col - 1];
                    if (tempEnergy + leftNode.totalEnergy < tempEnergy + upLeftNode.totalEnergy) {
                        table[row][col] = new Node(row, col - 1, tempEnergy + leftNode.totalEnergy);
                    } else {
                        table[row][col] = new Node(row - 1, col - 1, tempEnergy + upLeftNode.totalEnergy);
                    }
                } else {
                    Node upLeftNode = table[row - 1][col - 1];
                    Node leftNode = table[row][col - 1];
                    Node bottomLeftNode = table[row + 1][col - 1];
                    table[row][col] = new Node(row - 1, col - 1, tempEnergy + upLeftNode.totalEnergy);
                    if (table[row][col].totalEnergy > leftNode.totalEnergy + tempEnergy) {
                        table[row][col] = new Node(row, col - 1, tempEnergy + leftNode.totalEnergy);
                    }
                    if (table[row][col].totalEnergy > bottomLeftNode.totalEnergy + tempEnergy) {
                        table[row][col] = new Node(row + 1, col - 1, tempEnergy + bottomLeftNode.totalEnergy);
                    }

                }
            }
        }

        ArrayList<Integer> result = new ArrayList<>();
        Node min = table[0][numberOfRow - 1];
        result.add(0);
        for (int i = 1; i < numberOfCol; i++) {
            if (min.totalEnergy > table[i][numberOfRow - 1].totalEnergy) {
                min = table[i][numberOfRow - 1];
                result.set(0, i);
            }
        }
        for (int i = numberOfRow - 1; i > 0; i--) {
            result.add(min.preRow);
            min = table[min.preRow][min.preCol];
        }
        Collections.reverse(result);
        return result;
    }

    @Override
    public List<Integer> findVerticalSeam(double[][] energies) {
        int numberOfCol = energies[0].length; //5
        int numberOfRow = energies.length; // 6
        Node[][] table = new Node[numberOfCol][numberOfRow];


        for (int i = 0; i < numberOfRow; i++) {
            table[0][i] = new Node(0, i, energies[i][0]);
        }

        for (int row = 1; row < numberOfCol; row++) {
            for (int col = 0; col < numberOfRow; col++) {
                double tempEnergy = energies[col][row];

                if (col == 0) {
                    Node upNode = table[row - 1][col];
                    Node upRightNode = table[row - 1][col + 1];
                    if (tempEnergy + upNode.totalEnergy < tempEnergy + upRightNode.totalEnergy) {
                        table[row][col] = new Node(row - 1, col, tempEnergy + upNode.totalEnergy);
                    } else {
                        table[row][col] = new Node(row - 1, col + 1, tempEnergy + upRightNode.totalEnergy);
                    }

                } else if (col == numberOfRow - 1) {
                    Node upNode = table[row - 1][col];
                    Node upLeftNode = table[row - 1][col - 1];
                    if (tempEnergy + upNode.totalEnergy < tempEnergy + upLeftNode.totalEnergy) {
                        table[row][col] = new Node(row - 1, col, tempEnergy + upNode.totalEnergy);
                    } else {
                        table[row][col] = new Node(row - 1, col - 1, tempEnergy + upLeftNode.totalEnergy);
                    }
                } else {
                    Node upLeftNode = table[row - 1][col - 1];
                    Node upNode = table[row - 1][col];
                    Node upRightNode = table[row - 1][col + 1];
                    table[row][col] = new Node(row - 1, col - 1, tempEnergy + upLeftNode.totalEnergy);
                    if (upNode.totalEnergy + tempEnergy < table[row][col].totalEnergy) {
                        table[row][col] = new Node(row - 1, col, tempEnergy + upNode.totalEnergy);
                    }

                    if (upRightNode.totalEnergy + tempEnergy < table[row][col].totalEnergy) {
                        table[row][col] = new Node(row - 1, col + 1, tempEnergy + upRightNode.totalEnergy);
                    }
                }
            }
        }
        ArrayList<Integer> result = new ArrayList<>();
        Node min = table[numberOfCol - 1][0];
        result.add(0);
        for (int i = 1; i < numberOfRow; i++) {
            if (min.totalEnergy > table[numberOfCol - 1][i].totalEnergy) {
                min = table[numberOfCol - 1][i];
                result.set(0, i);
            }
        }
        for (int i = numberOfCol - 1; i > 0; i--) {
            result.add(min.preCol);
            min = table[min.preRow][min.preCol];
        }
        Collections.reverse(result);
        return result;
    }
}
