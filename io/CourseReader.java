package io;

import model.Course;
import graph.Digraph;
import java.io.*;
import java.util.*;

/**
 * Reads course information and prerequisites from an input file
 */
public class CourseReader {
    private Map<String, Integer> courseToId;
    private List<Course> courses;
    private int vertexCount;
    
    /**
     * Constructs a CourseReader
     */
    public CourseReader() {
        courseToId = new HashMap<>();
        courses = new ArrayList<>();
        vertexCount = 0;
    }
    
    /**
     * Reads an input file containing course prerequisites and builds the digraph
     * Input format:
     * First line: number of courses N
     * Next N lines: course_code, course_name
     * Next line: number of prerequisites M
     * Next M lines: prerequisite_course_code, course_code
     * 
     * @param filename input file path
     * @return Digraph representing prerequisite structure
     * @throws IOException if file cannot be read
     */
    public Digraph readCourses(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            if (line == null) {
                throw new IOException("Empty file");
            }
            
            // Read number of courses
            int numCourses = Integer.parseInt(line.trim());
            
            // Read courses
            for (int i = 0; i < numCourses; i++) {
                line = reader.readLine();
                if (line == null) break;
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    String code = parts[0].trim();
                    String name = parts[1].trim();
                    courseToId.put(code, vertexCount);
                    courses.add(new Course(code, name, vertexCount));
                    vertexCount++;
                }
            }
            
            // Create digraph with the number of vertices
            Digraph digraph = new Digraph(vertexCount);
            
            // Read number of prerequisites
            line = reader.readLine();
            if (line != null) {
                int numPrerequisites = Integer.parseInt(line.trim());
                
                // Read prerequisites
                for (int i = 0; i < numPrerequisites; i++) {
                    line = reader.readLine();
                    if (line == null) break;
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        String prereq = parts[0].trim();
                        String course = parts[1].trim();
                        
                        if (courseToId.containsKey(prereq) && courseToId.containsKey(course)) {
                            // Edge from prerequisite to course (prerequisite must be taken first)
                            int prereqId = courseToId.get(prereq);
                            int courseId = courseToId.get(course);
                            digraph.addEdge(prereqId, courseId);
                        }
                    }
                }
            }
            
            return digraph;
        }
    }
    
    /**
     * Returns the mapping from course code to vertex ID
     * @return course code to ID map
     */
    public Map<String, Integer> getCourseToId() {
        return courseToId;
    }
    
    /**
     * Returns the list of courses
     * @return list of courses
     */
    public List<Course> getCourses() {
        return courses;
    }
}