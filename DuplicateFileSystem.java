// package project;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

public class DuplicateFileSystem {
    public static void main(String[] args) {
        String folderPath = "D:\\test"; // Path to your folder
        try {
            removeDuplicateFiles(folderPath);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void removeDuplicateFiles(String folderPath) throws IOException, NoSuchAlgorithmException {
        Set<String> hashes = new HashSet<>();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.isDirectory()) { 
                    String fileHash = getFileHash(file);
                    if (!hashes.add(fileHash)) { 
                        if (file.delete()) {
                            System.out.println("Deleted duplicate file: " + file.getName());
                        } else {
                            System.out.println("Failed to delete duplicate file: " + file.getName());
                        }
                    }
                }
            }
        }
    }

    private static String getFileHash(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        InputStream is = new FileInputStream(file);
        byte[] buffer = new byte[8192];
        int read;
        while ((read = is.read(buffer)) > 0) {
            digest.update(buffer, 0, read);
        }
        byte[] hashBytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte hashByte : hashBytes) {
            sb.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
        }
        is.close();
        return sb.toString();
    }
}