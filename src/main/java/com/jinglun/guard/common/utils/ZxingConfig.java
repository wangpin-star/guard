package com.jinglun.guard.common.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;


public  class ZxingConfig {
	/** 
             * 设置 logo  
     * @param matrixImage 源二维码图片 
     * @return 返回带有logo的二维码图片 
     * @throws IOException 
     * @author Administrator sangwenhao 
     */  
     public BufferedImage logoMatrix(BufferedImage matrixImage,String topText,String bottomText,String imgPath) throws IOException{  
         /** 
          * 读取二维码图片，并构建绘图对象 
          */  
         Graphics2D g2 = matrixImage.createGraphics();  
         int matrixWidth = matrixImage.getWidth();    //图片宽300
         int matrixHeigh = matrixImage.getHeight();   //图片高400  
         /** 
                                    * 读取Logo图片 
          */  
       //  String imgPath="/logo/logo.png";
         BufferedImage logo = ImageIO.read(new FileInputStream(imgPath));  //logo图片
         //开始绘制图片  
         g2.drawImage(logo,matrixWidth/5*2,matrixHeigh/5*2, matrixWidth/5, matrixHeigh/7, null);//绘制       
         
     
         
         /** 设置生成图片的文字样式 * */ 
         Font font = new Font("思源黑体", Font.PLAIN, 20); //粗体+20号字
         g2.setFont(font); 
         g2.setPaint(Color.BLACK); 
         /** 设置字体在图片中的位置 在这里是居中* */ 
         FontRenderContext context = g2.getFontRenderContext();
         Rectangle2D bounds = font.getStringBounds(bottomText, context);  //底部文字矩形框(按照文字大小生成)
         Rectangle2D bound = font.getStringBounds(topText, context);
         
         double x1 =(matrixWidth - bound.getWidth()) / 2;               //居中显示
         double x =(matrixWidth - bounds.getWidth()) / 2;
         
         
         double y = (matrixHeigh - bounds.getHeight()); 
         double ascent = -bounds.getY(); 
         double baseY = y + ascent-5;
         double baseY1 = ascent+5;
         /** 防止生成的文字带有锯齿 * */ 
         g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
         /** 在图片上生成文字 * */ 
         g2.drawString(bottomText, (int) x, (int) baseY);
         g2.drawString(topText, (int) x1, (int) baseY1);
         
         //画一个白色圆弧，用于放置logo时背景色
         BasicStroke stroke = new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);   
         g2.setStroke(stroke);// 设置笔画对象  
         //指定弧度的圆角矩形  
         RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth/5*2, matrixHeigh/5*2, matrixWidth/5, matrixHeigh/7,20,20);  
         g2.setColor(Color.white);  
         g2.draw(round);// 绘制圆弧矩形  
         
         
         //设置logo边框 有一道灰色边框  
         BasicStroke stroke2 = new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);   
         g2.setStroke(stroke2);// 设置笔画对象  
         RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth/5*2, matrixHeigh/5*2, matrixWidth/5, matrixHeigh/7,20,20);  
         g2.setColor(new Color(128,128,128));  
         g2.draw(round2);// 绘制圆弧矩形  
           
         g2.dispose();  
         matrixImage.flush() ;  
         return matrixImage ;  
     } 
     
     /**
      * 
      * @param matrixImage
      * @param bottomText   设置底部文字
      * @return
      * @throws IOException
      */
     public BufferedImage BottomMatrix(BufferedImage matrixImage,String bottomText) throws IOException{  
         /** 
          * 读取二维码图片，并构建绘图对象 
          */  
         Graphics2D g2 = matrixImage.createGraphics();  
         int matrixWidth = matrixImage.getWidth();    //图片宽
         int matrixHeigh = matrixImage.getHeight();   //图片高 
       
     
         
         /** 设置生成图片的文字样式 * */ 
        // Font font = new Font("黑体", Font.ITALIC, 13); //粗体+14号字
         Font font = new Font("思源黑体", Font.PLAIN, 13);
         g2.setFont(font); 
         g2.setPaint(new Color(3,3,3)); 
         /** 设置字体在图片中的位置 在这里是居中* */ 
         FontRenderContext context = g2.getFontRenderContext();
         Rectangle2D bounds = font.getStringBounds(bottomText, context);  //底部文字矩形框(按照文字大小生成)

         double x =(matrixWidth - bounds.getWidth()) / 2;
         double y = (matrixHeigh - bounds.getHeight()); 
         double ascent = -bounds.getY(); 
         double baseY = y + ascent-1;
         /** 防止生成的文字带有锯齿 * */ 
         g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
         /** 在图片上生成文字 * */ 
         g2.drawString(bottomText, (int) x, (int) baseY);
      
         g2.dispose();  
         matrixImage.flush() ;  
         return matrixImage ;  
     }  
}
