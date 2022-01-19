import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class CSP
{
    private Square square;

    int nodes;
    int backtracks;

    public CSP(Square square)
    {
        this.square = square;

        nodes = 1;
        backtracks = 0;
    }

    public boolean checkConsistency(Variable variable, int value)
    {
        int row = variable.getRow();
        int column = variable.getColumn();

        int size = this.square.getSize();
        Variable[][] matrix = this.square.getMatrix();

        for (int i=0;i<size;i++)
        {
            if (matrix[i][column].getValue() == value && i != row)
                return false;

            if (matrix[row][i].getValue() == value && i != column)
                return false;
        }

        return true;
    }

    public void removeFromDomain(Variable variable, int value)
    {
        int row = variable.getRow();
        int column = variable.getColumn();

        int size = this.square.getSize();
        Variable[][] matrix = this.square.getMatrix();

        for (int i=0;i<size;i++)
        {
            if (matrix[i][column].getValue() == 0 && i != row)
            {
                matrix[i][column].removeValueFromDomain(value);
            }

            if (matrix[row][i].getValue() == 0 && i != column)
            {
                matrix[row][i].removeValueFromDomain(value);
            }
        }
    }

    public boolean removeFromDomainAndCheck(Variable variable, int value)
    {
        int row = variable.getRow();
        int column = variable.getColumn();

        int size = this.square.getSize();
        Variable[][] matrix = this.square.getMatrix();

        for (int i=0;i<size;i++)
        {
            if (matrix[i][column].getValue() == 0 && i != row)
            {
                matrix[i][column].removeValueFromDomain(value);

                if (matrix[i][column].getDomain().size() == 0)
                    return false;
            }

            if (matrix[row][i].getValue() == 0 && i != column)
            {
                matrix[row][i].removeValueFromDomain(value);

                if (matrix[row][i].getDomain().size() == 0)
                    return false;
            }
        }

        return true;
    }

    public void addToDomain(Variable variable, int value)
    {
        int row = variable.getRow();
        int column = variable.getColumn();

        int size = this.square.getSize();
        Variable[][] matrix = this.square.getMatrix();

        for (int i=0;i<size;i++)
        {
            if (matrix[i][column].getValue() == 0 && i != row
            && checkConsistency(matrix[i][column],value))
            {
                if (! matrix[i][column].getDomain().contains(value))
                {
                    matrix[i][column].getDomain().add(value);
                }
            }

            if (matrix[row][i].getValue() == 0 && i != column
                    && checkConsistency(matrix[row][i],value))
            {
                if (! matrix[row][i].getDomain().contains(value))
                {
                    matrix[row][i].getDomain().add(value);
                }
            }
        }
    }

    public int countUnassignedDegree(Variable variable)
    {
        int row = variable.getRow();
        int column = variable.getColumn();

        int size = this.square.getSize();
        Variable[][] matrix = this.square.getMatrix();

        int count = 0;

        for (int i=0;i<size;i++)
        {
            if (matrix[i][column].getValue() == 0 && i != row)
                count++;

            if (matrix[row][i].getValue() == 0 && i != column)
                count++;
        }

        return count;
    }

    public Variable randomChoice()
    {
        Variable variable = null;
        int size = this.square.getSize();
        Variable[][] matrix = this.square.getMatrix();

        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                if (matrix[i][j].getValue() == 0)
                {
                    variable = matrix[i][j];
                    break;
                }
            }
        }

        return variable;
    }

    public Variable smallestDomainFirst()
    {
        Variable chosen = null;
        int min = Integer.MAX_VALUE;

        int size = square.getSize();
        Variable[][] matrix = square.getMatrix();

        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                if (matrix[i][j].getValue() == 0)
                {
                    if (matrix[i][j].getDomain().size() < min)
                    {
                        min = matrix[i][j].getDomain().size();
                        chosen = matrix[i][j];
                    }
                }
            }
        }

        return chosen;
    }

    public Variable maxDynamicDegree()
    {
        Variable variable = null;

        int max = -1;
        int size = square.getSize();
        Variable[][] matrix = square.getMatrix();

        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                if (matrix[i][j].getValue() == 0)
                {
                    int degree = countUnassignedDegree(matrix[i][j]);

                    if (degree > max)
                    {
                        max = degree;
                        variable = matrix[i][j];
                    }
                }
            }
        }

        return variable;
    }

    public Variable brelaz()
    {
        Variable chosen = null;
        int size = square.getSize();
        Variable[][] matrix = square.getMatrix();

        int min = Integer.MAX_VALUE;
        int max = -1;

        ArrayList<Variable> sameDomainSizedVariables = new ArrayList<>();

        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                if (matrix[i][j].getValue() == 0)
                {
                    int domainSize = matrix[i][j].getDomain().size();

                    if (domainSize < min)
                    {
                        min = domainSize;
                        sameDomainSizedVariables.clear();
                        sameDomainSizedVariables.add(matrix[i][j]);
                    }
                    else if (domainSize == min)
                    {
                        sameDomainSizedVariables.add(matrix[i][j]);
                    }
                }
            }
        }

        for (Variable variable : sameDomainSizedVariables)
        {
            int degree = countUnassignedDegree(variable);

            if (degree > max)
            {
                max = degree;
                chosen = variable;
            }
        }

        return chosen;
    }

    public Variable domddeg()
    {
        Variable chosen = null;

        int size = square.getSize();
        Variable[][] matrix = square.getMatrix();

        double min = Double.MAX_VALUE;
        double ratio;

        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                if (matrix[i][j].getValue() == 0)
                {
                    int degree = countUnassignedDegree(matrix[i][j]);

                    if (degree != 0)
                    {
                        ratio = matrix[i][j].getDomain().size() / degree;
                    }
                    else
                        ratio = 100;

                    if (ratio < min)
                    {
                        min = ratio;
                        chosen = matrix[i][j];
                    }
                }
            }
        }

        return chosen;
    }

    public boolean revise(Arc arc)
    {
        boolean revised = false;

        Variable first = arc.first;
        Variable second = arc.second;

        if (second.getDomain().size() == 1)
        {
            for (int i=0;i<first.getDomain().size();i++)
            {
                int value = first.getDomain().get(i);

                if (second.getDomain().get(0) == value)
                {
                    first.removeValueFromDomain(value);
                    revised = true;
                }
            }
        }

        return revised;
    }

    public boolean AC3(Variable variable)
    {
        LinkedList<Arc> arcs = new LinkedList<>();

        int row = variable.getRow();
        int column = variable.getColumn();

        int size = square.getSize();
        Variable[][] matrix = square.getMatrix();

        for (int i=0;i<size;i++)
        {
            if (matrix[i][column].getValue() == 0 && i != row)
            {
                Variable first = matrix[i][column];
                int firstRow = first.getRow();
                int firstColumn = first.getColumn();

                for (int j=0;j<size;j++)
                {
                    if (matrix[j][firstColumn].getValue() == 0 && j != firstRow)
                    {
                        Variable second = matrix[j][firstColumn];
                        Arc arc = new Arc(first, second);
                        arcs.add(arc);
                    }

                    if (matrix[firstRow][j].getValue() == 0 && j != firstColumn)
                    {
                        Variable second = matrix[firstRow][j];
                        Arc arc = new Arc(first, second);
                        arcs.add(arc);
                    }
                }
            }

            if (matrix[row][i].getValue() == 0 && i != column)
            {
                Variable first = matrix[row][i];
                int firstRow = first.getRow();
                int firstColumn = first.getColumn();

                for (int j=0;j<size;j++)
                {
                    if (matrix[j][firstColumn].getValue() == 0 && j != firstRow)
                    {
                        Variable second = matrix[j][firstColumn];
                        Arc arc = new Arc(first, second);
                        arcs.add(arc);
                    }

                    if (matrix[firstRow][j].getValue() == 0 && j != firstColumn)
                    {
                        Variable second = matrix[firstRow][j];
                        Arc arc = new Arc(first, second);
                        arcs.add(arc);
                    }
                }
            }
        }


        while (!arcs.isEmpty())
        {
            Arc head = arcs.poll();

            if (revise(head))
            {
                if (head.first.getDomain().size() == 0)
                    return false;

                Variable first = head.first;

                row = first.getRow();
                column = first.getColumn();

                for (int i=0;i<size;i++)
                {
                    if (matrix[i][column] != head.second && matrix[i][column].getValue() == 0 && i != row)
                    {
                        Variable neighbor = matrix[i][column];
                        Arc arc = new Arc(neighbor, first);
                        arcs.add(arc);
                    }

                    if (matrix[row][i] != head.second && matrix[row][i].getValue() == 0 && i != column)
                    {
                        Variable neighbor = matrix[row][i];
                        Arc arc = new Arc(neighbor, first);
                        arcs.add(arc);
                    }
                }
            }
        }

        return true;
    }

    public boolean MAC()
    {
        //nodes++;
        if (this.square.remaining == 0)
            return true;

        //Variable variable = randomChoice();
        //Variable variable = smallestDomainFirst();
        //Variable variable = domddeg();
        //Variable variable = maxDynamicDegree();
        Variable variable = brelaz();

        if (variable.getDomain().size() == 0)
        {
            return false;
        }

        int size = square.getSize();
        Variable[][] matrix = square.getMatrix();


        ArrayList<Integer> domain = variable.getDomain();

        for (Integer val : domain)
        {
            if (checkConsistency(variable, val))
            {
                nodes++;
                variable.setValue(val);
                square.assigned++;
                square.remaining--;

                boolean flag = removeFromDomainAndCheck(variable, val);

                if (!flag)
                {
                    backtracks++;
                    variable.setValue(0);
                    square.assigned--;
                    square.remaining++;
                    addToDomain(variable, val);
                }

                else
                {
                    ArrayList[][] backup = new ArrayList[size][size];

                    for (int i=0;i<size;i++)
                    {
                        for (int j=0;j<size;j++)
                        {
                            backup[i][j] = new ArrayList<Integer>();
                            if (matrix[i][j].getValue() == 0)
                            {
                                for (int k=0;k<matrix[i][j].getDomain().size();k++)
                                {
                                    backup[i][j].add(matrix[i][j].getDomain().get(k));
                                }
                            }
                        }
                    }

                    boolean flag2 = AC3(variable);

                    if (flag2)
                    {
                        boolean result = MAC();

                        if (result)
                            return true;
                    }

                    backtracks++;
                    variable.setValue(0);
                    square.assigned--;
                    square.remaining++;
                    addToDomain(variable, val);

                    for (int i=0;i<size;i++)
                    {
                        for (int j=0;j<size;j++)
                        {
                            if (backup[i][j].size() != 0)
                            {
                                for (int k=0;k<backup[i][j].size();k++)
                                {
                                    int value = (int) backup[i][j].get(k);

                                    if (! matrix[i][j].getDomain().contains(value))
                                    {
                                        matrix[i][j].getDomain().add(value);
                                    }
                                }
                            }
                        }
                    }

                }



            }
        }

        return false;
    }

    public boolean BackTracking()
    {
        //nodes++;

        if (this.square.remaining == 0)
            return true;

        //Variable variable = randomChoice();
        //Variable variable = smallestDomainFirst();
        Variable variable = domddeg();
        //Variable variable = maxDynamicDegree();
        //Variable variable = brelaz();

        if (variable.getDomain().size() == 0)
        {
            //backtracks++;
            return false;
        }

        ArrayList<Integer> domain = variable.getDomain();

        for (Integer val : domain)
        {
            if (checkConsistency(variable, val))
            {
                nodes++;
                variable.setValue(val);
                square.assigned++;
                square.remaining--;

                removeFromDomain(variable, val);

                boolean result = BackTracking();

                if (result)
                    return true;

                backtracks++;
                variable.setValue(0);
                square.assigned--;
                square.remaining++;
                addToDomain(variable, val);
            }
        }

        //backtracks++;
        return false;
    }

    public boolean ForwardChecking()
    {
        //nodes++;
        if (this.square.remaining == 0)
            return true;

        //Variable variable = randomChoice();
        //Variable variable = smallestDomainFirst();
        //Variable variable = domddeg();
        //Variable variable = maxDynamicDegree();
        Variable variable = brelaz();

        if (variable.getDomain().size() == 0)
            return false;

        ArrayList<Integer> domain = variable.getDomain();

        for (Integer val : domain)
        {
            if (checkConsistency(variable, val))
            {
                nodes++;
                variable.setValue(val);
                square.assigned++;
                square.remaining--;

                boolean flag = removeFromDomainAndCheck(variable, val);

                if (flag)
                {
                    boolean result = ForwardChecking();

                    if (result)
                        return true;
                }

                backtracks++;
                variable.setValue(0);
                square.assigned--;
                square.remaining++;
                addToDomain(variable, val);
            }
        }

        return false;
    }


    public void print()
    {
        System.out.println("nodes = " + nodes);
        System.out.println("backtracks = " + backtracks);
    }

}























