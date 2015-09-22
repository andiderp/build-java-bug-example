package build.pluto.example;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.sugarj.common.FileCommands;

import build.pluto.builder.BuildRequest;
import build.pluto.builder.Builder;
import build.pluto.builder.BuilderFactory;
import build.pluto.builder.BuilderFactoryFactory;
import build.pluto.buildjava.JavaBuilder;
import build.pluto.buildjava.JavaInput;
import build.pluto.buildjava.compiler.JavaCompiler;
import build.pluto.buildjava.compiler.JavacCompiler;
import build.pluto.buildjava.util.FileExtensionFilter;
import build.pluto.output.None;

public class GenerateAndCompile extends Builder<GACInput, None> {

    public static BuilderFactory<GACInput, None, GenerateAndCompile> factory
        = BuilderFactoryFactory.of(GenerateAndCompile.class, GACInput.class);

    public GenerateAndCompile(GACInput input) {
        super(input);
    }

    @Override
	public File persistentPath(GACInput input) {
        return new File(input.src, "gac.dep");
    }

    @Override
    protected String description(GACInput input) {
        return "compile java";
    }

    @Override
    protected None build(GACInput input) throws Throwable {
        FileFilter javaFileFilter = new FileExtensionFilter("java");

        List<Path> javaSrcPathList =
            FileCommands.listFilesRecursive(input.src.toPath(), javaFileFilter);

        List<File> classPath = Arrays.asList(new File("lib/jeromq-0.3.4.jar"), new File("lib/json-simple-1.1.1.jar"));
        List<File> srcpath = Arrays.asList(new File(input.src, "src"));
        for (Path p : javaSrcPathList) {
			JavaInput javaInput = new JavaInput(
					p.toFile(), 
					input.target,
					srcpath, 
					classPath, 
					null,
					null,
					JavacCompiler.instance);

            requireBuild(JavaBuilder.request(javaInput));
        }

        return None.val;
    }
}
