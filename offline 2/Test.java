package com.company;

public class Test
{
    public static void main(String[] args)
    {
        int[] array = {1,1,2,1,2,1};

        System.out.println(array.toString());

        for (int i=0;i<array.length;i++)
        {
            System.out.print(array[i]+ " ");
        }
        System.out.println();

        for (int i=0;i<array.length;i++)
        {
            if (array[i] == 1)
                array[i] = 2;
            else
                array[i] = 1;
        }

        for (int i=0;i<array.length;i++)
        {
            System.out.print(array[i]+ " ");
        }
    }
}
