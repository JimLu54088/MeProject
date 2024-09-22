package jp.co.jim.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FilenameUtils;

public class ZipUnzipUtil {

	public static final byte[] buffer = new byte[1024];
	public static final String GZ_FILE = "GZ";

	private ZipUnzipUtil() {
		// To prevent making instances of this class.
	}

	public static void unzipFile(String zipFilePath, String targetDir) throws IOException {
		String fileExtension = FilenameUtils.getExtension(zipFilePath);
		if (GZ_FILE.equals(fileExtension.toUpperCase())) {
			unzipGZFile(zipFilePath, targetDir);
		} else {
			unzipZipFile(zipFilePath, targetDir);
		}
	}

	public static void unzipGZFile(String zipFilePath, String targetDir) throws IOException {
		String inputFile = FilenameUtils.getBaseName(zipFilePath);
		String outputFile = targetDir + File.separator + inputFile;
		byte[] buffer = new byte[1024];
		GZIPInputStream gzis = null;
		FileOutputStream out = null;
		try {
			gzis = new GZIPInputStream(new FileInputStream(zipFilePath));
			out = new FileOutputStream(outputFile);
			int len;
			while ((len = gzis.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			try {
				gzis.close();
				out.close();
			} catch (IOException ioe) {
				throw ioe;
			}
		}
	}

	public static void unzipZipFile(String zipFilePath, String targetDir) throws IOException {
		try (ZipFile zipFile = new ZipFile(zipFilePath)) {
			Enumeration<? extends ZipEntry> entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.isDirectory()) {
					String destPath = targetDir + File.separator + entry.getName();
					File file = new File(destPath);
					file.mkdirs();
				} else {
					String destPath = targetDir + File.separator + entry.getName();
					try (InputStream inputStream = zipFile.getInputStream(entry);
							FileOutputStream outputStream = new FileOutputStream(destPath);
							BufferedOutputStream bufout = new BufferedOutputStream(outputStream);) {
						int read = 0;
						while ((read = inputStream.read(buffer)) != -1) {
							bufout.write(buffer, 0, read);
						}
					}

				}
			}
		}
	}

}
