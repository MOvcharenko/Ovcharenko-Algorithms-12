package model;

/**
 * Represents a course in the curriculum
 */
public class Course {
    private final String code;
    private final String name;
    private final int id;
    
    /**
     * Constructs a Course with the specified code, name, and id
     * @param code course code (e.g., "SDT 100")
     * @param name course name
     * @param id unique identifier for the vertex in the graph
     */
    public Course(String code, String name, int id) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be null or empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty");
        }
        this.code = code;
        this.name = name;
        this.id = id;
    }
    
    /**
     * Returns the course code
     * @return course code
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Returns the course name
     * @return course name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the course ID (vertex index in graph)
     * @return course ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * Returns a string representation of this course
     * @return string with course code and name
     */
    @Override
    public String toString() {
        return code + ": " + name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return id == course.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}