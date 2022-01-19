package com.company;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws IOException
    {
//        String filename = "yor-f-83";
//        String filename = "car-s-91";
//        String filename = "kfu-s-93";
//        String filename = "tre-s-92";
        String filename = "car-f-92";
        File courseFile = new File(filename + ".crs");
        File studentFile = new File( filename +  ".stu");


        int nodes = 0;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(courseFile));

            while (reader.readLine() != null)
            {
                nodes++;
            }

            reader.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println(nodes);
        //Schedule schedule = new Schedule(5);
        Schedule schedule = new Schedule(nodes);

        try
        {
            Scanner scanner = new Scanner(courseFile);
            for (int i=0;i<nodes;i++)
            {
                int course = scanner.nextInt() - 1;
                int count = scanner.nextInt();

                schedule.addStudentCount(course, count);
            }

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        LinkedList<Student> students = new LinkedList<Student>();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(studentFile));

            int id = 1;

            String line = "";
            while ((line = reader.readLine()) != null)
            {
                Scanner scanner = new Scanner(line);

                LinkedList<Integer> conflicts = new LinkedList<Integer>();

                while (scanner.hasNext())
                {
                    int next = scanner.nextInt() - 1;
                    conflicts.add(next);
                }

                int size = conflicts.size();
                Integer[] studentArray = new Integer[size];
                Student student = new Student(id, conflicts.toArray(studentArray));

                //System.out.println(student);

                students.add(student);

                id++;

                for (int i=0;i<size-1;i++)
                {
                    for (int j=i+1;j<size;j++)
                    {
                        schedule.addConflict(conflicts.get(i), conflicts.get(j));
                    }
                }
            }


        } catch (Exception e)
        {
            e.printStackTrace();
        }

        //schedule.sequential();
        //schedule.showDegree();
        //schedule.highestDegree();
        //schedule.largestEnrollment();
        //schedule.DSatur();
        //schedule.largestWeightedDegree();
        //schedule.SWO();

        schedule.highestDegree();

        System.out.println(schedule.countSlots());

        //System.out.println(schedule.countSlots());
        FileWriter writer = new FileWriter("output.txt", true);
        writer.write(filename + "\t");

        for (int count =0;count<3;count++)
        {
            System.out.println("Scheme " + (count+1));
            if (count == 0)
                schedule.highestDegree();

            if (count == 1)
                schedule.largestEnrollment();

            if (count == 2)
                schedule.DSatur();

            schedule.setStudents(students);
            double avgPenalty = schedule.calculatePenalty();
            System.out.println(avgPenalty);


            double minPenalty = Integer.MAX_VALUE;
            int[] slots = new int[nodes];

            //schedule.SWO();

            int total = schedule.countSlots();
            System.out.println(total);
            writer.write(total + "\t");

            for(int i=0;i<100;i++)
            {
                schedule.Kempe();
                avgPenalty = schedule.calculatePenalty();

                if (avgPenalty < minPenalty)
                {
                    minPenalty = avgPenalty;
                    slots = schedule.getGivenSlots();
                }
            }

            System.out.println(minPenalty);
            writer.write(String.format("%.2f", minPenalty) + "\t");
            schedule.setGivenSlots(slots);
            //schedule.show();
            schedule.show(filename);

            System.out.println(schedule.checkValidation());
        }

        writer.write("\n");
        writer.close();
        //System.out.println(students);
        //schedule.show();
    }
}
