package com.jinglun.guard.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

public class FileUtils {
	public void deleteFile(File deleteFile)
	{
		if(deleteFile.isDirectory())
		{
			String[] child=deleteFile.list();
			if(null!=child) {
				for(int i=0;i<child.length;i++)
				{
					deleteFile(new File(deleteFile, child[i]));
				}
			}
		}
		deleteFile.delete();
	}
	public void zipFilesDiGui(String source,String destinct) {
		List<Object> fileList=loadFilename(new File(source));
		try {
		ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(new File(destinct)));
		byte[] buffere=new byte[8192];
		int length;
		BufferedInputStream bis;
		for(int i=0;i<fileList.size();i++) {
		           File file=(File) fileList.get(i);
		           zos.putNextEntry(new ZipEntry(getEntryName(source,file)));
		           bis=new BufferedInputStream(new FileInputStream(file));
		           
		           while(true) {
		               length=bis.read(buffere);
		               if(length==-1) break;
		               zos.write(buffere,0,length);
		           }
		           bis.close();
		           zos.closeEntry();
		       }
		       zos.close();
		} catch (Exception e) {
		e.printStackTrace();
		}
	}
	private String getEntryName(String base, File file) {
		File baseFile=new File(base);
		        String filename=file.getPath();
		        if(baseFile.getParentFile().getParentFile()==null)
		        return filename.substring(baseFile.getParent().length());
		        return filename.substring(baseFile.getParent().length()+1);
		}
	private List<Object> loadFilename(File file) {
		List<Object> filenameList=new ArrayList<Object>();
		if(null!=filenameList) {
	        if(file.isFile()) {
	            filenameList.add(file);
	        }
	        if(file.isDirectory()) {
	            for(File f:file.listFiles()) {
	                filenameList.addAll(loadFilename(f));
	            }
	        }
	      }
		
		        return filenameList;
		}
		public void downFile(HttpServletResponse response,String serverPath, String str) {    
		        try {    
		            String path = serverPath +"\\"+ str;    
		            System.out.println(path);
		            File file = new File(path);    
		            if (file.exists()) {    
		                InputStream ins = new FileInputStream(path);    
		                BufferedInputStream bins = new BufferedInputStream(ins);// 放到缓冲流里面    
		                OutputStream outs = response.getOutputStream();// 获取文件输出IO流    
		                BufferedOutputStream bouts = new BufferedOutputStream(outs);    
		                response.setContentType("application/x-download");// 设置response内容的类型    
		                response.setHeader(    
		                        "Content-disposition",    
		                        "attachment;filename="    
		                                + URLEncoder.encode(str, "GBK"));// 设置头部信息    
		                int bytesRead = 0;    
		                byte[] buffer = new byte[8192];    
		                 //开始向网络传输文件流    
		                while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {    
		                   bouts.write(buffer, 0, bytesRead);    
		               }    
		               bouts.flush();// 这里一定要调用flush()方法    
		                ins.close();    
		                bins.close();    
		                outs.close();    
		                bouts.close();    
		            } else {    
		                System.out.println("没有找到文件哎！");
		            }    
		        } catch (IOException e) {    
		            e.printStackTrace();  
		        }    
		    }  
}
