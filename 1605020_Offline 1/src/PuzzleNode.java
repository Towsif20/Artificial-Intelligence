import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class PuzzleNode
{
    int size;
    int[][] state;
    int[][] goal;
    PuzzleNode parent;

    private int rowBlank;
    private int colBlank;
    private int depth;
    private int move;



    public PuzzleNode(int size, int[][] state, int[][] goal)
    {
        this.size = size;

        this.state = state;

        this.goal = goal;

        depth = 0;

        move = 0;   //  root

        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                if (state[i][j] == 0)
                {
                    rowBlank = i;
                    colBlank = j;
                    break;
                }
            }
        }

        parent = null;
    }

    void print()
    {
        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                System.out.print(state[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("");
    }

    int getInversionCount()
    {
        ArrayList<Integer> list = new ArrayList<Integer>();

        for(int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                list.add(state[i][j]);
            }
        }

        int invcount = 0;

        for (int i=0;i<list.size()-1;i++)
        {
            for (int j=i+1;j<list.size();j++)
            {
                if (list.get(i) !=0 && list.get(j) != 0 && list.get(i) > list.get(j))
                {
                    invcount++;
                }
            }
        }

        return invcount;
    }

    int findBlankPosition()
    {
        if (goal[0][0] == 0)
            return rowBlank + 1;

        return size - rowBlank;
    }

    boolean isSolvable()
    {
        int count = getInversionCount();

        if (size%2 == 1)
        {
            if (count%2 == 0)
                return true;
        }

        else
        {
            int blank = findBlankPosition();

            if (blank%2 == 0 && count%2 == 1)
            {
                return true;
            }

            if (blank%2 == 1 && count%2 == 0)
            {
                return true;
            }
        }

        return false;
    }

    public void solve()
    {
        if (!isSolvable())
        {
            System.out.println("not solvable");
            return;
        }


        int cost = 1;
        int expandedNodes = 0;

        while (!Main.pq.isEmpty())
        {
            PuzzleNode node = Main.pq.poll();
            expandedNodes++;

            Main.closed.add(node);

            if (node.match())
            {
                System.out.println("\nDisplacement Heuristics");
                node.printPath();
                System.out.println("Depth = " + node.depth);
                System.out.println("Explored Nodes = " + cost);
                System.out.println("Expanded Nodes = " + expandedNodes);
                break;
            }

            PuzzleNode temp = node.moveUp();
            if (temp != null && node.move != 2)
            {
                if (temp.match())
                {
                    System.out.println("\nDisplacement Heuristics");
                    temp.printPath();
                    cost++;
                    System.out.println("Depth = " + temp.depth);
                    System.out.println("Explored Nodes = " + cost);
                    System.out.println("Expanded Nodes = " + expandedNodes);
                    break;
                }

                if (!temp.visited())
                {
                    Main.pq.add(temp);
                    cost++;
                }
            }

            temp = node.moveDown();
            if (temp != null && node.move != 1)
            {
                if (temp.match())
                {
                    System.out.println("\nDisplacement Heuristics");
                    temp.printPath();
                    cost++;
                    System.out.println("Depth = " + temp.depth);
                    System.out.println("Explored Nodes = " + cost);
                    System.out.println("Expanded Nodes = " + expandedNodes);
                    break;
                }
                if (!temp.visited())
                {
                    Main.pq.add(temp);
                    cost++;
                }
            }

            temp = node.moveLeft();
            if (temp != null && node.move != 3)
            {
                if (temp.match())
                {
                    System.out.println("\nDisplacement Heuristics");
                    temp.printPath();
                    cost++;
                    System.out.println("Depth = " + temp.depth);
                    System.out.println("Explored Nodes = " + cost);
                    System.out.println("Expanded Nodes = " + expandedNodes);
                    break;
                }
                if (!temp.visited())
                {
                    Main.pq.add(temp);
                    cost++;
                }

            }

            temp = node.moveRight();
            if (temp != null && node.move != 4)
            {
                if (temp.match())
                {
                    System.out.println("\nDisplacement Heuristics");
                    temp.printPath();
                    cost++;
                    System.out.println("Depth = " + temp.depth);
                    System.out.println("Explored Nodes = " + cost);
                    System.out.println("Expanded Nodes = " + expandedNodes);
                    break;
                }
                if (!temp.visited())
                {
                    Main.pq.add(temp);
                    cost++;
                }
            }

        }

        Main.closed.clear();
    }

    public void solveManhattan()
    {
        if (!isSolvable())
        {
            System.out.println("not solvable");
            return;
        }

        int cost = 1;
        int expandedNodes = 0;

        while (!Main.pq1.isEmpty())
        {
            PuzzleNode node = Main.pq1.poll();
            expandedNodes++;

            Main.closed.add(node);


            if (node.match())
            {
                System.out.println("\nManhattan Heuristics");
                node.printPath();
                System.out.println("Depth = " + node.depth);
                System.out.println("Explored Nodes = " + cost);
                System.out.println("Expanded Nodes = " + expandedNodes);
                break;
            }

            PuzzleNode temp = node.moveUp();
            if (temp != null && node.move != 2) //prev move was not down
            {

                if (temp.match())
                {
                    System.out.println("\nManhattan Heuristics");
                    temp.printPath();
                    cost++;
                    System.out.println("Depth = " + temp.depth);
                    System.out.println("Explored Nodes = " + cost);
                    System.out.println("Expanded Nodes = " + expandedNodes);
                    break;
                }
                if (!temp.visited())
                {
                    Main.pq.add(temp);
                    cost++;
                }
            }

            temp = node.moveDown();
            if (temp != null && node.move != 1) //prev move was not up
            {

                if (temp.match())
                {
                    System.out.println("\nManhattan Heuristics");
                    temp.printPath();
                    cost++;
                    System.out.println("Depth = " + temp.depth);
                    System.out.println("Explored Nodes = " + cost);
                    System.out.println("Expanded Nodes = " + expandedNodes);
                    break;
                }
                if (!temp.visited())
                {
                    Main.pq.add(temp);
                    cost++;
                }
            }

            temp = node.moveLeft();
            if (temp != null && node.move != 3) //prev move was not right
            {

                if (temp.match())
                {
                    System.out.println("\nManhattan Heuristics");
                    temp.printPath();
                    cost++;
                    System.out.println("Depth = " + temp.depth);
                    System.out.println("Explored Nodes = " + cost);
                    System.out.println("Expanded Nodes = " + expandedNodes);
                    break;
                }
                if (!temp.visited())
                {
                    Main.pq.add(temp);
                    cost++;
                }
            }

            temp = node.moveRight();
            if (temp != null && node.move != 4)     //prev move was not left
            {
                if (temp.match())
                {
                    System.out.println("\nManhattan Heuristics");
                    temp.printPath();
                    cost++;
                    System.out.println("Depth = " + temp.depth);
                    System.out.println("Explored Nodes = " + cost);
                    System.out.println("Expanded Nodes = " + expandedNodes);
                    break;
                }
                if (!temp.visited())
                {
                    Main.pq.add(temp);
                    cost++;
                }
            }

        }

        Main.closed.clear();
    }


    boolean match()
    {
        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                if (state[i][j] != goal[i][j])
                    return false;
            }
        }

        return true;
    }

    boolean match(int[][] n)
    {
        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                if (state[i][j] != n[i][j])
                    return false;
            }
        }

        return true;
    }

    PuzzleNode moveUp()
    {
        if (rowBlank == 0)
            return null;



        int[][] newState = new int[size][size];

        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                newState[i][j] = state[i][j];
            }
        }

        int temp = newState[rowBlank-1][colBlank];
        newState[this.rowBlank-1][colBlank] = 0;
        newState[rowBlank][colBlank] = temp;

        PuzzleNode puzzleNode = new PuzzleNode(size, newState,this.goal);

        puzzleNode.colBlank = this.colBlank;
        puzzleNode.rowBlank = this.rowBlank-1;
        //puzzleNode.state = newState;
        puzzleNode.depth = this.depth+1;
        puzzleNode.parent = this;
        puzzleNode.move = 1;

        //puzzleNode.print();

        return puzzleNode;
    }

    PuzzleNode moveDown()
    {
        if (rowBlank == size-1)
            return null;


        int[][] newState = new int[size][size];

        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                newState[i][j] = state[i][j];
            }
        }


        int temp = newState[rowBlank+1][colBlank];
        newState[this.rowBlank+1][colBlank] = 0;
        newState[rowBlank][colBlank] = temp;

        PuzzleNode puzzleNode = new PuzzleNode(size, newState,this.goal);

        puzzleNode.colBlank = this.colBlank;
        puzzleNode.rowBlank = this.rowBlank+1;
        //puzzleNode.state = newState;
        puzzleNode.depth = this.depth+1;
        puzzleNode.parent = this;
        puzzleNode.move = 2;

        //puzzleNode.print();

        return puzzleNode;
    }

    PuzzleNode moveRight()
    {
        if (colBlank == size-1)
            return null;


        int[][] newState = new int[size][size];

        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                newState[i][j] = state[i][j];
            }
        }

        int temp = newState[rowBlank][colBlank+1];
        newState[this.rowBlank][colBlank+1] = 0;
        newState[rowBlank][colBlank] = temp;

        PuzzleNode puzzleNode = new PuzzleNode(size, newState, this.goal);

        puzzleNode.colBlank = this.colBlank+1;
        puzzleNode.rowBlank = this.rowBlank;
        //puzzleNode.state = newState;
        puzzleNode.depth = this.depth+1;
        puzzleNode.parent = this;
        puzzleNode.move = 3;

        //puzzleNode.print();

        return puzzleNode;
    }


    PuzzleNode moveLeft()
    {
        if (colBlank == 0)
            return null;


        int[][] newState = new int[size][size];

        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                newState[i][j] = state[i][j];
            }
        }

        int temp = newState[rowBlank][colBlank-1];
        newState[this.rowBlank][colBlank-1] = 0;
        newState[rowBlank][colBlank] = temp;

        PuzzleNode puzzleNode = new PuzzleNode(size, newState, this.goal);

        puzzleNode.colBlank = this.colBlank-1;
        puzzleNode.rowBlank = this.rowBlank;
        //puzzleNode.state = newState;
        puzzleNode.depth = this.depth+1;
        puzzleNode.parent = this;
        puzzleNode.move = 4;

        //puzzleNode.print();

        return puzzleNode;
    }


    int displacementHeuristic()
    {
        int h = 0;

        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                if (this.state[i][j] != this.goal[i][j] && state[i][j] != 0)
                {
                    h++;
                }
            }
        }

        return this.depth + h;
    }

    int manhattanHeuristic()
    {
        int h = 0;

        int goal_i=0, goal_j=0;

        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                for (int p=0;p<size;p++)
                {
                    for (int q=0;q<size;q++)
                    {
                        if (state[i][j] == goal[p][q])
                        {
                            goal_i = p;
                            goal_j = q;
                        }
                    }
                }

                h += Math.abs(i - goal_i) + Math.abs(j - goal_j);
            }
        }

        return h + depth;
    }

    void printPath()
    {
        Stack<PuzzleNode> stack = new Stack<>();

        PuzzleNode node = this;

        while (node != null)
        {
            stack.push(node);

            node = node.parent;
        }

        int moveCount = 0;

        while (!stack.empty())
        {
            System.out.println("move : " + moveCount);
            moveCount++;
            stack.pop().print();
            System.out.println();
        }

    }

    boolean visited()
    {
        for (int i=0;i<Main.closed.size();i++)
        {
            PuzzleNode p = Main.closed.get(i);

            if (this.match(p.state))
            {
                return true;
            }


        }
        return false;
    }


}

