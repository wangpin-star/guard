package com.jinglun.guard.dataservice;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.ptr.IntByReference;

public interface CLibrary extends Library {

	CLibrary library = (CLibrary) Native.loadLibrary((Platform.isWindows() ? "dewlt_" + System.getProperty("sun.arch.data.model") : "c"), CLibrary.class);
	//loadLib(Platform.isWindows() ? "dewlt" : "c");
	//+ System.getProperty("sun.arch.data.model")
	/*(CLibrary) Native.loadLibrary((Platform.isWindows() ? "dewlt" : "c"), CLibrary.class);*/	
	
	int dewltbuf(byte[] strWlt, byte[] strBmp, IntByReference bmplen);
	
	int test(byte[] p);
	
	static CLibrary loadLib(String libName) {
		String systemType = System.getProperty("os.name");
		String libExtension = (systemType.toLowerCase().indexOf("win") != -1) ? ".dll" : ".so";

		String libFullName = libName + libExtension;

		String nativeTempDir = System.getProperty("java.io.tmpdir");

		InputStream in = null;
		BufferedInputStream reader = null;
		FileOutputStream writer = null;

		File extractedLibFile = new File(nativeTempDir + File.separator + libFullName);
		if (extractedLibFile.exists()) {
			extractedLibFile.delete();
		}
		
		try {
			in = CLibrary.class.getResourceAsStream("/" + libFullName);
			if (in == null)
				in = CLibrary.class.getResourceAsStream("/" + libFullName);
			//CLibrary.class.getResource(libFullName);
			reader = new BufferedInputStream(in);
			writer = new FileOutputStream(extractedLibFile);

			byte[] buffer = new byte[1024];

			while (reader.read(buffer) > 0) {
				writer.write(buffer);
				buffer = new byte[1024];
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return (CLibrary) Native.loadLibrary(extractedLibFile.toString(), CLibrary.class);
	}
}