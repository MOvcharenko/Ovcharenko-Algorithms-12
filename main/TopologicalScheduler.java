package main;

import graph.Digraph;
import sort.TopologicalSort;
import model.Course;
import io.CourseReader;
import io.ScheduleWriter;
import scheduler.SemesterScheduler;

import java.io.*;
import java.util.*;

/**
 * Main application class that reads course prerequisites,
 * performs topological sort, and outputs the course sequence
 */
public class TopologicalScheduler {
    
    public static void main(String[] args) {
        // Default input and output files
        String inputFile = "courses.txt";
        String outputFile = "schedule_output.txt";
        String semesterOutputFile = "semester_schedule.txt";
        
        // Override with command line arguments if provided
        if (args.length >= 1) inputFile = args[0];
        if (args.length >= 2) outputFile = args[1];
        
        try {
            System.out.println("Course Topological Scheduler");
            System.out.println("============================");
            System.out.println();
            
            // Step 1: Read courses and build digraph
            System.out.println("Reading courses from: " + inputFile);
            CourseReader reader = new CourseReader();
            Digraph digraph = reader.readCourses(inputFile);
            List<Course> courses = reader.getCourses();
            
            System.out.println("Loaded " + courses.size() + " courses");
            System.out.println("Created digraph with " + digraph.V() + 
                             " vertices and " + digraph.E() + " edges");
            System.out.println();
            
            // Print course list
            System.out.println("Courses Loaded:");
            for (Course course : courses) {
                System.out.println("  " + course);
            }
            System.out.println();
            
            // Step 2: Perform topological sort
            System.out.println("Performing topological sort...");
            TopologicalSort topological = new TopologicalSort(digraph);
            
            // Step 3: Check for cycles and output result
            if (topological.hasCycle()) {
                System.out.println("ERROR: The prerequisite graph contains a cycle!");
                System.out.print("Cycle detected: ");
                for (int v : topological.cycle()) {
                    System.out.print(v + " ");
                }
                System.out.println();
                System.out.println("Cannot create a valid schedule. Please check prerequisites.");
                return;
            }
            
            // Get the topological order
            List<Integer> orderList = topological.getOrderList();
            
            System.out.println("Topological sort successful!");
            System.out.println("Order: " + orderList);
            System.out.println();
            
            // Step 4: Write results to output file
            System.out.println("Writing results to: " + outputFile);
            ScheduleWriter writer = new ScheduleWriter();
            writer.writeOrder(outputFile, orderList, courses);
            System.out.println("Schedule written successfully!");
            
            // Step 5: Bonus - Create semester schedule
            System.out.println("Creating semester schedule...");
            SemesterScheduler scheduler = new SemesterScheduler();
            Map<Integer, List<Course>> semesterPlan = 
                scheduler.scheduleBySemester(orderList, courses);
            
            System.out.println("Writing semester schedule to: " + semesterOutputFile);
            writer.writeSemesterPlan(semesterOutputFile, semesterPlan);
            System.out.println("Semester schedule written successfully!");
            
            // Print summary to console
            System.out.println();
            System.out.println("========================================");
            System.out.println("TOPOLOGICAL ORDER SUMMARY");
            System.out.println("========================================");
            for (int i = 0; i < orderList.size(); i++) {
                int courseId = orderList.get(i);
                for (Course course : courses) {
                    if (course.getId() == courseId) {
                        System.out.printf("%2d. %s%n", (i + 1), course);
                        break;
                    }
                }
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("Error: Input file not found - " + e.getMessage());
            System.err.println("Please ensure the file '" + inputFile + "' exists.");
        } catch (IOException e) {
            System.err.println("Error reading/writing file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid number format in input file - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}