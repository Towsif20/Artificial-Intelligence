package com.company;

import java.util.Arrays;

public class Student
{
    int id;
    Integer[] courses;

    public Student(int id, Integer[] courses)
    {
        this.id = id;
        this.courses = courses;
    }

    @Override
    public String toString()
    {
        return "Student{" +
                "id=" + id +
                ", courses=" + Arrays.toString(courses) +
                '}';
    }
}
