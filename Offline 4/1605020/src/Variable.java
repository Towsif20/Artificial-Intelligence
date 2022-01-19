import java.util.ArrayList;

public class Variable
{
    private int value;
    private ArrayList<Integer> domain;
    private int row;
    private int column;

    public Variable()
    {
        value = 0;
        domain = new ArrayList<>();
        row = -1;
        column = -1;
    }

    public void initializeDomain(int size)
    {
        for (int i=0;i<size;i++)
        {
            domain.add(i+1);
        }
    }

    public void removeValueFromDomain(int toBeRemoved)
    {
        for (int i=0;i<domain.size();i++)
        {
            if (domain.get(i) == toBeRemoved)
            {
                domain.remove(i);
                break;
            }
        }
    }

    public void clearDomain()
    {
        domain.clear();
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public ArrayList<Integer> getDomain()
    {
        return domain;
    }

    public void setDomain(ArrayList<Integer> domain)
    {
        this.domain = domain;
    }

    public int getRow()
    {
        return row;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public int getColumn()
    {
        return column;
    }

    public void setColumn(int column)
    {
        this.column = column;
    }

    public void printDomain()
    {
        System.out.println("Domain for " + this.value );

        if (domain.size() == 0)
        {
            System.out.println("empty domain");
            return;
        }

        for (Integer val : domain)
        {
            System.out.println(val + " ");
        }
        System.out.println();
    }

}
