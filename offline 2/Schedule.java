package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Schedule
{
    private int courses; // nodes
    private int[] numberOfStudents;
    private int[] degree;
    private int[][] matrix;
    private int[] blame;
    ArrayList<Integer> sequence;
    LinkedList<Student> students;

    public void setStudents(LinkedList<Student> students)
    {
        this.students = students;
    }

    private LinkedList[] edgeLists;

    private int[] givenSlots; // colors
    private int[] best; // colors
    private boolean[] available;
    LinkedList<Integer> chainList;

    public int[] getGivenSlots()
    {
        return givenSlots;
    }

    public void setGivenSlots(int[] givenSlots)
    {
        this.givenSlots = givenSlots;
    }

    public Schedule(int courses)
    {
        this.courses = courses;

        edgeLists = new LinkedList[courses];
        givenSlots = new int[courses];
        degree = new int[courses];
        blame = new int[courses];
        best = new int[courses];

        matrix = new int[courses][courses];

        for (int i = 0; i < courses; i++)
        {
            edgeLists[i] = new LinkedList<Integer>();
            degree[i] = 0;
            blame[i] = 0;
        }

        for (int i = 0; i < courses; i++)
        {
            for (int j = 0; j < courses; j++)
            {
                matrix[i][j] = 0;
            }
        }

        resetSlots();

        numberOfStudents = new int[courses];

        chainList = new LinkedList<Integer>();
        sequence = new ArrayList<Integer>();
    }

    void addStudentCount(int course, int count)
    {
        numberOfStudents[course] = count;
    }

    void addConflict(int course1, int course2) // add edge
    {
        matrix[course1][course2]++;
        matrix[course2][course1]++;

        if (!edgeLists[course1].contains(course2))
        {
            edgeLists[course1].add(course2);
            edgeLists[course2].add(course1);

            degree[course1]++;
            degree[course2]++;
        }
    }

    void allotSlot(int currentCourse)
    {
        available = new boolean[courses];

        Arrays.fill(available, true);

        Iterator<Integer> iterator = edgeLists[currentCourse].iterator();

        while (iterator.hasNext()) {
            int adjacentCourse = iterator.next();

            if (givenSlots[adjacentCourse] != -1)
            {
                available[givenSlots[adjacentCourse]] = false;
            }
        }

        for (int i = 0; i < courses; i++)
        {
            if (available[i]) {
                givenSlots[currentCourse] = i;
                break;
            }
        }

        Arrays.fill(available, true);
    }

    void largestWeightedDegree()
    {
        resetSlots();

        int[] sumWeights = new int[courses];

        for (int i = 0; i < courses; i++)
        {
            int sum = 0;

            for (int j = 0; j < courses; j++)
            {
                sum += matrix[i][j];
            }

            sumWeights[i] = sum;
        }

        for (int i = 0; i < courses; i++)
        {
            int max = -1;
            int node = 0;

            for (int j = 0; j < courses; j++)
            {
                if (max < sumWeights[j] && givenSlots[j] == -1)
                {
                    max = sumWeights[j];
                    node = j;
                }
            }

            allotSlot(node);
        }
    }

    public void sequential()
    {
        for (int i = 0; i < courses; i++)
        {
            sequence.add(i);
            // allotSlot(i);
        }

        // resetSlots();
    }

    public void highestDegree()
    {
        resetSlots();
        sequence.clear();

        for (int i = 0; i < courses; i++)
        {
            int max = -1;
            int node = 0;

            for (int j = 0; j < courses; j++)
            {
                if (max < degree[j] && givenSlots[j] == -1)
                {
                    max = degree[j];
                    node = j;
                }
            }

            // System.out.println("max degree node: " + node);

            allotSlot(node);
            sequence.add(node);
        }

        // resetSlots();
    }

    public void largestEnrollment()
    {
        resetSlots();
        sequence.clear();

        for (int i = 0; i < courses; i++)
        {
            int max = -1;
            int node = 0;

            for (int j = 0; j < courses; j++)
            {
                if (max < numberOfStudents[j] && givenSlots[j] == -1)
                {
                    max = numberOfStudents[j];
                    node = j;
                }
            }

            // System.out.println("max degree node: " + node);

            allotSlot(node);
            sequence.add(node);
        }

        // resetSlots();
    }

    public void DSatur()
    {
        resetSlots();
        sequence.clear();

        for (int k = 0; k < courses; k++)
        {
            int max = -1;
            LinkedList<Integer> maxDsaturNodes = new LinkedList<Integer>();
            int maxDegree = -1;

            for (int i = 0; i < courses; i++)
            {
                Set<Integer> set = new HashSet<Integer>();

                if (givenSlots[i] == -1)
                {
                    int len = edgeLists[i].size();

                    for (int j = 0; j < len; j++)
                    {
                        Integer adjacent = (Integer) edgeLists[i].get(j);

                        if (givenSlots[adjacent] != -1)
                        {
                            set.add(givenSlots[adjacent]);
                        }
                    }
                    if (max < set.size())
                    {
                        max = set.size();
                        maxDsaturNodes.clear();
                        maxDsaturNodes.add(i);
                    }
                    else if (max == set.size())
                    {
                        maxDsaturNodes.add(i);
                    }
                }

                set.clear();
            }

            //System.out.println(maxDsaturNodes.size());

            int node = 0;

            for (Integer maxDsaturNode : maxDsaturNodes)
            {
                if (maxDegree < degree[maxDsaturNode])
                {
                    maxDegree = degree[maxDsaturNode];
                    node = maxDsaturNode;
                }
            }

            allotSlot(node);
            sequence.add(node);
        }

        // resetSlots();
    }


    public int random()
    {
        Random random = new Random();

        int rand = random.nextInt();

        if (rand < 0)
            rand = -rand;

        return rand;
    }

    void chain(int course, int alternate)
    {
        chainList.add(course);

        int len = edgeLists[course].size();

        for (int i = 0; i < len; i++)
        {
            Integer adjacent = (Integer) edgeLists[course].get(i);

            if (givenSlots[adjacent] == alternate && !chainList.contains(adjacent))
            {
                chain(adjacent, givenSlots[course]);
            }
        }
    }

    void Kempe()
    {
        chainList.clear();

        int u = random() % courses;

        int len = edgeLists[u].size();

        if (len == 0)
            return;

        int i = random() % len;

        Integer v = (Integer) edgeLists[u].get(i);

        // v = 5;

        chain(u, givenSlots[v]);

        int slot1 = givenSlots[u];
        int slot2 = givenSlots[v];

        // System.out.println("chain");
        for (Integer node : chainList)
        {
            // System.out.println("node " + (node + 1) + " : " + "slot " +
            // (givenSlots[node]+1));
        }

        // swap the colors
        for (Integer node : chainList)
        {
            if (givenSlots[node] == slot2)
            {
                givenSlots[node] = slot1;
            }

            else
            {
                givenSlots[node] = slot2;
            }
        }
    }

    void pairSwap() {

        int len = countSlots();

        int u = -1;

        // Collections.shuffle(list);

        for (int i = 0; i < courses; i++) {
            Set<Integer> set = new HashSet<Integer>();

            // if (givenSlots[i] == -1)
            {
                int length = edgeLists[i].size();

                for (int j = 0; j < length; j++) {
                    Integer adjacent = (Integer) edgeLists[i].get(j);

                    // if (givenSlots[adjacent] != -1)
                    {
                        set.add(givenSlots[adjacent]);
                    }
                }
                System.out.println(set.size());
                if (set.size() < len - 1) {
                    u = i;
                    break;
                }
            }

            set.clear();
        }

        // int u = list.get(0);
        // u = 43;
        int slot1 = givenSlots[u];

        boolean[] usedSlotsByNeighbors = new boolean[len];

        for (int i = 0; i < len; i++) {
            usedSlotsByNeighbors[i] = false;
        }

        for (int i = 0; i < len; i++) {
            System.out.println((i + 1) + " " + usedSlotsByNeighbors[i]);
        }

        int size = edgeLists[u].size();
        System.out.println(size);

        usedSlotsByNeighbors[givenSlots[u]] = true;

        for (int i = 0; i < size; i++) {
            Integer node = (Integer) edgeLists[u].get(i);
            int slot = givenSlots[node];

            usedSlotsByNeighbors[slot] = true;
        }

        for (int i = 0; i < len; i++) {
            System.out.println((i + 1) + " " + usedSlotsByNeighbors[i]);
        }
        int slot2 = -1;

        for (int i = 0; i < len; i++) {
            if (!usedSlotsByNeighbors[i]) {
                slot2 = i;
                break;
            }
        }

        if (slot2 == -1) {
            return;
        }

        int v = -1;

        for (int i = 0; i < courses; i++) {
            if (givenSlots[i] == slot2) {
                if (!isSlotUsed(i, slot1)) {
                    v = i;

                    break;
                }
            }
        }

        // v = 22;

        if (v == -1) {
            System.out.println("v is not found");
            return;
        }
        System.out.println("before");
        System.out.println("u = " + (u + 1) + " : slot = " + (givenSlots[u] + 1));
        System.out.println("v = " + (v + 1) + " : slot = " + (givenSlots[v] + 1));
        givenSlots[v] = slot1;
        givenSlots[u] = slot2;

        System.out.println("after");
        System.out.println("u = " + (u + 1) + " : slot = " + (givenSlots[u] + 1));
        System.out.println("v = " + (v + 1) + " : slot = " + (givenSlots[v] + 1));
    }

    void SWO()
    {
        double a = 0.95;
        int b = 4;

        int max = this.countSlots();
        int current = 0;
        boolean updated = false;

        //System.out.println(this.countSlots());

        for (int i = 0; i < 100; i++)
        {
            resetSlots();

            for (int j = 0; j < courses; j++)
            {
                allotSlot(sequence.get(j));
            }

            current = this.countSlots();

            if (max > current)
            {
                max = current;
                best = givenSlots;
                System.out.println("updated " + this.countSlots());
                updated = true;
            }

            for (int j = 0; j < courses; j++)
            {
                blame[sequence.get(j)] = j;

                if ((double) givenSlots[sequence.get(j)] > a * (double) current)
                {
                    blame[sequence.get(j)] += b;
                }
            }

            for (int j = 0; j < courses - 1; j++)
            {
                for (int k = j + 1; k < courses; k++)
                {
                    if (blame[sequence.get(j)] > blame[sequence.get(k)])
                    {
                        int temp = sequence.get(j);
                        int temp2 = sequence.get(k);

                        sequence.set(j, temp2);
                        sequence.set(k, temp);

                        temp = blame[sequence.get(j)];
                        blame[sequence.get(j)] = blame[sequence.get(k)];
                        blame[sequence.get(k)] = temp;

                    }
                }
            }
        }

        //System.out.println(max);

        if (updated)
            givenSlots = best;
    }

    boolean isSlotUsed(int course, int initialSlot)
    {
        for (int i = 0; i < edgeLists[course].size(); i++)
        {
            if (givenSlots[(Integer) edgeLists[course].get(i)] == initialSlot)
                return true;
        }

        return false;
    }

    public double calculatePenalty()
    {
        int penalty = 0;

        for (Student s : students) {
            int len = s.courses.length;

            for (int i = 0; i < len - 1; i++)
            {
                for (int j = i + 1; j < len; j++)
                {
                    if (givenSlots[s.courses[i]] > givenSlots[s.courses[j]])
                    {
                        int temp = s.courses[i];
                        s.courses[i] = s.courses[j];
                        s.courses[j] = temp;
                    }
                }
            }

            // System.out.println("Student " + s.id);
            for (int i = 0; i < len; i++)
            {
                // System.out.println((s.courses[i]+1) + " " + (givenSlots[s.courses[i]] + 1) +
                // " ");
            }

            // System.out.println();

            int sum = 0;

            for (int i = 0; i < len - 1; i++)
            {
                int dif = givenSlots[s.courses[i + 1]] - givenSlots[s.courses[i]];

                int val;

                // System.out.println("dif = " + dif);

                if (dif > 5) {
                    val = 0;
                } else {
                    val = (int) Math.pow(2, 5 - dif);
                }

                sum += val;
            }

            // System.out.println("sum = " + sum);
            penalty += sum;
        }

        // System.out.println(penalty);

        return ((double) penalty) / students.size();
    }

    int countSlots() {
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < courses; i++)
        {
            if (max < givenSlots[i])
                max = givenSlots[i];
        }

        return max + 1;
    }

    boolean isInConsistency(int course)
    {
        int len = edgeLists[course].size();

        for (int i = 0; i < len; i++)
        {
            Integer adjacent = (Integer) edgeLists[course].get(i);

            if (givenSlots[course] == givenSlots[adjacent]) {
                System.out.println(course + " " + givenSlots[course]);
                return true;
            }
        }

        return false;
    }

    boolean checkValidation()
    {
        for (int i = 0; i < courses; i++) {
            if (isInConsistency(i)) {
                // System.out.println("conflict in " + (i+1));
                return false;
            }
        }

        return true;
    }

    public void show()
    {
        for (int i = 0; i < courses; i++)
        {
            System.out.println("Course " + (i + 1) + " : Slot " + (givenSlots[i] + 1));
        }
    }

    public void show(String filename)
    {
        File file = new File(filename + ".sol");

        try
        {
            FileWriter writer = new FileWriter(file);

            for (int i = 0; i < courses; i++)
            {
                //System.out.println("Course " + (i + 1) + " : Slot " + (givenSlots[i] + 1));
                writer.write("Course " + (i + 1) + " : Slot " + (givenSlots[i] + 1) + "\n");
            }

            writer.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    public void showDegree()
    {
        for (int i = 0; i < courses; i++) {
            System.out.println("node " + (i + 1) + " : " + " degree " + degree[i]);
        }
    }

    void resetSlots()
    {
        for (int i = 0; i < courses; i++)
        {
            givenSlots[i] = -1;
        }
    }

    void printColors()
    {
        for (int i = 0; i < courses; i++) {
            System.out.println(givenSlots[i]);
            for (Object node : edgeLists[i]) {
                System.out.print(givenSlots[(Integer) node] + " ");
            }
            System.out.println();
        }
    }
}
