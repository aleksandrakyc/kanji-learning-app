package pl.polsl.kanjiapp.models;

import java.util.ArrayList;

public class UserModel {
    private String id, email;
    private ArrayList<String> groups;
    private boolean isAdmin, isTeacher, teacherRequest;

    public UserModel() {
    }

    public UserModel(String id, String email, ArrayList<String> groups, boolean isAdmin, boolean isTeacher, boolean teacherRequest) {
        this.id = id;
        this.email = email;
        this.groups = groups;
        this.isAdmin = isAdmin;
        this.isTeacher = isTeacher;
        this.teacherRequest = teacherRequest;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public boolean isIsTeacher() {
        return isTeacher;
    }

    public boolean isTeacherRequest() {
        return teacherRequest;
    }
}
