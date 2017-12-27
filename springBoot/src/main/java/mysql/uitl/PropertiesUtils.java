package mysql.uitl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author: hany
 */
public class PropertiesUtils {
	 private static Logger log = LoggerFactory.getLogger(PropertiesUtils.class);
	/**
	 * 
	 * getValue 根据key 获取value
	 * 
	 * @author hany
	 * @param key
	 * @param proName
	 *            properties存放的路径
	 * @return
	 * @since JDK 1.7
	 */
	public static String getValue(String key, String proName) {

		String configPath = PropertiesUtils.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		try {
			configPath = URLDecoder.decode(configPath, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
		String filePathString = "";
		if (configPath.indexOf("/WEB-INF") != -1) {
			if (configPath.indexOf("/") > -1) {
				configPath = configPath.substring(0,
						configPath.lastIndexOf("/WEB-INF"));
			} else {
				configPath = configPath.substring(0,
						configPath.lastIndexOf("\\WEB-INF"));
			}
			filePathString = StringUtils.getParameterToString(configPath,
					File.separator, "WEB-INF", File.separator, "config"
							+ File.separator + proName + ".properties");
		}

		FileInputStream fis = null;
		InputStream in = null;
		ResourceBundle resb = null;
		try {
			if (filePathString != null && !filePathString.equals("")) {
				System.out.println(filePathString);
				fis = new FileInputStream(filePathString);
				in = new BufferedInputStream(fis);
				resb = new PropertyResourceBundle(in);
				return resb.getString(key);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
		return null;
	}


	/**
	 * 读取properties的全部信息
	 * 
	 * @param filePath
	 *            文件路径
	 */
	public static Map<String, String> readProperties(String filePath) {
		Properties props = new Properties();
		Map<String, String> map = new HashMap<String, String>();
		FileInputStream fileIn = null;
		InputStream in = null;
		try {
			fileIn = new FileInputStream(filePath);
			in = new BufferedInputStream(fileIn);
			props.load(in);
			Enumeration<?> en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String Property = props.getProperty(key);
				map.put(key, Property);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}finally{
			if(fileIn != null){
				try {
					fileIn.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {	log.error(e.getMessage());
				}
			}
		}
		return map;
	}

	/**
	 * 读取properties的全部信息
	 * 
	 * @param fileName
	 *            文件名称 必须放在资源文件名录下也就是class根目录
	 */
	public static Map<String, String> getProperties(String fileName) {
		Properties props = new Properties();
		Map<String, String> map = new HashMap<String, String>();
		InputStream in = null;
		try {
			ClassLoader classLoader = PropertiesUtils.class.getClassLoader();
			if(classLoader != null){
				in = new BufferedInputStream(classLoader.getResourceAsStream(fileName));
				props.load(in);
				Enumeration<?> en = props.propertyNames();
				while (en.hasMoreElements()) {
					String key = (String) en.nextElement();
					String Property = props.getProperty(key);
					map.put(key, Property);
				}
				return map;
			}else{
				return null;
			}
		} catch (IOException e) {
				log.error(e.getMessage());
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
						log.error(e.getMessage());
				}
			}
		}
		return null;
	}

	/**
	 * 写入properties信息
	 * 
	 * @param filePath
	 *            文件路径
	 * @param parameterName
	 *            属性名
	 * @param parameterValue
	 *            属性值
	 */
	public static void writeProperties(String filePath, String parameterName,
			String parameterValue) {
		Properties prop = new Properties();
		InputStream fis = null;
		OutputStream fos = null;


		String configPath = PropertiesUtils.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		try {
			configPath = URLDecoder.decode(configPath, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
		String filePathString = "";
		if (configPath.indexOf("/WEB-INF") != -1) {
			if (configPath.indexOf("/") > -1) {
				configPath = configPath.substring(0,
						configPath.lastIndexOf("/WEB-INF"));
			} else {
				configPath = configPath.substring(0,
						configPath.lastIndexOf("\\WEB-INF"));
			}
			filePathString = StringUtils.getParameterToString(configPath,
					File.separator, "WEB-INF", File.separator, "config"
							+ File.separator + filePath + ".properties");
		}

		try {
			fis = new FileInputStream(filePathString);
			// 从输入流中读取属性列表（键和元素对）
			prop.load(fis);
			// 调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
			// 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
			fos = new FileOutputStream(filePathString);
			prop.setProperty(parameterName, parameterValue);
			// 以适合使用 load 方法加载到 Properties 表中的格式，
			// 将此 Properties 表中的属性列表（键和元素对）写入输出流
			prop.store(fos, "");
		} catch (IOException e) {
			e.printStackTrace();
				log.error(e.getMessage());
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
						log.error(e.getMessage());
				}
			}
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
						log.error(e.getMessage());
				}
			}
		}
	}

	/**
	 * 通过去匹配Map的键取出对应的值
	 * 
	 * @param key
	 * @param map
	 * @return
	 */
	public static String equalsKeyGetValue(String key, Map<String, String> map) {
		String value = "";
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (key.equals(entry.getKey())) {
				value = entry.getValue();
			}
		}
		return value;
	}
	
	public static ResourceBundle getResourceBundle(String proName) {

		String configPath = PropertiesUtils.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		try {
			configPath = URLDecoder.decode(configPath, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
		String filePathString = "";
		if (configPath.indexOf("/WEB-INF") != -1) {
			if (configPath.indexOf("/") > -1) {
				configPath = configPath.substring(0,
						configPath.lastIndexOf("/WEB-INF"));
			} else {
				configPath = configPath.substring(0,
						configPath.lastIndexOf("\\WEB-INF"));
			}
			filePathString = StringUtils.getParameterToString(configPath,
					File.separator, "WEB-INF", File.separator, "config"
							+ File.separator + proName + ".properties");
		}

		FileInputStream fis = null;
		InputStream in = null;
		ResourceBundle resb = null;
		try {
			if (filePathString != null && !filePathString.equals("")) {
				fis = new FileInputStream(filePathString);
				in = new BufferedInputStream(fis);
				resb = new PropertyResourceBundle(in);
				return resb;
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
				log.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
				log.error(e.getMessage());
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
						log.error(e.getMessage());
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
						log.error(e.getMessage());
				}
			}
		}
		return null;
	}
}
