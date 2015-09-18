package build.pluto.buildmonto;

import java.io.File;
import java.io.Serializable;

public class GACInput implements Serializable {
    private static final long serialVersionUID = -123258349893242L;
    public final File src;
    public final File target;

    public GACInput(File src, File target) {
        this.src = src;
        this.target = target;
    }
}
