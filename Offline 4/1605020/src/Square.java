public class Square
{
    private Variable[][] matrix;
    private int size;
    public int assigned;
    public int remaining;

    public Square(int size)
    {
        this.size = size;
        matrix = new Variable[size][size];
        assigned = 0;
        remaining = 0;
    }

    public int getAssigned()
    {
        return assigned;
    }

    public int getSize()
    {
        return size;
    }

    public void setMatrix(int[][] matrix)
    {
        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                this.matrix[i][j] = new Variable();
                this.matrix[i][j].setRow(i);
                this.matrix[i][j].setColumn(j);
                this.matrix[i][j].setValue(matrix[i][j]);
                this.matrix[i][j].initializeDomain(size);
            }
        }

        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                if (this.matrix[i][j].getValue() != 0)
                {
                    int value = this.matrix[i][j].getValue();

                    for (int k=0;k<size;k++)
                    {
                        this.matrix[k][j].removeValueFromDomain(value);
                        this.matrix[i][k].removeValueFromDomain(value);
                    }

                    this.matrix[i][j].clearDomain();

                    this.assigned++;
                }
            }
        }

        this.remaining = size*size - assigned;
    }

    public Variable[][] getMatrix()
    {
        return matrix;
    }

    public void print()
    {
        for (int i=0;i<size;i++)
        {
            for (int j=0;j<size;j++)
            {
                System.out.print(matrix[i][j].getValue() + " ");
            }
            System.out.println();
        }

        System.out.println();
    }
}
