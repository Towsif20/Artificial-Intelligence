import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

public class Main
{
    static PriorityQueue<PuzzleNode> pq = new PriorityQueue<PuzzleNode>(new NodeComparator());
    static PriorityQueue<PuzzleNode> pq1 = new PriorityQueue<>(new NodeComparatorManhattan());

    static ArrayList<PuzzleNode> closed = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException
    {
        File file = new File("input.txt");
        int n;
        int size;

        Scanner scanner = new Scanner(file);

        n = scanner.nextInt();

        size = 4;

        int[][] state;
        int[][] goal = new int[size][size];

        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                goal[i][j] = scanner.nextInt();
            }
        }

        PuzzleNode node;

        for (int k=0;k<n-1;k++)
        {
            state = new int[size][size];
            //goal = new int[size][size];

            for (int i=0;i<size;i++)
            {
                for (int j=0;j<size;j++)
                {
                    //state[i][j] = random.nextInt()%20;
                    state[i][j] = scanner.nextInt();
                }
            }

            node = new PuzzleNode(size, state, goal);

            //node.print();

            System.out.println(node.getInversionCount());

            //System.out.println(node.displacementHeuristic());

            pq.add(node);
            pq1.add(node);

            //node.solve();

            node.solveManhattan();

            node.solve();
            pq.clear();
            pq1.clear();


        }

        //System.out.println(node.manhattanHeuristic());

    }
}
