package him.sniffer.config;

import com.google.gson.annotations.SerializedName;

import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Targets {
    @SerializedName("comment")
    private final String comment;
    @SerializedName("version")
    private final String version;
    @SerializedName("targets")
    private final Set<Target> targets;

    public Targets() {
        comment = "targets";
        version = "1.7.10";
        targets = new HashSet<Target>();
        targets.add(new Target("coal_ore", "#000000"));
        targets.add(new Target("iron_ore", Color.PINK));
    }

    public Iterator<Target> iterator() {
        return targets.iterator();
    }
}
