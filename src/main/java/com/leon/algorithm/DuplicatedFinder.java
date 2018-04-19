package com.leon.algorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

//TODO: 4/19/2018 Write unit tests
/**
 * @see <a href="https://www.interviewcake.com/question/java/find-duplicate-files">Source</a>
 * We've made a few assumptions here:
 * 1. Two different files won't have the same fingerprints.
 * 2. The most recently edited file is the duplicate.
 * 3. Two files with the same contents are the same file. (empty files)
 * Some ideas for further improvements:
 * 1. If a file wasn't last edited or accessed around the time your friend got a hold of your computer, you know it probably wasn't created by your friend.
 * 2. Make the file size the fingerprint. Take hash-based fingerprint only on the files which have matching sizes. Fully compare file contents if they have the same hash.
 * 3. Some file systems also keep track of when a file was created.
 * 4. When do compare full file contents to ensure two files are the same, no need to read the entire files into memory.
 * O(n) time and O(n) space where n is the number of files on the file system.
 * The main insight was to save time and space by 'fingerprinting' each file.
 * A 'messy' interview problem => Instead of one optimal solution, there's a big knot of optimizations and trade-offs.
 * Focus on clearly explaining to your interviewer what the trade-offs are for each decision you make.
 */
public class DuplicatedFinder {

    private static final int SAMPLE_SIZE = 4000;

    static class FilePath {

        private Path duplicatedPath;

        private Path originalPath;

        FilePath(Path duplicatedPath, Path originalPath) {
            this.duplicatedPath = duplicatedPath;
            this.originalPath = originalPath;
        }

        Path getDuplicatedPath() {
            return duplicatedPath;
        }

        Path getOriginalPath() {
            return originalPath;
        }

        @Override
        public String toString() {
            return String.format("(duplicated: %s, original: %s", duplicatedPath, originalPath);
        }

    }

    static class FileInfo {

        private long lastEdited;

        private Path path;

        FileInfo(long lastEdited, Path path) {
            this.lastEdited = lastEdited;
            this.path = path;
        }

    }

    public static List<FilePath> findDuplicatedFiles(Path startingDirectory) {
        Map<String, FileInfo> filesSeenAlready = new HashMap<>();
        Stack<Path> stack = new Stack<>();
        stack.push(startingDirectory);

        List<FilePath> duplicated = new ArrayList<>();

        while (!stack.isEmpty()) {
            Path currentPath = stack.pop();
            File currentFile = new File(currentPath.toString());

            // if it's a directory, put the contents in our stack
            if (currentFile.isDirectory()) {
                for (File file : Objects.requireNonNull(currentFile.listFiles())) {
                    stack.push(file.toPath());
                }
                // if it's a file
            } else {
                // get its hash
                String fileHash;
                try {
                    fileHash = sampleHashFile(currentPath);
                } catch (NoSuchAlgorithmException | IOException e) {
                    // show error and skip this file
                    e.printStackTrace();
                    continue;
                }

                // get its last edited time
                long currentLastEdited = currentFile.lastModified();

                // if we've seen it before
                if (filesSeenAlready.containsKey(fileHash)) {
                    FileInfo fileInfo = filesSeenAlready.get(fileHash);
                    long existingLastEdited = fileInfo.lastEdited;
                    Path existingPath = fileInfo.path;

                    if (currentLastEdited > existingLastEdited) {
                        // current file is the dupe!
                        duplicated.add(new FilePath(currentPath, existingPath));
                    } else {
                        // old file is the dupe!
                        duplicated.add(new FilePath(existingPath, currentPath));
                        // but also update filesSeenAlready to have the new file's info
                        filesSeenAlready.put(fileHash, new FileInfo(currentLastEdited, currentPath));
                    }
                    // if it's a new file, throw it in filesSeenAlready and record its path and last edited time, so we can tell later if it's a dupe
                } else {
                    filesSeenAlready.put(fileHash, new FileInfo(currentLastEdited, currentPath));
                }
            }
        }

        return duplicated;
    }

    private static String sampleHashFile(Path currentPath) throws IOException, NoSuchAlgorithmException {
        final long totalBytes = new File(currentPath.toString()).length();
        try (InputStream inputStream = new FileInputStream(currentPath.toString())) {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            DigestInputStream digestInputStream = new DigestInputStream(inputStream, digest);

            // if the file is too short to take 3 samples, hash the entire file
            if (totalBytes < SAMPLE_SIZE * 3) {
                byte[] bytes = new byte[(int) totalBytes];
                digestInputStream.read(bytes);
            } else {
                byte[] bytes = new byte[SAMPLE_SIZE * 3];
                long numOfBytesBetweenSamples = (totalBytes - SAMPLE_SIZE * 3) / 2;

                // read first, middle and last bytes
                for (int i = 0; i < 3; i++) {
                    digestInputStream.read(bytes, i * SAMPLE_SIZE, SAMPLE_SIZE);
                    digestInputStream.skip(numOfBytesBetweenSamples);
                }
            }

            return new BigInteger(1, digest.digest()).toString(16);
        }
    }

}
