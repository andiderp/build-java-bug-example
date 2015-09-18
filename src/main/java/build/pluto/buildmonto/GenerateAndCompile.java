package build.pluto.buildmonto;

import build.pluto.builder.BuildRequest;
import build.pluto.builder.Builder;
import build.pluto.builder.BuilderFactory;
import build.pluto.buildgit.GitInput;
import build.pluto.buildgit.GitRemoteSynchronizer;
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
        return new File("gac.dep");
    }

    @Override
    protected String description(GACInput input) {
        return "compile java";
    }

    @Override
    protected None build(GACInput input) throws Throwable {

        GitInput gitInput = new GitInput.Builder(input.src, "https://github.com/andiderp/cycle.git").build();
        this.requireBuild(GitRemoteSynchronizer.factory, gitInput);

        List<BuildRequest<?, ?, ?, ?>> requiredUnits = new ArrayList<>();
        requiredUnits.add(
                new BuildRequest(
                    GitRemoteSynchronizer.factory,
                    gitInput));

        FileFilter javaFileFilter = new FileExtensionFilter("java");

        List<Path> javaSrcPathList =
            FileCommands.listFilesRecursive(input.src.toPath(), javaFileFilter);

        List<File> sourcePath =
            Arrays.asList(new File(input.src, "src"));
        for (Path p : javaSrcPathList) {
            JavaInput javaInput = new JavaInput(
                    p.toFile(),
                    input.target,
                    sourcePath,
                    null,
                    null,
                    requiredUnits);
            requireBuild(JavaBuilder.request(javaInput));
        }

        return None.val;
    }
}
