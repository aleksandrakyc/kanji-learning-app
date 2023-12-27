package pl.polsl.kanjiapp.models;

import java.util.HashMap;

public class SetModel {
    private String id, owner, name, groupId; //optional
    private HashMap<String, Double> kanjiInfo;

    public SetModel() {
    }

    public SetModel(String id, String owner, String name, HashMap<String, Double> kanjiInfo) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.kanjiInfo = kanjiInfo;
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Double> getKanjiInfo() {
        return kanjiInfo;
    }

    public void setKanjiInfo(HashMap<String, Double> kanjiInfo) {
        this.kanjiInfo = kanjiInfo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "SetModel{" +
                "id='" + id + '\'' +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", groupId='" + groupId + '\'' +
                ", kanjiInfo=" + kanjiInfo +
                '}';
    }
}
