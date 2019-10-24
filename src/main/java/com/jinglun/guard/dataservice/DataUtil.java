package com.jinglun.guard.dataservice;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Key;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import com.jinglun.guard.GuardApplication;
import com.sun.jna.ptr.IntByReference;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j

public class DataUtil {
	public static byte[] get24Byte(byte[] oldkey) {
		byte[] newkey;
		if (oldkey.length == 16) {
			newkey = new byte[24];
			System.arraycopy(oldkey, 0, newkey, 0, 16);
			System.arraycopy(oldkey, 0, newkey, 16, 8);
		} else {
			newkey = oldkey;
		}

		return newkey;
	}
	
	private static int char2int(char ch) {
		int value = -1;
		if (ch >= '0' && ch <= '9') {
			value = ch - '0';
		} else if (ch >= 'a' && ch <= 'f') {
			value = ch - 'a' + 10;
		} else if (ch >= 'A' && ch <= 'F') {
			value = ch - 'A' + 10;
		}
		return value;
	}

	public static byte[] str2byteArr(String str) {
		byte[] bArr = new byte[str.length() >> 1];
		for (int i = 0; i < str.length();) {
			int bArrIndex = i / 2;
			int h = char2int(str.charAt(i++));
			int l = char2int(str.charAt(i++));
			bArr[bArrIndex] = (byte) (h * 0x10 + l);
		}
		return bArr;
	}
	
	static byte[] promotion16To24(byte[] inputKey) throws IllegalArgumentException {
        if (inputKey == null || inputKey.length != 16) {
            throw new IllegalArgumentException("input error");
        }
        byte[] outputKey = new byte[24];
        System.arraycopy(inputKey, 0, outputKey, 0, 16);
        System.arraycopy(inputKey, 0, outputKey, 16, 8);
        return outputKey;
    }
	
	/**
	 * 3DES
	 * 
	 * @param key
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] des3EncodeECB(byte[] key, byte[] data) throws Exception {
		Key desKey;
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
		desKey = keyFactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede/ECB/NoPadding");

		cipher.init(Cipher.ENCRYPT_MODE, desKey);

		return cipher.doFinal(data);
	}
    
    // 3DESECB加密,key必须是长度大于等于 3*8 = 24 位哈
    public static byte[] encryptThreeDESECB(final String src, final byte[] key) throws Exception {
        final DESedeKeySpec dks = new DESedeKeySpec(key);
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        final SecretKey securekey = keyFactory.generateSecret(dks);

        final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, securekey);
        final byte[] b = cipher.doFinal(src.getBytes());

        //final BASE64Encoder encoder = new BASE64Encoder();
        return Base64.encodeBase64(b);//.replaceAll("\r", "").replaceAll("\n", "");
    }
    
    public static byte[] toByteArray(String hexString) {
		if (StringUtils.isEmpty(hexString))
			throw new IllegalArgumentException("this hexString must not be empty");
		
		String strHex = hexString.toLowerCase();
		final byte[] byteArray = new byte[strHex.length() / 2];
		int k = 0;
		for (int i = 0; i < byteArray.length; i++) {// 因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
			byte high = (byte) (Character.digit(strHex.charAt(k), 16) & 0xff);
			byte low = (byte) (Character.digit(strHex.charAt(k + 1), 16) & 0xff);
			byteArray[i] = (byte) (high << 4 | (low & 0xff));
			k += 2;
		}
		return byteArray;
	}
	
	public static String byteArr2str(byte[] bArr) {
		String tmp = "";
		for (byte b : bArr) {
			tmp += String.format("%02x", b);
		}
		return tmp;
	}
	
	private Properties getProperties(){
		Properties pro = new Properties();
		InputStream in = getClass().getResourceAsStream("/application.properties");
		try {
			pro.load(in);
		} catch (IOException e) {
			log.error("读取application.properties配置文件失败！");
		}
		return pro;
	}
	
	public static String EncryptPwd(String pwd, String key) {
		if(pwd == null || pwd.isEmpty()) return "";
		byte[] pwdbytes = new byte[16];
		System.arraycopy(pwd.getBytes(), 0, pwdbytes, 0, pwd.getBytes().length);
		String base64Pwd = null;
		try {
			byte[] newkey = DataUtil.get24Byte(DataUtil.str2byteArr(key));
			base64Pwd = new String(Base64.encodeBase64(DataUtil.des3EncodeECB(newkey, pwdbytes)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.debug(e.getMessage());
		}
		
		return base64Pwd;
	}

	/** 将身份证号码转换为16进制字符串
	 * @param id 身份证号码
	 * @return 转换后内容，如失败则返回null，需判断
	 */
	public static String Id2HexStr(String id) {
		String strId = "";
		if(StringUtils.isEmpty(id) || id.trim().length() != 18 && id.trim().length() != 15) {
			log.debug("id's length is invalid!");
			return null;
		}
		
		if(id.endsWith("X")) {
			strId = id.trim().substring(0, id.length() - 1);
		} else {
			strId = id;
		}
		
		Long l = 0l;
		try {
			l = Long.parseLong(strId);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			return null;
		}
		
		return String.format("%016X", l);
	}
	
	public static String HexStr2Id(String hexId) {
		if(StringUtils.isEmpty(hexId) || hexId.trim().length() != 16) {
			log.debug("id's length is invalid!");
			return null;
		}
		
		Long l = 0l;
		try {
			l = Long.parseLong(hexId.trim(), 16);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			return null;
		}

		return l.toString().length() == 17 ? l + "X" : l.toString();
	}
	
	public static String EncodeImg(String imgPath) {
		byte[] photoBin = null;
		try {
			File photo = new File(imgPath);
			if(photo.exists()) {
				FileInputStream fis = new FileInputStream(photo);
				photoBin = new byte[fis.available()];
				int readLen = 0;
				while(readLen < photoBin.length) {
					readLen += fis.read(photoBin, readLen, photoBin.length);
				}
				fis.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			return null;
		}
		
		if(photoBin != null) {
			return new String(Base64.encodeBase64(photoBin));
		} else {
			return null;
		}
	}
	
	public static byte[] compress(String str, String encoding) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            gzip.close();
        } catch ( Exception e) {
        	log.debug(e.getMessage());
        }
        return out.toByteArray();
    }
	
	public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (Exception e) {
        	log.debug(e.getMessage());
        }
        return out.toByteArray();
    }
	
	public static boolean isIPAddress(String ipaddr) {
		Pattern pattern = Pattern.compile(
				"\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher m = pattern.matcher(ipaddr);
		return m.matches();
	}
	
	public static boolean isIPPort(int port) {
		return port > 0 && port < 65535;
	}
	
	public static String getAbsPath() {
		return DataUtil.class.getResource("").toString();
	}
	
	public static String SendRequest(String parmJson, String serverName) {
		URL targetUrl = null;
		HttpURLConnection httpURLConnection = null;
		OutputStream outputStream = null;
		String output = "";
		InputStream is = null;
		InputStreamReader isr = null;
		
		Properties pro = new DataUtil().getProperties();
		String servIp = null;
		String servPort = null;

		if(StringUtils.isEmpty(serverName)) {
			if(!StringUtils.isEmpty(GuardApplication.ip)) {
				servIp = GuardApplication.ip;
			} else {
				servIp = pro.getProperty("web_server_addr").trim();
			}
		} else {
			if(!StringUtils.isEmpty(GuardApplication.s_matchServerIP) && isIPAddress(GuardApplication.s_matchServerIP)) {
				servIp = GuardApplication.s_matchServerIP;
			} else if(!StringUtils.isEmpty(GuardApplication.ip) && isIPAddress(GuardApplication.ip)) {
				servIp = GuardApplication.ip;
			} else {
				servIp = pro.getProperty(serverName + "_addr").trim();
			}
		}

		if(StringUtils.isEmpty(serverName)) {
			if(!StringUtils.isEmpty(GuardApplication.port)) {
				servPort = GuardApplication.port;
			} else {
				servPort = pro.getProperty("web_server_port").trim();
			}
		} else {
			servPort = pro.getProperty(serverName + "_port").trim();
		}
		
		log.trace("ip地址：" + servIp + "，端口：" + servPort);
		String targetURL = "http://" + servIp + ":" + servPort + "/" ;
		try {
			targetUrl = new URL(targetURL);
			httpURLConnection = (HttpURLConnection) targetUrl.openConnection();
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Content-Type", "application/json");
			httpURLConnection.setDoInput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setConnectTimeout(100000);
			httpURLConnection.setReadTimeout(100000);
			
			outputStream = httpURLConnection.getOutputStream();
			parmJson = parmJson.replace("\\\\", "");
			outputStream.write(parmJson.getBytes());
			outputStream.flush();

			is = httpURLConnection.getInputStream();

			//判断返回内容是否使用了gzip进行压缩
			Map<String, List<String>> httpHeader = httpURLConnection.getHeaderFields();
			List<String> encoding = httpHeader.get("Content-Encoding");
			if(encoding != null && ("gzip").equals(encoding.get(0))) {
				int len = Integer.parseInt(httpHeader.get("Content-Length").get(0));
				byte[] bContent = new byte[len];
				int readLen = 0;
				while(readLen < len) {
					readLen += is.read(bContent, readLen, len - readLen);
				}
				output = new String(uncompress(bContent));
			} else {
				isr = new InputStreamReader(is);
				BufferedReader responseBuffer = new BufferedReader(isr);
				int code = httpURLConnection.getResponseCode();
				if (code != 200) {
					throw new DataServiceException("Failed : HTTP error code : " + httpURLConnection.getResponseCode());
				}
				
				String str = null;
				while((str = responseBuffer.readLine()) != null) {
					output += str;
				}
			}
		} catch (MalformedURLException e) {
			log.error("请求URL地址无效," + e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			try {
				if (isr != null) {
					isr.close();
				}
				if(is != null){
					is.close();
				}
				if(outputStream != null){
					outputStream.close();
				}
				if(httpURLConnection !=null){
					httpURLConnection.disconnect();
				}
			} catch (IOException e) {
				log.debug(e.getMessage());
			}
		}
		return output;
	}
	
	public static JSONObject SendRequest(JSONObject jsonObject) {
		String input = jsonObject.toString();
		String output = SendRequest(input, null);
		JSONObject retObj = null;
		log.trace("command:[" + jsonObject.getString("command") + "], ret json:" + output);
		try {
			retObj = JSONObject.fromObject(output);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		
		return retObj;
	}
	
	public static JSONObject SendRequest(JSONObject jsonObject, String serverName) {
		String input = jsonObject.toString();
		String output = SendRequest(input, serverName);
		JSONObject retObj = null;
		
		log.trace("json:" + output);
		try {
			retObj = JSONObject.fromObject(output);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		
		return retObj;
	}
	
	public static String decodeWlt(String base64Wlt) {
		if(StringUtils.isEmpty(base64Wlt) || !base64Wlt.startsWith("V0xmAH4AMgAA")) return null;
		String strPhotoBase64 = null;
		int ret = 0;
		byte[] retBytes = new byte[1024 * 50];
		IntByReference bmplen = new IntByReference();
		bmplen.setValue(0);
		try {
			byte[] wltBytes = Base64.decodeBase64(base64Wlt);
			if(wltBytes == null || wltBytes.length == 0) return null;
			ret = CLibrary.library.dewltbuf(wltBytes, retBytes, bmplen);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
		if (ret == 1 && bmplen.getValue() > 0) {
			byte[] bmpBytes = new byte[bmplen.getValue()];
			System.arraycopy(retBytes, 0, bmpBytes, 0, bmplen.getValue());
			strPhotoBase64 = new String(Base64.encodeBase64(bmpBytes));
		}
		
		return strPhotoBase64;
	}
}
