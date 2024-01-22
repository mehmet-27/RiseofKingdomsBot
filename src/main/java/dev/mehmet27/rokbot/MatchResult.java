package dev.mehmet27.rokbot;

public class MatchResult {

    private final boolean isMatch;
    private final LocXY loc;

    public MatchResult(boolean isMatch, LocXY loc) {
        this.isMatch = isMatch;
        this.loc = loc;
    }

    public boolean isMatch() {
        return isMatch;
    }

    public LocXY getLoc() {
        return loc;
    }
}
