package build.pluto.example;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import build.pluto.builder.BuildManagers;
import build.pluto.builder.BuildRequest;

public class GACInput implements Serializable {
    public final File src;
    public final File target;

    public GACInput(File src, File target) {
        this.src = src;
        this.target = target;
    }
    
    public static void main(String[] args) throws IOException {
    	GACInput input = new GACInput(new File("srcfiles"), new File("targetclass"));
    	BuildManagers.build(new BuildRequest<>(GenerateAndCompile.factory, input));
	}
}
