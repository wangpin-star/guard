package com.jinglun.guard.employeeManage.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.devicemanage.domain.Device.DeviceFlag;

import com.jinglun.guard.employeeManage.domain.AddFaceInfoParam;
import com.jinglun.guard.employeeManage.domain.AddFaceInfoParam1;

import lombok.extern.slf4j.Slf4j;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.expression.Operation;
import org.springframework.stereotype.Service;

import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.dataservice.DataService.FDResult;
import com.jinglun.guard.employeeManage.domain.FaceInfo;
import com.jinglun.guard.employeeManage.domain.FaceInfo.Picture;
import com.jinglun.guard.employeeManage.domain.FaceInfo.Picture.Option;
import com.jinglun.guard.employeeManage.service.EmployeeManageService;
import com.jinglun.guard.systemManage.domain.Depart;
import com.jinglun.guard.user.domain.User;

/**
 * 员工管理ServiceImpl
 * 
 * @author cq
 *
 */
@Service
@Slf4j
public class EmployeeManageServiceImpl implements EmployeeManageService {

	private Map<String,Integer> levelDepartMap = new HashMap<>();

	// 解析excel为faceinfo对象
	@Override
	public List<FaceInfo> getDataFromExcel(String imgPath, String filePath) {
		List<FaceInfo> faceInfo = new ArrayList<FaceInfo>();
		// 判断是否为excel类型文件
		if (!filePath.endsWith(".xls") && !filePath.endsWith(".xlsx")) {
			log.info("文件不是excel类型");
		}

		FileInputStream fis = null;
		Workbook wookbook = null;

		try {
			// 获取一个绝对地址的流
			fis = new FileInputStream(filePath);
		} catch (Exception e) {
			e.printStackTrace();
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

		// 得到一个工作表
		Sheet sheet = wookbook.getSheetAt(0);

		// 获得数据的总行数
		int totalRowNum = sheet.getLastRowNum();

		// 获得所有数据
		for (int i = 0; i <= totalRowNum; i++) {
			// 要获得属性
			FaceInfo info = new FaceInfo();
			// 获得第i行对象
			Row row = sheet.getRow(i);
			if (!isRowEmpty(row)) {
				// 获得获得第i行第0列的 String类型对象
				// 姓名
				Cell cell = row.getCell((short) 0);
				String name = cell.getStringCellValue().toString();
				if (name.contains("姓名")) {
					continue;
				}else {
					info.setName(cell.getStringCellValue().toString());
				}

				// 身份证号码
				cell = row.getCell((short) 1);
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
					if (cell.getCellType() ==Cell.CELL_TYPE_STRING) {
						info.setIdcard(cell.getStringCellValue().toString());
					}
					if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						DecimalFormat df = new DecimalFormat("0");
						info.setIdcard(String.valueOf(df.format(cell.getNumericCellValue())));
					}
				} else {
					info.setIdcard("");
				}

				// 身份证卡体管理号
				cell = row.getCell((short) 2);
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
					if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						DecimalFormat df = new DecimalFormat("0");
						info.setIdcard_cid(String.valueOf(df.format(cell.getNumericCellValue())));
					}else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						info.setIdcard_cid(cell.getStringCellValue().toString());
					}

				} else {
					info.setIdcard_cid("");
				}

				// 员工号
				cell = row.getCell((short) 3);
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
					if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						DecimalFormat df = new DecimalFormat("0");
						info.setEmpcard(String.valueOf(df.format(cell.getNumericCellValue())));
					}else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						info.setEmpcard(cell.getStringCellValue().toString());
					}
				} else {
					info.setEmpcard("");
				}

				// 部门
				cell = row.getCell((short) 4);
				info.setDepart_id(levelDepartMap.get(cell.getStringCellValue().toString()));
				String depart = cell.getStringCellValue().toString();
                String departPath = depart.replace("-", "/");

				// 性别
				cell = row.getCell((short) 5);
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
					if (cell.getStringCellValue().toString().equals("男")) {
						info.setSex(1);
					} else if (cell.getStringCellValue().toString().equals("女")) {
						info.setSex(2);
					}
				} else {
					info.setSex(0);
				}
				//手机号
				cell = row.getCell((short) 6);
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						info.setTel_no(cell.getStringCellValue().toString());
					}
					if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						DecimalFormat df = new DecimalFormat("0");
						info.setTel_no(String.valueOf(df.format(cell.getNumericCellValue())));
					}
				} else {
					info.setTel_no("");
				}
				//工牌号
				cell = row.getCell((short) 7);
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						info.setEmployee_id(cell.getStringCellValue().toString());
					}
					if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						DecimalFormat df = new DecimalFormat("0");
						info.setEmployee_id(String.valueOf(df.format(cell.getNumericCellValue())));
					}
				} else {
					info.setEmployee_id("");
				}
				//职位
				cell = row.getCell((short) 8);
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
					info.setPosition(cell.getStringCellValue().toString());
				} else {
					info.setPosition("");
				}
				File fileJpg = new File(imgPath + departPath + "\\" + name + ".jpg");
				if (!fileJpg.exists()) {
					File filePng = new File(imgPath + departPath + "\\" + name + ".png");
					if (!filePng.exists()) {
						File fileBmp = new File(imgPath + departPath + "\\" + name + ".bmp");
						if (!fileBmp.exists()) {
							info.setFace_info_url("");
						} else {
							info.setFace_info_url(imgPath + departPath + "\\" + name + ".bmp");
						}
					} else {
						info.setFace_info_url(imgPath + departPath + "\\" + name + ".png");
					}
				} else {
					info.setFace_info_url(imgPath + departPath + "\\" + name + ".jpg");
				}

				faceInfo.add(info);
			}
		}
		levelDepartMap.clear();
		return faceInfo;
	}

	public boolean isRowEmpty(Row row) {
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
				return false;
		}
		return true;
	}

	// 上报人员信息
	@Override
	public ResultDomain<FaceInfo> FaceInfoUpload(User user, FaceInfo face) {
		ResultDomain<FaceInfo> fResultDomain = DataService.FaceInfoUpload(user, face);
		return fResultDomain;
	}

	// 检验excel是否符合规定
	@Override
	public String checkExcel(String filePath) {
		// 判断是否为excel类型文件
		String result = "";
		if (!filePath.endsWith(".xls") && !filePath.endsWith(".xlsx")) {
			result = "检查文件是否为excel！";
			return result;
		}

		FileInputStream fis = null;
		Workbook wookbook = null;

		try {
			// 获取一个绝对地址的流
			fis = new FileInputStream(filePath);
		} catch (Exception e) {
			e.printStackTrace();
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

		Sheet sheet=null;
		if (wookbook != null) {
			// 得到一个工作表
			sheet = wookbook.getSheetAt(0);
		}else {
			log.info("excel表为空");
		}
		

		// 获得表头
		Row rowHead = sheet.getRow(0);

		// 判断表头是否正确
		if (rowHead.getPhysicalNumberOfCells() != 9) {
			result = "检查表头数量";
			return result;
		}

		// 获得数据的总行数
		int totalRowNum = sheet.getLastRowNum();

		// 获得所有数据
		for (int i = 1; i <= totalRowNum; i++) {
			// 要获得属性
			// 获得第i行对象
			Row row = sheet.getRow(i);

			// 获得获得第i行第0列的 String类型对象
			Cell cell = row.getCell((short) 0);
			if (cell == null || cell.getCellType() == cell.CELL_TYPE_BLANK) {
				result = "检查第" + i + "行第1列是否为空";
				return result;
			}

			Cell cell1 = row.getCell((short) 1);
			Cell cell6 = row.getCell((short) 6);
			if ((cell1 == null || cell1.getCellType() == cell.CELL_TYPE_BLANK)
					&& (cell6 == null || cell6.getCellType() == cell.CELL_TYPE_BLANK)) {
				result = "检查第" + i + "行身份证号码和手机号是否同时为空";
				return result;
			}

			cell = row.getCell((short) 4);
			if (cell == null || cell.getCellType() == cell.CELL_TYPE_BLANK) {
				result = "检查第" + i + "行第5列是否为空";
				return result;
			}

		}
		result = "校验完成";
		return result;
	}

	// 部门id和部门名称映射
	@Override
	public Map<String, Integer> DepartInfoUpload(User user, String filePath) {
		Map<String, Integer> map = new HashMap<>();
		List<String> list = DepartInfo(filePath);
		for (String string : list) {
			Depart depart = new Depart();
			depart.setDepart_name(string);
			ResultDomain<Depart> resultDomain = DataService.DepartInfoUpload(user, depart);
			Depart depart2 = resultDomain.getResultData();
			map.put(string, depart2.getDepart_id());
		}
		return map;
	}

	/**
	 * 递归上传部门
	 * @param user
	 * @param departDirectoryPath 部门文件夹的路径
	 * @param parentDepartId 父部门id
	 * @param parentDepartsMapKey 部门在Map中的key,由之前所有的父部门名称和此部门名称组成.此map定义的全局变量,用于临时存储上报部门后返回的部门id
	 */
	public void recursiveDepartUpload(User user,String departDirectoryPath,Integer parentDepartId,String parentDepartsMapKey) {
		List<Integer> departs=user.getDepartList();
		File file = new File(departDirectoryPath);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				return;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {//是文件夹
					    String departAbsolutePath = file2.getAbsolutePath();
					    String departName = file2.getName();
					    log.info("部门路径为{},部门名称为{}",departAbsolutePath,departName);
					    Depart depart = new Depart();
					    depart.setParent_depart_id(parentDepartId);
					    depart.setDepart_name(departName);
						ResultDomain<List<Depart>> listResultDomain = DataService.DepartInfoQuery(user,-1,0);
						ResultDomain<Depart> resultDomain = DataService.DepartInfoUpload(user, depart);
						List<Depart> departLists = listResultDomain.getResultData();
						List<Depart> departList = new ArrayList<>();
						if (null != listResultDomain.getResultData() && listResultDomain.getResultCode() == 0 && !listResultDomain.getResultData().isEmpty()) {

							for (int i = 0; i < departLists.size(); i++) {
								if (departLists.get(i).getDel_flag() == 0) {
									departList.add(departLists.get(i));
								}
							}
						}



                        if (null != resultDomain && null != resultDomain.getResultData() && resultDomain.getResultCode() == 0) {
                            Integer thisDepartId = resultDomain.getResultData().getDepart_id();
                            String thisDepartMapKey = "";
                            if (parentDepartsMapKey.equals("")) {
                                thisDepartMapKey = departName;
                            } else {
                                thisDepartMapKey = parentDepartsMapKey + "-" + departName;
                            }
                            levelDepartMap.put(thisDepartMapKey,thisDepartId);
                            log.info("thisDepartId={},thisDepartMapKey={}",thisDepartId,thisDepartMapKey);
                            recursiveDepartUpload(user,departAbsolutePath,thisDepartId,thisDepartMapKey);
                            //更新权限列表
							int departFlag=0;
							for (Depart depart1:departList
								 ) {
								if (depart1.getDepart_id()==thisDepartId){
									departFlag=1;
								}
							}
							if (departFlag!=1){
								departs.add(thisDepartId);
								user.setDepartList(departs);
							}
                        } else {
                            log.error("DepartInfoUpload接口错误,parentDepartId={},departName={}",parentDepartId,departName);
                            return;
                        }
					}
				}
			}
		} else {
			log.error("文件不存在!");
		}
	}

	// 获取部门list
	@Override
	public List<String> DepartInfo(String filePath) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		FileInputStream fis = null;
		Workbook wookbook = null;

		try {
			// 获取一个绝对地址的流
			fis = new FileInputStream(filePath);
		} catch (Exception e) {
			e.printStackTrace();
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

		// 得到一个工作表
		Sheet sheet = wookbook.getSheetAt(0);

		// 获得数据的总行数
		int totalRowNum = sheet.getLastRowNum();

		// 获得所有数据
		for (int i = 0; i <= totalRowNum; i++) {

			// 获得第i行对象
			Row row = sheet.getRow(i);
			if (!isRowEmpty(row)) {
				// 部门
				Cell cell = row.getCell((short) 4);
				list.add(cell.getStringCellValue().toString());
			}
		}
		return list;
	}

	// 上报完成后删除上传的文件
	@Override
	public void delFolder(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {// 判断是否待删除目录是否存在
			log.info("待删除目录不存在！");
		}

		String[] content = file.list();// 取得当前目录下所有文件和文件夹
		for (String name : content) {
			File temp = new File(filePath, name);
			if (temp.isDirectory()) {// 判断是否是目录
				delFolder(temp.getAbsolutePath());// 递归调用，删除目录里的内容
				temp.delete();// 删除空目录
			} else {
				if (!temp.delete()) {// 直接删除文件
					log.info("Failed to delete " + name);
				}
			}
		}
	}
	
	
	

	@Override
	public List<Device> queryAllDevice(User user) {
		Device d = new Device();
		d.setTerm_id(0);
		d.setTerm_type(0);
		d.setDel_flag(DeviceFlag.ACTIVATED);
		ResultDomain<List<Device>> deviceResultDomain = DataService.TermInfoQuery(user, d,"");
		ResultDomain<List<Depart>> departResultDomain = DataService.DepartInfoQuery(user, -1, 0);
		List<Device> devicelist = new ArrayList<>();
		List<Depart> departList;
		if (null != deviceResultDomain.getResultData() && deviceResultDomain.getResultCode() == 0 && deviceResultDomain.getResultData().size() > 0) {
			//过滤出del_flag==0已启用的设备，不在设备列表显示,过滤掉"会议"
			devicelist = deviceResultDomain.getResultData().stream().filter(device -> device.getDel_flag() == DeviceFlag.ACTIVATED && device.getBusiness_type() == 0).collect(Collectors.toList());
			if (null != departResultDomain.getResultData() && departResultDomain.getResultCode() == 0 && departResultDomain.getResultData().size() > 0) {
				departList = departResultDomain.getResultData();
				Map<Integer, String> departMap = departList.stream().collect(Collectors.toMap(Depart::getDepart_id, Depart::getDepart_name));
				devicelist.forEach(e -> {
					int size = e.getDepart_id().size();
					StringBuilder buffer = new StringBuilder();
					if (size > 1) {
						for(int i=0;i<size;i++){
							String str = departMap.get(e.getDepart_id().get(i));
							buffer.append(str+",");
						}
						String bufferStr = buffer.toString();
						String departname = bufferStr.substring(0,bufferStr.length() - 1);
						e.setDepart_name(departname);
					} else {
						if (e.getDepart_id().get(0) == 0) {
							e.setDepart_name("未分配");
						} else {
							e.setDepart_name(departMap.get(e.getDepart_id().get(0)));
						}
					}
				});
			} else {
				log.info("查询部门接口错误或没有部门");
			}
		} else {
			log.info("查询设备接口错误或没有设备");
		}
		return devicelist;
	}


	
	public ResultDomain<FaceInfo> addFaceInfo1(AddFaceInfoParam1 addFaceInfoParam, User user) {
		ResultDomain<FaceInfo> faceInfoResultDomain = DataService.FaceInfoUpload(user, addFaceInfoParam.getFaceInfo());
		if (null != faceInfoResultDomain.getResultData() && faceInfoResultDomain.getResultCode() == 0) {
			if (null != addFaceInfoParam.getDeviceList() && addFaceInfoParam.getDeviceList().size() > 0) {
				FaceInfo faceInfo = faceInfoResultDomain.getResultData();
				if (faceInfo.getAttribute() == 0) {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
					String nowDate = df.format(new Date());
					faceInfo.setVisit_time(nowDate);
					faceInfo.setExpire_time("2038-12-31 11:59:59");
				}
				List<Integer> termIdList = addFaceInfoParam.getDeviceList();
				ResultDomain resultDomain = DataService.FacePermitConfig(user, faceInfo, termIdList, 0);
				if (resultDomain.getResultCode() != 0) {
					log.info("添加FaceInfo失败,FacePermitConfig接口错误");
					faceInfoResultDomain.setResultCode(-2);
				}
			} else {
				log.info("没有配置权限设备");
			}
		} else {
			log.info("添加FaceInfo失败,FaceInfoUpload接口错误");
			faceInfoResultDomain.setResultCode(-1);
		}
		return faceInfoResultDomain;
	}

}
