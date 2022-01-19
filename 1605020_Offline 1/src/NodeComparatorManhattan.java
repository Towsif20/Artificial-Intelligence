import java.util.Comparator;

public class NodeComparatorManhattan implements Comparator<PuzzleNode>
{
    @Override
    public int compare(PuzzleNode o1, PuzzleNode o2)
    {
        int h1 = o1.manhattanHeuristic();
        int h2 = o2.manhattanHeuristic();

        if (h1 < h2)
            return -1;

        else if (h1 > h2)
            return 1;

        return 0;
    }
}
