package scheduler;

import model.Course;
import java.util.*;

/**
 * Schedules courses across semesters (Bonus feature)
 * Distributes courses across 8 semesters (4 years), 1-3 courses per semester
 */
public class SemesterScheduler {
    private static final int SEMESTERS = 8; // 4 years * 2 semesters
    private static final int MIN_COURSES_PER_SEMESTER = 1;
    private static final int MAX_COURSES_PER_SEMESTER = 3;
    
    /**
     * Distributes courses in topological order across semesters
     * @param courseOrder list of courses in topological order
     * @param courses list of all courses
     * @return map from semester number to list of courses
     */
    public Map<Integer, List<Course>> scheduleBySemester(List<Integer> courseOrder, List<Course> courses) {
        Map<Integer, List<Course>> semesterPlan = new LinkedHashMap<>();
        
        // Initialize empty semesters
        for (int i = 1; i <= SEMESTERS; i++) {
            semesterPlan.put(i, new ArrayList<>());
        }
        
        // Calculate optimal distribution
        int totalCourses = courseOrder.size();
        int baseCoursesPerSemester = totalCourses / SEMESTERS;
        int extraCourses = totalCourses % SEMESTERS;
        
        // Distribute courses
        int courseIndex = 0;
        for (int semester = 1; semester <= SEMESTERS && courseIndex < totalCourses; semester++) {
            int coursesThisSemester = baseCoursesPerSemester;
            if (extraCourses > 0) {
                coursesThisSemester++;
                extraCourses--;
            }
            
            // Ensure we don't exceed max or go below min
            coursesThisSemester = Math.min(coursesThisSemester, MAX_COURSES_PER_SEMESTER);
            coursesThisSemester = Math.max(coursesThisSemester, MIN_COURSES_PER_SEMESTER);
            
            // Adjust for remaining courses
            int remainingCourses = totalCourses - courseIndex;
            int remainingSemesters = SEMESTERS - semester + 1;
            if (remainingCourses < remainingSemesters * MIN_COURSES_PER_SEMESTER) {
                // Not enough courses to fill minimum, adjust
                coursesThisSemester = Math.max(1, remainingCourses / remainingSemesters);
            }
            
            for (int j = 0; j < coursesThisSemester && courseIndex < totalCourses; j++) {
                int courseId = courseOrder.get(courseIndex);
                Course course = findCourseById(courses, courseId);
                if (course != null) {
                    semesterPlan.get(semester).add(course);
                }
                courseIndex++;
            }
        }
        
        return semesterPlan;
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