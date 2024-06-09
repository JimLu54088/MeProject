package deleteFilesInFolder;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class DeleteFilesInFolder {

	public static void main(String[] args) {

		try {

			String folderPath = args[0];
			AtomicInteger deletedFileCount = new AtomicInteger(0);

			Files.walkFileTree(Paths.get(folderPath), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					System.out.println("Deleted file: " + file);
					deletedFileCount.incrementAndGet();
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
					// Handle the exception
					System.out.println("Error occured.");
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					// Do nothing for directories
					return FileVisitResult.CONTINUE;
				}
			});
			System.out.println(deletedFileCount.get() + " files deleted successfully.");
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
