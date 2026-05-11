package scheduler;

import java.util.*;
import model.Course;

/**
 * Schedules courses across semesters considering both topological order
 * and course level (1XX, 2XX, 3XX, 4XX)
 * Bonus feature: split between semesters across four years, with 1-3 courses per semester
 */
public class SemesterScheduler {
    private static final int SEMESTERS = 8; // 4 years * 2 semesters
    private static final int MIN_COURSES_PER_SEMESTER = 1;
    private static final int MAX_COURSES_PER_SEMESTER = 3;
    
    /**
     * Extracts the course level from the course code
     * @param courseCode e.g., "SDT 100", "SDT 250", etc.
     * @return level (1, 2, 3, or 4)
     */
    private int getCourseLevel(String courseCode) {
        try {
            // Extract the number part
            String[] parts = courseCode.split("\\s+");
            if (parts.length >= 2) {
                String numStr = parts[1].replaceAll("[^0-9]", "");
                if (!numStr.isEmpty()) {
                    int num = Integer.parseInt(numStr);
                    return num / 100; // 100-199 -> 1, 200-299 -> 2, etc.
                }
            }
        } catch (NumberFormatException e) {
            // Ignore and return default
        }
        return 1; // Default to level 1
    }
    
    /**
     * Distributes courses in topological order across semesters
     * considering course levels
     * @param courseOrder list of course IDs in topological order (prerequisites first)
     * @param courses list of all courses
     * @return map from semester number to list of courses
     */
    public Map<Integer, List<Course>> scheduleBySemester(List<Integer> courseOrder, List<Course> courses) {
        Map<Integer, List<Course>> semesterPlan = new LinkedHashMap<>();
        
        // Initialize empty semesters
        for (int i = 1; i <= SEMESTERS; i++) {
            semesterPlan.put(i, new ArrayList<>());
        }
        
        // Group courses by level
        Map<Integer, List<Course>> levelGroups = new LinkedHashMap<>();
        for (int i = 1; i <= 4; i++) {
            levelGroups.put(i, new ArrayList<>());
        }
        
        // Create a map of course ID to Course object for quick lookup
        Map<Integer, Course> idToCourse = new HashMap<>();
        for (Course course : courses) {
            idToCourse.put(course.getId(), course);
        }
        
        // Group courses by level while maintaining relative topological order
        for (int courseId : courseOrder) {
            Course course = idToCourse.get(courseId);
            if (course != null) {
                int level = getCourseLevel(course.getCode());
                level = Math.max(1, Math.min(4, level)); // Clamp between 1-4
                levelGroups.get(level).add(course);
            }
        }
        
        // Distribute courses by level to appropriate years
        // Level 1 -> Year 1 (semesters 1-2)
        // Level 2 -> Year 2 (semesters 3-4)
        // Level 3 -> Year 3 (semesters 5-6)
        // Level 4 -> Year 4 (semesters 7-8)
        
        for (int level = 1; level <= 4; level++) {
            List<Course> levelCourses = levelGroups.get(level);
            int startSemester = (level - 1) * 2 + 1;
            int endSemester = startSemester + 1;
            
            int coursesPerSemester = (int) Math.ceil((double) levelCourses.size() / 2);
            coursesPerSemester = Math.min(coursesPerSemester, MAX_COURSES_PER_SEMESTER);
            coursesPerSemester = Math.max(coursesPerSemester, MIN_COURSES_PER_SEMESTER);
            
            // If we have too many courses for this level, overflow to next year
            int totalSlots = MAX_COURSES_PER_SEMESTER * 2;
            if (levelCourses.size() > totalSlots) {
                // Split evenly, but cap at MAX
                coursesPerSemester = MAX_COURSES_PER_SEMESTER;
            }
            
            // Distribute courses across the two semesters of this year
            int courseIndex = 0;
            for (int sem = startSemester; sem <= endSemester && courseIndex < levelCourses.size(); sem++) {
                List<Course> semCourses = semesterPlan.get(sem);
                int slotsAvailable = MAX_COURSES_PER_SEMESTER - semCourses.size();
                int coursesToAdd = Math.min(coursesPerSemester, slotsAvailable);
                coursesToAdd = Math.min(coursesToAdd, levelCourses.size() - courseIndex);
                
                for (int j = 0; j < coursesToAdd && courseIndex < levelCourses.size(); j++) {
                    semCourses.add(levelCourses.get(courseIndex));
                    courseIndex++;
                }
            }
            
            // If there are remaining courses that couldn't fit, add to next year
            if (courseIndex < levelCourses.size()) {
                int nextYear = level + 1;
                if (nextYear <= 4) {
                    int overflowSem = nextYear * 2 - 1; // Fall semester of next year
                    for (int i = courseIndex; i < levelCourses.size(); i++) {
                        if (semesterPlan.get(overflowSem).size() < MAX_COURSES_PER_SEMESTER) {
                            semesterPlan.get(overflowSem).add(levelCourses.get(i));
                        } else {
                            semesterPlan.get(overflowSem + 1).add(levelCourses.get(i));
                        }
                    }
                }
            }
        }
        
        // Balance the semesters - move courses if some semesters are too full
        balanceSemesters(semesterPlan);
        
        return semesterPlan;
    }
    
    /**
     * Balances the course load across semesters
     * @param semesterPlan the semester plan to balance
     */
    private void balanceSemesters(Map<Integer, List<Course>> semesterPlan) {
        // Simple balancing: if any semester has more than MAX, move to adjacent semester
        for (int sem = 1; sem <= SEMESTERS; sem++) {
            List<Course> currentCourses = semesterPlan.get(sem);
            
            // If too many courses, move some to next semester
            if (currentCourses.size() > MAX_COURSES_PER_SEMESTER) {
                int overflow = currentCourses.size() - MAX_COURSES_PER_SEMESTER;
                for (int i = 0; i < overflow && sem < SEMESTERS; i++) {
                    // Move the last course to the next semester
                    Course moved = currentCourses.remove(currentCourses.size() - 1);
                    semesterPlan.get(sem + 1).add(0, moved); // Add at beginning to maintain some order
                }
            }
        }
        
        // Ensure no semester has more than MAX
        for (int sem = 1; sem <= SEMESTERS; sem++) {
            while (semesterPlan.get(sem).size() > MAX_COURSES_PER_SEMESTER) {
                Course moved = semesterPlan.get(sem).remove(semesterPlan.get(sem).size() - 1);
                if (sem < SEMESTERS) {
                    semesterPlan.get(sem + 1).add(0, moved);
                }
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