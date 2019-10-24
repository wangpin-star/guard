package com.jinglun.guard.common.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;

import com.google.zxing.common.BitMatrix;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class Zxing {
	private static final int BLACK = 0x00458E;//用于设置图案的颜色  
    private static final int WHITE = 0xFFFFFF; //用于背景色  

	  private Zxing() {}
	  public static BufferedImage toBufferedImage(BitMatrix matrix) {  
	        int width = matrix.getWidth();  
	        int height = matrix.getHeight();  
	        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
	        for (int x = 0; x < width; x++) {  
	            for (int y = 0; y < height; y++) {  
	                image.setRGB(x, y,  (matrix.get(x, y) ? BLACK : WHITE)); 
	            }  
	        }  
	        return image;  
	    }  
	  
	    public static void writeToFile(BitMatrix matrix, String format, File file,String topText,String bottomText,String imgPath) throws IOException {  
	        BufferedImage image = toBufferedImage(matrix);  
	        //设置logo图标  
	        ZxingConfig logoConfig = new ZxingConfig();  
	        image = logoConfig.logoMatrix(image,topText,bottomText,imgPath);  
	          
	        if (!ImageIO.write(image, format, file)) {
	        	log.error("Could not write an image of format " + format + " to " + file);
	        }else{  
	            System.out.println("图片生成成功！");  
	        }  
	    }  
	    
	    /***
	     * 
	     * @param matrix              
	     * @param format           图片格式
	     * @param stream           图片流
	     * @param bottom           底部文字
	     * @throws IOException
	     */
	    public static void writeToStream(BitMatrix matrix, String format, OutputStream stream,String bottom) throws IOException {  
	    	 //不设置logo图标 
	    	BufferedImage image = toBufferedImage(matrix);   
	        ZxingConfig logoConfig = new ZxingConfig();  
	        image = logoConfig.BottomMatrix(image, bottom); 
	        if (!ImageIO.write(image, format, stream)) {  
	        	log.error("Could not write an image of format " + format);  
	        }  
	    }  
	    
	    
	    public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {  
	    	 //不设置logo图标 
	    	BufferedImage image = toBufferedImage(matrix);  
	      /*  //设置logo图标  
	        ZxingConfig logoConfig = new ZxingConfig();  
	        image = logoConfig.ButtomMatrix(image, bottomText); */
	        if (!ImageIO.write(image, format, stream)) {  
	            throw new IOException("Could not write an image of format " + format);  
	        }  
	    }  
}
