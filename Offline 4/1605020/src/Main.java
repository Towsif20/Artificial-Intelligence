import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main
{
    static int size = 0;

    public static void main(String[] args)
    {
        String[] names = {
                        "d-10-01.txt.txt",
                        "d-10-06.txt.txt",
                        "d-10-07.txt.txt",
                        "d-10-08.txt.txt",
                        "d-10-09.txt.txt",
                        "d-15-01.txt.txt"
                        };

        for (int i=0;i<6;i++)
        {
            int[][] array = readFile(names[i]);
            Square square = new Square(size);
            square.setMatrix(array);

            square.print();
            System.out.println();

            CSP csp = new CSP(square);

            //csp.BackTracking();
            //csp.ForwardChecking();
            csp.MAC();
            square.print();
            System.out.println(names[i]);
            csp.print();
        }


    }

    public static int[][] readFile(String name)
    {
        int[][] array = null;
        try
        {
            File file = new File(name);

            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = reader.readLine();

            line = line.replaceAll(";","");

            String [] temp = line.split("=");

            size = Integer.parseInt(temp[1]);
            array = new int[size][size];

            System.out.println(size);

            reader.readLine();
            reader.readLine();

            for (int i=0;i<size;i++)
            {
                line = reader.readLine();
                line = line.replaceAll(",", "");
                line = line.replaceAll("|", "");
                line = line.replaceAll("];", "");
                temp = line.split(" ");
                for (int j=0;j<size;j++)
                {
                    array[i][j] = Integer.parseInt(temp[j]);
                }
            }

            reader.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return array;
    }
}
