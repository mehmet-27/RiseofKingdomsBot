package dev.mehmet27.rokbot;

public enum IMAGEPATHS {
    MAP_BUTTON("map_button.png", null),
    SCOUT_CAMP("scout_camp.png", null),
    SCOUT_BUTTON("scout_button.png", null),
    VILLAGES_BUTTON("villages_button.png", null),
    VILLAGES_GO_BUTTON("villages_go_button.png", null),
    SCOUT_SEND_BUTTON("scout_send_button.png", null),
    EXPLORE_BUTTON("explore_button.png", null),
    EXPLORE_BUTTON_2("explore_button_2.png", null),
    SCOUT_MANAGEMENT("scout_management.png", null),
    SCOUT_CAMP_EXIT_BUTTON("scout_camp_exit_button.png", null),
    ALLIANCE_BUTTON("alliance_button.png", "alliance_help.png"),
    ALLIANCE_HELP("alliance_help.png", null),
    ALLIANCE_HELP_BUTTON("alliance_help_button.png", null),
    RESOURCE_SEARCH_BUTTON("resource_search_button.png", null),
    RESOURCE_GATHER_BUTTON("resource_gather_button.png", null),
    HAS_MATCH_QUERY("has_match_query.png", null),
    HOME_BUTTON("home_button.png", null);

    private final String fileName;
    private final String checker;

    IMAGEPATHS(String fileName, String checker) {
        this.fileName = fileName;
        this.checker = checker;
    }

    public String getFileName() {
        return fileName;
    }

    public String getChecker() {
        return checker;
    }
}
