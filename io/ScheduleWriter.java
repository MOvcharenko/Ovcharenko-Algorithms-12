package io;

import model.Course;
import java.io.*;
import java.util.*;

/**
 * Writes the course schedule to an output file
 */
public class ScheduleWriter {
    
    /**
     * Writes the topological order of courses to a file
     * @param filename output file path
     * @param courseOrder topological order of course IDs
     * @param courses list of all courses
     * @throws IOException if file cannot be written
     */
    public void writeOrder(String filename, List<Integer> courseOrder, List<Course> courses) 
            throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("========================================");
            writer.println("TOPOLOGICAL ORDER OF COURSES");
            writer.println("========================================");
            writer.println();
            writer.println("Courses in order (prerequisites first):");
            writer.println();
            
            for (int i = 0; i < courseOrder.size(); i++) {
                int courseId = courseOrder.get(i);
                Course course = findCourseById(courses, courseId);
                if (course != null) {
                    writer.printf("%2d. %s%n", (i + 1), course);
                }
            }
        }
    }
    
    /**
     * Writes the semester schedule to a file (Bonus)
     * @param filename output file path
     * @param semesterPlan map from semester to courses
     * @throws IOException if file cannot be written
     */
    public void writeSemesterPlan(String filename, Map<Integer, List<Course>> semesterPlan) 
            throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("========================================");
            writer.println("FOUR-YEAR SEMESTER SCHEDULE");
            writer.println("========================================");
            writer.println();
            
            String[] yearLabels = {"Freshman", "Sophomore", "Junior", "Senior"};
            
            for (int semester = 1; semester <= semesterPlan.size(); semester++) {
                int yearIndex = (semester - 1) / 2;
                String term = (semester % 2 == 1) ? "Fall" : "Spring";
                int year = yearIndex + 1;
                
                writer.println("----------------------------------------");
                writer.printf("Year %d (%s) - %s Semester%n", year, yearLabels[yearIndex], term);
                writer.println("----------------------------------------");
                
                List<Course> courses = semesterPlan.get(semester);
                if (courses.isEmpty()) {
                    writer.println("  No courses scheduled");
                } else {
                    for (Course course : courses) {
                        writer.printf("  • %s%n", course);
                    }
                }
                writer.println();
            }
        }
    }
    
    /**
     * Finds a course by its ID
     * @param courses list of courses
     * @param id course ID
     * @return course or null if not found
     */
    private Course findCourseById(List<Course> courses, int id) {
        for (Course course : courses) {
            if (course.getId() == id) {
                return course;
            }
        }
        return null;
    }
}