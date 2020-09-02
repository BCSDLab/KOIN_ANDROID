package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.SerializedName;

public class Semester {
    @SerializedName("id")
    private int id;

    @SerializedName("semester")
    private String semester;

    private boolean isSelected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
