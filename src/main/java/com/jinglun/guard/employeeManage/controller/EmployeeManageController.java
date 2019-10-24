package com.jinglun.guard.employeeManage.controller;

import com.alibaba.fastjson.JSON;
import com.jinglun.guard.common.utils.ZipUtil;
import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.PageResult;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.dataservice.DataService.FDResult;
import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.devicemanage.domain.TermFace;
import com.jinglun.guard.employeeManage.domain.*;
import com.jinglun.guard.employeeManage.domain.FaceInfo.Picture;
import com.jinglun.guard.employeeManage.domain.FaceInfo.Picture.Option;
import com.jinglun.guard.employeeManage.service.impl.EmployeeManageServiceImpl;
import com.jinglun.guard.systemManage.domain.Depart;
import com.jinglun.guard.user.domain.User;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employeeManage")
@Slf4j
public class EmployeeManageController {
    private static Integer EXCELPROGRESS = 0;
    private static Integer UPLOADPROGRESS = 0;
    private Integer SUM = 0;
    private static Integer NUM = 0;
    private static boolean FLAG = true;
    private Map<String,Integer> uploadmap=new HashMap<>();
    private Map<String,Integer> excelmap=new HashMap<>();
    private Map<String,Boolean> cancelupload=new HashMap<>();
    String tmpdir="java.io.tmpdir";
    String fileName1="fileName";
    String zipName1="zipName";
    String xlsPath1="xlsPath";
    String delzipfiles="删除压缩文件成功";
    String delfiles="删除解压文件成功";
    @Resource(name = "employeeManageServiceImpl")
    private EmployeeManageServiceImpl employeeManageServiceImpl;

    @RequestMapping("/toEmployeeManage")
    public String toEmployeeManage( HttpSession httpSession,Model model) {
    	User user = (User) httpSession.getAttribute("user");
    	model.addAttribute("user", user);
        return "employeeManage/employeeList";
    }

    @RequestMapping("/selectEmployeeList")
    @ResponseBody
    public String selectEmployeeList(@RequestParam(value = "page", defaultValue = "1") String page,
                                     @RequestParam(value = "limit", defaultValue = "10") String limit,
                                     @RequestParam(value = "searchContent", defaultValue = "") String searchContent,
                                     @RequestParam(value = "departId", defaultValue = "0") Integer departId,
                                     @RequestParam(value = "photoNum", defaultValue = "0") Integer photoNum, HttpSession httpSession) {
        log.info("page={},limit={},searchContent={},departId={}", page, limit, searchContent, departId);
        User user = (User) httpSession.getAttribute("user");
        String sessionId=httpSession.getId();
        List<Integer> authList =user.getDepartList();
        excelmap.put(sessionId,0);
        Integer start = (Integer.parseInt(page) - 1) * Integer.parseInt(limit);
        List<FaceInfo> faceInfoList = new ArrayList<>();
        ResultDomain<List<FaceInfo>> faceInfoResultDomain;
        FaceInfo faceInfo = new FaceInfo();
        faceInfo.setAttribute(0);
        faceInfo.setStatus(0);
        faceInfo.setDepart_id(departId);
        faceInfo.setPic_num(photoNum);
        faceInfoResultDomain = DataService.FaceInfoQuery(user, faceInfo, searchContent, start, Integer.parseInt(limit),authList);
        ResultDomain<List<Depart>> departResultDomain = DataService.DepartInfoQuery(user, -1, 0);
        if (null != faceInfoResultDomain.getResultData() && faceInfoResultDomain.getResultData().size() > 0
                && null != departResultDomain.getResultData() && departResultDomain.getResultData().size() > 0) {
            faceInfoList = faceInfoResultDomain.getResultData();
            List<Depart> departList = departResultDomain.getResultData();
            Map<Integer, String> departMap = departList.stream()
                    .collect(Collectors.toMap(Depart::getDepart_id, Depart::getDepart_name));
            faceInfoList.forEach(e -> e.setDepartName(departMap.get(e.getDepart_id())));
        } else {
            log.info("查询员工列表为空");
        }
        PageResult pageResult = new PageResult(faceInfoResultDomain.getResultCode(), faceInfoList);
        return JSON.toJSONString(pageResult);
    }


    @RequestMapping("/selectFacePhoto")
    @ResponseBody
    public String selectFacePhoto(HttpSession httpSession, FaceInfo faceInfo) {
        User user = (User) httpSession.getAttribute("user");
        PictureSort pictureSort = new PictureSort();
        ResultDomain<List<FaceInfo.Picture>> listResultDomain = DataService.FacePhotoQuery(user, faceInfo);
        List<FaceInfo.Picture> comparePictureList = new ArrayList<>();
        List<FaceInfo.Picture> headPictureList = new ArrayList<>();
        if (null != listResultDomain && listResultDomain.getResultCode() == 0) {
            if (null != listResultDomain.getResultData() && listResultDomain.getResultData().size() > 0) {
                List<FaceInfo.Picture> pictureList = listResultDomain.getResultData();
                comparePictureList = pictureList.stream().filter(p -> p.getImage_type() == 0).collect(Collectors.toList());
                headPictureList = pictureList.stream().filter(p -> p.getImage_type() == 1).collect(Collectors.toList());
            }
            pictureSort.setResultCode(0);
        } else {
            log.info("查询facePhoto错误");
            pictureSort.setResultCode(-1);
        }
        pictureSort.setComparePictureList(comparePictureList);
        pictureSort.setHeadPictureList(headPictureList);
        return JSON.toJSONString(pictureSort);
    }

    @RequestMapping("/toAddEmployee")
    public String toAddEmployee(HttpSession httpSession, Model model, @RequestParam(defaultValue = "0") Integer faceId) {
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("faceId", faceId);
        List<DataService.ParamConfigInfo> paramConfigInfoList ;
        String facemod="face_info_input_mod";
        String isneedcard="is_need_employee_card";
        String photosize="life_photo_max_size";
        ResultDomain<List<DataService.ParamConfigInfo>> ParamListResult = DataService.LibraryParamQuery(user);
        if (ParamListResult.getResultCode() == 0 && ParamListResult.getResultData().size() > 0) {
            paramConfigInfoList = ParamListResult.getResultData();
            Map<String, String> paramConfigInfoMap = paramConfigInfoList.stream().collect(Collectors
                    .toMap(DataService.ParamConfigInfo::getParam_key, DataService.ParamConfigInfo::getParam_value));
            if (null != paramConfigInfoMap.get(facemod)) {
                model.addAttribute(facemod, paramConfigInfoMap.get(facemod));
            } else {
                model.addAttribute(facemod, "1");
            }
            if (null != paramConfigInfoMap.get(isneedcard)) {
                model.addAttribute(isneedcard, paramConfigInfoMap.get(isneedcard));
            } else {
                model.addAttribute(isneedcard, "1");
            }
            if (null != paramConfigInfoMap.get(photosize)) {
                model.addAttribute(photosize, paramConfigInfoMap.get(photosize));
            } else {
                model.addAttribute(photosize, "100");
            }
        } else {
            log.warn("查询业务参数接口出错或没有数据");
            model.addAttribute(facemod, "1");
            model.addAttribute(isneedcard, "1");
            model.addAttribute(photosize, "100");
        }
        ResultDomain<List<Depart>> departResultDomain = DataService.DepartInfoQueryByAuth(user, -1, 0);
        if (null != departResultDomain.getResultData() && departResultDomain.getResultData().size() > 0
                && departResultDomain.getResultCode() == 0) {
            List<Depart> departList = departResultDomain.getResultData();
            model.addAttribute("departList", departList);
        }
        return "employeeManage/addEmployee";
    }

    //跳转离职员工页面
    @RequestMapping("/toLeaveEmployee")
    public String toLeaveEmployee() {
        return "employeeManage/leaveEmployee";
    }

    @RequestMapping("/selectFaceInfoByParam")
    @ResponseBody
    public String selectFaceInfoByParam(HttpSession httpSession, FaceInfo faceInfo) {
        User user = (User) httpSession.getAttribute("user");
        if (faceInfo.getFace_id() != 0) {
            ResultDomain<List<FaceInfo>> listResultDomain = DataService.FaceInfoQuery(user, faceInfo, "",
                    0, -1);
            if (null != listResultDomain.getResultData() && listResultDomain.getResultData().size() == 1) {
                List<FaceInfo> resultData = listResultDomain.getResultData();
                return resultData.get(0).getName();
            } else {
                log.info("FaceInfoQuery接口错误,或查询结果有多个");
                return "暂未设置";
            }
        } else {
            log.info("领导ID为0");
            return "暂未设置";
        }
    }

    //查询离职员工
    @RequestMapping("/selectLeaveEmployee")
    @ResponseBody
    public String selectleaveEmployee(HttpSession httpSession, Integer faceId,
                                      @RequestParam(value = "page", defaultValue = "1") String page,
                                      @RequestParam(value = "searchContent", defaultValue = "") String searchContent,
                                      @RequestParam(value = "limit", defaultValue = "10") String limit) {

        User user = (User) httpSession.getAttribute("user");
        List<FaceInfo> faceInfoList = new ArrayList<>();
        ResultDomain<List<FaceInfo>> faceInfoResultDomain;
        Integer start = (Integer.parseInt(page) - 1) * Integer.parseInt(limit);

        //判断搜索条件
        if (searchContent.equals("")) {
            FaceInfo faceInfo = new FaceInfo();
            faceInfo.setStatus(1);
            faceInfo.setAttribute(0);
            faceInfoResultDomain = DataService.FaceInfoQuery(user, faceInfo, "", start, Integer.parseInt(limit));
        } else {
            FaceInfo faceInfo = new FaceInfo();
            faceInfo.setStatus(1);
            faceInfo.setAttribute(0);
            faceInfoResultDomain = DataService.FaceInfoQuery(user, faceInfo, searchContent, start, Integer.parseInt(limit));
        }

        //员工列表和部门列表
        ResultDomain<List<Depart>> departResultDomain = DataService.DepartInfoQuery(user, -1, 0);
        if (null != faceInfoResultDomain.getResultData() && faceInfoResultDomain.getResultData().size() > 0
                && null != departResultDomain.getResultData() && departResultDomain.getResultData().size() > 0) {
            faceInfoList = faceInfoResultDomain.getResultData();
            List<Depart> departList = departResultDomain.getResultData();
            Map<Integer, String> departMap = departList.stream()
                    .collect(Collectors.toMap(Depart::getDepart_id, Depart::getDepart_name));
            faceInfoList.forEach(e -> e.setDepartName(departMap.get(e.getDepart_id())));
        } else {
            log.info("查询员工列表错误");
        }

        PageResult pageResult = new PageResult(faceInfoResultDomain.getResultCode(), faceInfoList);
        return JSON.toJSONString(pageResult);
    }

    //员工离职
    @RequestMapping("/leaveEmployee")
    @ResponseBody
    public void delete(HttpServletRequest request, Integer faceId) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        FaceInfo face = new FaceInfo();

        face.setFace_id(faceId);
        face.setAttribute(0);

        ResultDomain resultDomain = DataService.FaceInfoDelete(user, face);
        log.info(String.valueOf(resultDomain.getResultCode()));
    }

    // 下载导入模板
    @RequestMapping("/download")
    public String downloadFilesTest() {
        String title = "批量模板.zip";
        try {
            title = java.net.URLEncoder.encode(title, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "redirect:/" + title;

    }

    // 上传压缩包
    @RequestMapping("/upload")
    @ResponseBody
    public Map<String, Object> upload(Model model, HttpServletRequest request, MultipartFile file) {
        log.info("开始校验");
        FLAG = true;
        SUM = 0;
        NUM = 0;
        HttpSession session = request.getSession();
        String sessionId = session.getId();


        Map<String, Object> result = new HashMap<>();
        String sourceFileName = System.getProperty(tmpdir)+"\\"+sessionId;
        String oFileName = file.getOriginalFilename();


        String originalFileName = oFileName;
        if (oFileName.indexOf("\\") != -1) {
            originalFileName = oFileName.substring(oFileName.lastIndexOf("\\"));
            originalFileName = originalFileName.substring(1);
        }
        if (StringUtils.isNotBlank(originalFileName)) {
            try {
                file.transferTo(new File(sourceFileName + originalFileName));
                result.put("code", 200);
                result.put("msg", "文件上传成功！");
            } catch (IOException e) {
                result.put("code", 100);
                result.put("msg", "文件上传出错，请重新上传！");
                e.printStackTrace();
                return result;
            }
        }



        // zip文件存放位置（加上文件名）D:\pgp\temp\ecg\1511337845943\111.zip
        String zipPath = sourceFileName + originalFileName;


        File zipFile = new File(zipPath);
        // zip文件解压后的路径 D:\pgp\temp\ecg\1511337845943\
        String descDir = sourceFileName;
        // 保存文件路径信息(可选)
        List<String> urlList = new ArrayList<>();
        //解压会返回解压后的目录名字.为了实现压缩包名和文件夹名不一样.
        String firstDirName = ZipUtil.unZip(zipFile, descDir, urlList);
        String name = firstDirName.substring(0, firstDirName.length() - 1);
        // 校验excel
        String filename = originalFileName;
        String xlsPath = sourceFileName + name + "/list.xls";
        String xlsPathx = sourceFileName + name + "/list.xlsx";
        String imgPath = sourceFileName + name + "/";

        cancelupload.put(sessionId+"flag",true);

        session.setAttribute("imgPath", imgPath);
        session.setAttribute(fileName1, name);
        session.setAttribute(zipName1, filename);
        FileInputStream fis = null;
        try {
            // 获取一个绝对地址的流
            fis = new FileInputStream(xlsPath);
            session.setAttribute(xlsPath1, xlsPath);
        } catch (Exception e) {
            xlsPath = xlsPathx;
            session.setAttribute(xlsPath1, xlsPathx);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ExcelResult excelResult = checkExcel(xlsPath,sessionId);
        if (excelResult.getResult().equals("校验完成")) {
            session.setAttribute("check", 1);
            result.put("checkcode", 200);
            result.put("checkmsg", excelResult.getResult());
            result.put("totalCount", excelResult.getTotalCount());
            result.put("emptyCount", excelResult.getEmptyCount());
        } else {
            session.setAttribute("check", 0);
            if (DeleteFolder(sourceFileName + name)) {
                log.info(delfiles);
            }
            if (DeleteFolder(zipPath)) {
                log.info(delzipfiles);
            }
            result.put("checkcode", 100);
            result.put("checkmsg", excelResult.getResult());
        }
        NUM = 0;
        return result;
    }

    // 检验excel不符合规定
    public ExcelResult checkExcel(String filePath,String sessionId) {
        // 判断不为excel类型文件
        ExcelResult excelResult = new ExcelResult();
        String result = "";
        if (!filePath.endsWith(".xls") && !filePath.endsWith(".xlsx")) {
            result = "检查文件不为excel！";
            excelResult.setResult(result);
            return excelResult;
        }

        FileInputStream fis = null;
        Workbook wookbook = null;

        try {
            // 获取一个绝对地址的流
            fis = new FileInputStream(filePath);
        } catch (Exception e) {
            result = "检验excel不存在，请按照导入模板导入！";
            excelResult.setResult(result);
            return excelResult;
        }

        try {
            // 2003版本的excel，用.xls结尾
            wookbook = new HSSFWorkbook(fis);// 得到工作簿

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                // 2007版本的excel，用.xlsx结尾

                wookbook = new XSSFWorkbook(fis);// 得到工作簿
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        Sheet sheet = null;
        if (wookbook != null) {
            // 得到一个工作表
            sheet = wookbook.getSheetAt(0);
        } else {
            log.info("excel表为空");
            result = "excel表为空";
            excelResult.setResult(result);
            return excelResult;
        }
        //定义空行数
        int emptyCount = 0;
        // 获得数据的总行数
        int totalRowNum = sheet.getLastRowNum();
        SUM = totalRowNum + 1;
        DecimalFormat df = new DecimalFormat("0");
        // 获得所有数据
        for (int i = 0; i <= totalRowNum; i++) {
            // 要获得属性
            // 获得第i行对象
            Row row = sheet.getRow(i);
            //判定该行是不是空,若为空则跳过
            if (!isRowEmpty(row)) {
                //int j=i;
                //姓名
                Cell cell = row.getCell(0);
                if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                    result = "表格中第" + (++i) + "行姓名未填写，请检查list文件！";
                    excelResult.setResult(result);
                    return excelResult;
                } else if (cell.getCellType() != Cell.CELL_TYPE_STRING) {
                    result = "表格中第" + (++i) + "行姓名不为文本，请检查list文件！！";
                    excelResult.setResult(result);
                    return excelResult;
                } else {
                    String name = cell.getStringCellValue().toString();
                    if (name.contains("姓名")) {
                        continue;
                    }
                }

                //身份证号码和手机号
                Cell cell1 = row.getCell(1);
                Cell cell6 = row.getCell(6);
                if ((cell1 == null || cell1.getCellType() == Cell.CELL_TYPE_BLANK)
                        && (cell6 == null || cell6.getCellType() == Cell.CELL_TYPE_BLANK)) {
                    result = "表格中第" + (++i) + "行身份证号码和手机号均未填写，请检查list文件！！";
                    excelResult.setResult(result);
                    return excelResult;
                } else if (null != cell1 && cell1.getCellType() != Cell.CELL_TYPE_BLANK && cell1.getCellType() != Cell.CELL_TYPE_STRING) {
                    result = "表格中第" + (++i) + "行身份证号码不为文本，请检查list文件！！";
                    excelResult.setResult(result);
                    return excelResult;
                } else if (null != cell6 && cell6.getCellType() != Cell.CELL_TYPE_BLANK && cell6.getCellType() != Cell.CELL_TYPE_STRING) {
                    result = "表格中第" + (++i) + "行手机号不为文本，请检查list文件！";
                    excelResult.setResult(result);
                    return excelResult;
                }

                //身份证卡体管理号
                cell = row.getCell(2);
                if ((null != cell && cell.getCellType() != Cell.CELL_TYPE_BLANK)
                        && cell.getCellType() != Cell.CELL_TYPE_STRING) {
                    result = "表格中第" + (++i) + "行身份证卡体管理号不为文本，请检查list文件！";
                    excelResult.setResult(result);
                    return excelResult;
                }

                //员工卡号
                cell = row.getCell(3);
                if ((null != cell && cell.getCellType() != Cell.CELL_TYPE_BLANK)
                        && cell.getCellType() != Cell.CELL_TYPE_STRING) {
                    result = "表格中第" + (++i) + "行员工卡号不为文本，请检查list文件！";
                    excelResult.setResult(result);
                    return excelResult;
                }

                //部门
                cell = row.getCell(4);
                if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                    result = "表格中第" + (++i) + "行部门未填写，请检查list文件！";
                    excelResult.setResult(result);
                    return excelResult;
                } else if (cell.getCellType() != Cell.CELL_TYPE_STRING) {
                    result = "表格中第" + (++i) + "行部门不为文本，请检查list文件！";
                    excelResult.setResult(result);
                    return excelResult;
                }

                //性别
                cell = row.getCell(5);
                if ((null != cell && cell.getCellType() != Cell.CELL_TYPE_BLANK)
                        && cell.getCellType() != Cell.CELL_TYPE_STRING) {
                    result = "表格中第" + (++i) + "行性别不为文本，请检查list文件！";
                    excelResult.setResult(result);
                    return excelResult;
                }

                //工号
                cell = row.getCell(7);
                if ((null != cell && cell.getCellType() != Cell.CELL_TYPE_BLANK)
                        && cell.getCellType() != Cell.CELL_TYPE_STRING) {
                    result = "表格中第" + (++i) + "行工号不为文本，请检查list文件！";
                    excelResult.setResult(result);
                    return excelResult;
                }

                //职位
                cell = row.getCell(8);
                if ((null != cell && cell.getCellType() != Cell.CELL_TYPE_BLANK)
                        && cell.getCellType() != Cell.CELL_TYPE_STRING) {
                    result = "表格中第" + (++i) + "行职位不为文本，请检查list文件！";
                    excelResult.setResult(result);
                    return excelResult;
                }
                NUM = ++i;
                if (i == 0) {
                    EXCELPROGRESS = ++i;
                    excelmap.put(sessionId,EXCELPROGRESS);
                } else {
                    EXCELPROGRESS = Integer.parseInt(df.format(((float) i / (totalRowNum + 1)) * 100));
                    excelmap.put(sessionId,EXCELPROGRESS);
                }
                i--;
                if (!cancelupload.get(sessionId+"flag")) {
                    break;
                }
            } else {
                EXCELPROGRESS = Integer.parseInt(df.format(((float) i / (totalRowNum + 1)) * 100));
                excelmap.put(sessionId,EXCELPROGRESS);
                //若是空行,需要+1
                emptyCount++;
            }
        }
        result = "校验完成";
        //返回效验结果,空行数,总行数
        excelResult.setResult(result);
        excelResult.setEmptyCount(emptyCount);
        excelResult.setTotalCount(totalRowNum);
        log.info(result);
        return excelResult;
    }

    public boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
                return false;
        }
        return true;
    }

    // 返回全局变量，判断是否取消上传
    @RequestMapping("/cancelupload")
    @ResponseBody
    public void cancelUpload(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        cancelupload.put(sessionId+"flag",false);
        //FLAG = false;
    }

    // 返回全局变量，显示校验excel进度
    @RequestMapping("/excelprogress")
    @ResponseBody
    public Progress excelProgress(HttpServletRequest request) throws IOException {
        HttpSession session=request.getSession();
        String sessionId=session.getId();
        Progress progress = new Progress();
        progress.setProgress(excelmap.get(sessionId));
        progress.setSum(SUM);
        progress.setNum(NUM);
        return progress;
    }

    // 返回全局变量，显示上传进度
    @RequestMapping("/uploadprogress")
    @ResponseBody
    public Progress uploadProgress(HttpServletRequest request) throws IOException {
        HttpSession session=request.getSession();
        String sessionId=session.getId();
        Progress progress = new Progress();
        progress.setProgress(uploadmap.get(sessionId));
        progress.setSum(SUM);
        progress.setNum(NUM);
        return progress;
    }


    @RequestMapping("/delete")
    @ResponseBody
    public int delete(HttpSession session) throws IOException {
        String fileName = (String) session.getAttribute(fileName1);
        String zipName = (String) session.getAttribute(zipName1);
        String filePath = System.getProperty(tmpdir) + fileName;
        String zipPath = System.getProperty(tmpdir) + zipName;
        if (DeleteFolder(filePath)) {
            log.info(delfiles);
        }
        if (DeleteFolder(zipPath)) {
            log.info(delzipfiles);
        }
        return 0;
    }

    // 上报
    @RequestMapping("/read")
    @ResponseBody
    public String readExcel(HttpServletRequest request) throws IOException {
        //FLAG = true;

        HttpSession session = request.getSession();
        String sessionId= session.getId();
        cancelupload.put(sessionId+"flag",true);
        uploadmap.put(sessionId,0);
        User user = (User) session.getAttribute("user");
        String xlsPath = (String) session.getAttribute(xlsPath1);
        String imgPath = (String) session.getAttribute("imgPath");
        String fileName = (String) session.getAttribute(fileName1);
        String zipName = (String) session.getAttribute(zipName1);
        //递归上传部门
        employeeManageServiceImpl.recursiveDepartUpload(user, imgPath, 0, "");
        List<FaceInfo> faceInfo = employeeManageServiceImpl.getDataFromExcel(imgPath, xlsPath);
        int i = 0;
        int j = faceInfo.size();
        StringBuilder errorRow = new StringBuilder();
        for (FaceInfo faceInfo2 : faceInfo) {
            i++;
            DecimalFormat df = new DecimalFormat("0");
            UPLOADPROGRESS = Integer.parseInt(df.format(((float) i / j) * 100));
            uploadmap.put(sessionId,UPLOADPROGRESS);
            NUM = i;
            if (!faceInfo2.getFace_info_url().equals("")) {
                ResultDomain<List<FDResult>> resultDomain = DataService.FaceImageDetect(faceInfo2.getFace_info_url(), 1);
                if (null != resultDomain.getResultData()) {
                    List<FDResult> result = resultDomain.getResultData();
                    FDResult fdResult = result.get(0);
                    FaceInfo.Picture picture = new FaceInfo.Picture();
                    picture.setPic_id(0);
                    picture.setOption(Option.UPLOAD_OR_MODIFY);
                    picture.setImage_type(0);
                    picture.setWidth(fdResult.getFace_width());
                    picture.setHeight(fdResult.getFace_height());
                    picture.setData(fdResult.getFace_data());
                    picture.setFeature(fdResult.getFeature());
                    picture.setFeature_ver(fdResult.getFeature_ver());
                    List<Picture> pictures = new ArrayList<FaceInfo.Picture>();
                    pictures.add(picture);
                    faceInfo2.setPictures(pictures);
                }else {
                    log.error("检测照片+提取特征值出错(FaceImageDetect)");
                }
            }
            ResultDomain<FaceInfo> fResultDomain = employeeManageServiceImpl.FaceInfoUpload(user, faceInfo2);
            if (fResultDomain.getResultCode() != 0) {
                errorRow.append(i).append(",");
                log.error(errorRow+"上传人员信息出错(FaceInfoUpload)");
            }

            if (!cancelupload.get(sessionId+"flag")) {
                break;
            }
        }
        UPLOADPROGRESS = 0;
        EXCELPROGRESS = 0;
        String filePath = System.getProperty(tmpdir) + sessionId + fileName;
        String zipPath = System.getProperty(tmpdir) +sessionId+ zipName;
        if (DeleteFolder(filePath)) {
            log.info(delfiles);
        }
        if (DeleteFolder(zipPath)) {
            log.info(delzipfiles);
        }
        if (errorRow.toString().equals("")) {
            return "0";
        } else {
            return errorRow.toString();
        }
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件不存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断不为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping("/addFaceInfo")
    @ResponseBody
    public String addFaceInfo(@RequestBody FaceInfo faceInfo, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        ResultDomain<FaceInfo> faceInfoResultDomain = DataService.FaceInfoUpload(user, faceInfo);
        if (null == faceInfoResultDomain.getResultData() || faceInfoResultDomain.getResultCode() != 0) {
            log.info("添加FaceInfo失败,FaceInfoUpload接口错误");
        }
        return JSON.toJSONString(faceInfoResultDomain);
    }

    @RequestMapping("/facePermitConfig")
    @ResponseBody
    public String facePermitConfig(HttpSession httpSession, @RequestBody FacePermitParam facePermitParam) {
        User user = (User) httpSession.getAttribute("user");
        FaceInfo faceInfo = facePermitParam.getFaceInfo();
        if (null == faceInfo.getExpire_time() || faceInfo.getExpire_time().equals("")) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
            String nowDate = df.format(new Date());
            faceInfo.setVisit_time(nowDate);
            faceInfo.setExpire_time("2038-12-31 11:59:59");
        }
        List<Integer> termIdList = facePermitParam.getTermIdList();
        ResultDomain resultDomain = DataService.FacePermitConfig(user, faceInfo, termIdList, facePermitParam.getRecId());
        return JSON.toJSONString(resultDomain);
    }

    @RequestMapping("/addFaceInfos")
    @ResponseBody
    public String addFaceInfos(@RequestBody AddFaceInfoParam1 addFaceInfoParam, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        addFaceInfoParam.getFaceInfo().setPictures(addFaceInfoParam.getPictureList());
        ResultDomain<FaceInfo> faceInfoResultDomain = employeeManageServiceImpl.addFaceInfo1(addFaceInfoParam, user);
        return JSON.toJSONString(faceInfoResultDomain);
    }

    /**
     * 查询所有员工
     *
     * @param httpSession
     * @param faceInfo
     * @return
     */
    @RequestMapping("/selectAllFaceInfo")
    @ResponseBody
    public String searchLeader(HttpSession httpSession, FaceInfo faceInfo) {
        User user = (User) httpSession.getAttribute("user");
        List<Integer> authlist = user.getDepartList();
        PageResult pageResult = new PageResult();
        List<FaceInfo> resultData = new ArrayList<>();
        ResultDomain<List<FaceInfo>> listResultDomain = DataService.FaceListGet(user, faceInfo,authlist);
        if (null != listResultDomain.getResultData()) {
            resultData = listResultDomain.getResultData();
        } else {
            log.info("搜索全部faceInfo出错");
        }
        pageResult.setData(resultData);
        return JSON.toJSONString(pageResult);
    }

    /**
     * 前往拍照页面
     *
     * @param browserType 谷歌浏览器与IE跳转的页面不一样
     * @return
     */
    @RequestMapping("/toTakePhoto")
    public String toTakePhoto(String browserType) {
        if (browserType.equals("Chrome")) {
            return "html5Camera";
        } else {
            return "test";
        }
    }

    @RequestMapping("/queryDevice")
    @ResponseBody
    public String queryDeviceList(HttpSession httpSession, TermFace face) {
        User user = (User) httpSession.getAttribute("user");
        List<Device> devicelist = employeeManageServiceImpl.queryAllDevice(user);
        PageResult pageResult = new PageResult();
        if (face.getFace_id() != 0) {
            ResultDomain<List<TermFace>> resultDomain = DataService.FacePermitQuery(user, face);
            if (null != resultDomain.getResultData()) {
                List<TermFace> tList = resultDomain.getResultData();
                tList.forEach(t -> {
                    devicelist.forEach(d -> {
                        if (d.getTerm_id() == t.getTerm_id()) {
                            d.setLAY_CHECKED(true);
                        }
                    });
                });
                pageResult.setData(devicelist);
                String string = JSON.toJSONString(pageResult).replace("lAY_CHECKED", "LAY_CHECKED");
                return string;
            }
        }
        pageResult.setData(devicelist);
        return JSON.toJSONString(pageResult);
    }

    @RequestMapping("/facePermitQuery")
    @ResponseBody
    public TermVO facePermitQuery(HttpSession httpSession, TermFace face) {
        User user = (User) httpSession.getAttribute("user");
        List<Device> devicelist = employeeManageServiceImpl.queryAllDevice(user);
        ResultDomain<List<TermFace>> resultDomain = DataService.FacePermitQuery(user, face);
        StringBuilder termNameBuffer = new StringBuilder();
        StringBuilder termModeBuffer = new StringBuilder();
        if (null != resultDomain.getResultData()) {
            List<TermFace> tList = resultDomain.getResultData();
            tList.forEach(t -> {
                devicelist.forEach(d -> {
                    if (d.getTerm_id() == t.getTerm_id()) {
                        termNameBuffer.append(d.getTerm_name() + ",");
                        int term_model = t.getTerm_model();
                        if (term_model == 0) {
                            termModeBuffer.append(4 + ",");//未知类型
                        } else if (term_model == 1 || term_model == 2 || term_model == 3 || term_model == 4 || term_model == 5 || term_model == 6 || term_model == 9) {
                            termModeBuffer.append(3 + ",");//iDR类型
                        } else if (term_model == 100) {
                            termModeBuffer.append(2 + ",");
                            ;//facecheck动态类型
                        } else {
                            termModeBuffer.append(1 + ",");//14T类型
                        }
                    }
                });
            });
        }
        String termName = termNameBuffer.toString();
        String termMode = termModeBuffer.toString();
        TermVO termVO = new TermVO();
        if (!termName.equals("")) {
            String termNameStr = termName.substring(0, termName.length() - 1);
            String termModeStr = termMode.substring(0, termMode.length() - 1);
            Integer termLength = termNameStr.split(",").length;
            termVO.setTermName(termNameStr);
            termVO.setTermMode(termModeStr);
            termVO.setTermLength(termLength);
            return termVO;
        } else {
            return null;
        }
    }

    /**
     * 通过部门查询人员
     *
     * @param faceInfo
     * @param httpSession
     * @return
     */
    @RequestMapping("/selectByDepart")
    @ResponseBody
    public String selectByDepart(FaceInfo faceInfo, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        List<Integer> authlist = user.getDepartList();
        PageResult pageResult = new PageResult();
        List<FaceInfo> faceInfoList = new ArrayList<>();
        ResultDomain<List<FaceInfo>> listResultDomain = DataService.FaceListGet(user, faceInfo,authlist);
        if (null != listResultDomain.getResultData()) {
            faceInfoList = listResultDomain.getResultData();
        }
        pageResult.setData(faceInfoList);
        pageResult.setCount(faceInfoList.size());
        return JSON.toJSONString(pageResult);
    }

    /**
     * 检测人脸
     *
     * @param imgBase64
     * @return
     */
    @RequestMapping("/checkFace")
    @ResponseBody
    public String checkFace(String imgBase64) {
        ResultDomain<List<DataService.FDResult>> listResultDomain = DataService.FaceImageDetect(imgBase64, 2);
        return JSON.toJSONString(listResultDomain);
    }

    /**
     * 比对照片相似度
     *
     * @param img1
     * @param img2
     * @return
     */
    @RequestMapping("/comparePhoto")
    @ResponseBody
    public String comparePhoto(String img1, String img2) {
        ResultDomain<Double> doubleResultDomain = DataService.FaceImageCompare(img1, img2, 2);
        if (null != doubleResultDomain && null != doubleResultDomain.getResultData() && doubleResultDomain.getResultCode() == 0) {
            Double resultData = doubleResultDomain.getResultData();
            return String.valueOf(resultData);
        } else {
            log.info("FaceImageCompare接口错误");
            return "0";
        }
    }

    /**
     * 下载插件
     *
     * @param pluginName 插件名
     * @return
     */
    @RequestMapping("/downloadPlugin")
    public String downloadPlugin(String pluginName) {
        String title = "";
        if (pluginName.equals("flash")) {
            title = "flashplayerax_install_cn.exe";
        } else {
            title = "SetupOCXv1.0.0.4.exe";
        }
        try {
            title = java.net.URLEncoder.encode(title, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "redirect:/" + title;
    }

    /**
     * 查询所有部门,不分层级
     *
     * @param departId
     * @param httpSession
     * @return
     */
    @RequestMapping("/selectDepart")
    @ResponseBody
    public String selectDepart(Integer departId, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        ResultDomain<List<Depart>> listResultDomain = DataService.DepartInfoQuery(user, -1, departId);
        return JSON.toJSONString(listResultDomain);
    }
    
    /**
     * 跳转添加车辆主界面
     * @return
     */
    @RequestMapping("/toCar")
    public String toAddConference(Model model,
    							  @RequestParam(value = "carnum", defaultValue = "") String carnum,
    							  @RequestParam(value = "licensetype", defaultValue = "") String licensetype,
    							  @RequestParam(value = "carcolor", defaultValue = "") String carcolor,
    							  @RequestParam(value = "cartype", defaultValue = "") String cartype){
    	
    	if(!"".equals(carnum)){
    		model.addAttribute("carnum", carnum);
    	}
    	if(!"".equals(licensetype)){
    		model.addAttribute("licensetype", licensetype);
    	}
    	if(!"".equals(carcolor)){
    		model.addAttribute("carcolor", carcolor);
    	}
    	if(!"".equals(cartype)){
    		model.addAttribute("cartype", cartype);
    	}
    	return "employeeManage/addCar";
    }
}
