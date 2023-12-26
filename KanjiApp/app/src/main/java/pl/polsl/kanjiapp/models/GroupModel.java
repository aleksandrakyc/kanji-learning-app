package pl.polsl.kanjiapp.models;

public class GroupModel {
    private String id, name, groupId;

    public GroupModel() {
    }

    public GroupModel(String id, String name, String groupId) {
        this.id = id;
        this.name = name;
        this.groupId = groupId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGroupId() {
        return groupId;
    }
}
