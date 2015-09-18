package build.pluto.example;

import build.pluto.builder.BuildRequest;
import build.pluto.builder.Builder;
import build.pluto.builder.BuilderFactory;
import build.pluto.buildjava.JavaBuilder;
import build.pluto.buildjava.JavaInput;
import build.pluto.buildjava.util.FileExtensionFilter;

import build.pluto.output.None;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;
import org.sugarj.common.FileCommands;

public class GenerateAndCompile extends Builder<GACInput, None> {

    public static BuilderFactory<GACInput, None, GenerateAndCompile> factory
        = BuilderFactory.of(GenerateAndCompile.class, GACInput.class);

    public GenerateAndCompile(GACInput input) {
        super(input);
    }

    @Override
    protected File persistentPath(GACInput input) {
        return new File(input.src, "gac.dep");
    }

    @Override
    protected String description(GACInput input) {
        return "compile java";
    }

    @Override
    protected None build(GACInput input) throws Throwable {
        JavaFileGenerator.Input jfgInput = new JavaFileGenerator.Input(input.src);
        this.requireBuild(JavaFileGenerator.factory, jfgInput);
        List<BuildRequest<?, ?, ?, ?>> requiredUnits = new ArrayList();
        requiredUnits.add(
                new BuildRequest<>(JavaFileGenerator.factory, jfgInput));

        FileFilter javaFileFilter = new FileExtensionFilter("java");

        List<Path> javaSrcPathList =
            FileCommands.listFilesRecursive(input.src.toPath(), javaFileFilter);

        List<File> srcpath = Arrays.asList(new File(input.src, "src"));
        for (Path p : javaSrcPathList) {
            JavaInput javaInput = new JavaInput(
                    p.toFile(),
                    input.target,
                    srcpath,
                    null,
                    null,
                    requiredUnits);
            requireBuild(JavaBuilder.request(javaInput));
        }

        return None.val;
    }
}
