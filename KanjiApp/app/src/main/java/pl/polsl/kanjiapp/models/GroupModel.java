package pl.polsl.kanjiapp.models;

import java.util.ArrayList;

public class GroupModel {
    private String id, name, owner;
    private ArrayList<String> members;

    public GroupModel() {
    }

    public GroupModel(String ownerId, String name) {
        this.id = ownerId+name;
        this.name = name;
        this.owner = ownerId;
        this.members = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public ArrayList<String> getMembers() {
        return members;
    }
}
