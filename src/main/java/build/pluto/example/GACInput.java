package build.pluto.example;

import java.io.File;
import java.io.Serializable;

public class GACInput implements Serializable {
    public final File src;
    public final File target;

    public GACInput(File src, File target) {
        this.src = src;
        this.target = target;
    }
}
