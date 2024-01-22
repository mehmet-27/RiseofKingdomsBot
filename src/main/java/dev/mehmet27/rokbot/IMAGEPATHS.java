package dev.mehmet27.rokbot;

public enum IMAGEPATHS {
    MAP_BUTTON("map_button.png"),
    SCOUT_CAMP("scout_camp.png"),
    SCOUT_BUTTON("scout_button.png"),
    VILLAGES_BUTTON("villages_button.png"),
    VILLAGES_GO_BUTTON("villages_go_button.png"),
    SCOUT_SEND_BUTTON("scout_send_button.png"),
    EXPLORE_BUTTON("explore_button.png"),
    EXPLORE_BUTTON_2("explore_button_2.png"),
    SCOUT_MANAGEMENT("scout_management.png"),
    HOME_BUTTON("home_button.png");

    private String fileName;
    IMAGEPATHS(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
