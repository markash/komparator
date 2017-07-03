package io.threesixty.kt.filesystem;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * @author Mark P Ashworth
 */
public class TestFileSystem {

    @Test
    public void testIterator() throws IOException {
//        List<Path> paths = new ArrayList<>();
//        blah(paths, Paths.get("C:\\SDE"), 3, "*.java");
//
//        paths.forEach(System.out::println);

        Set<FileVisitOption> options = new HashSet<>();
        options.add(FileVisitOption.FOLLOW_LINKS);
        Files.walkFileTree(Paths.get("C:\\SDE"), options, 3, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);
                return super.visitFile(file, attrs);
            }
        });
    }

//    private void blah(final List<Path> paths, final Path parent, final int level, final String glob) throws IOException {
//
//        try(DirectoryStream<Path> stream = Files.newDirectoryStream(parent)) {
//            for(Path path : stream) {
//                if (path.toFile().isDirectory() && level > 0) {
//                    blah(paths, path, level - 1, glob);
//                } else if (path){
//                    paths.add(path);
//                }
//            }
//        }
//    }
}
