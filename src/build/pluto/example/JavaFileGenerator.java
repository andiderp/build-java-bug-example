package build.pluto.example;

import build.pluto.builder.BuildRequest;
import build.pluto.builder.Builder;
import build.pluto.builder.BuilderFactory;
import build.pluto.builder.BuilderFactoryFactory;
import build.pluto.output.None;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sugarj.common.FileCommands;

public class JavaFileGenerator extends Builder<JavaFileGenerator.Input, None> {

    public static class Input implements Serializable {
        public final File location;

        public Input(File location) {
            this.location = location;
        }
    }

    public static BuilderFactory<Input, None, JavaFileGenerator> factory
        = BuilderFactoryFactory.of(JavaFileGenerator.class, JavaFileGenerator.Input.class);

    public JavaFileGenerator(Input input) {
        super(input);
    }

    @Override
	public File persistentPath(Input input) {
        return new File(input.location, "jfg.dep");
    }

    @Override
    protected String description(Input input) {
        return "generate java";
    }

    @Override
    protected None build(Input input) throws Throwable {
        String fileOneContent = "package root;\npublic class One{public Two other;}\n";
        String fileTwoContent = "package root;\npublic class Two{public One other;}\n";
        File srcDIR = new File(input.location, "src");
        File packageDIR = new File(srcDIR, "root");
        File fileOne = new File(packageDIR, "One.java");
        File fileTwo = new File(packageDIR, "Two.java");
        FileCommands.createDir(srcDIR.toPath());
        FileCommands.createDir(packageDIR.toPath());
        FileCommands.createFile(fileOne);
        FileCommands.createFile(fileTwo);
        FileCommands.writeToFile(fileOne, fileOneContent);
        FileCommands.writeToFile(fileTwo, fileTwoContent);
        this.provide(fileOne);
        this.provide(fileTwo);
        return None.val;
    }
}
