package it.polimi.ingsw.Model;

/**
 * @author Giuseppe Bonanno
 * The enum of the towers with the relative image that will be used for the GUI
 */
public enum Color {
    PINK("student_pink.png","teacher_pink.png"),
    RED("student_red.png","teacher_red.png"),
    YELLOW("student_yellow.png","teacher_yellow.png"),

    BLUE("student_blue.png","teacher_blue.png"),
    GREEN("student_green.png","teacher_green.png");

    private String fileStudent;
    private String fileTeacher;

    public String getFileStudent() {
        return fileStudent;
    }

    public String getFileTeacher() {
        return fileTeacher;
    }

    Color(String fileStudent, String fileTeacher) {
        this.fileStudent = fileStudent;
        this.fileTeacher = fileTeacher;
    }
}
