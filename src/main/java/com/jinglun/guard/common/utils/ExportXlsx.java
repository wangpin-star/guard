package com.jinglun.guard.common.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.creditcities.crm.invest.viewModel.InvestApplyBean;

public class ExportXlsx<T> {

    public ByteArrayInputStream exportXlsx(List<String> sheetNames, Map<String, String[]> headers, Map<String, List<T>> dataset,
                                           String pattern, Map<String, String[]> headernums, Map<String, int[]> colWidths) throws Exception {
        XSSFWorkbook workBook = new XSSFWorkbook();
        for (String sheetName : sheetNames) {
            List<T> dataList = dataset.get(sheetName);
            String[] header = headers.get(sheetName);
            String[] headernum = headernums.get(sheetName);
            int[] colWidth = colWidths.get(sheetName);
            createSheet(workBook, sheetName, header, dataList, pattern, headernum, colWidth);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workBook.write(baos); // XSSFWorkbook here
        ByteArrayInputStream bias = new ByteArrayInputStream(baos.toByteArray());
        return bias;
    }

    @SuppressWarnings("rawtypes")
    private void createSheet(XSSFWorkbook workBook, String title, String[] headers, Collection<T> dataset,
                             String pattern, String[] headernum, int[] colWidths) throws Exception {
        XSSFSheet sheet = workBook.createSheet(title);
        // 设置每列宽度
        if (colWidths != null && colWidths.length > 0) {
            for (int i = 0; i < colWidths.length; i++) {
                sheet.setColumnWidth(i, colWidths[i]);
            }
        }
        XSSFCellStyle style = workBook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor((short) 40);
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        XSSFFont font = workBook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);

        XSSFRow row0 = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++) {
            cteateCell(row0, i, headers[i], style);
        }
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            XSSFRow row = sheet.createRow(index);
            T t = (T) it.next();
            if (t instanceof Map) {
                for (short j = 0; j < headernum.length; j++) {
                    addCell(row, j, pattern, ((Map) t).get(headernum[j]), workBook);
                }
            } else {
                // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
                Field[] fields = t.getClass().getDeclaredFields();
                for (short i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    String fieldName = field.getName();
                    short number = -1;// 判断当前属性在第几列显示
                    for (short j = 0; j < headernum.length; j++) {
                        String tmp = headernum[j];
                        if (fieldName.equals(tmp)) {
                            number = j;
                            break;
                        }
                    }
                    // 如果没有该列则进行下次循环，不现实该属性
                    if (number == -1) {
                        continue;
                    }
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    try {
                        Class tCls = t.getClass();
                        @SuppressWarnings("unchecked")
                        Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                        Object value = getMethod.invoke(t, new Object[]{});
                        addCell(row, number, pattern, value, workBook);
                    } catch (Exception e) {
                        System.out.println("e=" + e.toString());
                    } finally {
                    }
                }
            }
        }
    }

    /**
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
     *
     * @param title     表格标题名
     * @param headers   表格属性列名数组
     * @param dataset   需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                  <p/>
     *                  javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
     * @param out       与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern   如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     * @param headernum 属性的现实顺序
     * @param colWidths 属性对应的列的宽度
     */
    public ByteArrayInputStream exportXlsx(String title, String[] headers, Collection<T> dataset, String pattern,
                                           String[] headernum, Integer[] colWidths) throws Exception {
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet(title);
        //设置每列宽度
        if (colWidths != null && colWidths.length > 0) {
            for (int i = 0; i < colWidths.length; i++) {
                sheet.setColumnWidth(i, colWidths[i]);
            }
        }
        XSSFCellStyle style = workBook.createCellStyle();

        // 设置这些样式
        style.setFillForegroundColor((short) 40);
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        XSSFFont font = workBook.createFont();
//		font.setColor((short) 40);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);

        XSSFRow row0 = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++) {
            cteateCell(row0, i, headers[i], style);
            //System.out.println(headers[i]);
        }
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            XSSFRow row = sheet.createRow(index);
            T t = (T) it.next();
            if (t instanceof Map) {
                for (short j = 0; j < headernum.length; j++) {
                    addCell(row, j, pattern, ((Map) t).get(headernum[j]), workBook);
                }

            } else {
                // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
                Field[] fields = t.getClass().getDeclaredFields();
                for (short i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    String fieldName = field.getName();
                    short number = -1;// 判断当前属性在第几列显示
                    for (short j = 0; j < headernum.length; j++) {
                        String tmp = headernum[j];
                        if (fieldName.equals(tmp)) {
                            number = j;
                            break;
                        }
                    }
                    // 如果没有该列则进行下次循环，不现实该属性
                    if (number == -1) {
                        continue;
                    }
//				XSSFCell cell = row.createCell(number);
//				cell.setCellStyle(style2);

                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    try {
                        Class tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                        Object value = getMethod.invoke(t, new Object[]{});
                        addCell(row, number, pattern, value, workBook);
                    } catch (Exception e) {
                        System.out.println("e1=" + e.toString());
                        e.printStackTrace();
                    } finally {
                    }
                }
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workBook.write(baos); // XSSFWorkbook here
        ByteArrayInputStream bias = new ByteArrayInputStream(baos.toByteArray());
        return bias;
    }

    private void cteateCell(XSSFRow row, int col, String value, XSSFCellStyle style) {
        //创建单元格 
        XSSFCell cell = row.createCell(col);
        //给单元格赋值 
        cell.setCellStyle(style);
        cell.setCellValue(value);
    }

    private void addCell(XSSFRow row, int col, String patern, Object value, XSSFWorkbook workBook) throws Exception {
        // 创建单元格
        XSSFCell cell = row.createCell(col);
        String textValue = null;
        if (value == null) {
            textValue = "";
        } else if (value instanceof Boolean) {
            boolean bValue = (Boolean) value;
            textValue = "是";
            if (!bValue) {
                textValue = "否";
            }

        } else if (value instanceof Date) {
            Date date = (Date) value;
            SimpleDateFormat sdf = new SimpleDateFormat(patern);
            textValue = sdf.format(date);
            // System.out.println(textValue);
        } else if (value instanceof Number) {
            //	textValue=new DecimalFormat("#.##").format(ArithUtil.round(((Number)value).doubleValue(),2));
            XSSFCellStyle cellstyle = workBook.createCellStyle();
            short f = workBook.createDataFormat().getFormat("0.00;\\-0.00;0;General");
            short s = 0;
            cellstyle.setDataFormat(s);
            cell.setCellStyle(cellstyle);
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            if (((Number) value).doubleValue() > 9000000000d) {
                cellstyle.setDataFormat(workBook.createDataFormat().getFormat("0.00;\\-0.00;0;General"));
            }
            cell.setCellValue(((Number) value).doubleValue());
            cellstyle.setAlignment(HorizontalAlignment.RIGHT);
            cell.setCellStyle(cellstyle);
            return;
        } else {
            textValue = value.toString();
        }
        /*
		 * else if (value instanceof byte[]) {
		 * 
		 * // 有图片时，设置行高为60px;
		 * 
		 * row.setHeightInPoints(60);
		 * 
		 * // 设置图片所在列宽度为80px,注意这里单位的一个换算
		 * 
		 * sheet.setColumnWidth(i, (short) (35.7 80));
		 * 
		 * // sheet.autoSizeColumn(i);
		 * 
		 * byte[] bsValue = (byte[]) value;
		 * 
		 * HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
		 * 
		 * 1023, 255, (short) 6, index, (short) 6, index);
		 * 
		 * anchor.setAnchorType(2);
		 * 
		 * patriarch.createPicture(anchor, workbook.addPicture(
		 * 
		 * bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
		 * 
		 * }
		 */

        // 其它数据类型都当作字符串简单处理

        // }

        // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成

        if (textValue != null) {

            Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");

            Matcher matcher = p.matcher(textValue);

			/*
			 * if(matcher.matches()){
			 * 
			 * //是数字当作double处理
			 * 
			 * cell.setCellValue(Double.parseDouble(textValue));
			 * 
			 * }else{
			 */

            // }

        }
        XSSFRichTextString richString = new XSSFRichTextString(textValue);
        cell.setCellValue(richString);
    }

}
