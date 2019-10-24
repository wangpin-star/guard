package com.jinglun.guard.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.springframework.util.FileCopyUtils;

public class ManifestUtils {
	private static final String MANIFEST_DIRECTORY_LOCATION = "META-INF" + File.separator + "MANIFEST.MF";

	private static final String MANIFEST_ENTRY = "META-INF/MANIFEST.MF";

	/**
	 * 
	 * Creates a {@link Reader} for the manifest in the supplied exploded JAR
	 * directory.
	 * 
	 * 
	 * 
	 * @param directory
	 *            the exploded JAR directory.
	 * 
	 * @return the <code>Reader</code> or <code>null</code> if the manifest
	 *         cannot be found.
	 */
	public static final Reader manifestReaderFromExplodedDirectory(File directory) {
		if (directory == null || !directory.isDirectory()) {
			throw new IllegalArgumentException("Must supply a valid directory");
		}
		try {
			File manifestFile = new File(directory.getAbsolutePath() + File.separator + MANIFEST_DIRECTORY_LOCATION);
			if (manifestFile.exists()) {
				return new FileReader(manifestFile);
			} else {
				return null;
			}
		} catch (IOException e) {
			throw new RuntimeException(
					"Unable to read MANIFEST for exploded directory '" + directory.getAbsolutePath() + "'.", e);
		}
	}

	/**
	 * 
	 * Creates a {@link Reader} for the manifest in the supplied JAR file.
	 * 
	 * 
	 * 
	 * @param file
	 *            the JAR file.
	 * 
	 * @return the <code>Reader</code> or <code>null</code> if the manifest
	 *         cannot be found.
	 */
	public static final Reader manifestReaderFromJar(File file) {
		JarFile jar = null;
		try {
			jar = new JarFile(file);
			JarEntry entry = jar.getJarEntry(MANIFEST_ENTRY);
			if (entry != null) {
				StringWriter writer = new StringWriter();
				FileCopyUtils.copy(new InputStreamReader(jar.getInputStream(entry)), writer);
				jar.close();
				return new StringReader(writer.toString());
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException("Cannot read MANIFEST.MF from jar '" + file.getAbsolutePath() + "'.", e);
		} finally {
			if (jar != null) {
				try {
					jar.close();
				} catch (IOException ioe) {
					throw new RuntimeException("Failed to close jar '" + file.getAbsolutePath() + "'.", ioe);
				}
			}
		}
	}

	/**
	 * 从META-INF/MANIFEST.MF文件中读取“deploy-version”发布版本号信息。
	 * 
	 * @param realPath
	 *            web发布真实路径
	 * @return
	 */
	public static String getDeployVersion(String realPath) {
		File file = new File(realPath);
//		Reader reader = manifestReaderFromExplodedDirectory(file);
		Reader reader = manifestReaderFromJar(file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		String lineTxt = null;
		String deployVersion = "";
		try {
			while ((lineTxt = bufferedReader.readLine()) != null) {
				if (lineTxt.startsWith("deploy-version:")) {
					deployVersion = lineTxt.replace("deploy-version:", "");
					return deployVersion;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != bufferedReader) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
				}
			}
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return deployVersion;
	}
}
