package com.jinglun.guard.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

public class ZipUtil {
	public static void compress(ZipOutputStream zos, BufferedOutputStream bos, File sourceFile, String base) throws Exception {
		// 如果路径为目录（文件夹）
		if (sourceFile.isDirectory()) {

			// 取出文件夹中的文件（或子文件夹）
			File[] flist = sourceFile.listFiles();
			System.out.println(flist.length);
			// if (flist!=null) {
			if (flist.length == 0)// 如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
			{
				System.out.println(base + "/");
				zos.putNextEntry(new ZipEntry(base + "//"));
			} else// 如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
			{
				for (int i = 0; i < flist.length; i++) {
					compress(zos, bos, flist[i], base + "/" + flist[i].getName());
				}
				// }
			}

		} else// 如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
		{
			zos.putNextEntry(new ZipEntry(base));
			FileInputStream fos = new FileInputStream(sourceFile);
			BufferedInputStream bis = new BufferedInputStream(fos);

			int tag;
			System.out.println(base);
			// 将源文件写入到zip文件中
			while ((tag = bis.read()) != -1) {
				zos.write(tag);
			}
			bis.close();
			fos.close();

		}
	}

	/**
	 * 解压文件
	 * @param zipFile 目标文件
	 * @param descDir 指定解压目录
	 * @param urlList 存放解压后的文件目录（可选）
	 * @return
	 */
	public static String unZip(File zipFile, String descDir,  List<String> urlList) {
		String firstDirName = "";
		boolean flag = true;
	    File pathFile = new File(descDir);
	    if(!pathFile.exists()){
	        pathFile.mkdirs();
	    }
	    ZipFile zip = null;
	    try {
	        //指定编码，否则压缩包里面不能有中文目录
	        zip = new ZipFile(zipFile, Charset.forName("gbk"));
	        for(Enumeration entries = zip.entries(); entries.hasMoreElements();){
	            ZipEntry entry = (ZipEntry)entries.nextElement();
	            String zipEntryName = entry.getName();
	            if (flag) {
	            	firstDirName = zipEntryName;
	            	flag = false;
				}
	            InputStream in = zip.getInputStream(entry);
	            String outPath = (descDir+zipEntryName).replace("/", File.separator);
	            //判断路径是否存在,不存在则创建文件路径
	            File file = new File(outPath.substring(0, outPath.lastIndexOf(File.separator)));
	            if(!file.exists()){
	                file.mkdirs();
	            }
	            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
	            if(new File(outPath).isDirectory()){
	                continue;
	            }
	            //保存文件路径信息
	            urlList.add(outPath);

	            OutputStream out = new FileOutputStream(outPath);
	            byte[] buf1 = new byte[2048];
	            int len;
	            while((len=in.read(buf1))>0){
	                out.write(buf1,0,len);
	            }
	            in.close();
	            out.close();
	        }
	        //必须关闭，否则无法删除该zip文件
	        zip.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return firstDirName;
	}
}
