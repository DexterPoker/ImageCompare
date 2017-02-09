package dexter.poker.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
* @author DexterPoker
* @date 2016年12月28日-上午8:49:28
**/
public class FileUtil {

	/**
	 * 获取当前文件夹所有文件(包括文件夹下的子文件)
	 * @param obj
	 * @return
	 */
	public static ArrayList<File> getListAllFiles(Object obj) {
		File directory = null;
		if (obj instanceof File) {
			directory = (File) obj;
		} else {
			directory = new File(obj.toString());
		}
		ArrayList<File> files = new ArrayList<File>();
		if (directory.isFile()) {
			files.add(directory);
			return files;
		} else if (directory.isDirectory()) {
			File[] fileArr = directory.listFiles();
			for (int i = 0; i < fileArr.length; i++) {
				File fileOne = fileArr[i];
				files.addAll(getListAllFiles(fileOne));
			}
		}
		return files;
	}
	
	/**
	 * 获取当前路径下的文件(不包含文件夹下的文件)
	 * @param obj
	 * @return
	 */
	public static ArrayList<File> getListFilesCurrentDirectory(Object obj) {
		File directory = null;
		if (obj instanceof File) {
			directory = (File) obj;
		} else {
			directory = new File(obj.toString());
		}
		ArrayList<File> files = new ArrayList<File>();
		if (directory.isDirectory()) {
			File[] fileArr = directory.listFiles();
			for (int i = 0; i < fileArr.length; i++) {
				File fileOne = fileArr[i];
				if(fileOne.isFile())
					files.add(fileOne);
			}
		}
		return files;
	}
	
	/**
	 * 获取当前路径下的文件夹
	 * @param obj
	 * @return
	 */
	public static ArrayList<File> getListDirCurrentDirectory(Object obj) {
		File directory = null;
		if (obj instanceof File) {
			directory = (File) obj;
		} else {
			directory = new File(obj.toString());
		}
		ArrayList<File> files = new ArrayList<File>();
		if (directory.isDirectory()) {
			File[] fileArr = directory.listFiles();
			for (int i = 0; i < fileArr.length; i++) {
				File fileOne = fileArr[i];
				if(fileOne.isDirectory())
					files.add(fileOne);
			}
		}
		return files;
	}
	
	/**
	 * 文件对比
	 * @param f1
	 * @param f2
	 * @return
	 */
	public static boolean compareFile(File f1,File f2){ 
		FileInputStream fis1=null,fis2=null;
		try {
			fis1 = new FileInputStream(f1);
			fis2 = new FileInputStream(f2);
			int len1 = fis1.available();
			int len2 = fis2.available();
			if(len1==len2){
				byte[] data1 = new byte[len1];
				byte[] data2 = new byte[len2];
				fis1.read(data1);
				fis2.read(data2);
				for(int i = 0 ;i < len1; i++){
					if(data1[i]==data2[i]){
						continue;
					}else{
						return false;
					}
				}
				return true;
			}else{
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				fis1.close();
				fis2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 获取文件大小
	 * @param f 文件
	 * @return
	 */
	public static int getFileSize(File f){
		int fileSize = 0;
		FileInputStream fis = null;
		try {
			fis= new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			fileSize = fis.available();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileSize;
	}
	
	/**
	 * 文件复制
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static void copyFileUsingFileChannels(File source, File dest) throws IOException {    
        FileChannel inputChannel = null;    
        FileChannel outputChannel = null;    
	    try {
	        inputChannel = new FileInputStream(source).getChannel();
	        outputChannel = new FileOutputStream(dest).getChannel();
	        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
	    } finally {
	        inputChannel.close();
	        outputChannel.close();
	    }
	}
	
	/**
	 * 文件不存在则创建
	 * @param path 文件路径加文件名
	 */
	public static void fileNotExistCreate(String path){

		String filename = path;
		if(path==null||path.equals("")){
			return;
		}
		
		File file = new File(filename);
		if (!file.exists())
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static void dirNotExistCreate(String path){
		String filename = path;
		if(path==null||path.equals("")){
			return;
		}
		File file = new File(filename);
		if (!file.exists())
			file.mkdirs();
	}
	
	public static String getWallPaperPath(String username){
		String basepath = null;
		if(username.contains(":"))
			basepath = username;
		else
			basepath = "c:\\Users\\"+username;
		File basepathFile = new File(basepath);
		if(basepathFile.exists()&&basepathFile.isDirectory()){
			basepath = "c:\\Users\\" + username + "\\AppData\\Local\\Packages\\";
			List<File> fileList = FileUtil.getListDirCurrentDirectory(basepath);
			for(File f : fileList){
				if(f.getAbsolutePath().contains("Microsoft.Windows.ContentDeliveryManager_"))
					return f.getAbsolutePath() + "\\LocalState\\Assets";
			}
			return "无法找到壁纸文件夹...";
		}else{
			return "未找到用户文件夹" + basepath;
		}
	}
	
	/**
	 * 删除文件夹
	 * @param dir 文件夹
	 * @return
	 */
	public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
	
	/**
	 * 读取prop文件
	 * @param path 文件路径
	 * @return 内容map返回
	 */
	public static Map<String,String> readProperties(String path){
		Properties properties = new Properties();
        InputStream input = null;
        Map<String,String> json = new HashMap<String, String>();
        try {
        	fileNotExistCreate(path);
            input = new FileInputStream(path);
            properties.load(input);
            properties.keySet();
            for(Object key : properties.keySet()){
            	json.put(key.toString(), (String) properties.get(key));
            }
            return json;
        }catch(Exception e){
        	e.printStackTrace();
        	System.out.println(">>>>>读取配置文件异常：" + e.getMessage());
        	return null;
        }finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(">>>>>input流关闭异常：" + e.getMessage());
                }
            }
        }
	}
	
	/**
	 * 写入prop文件
	 * @param key 键
	 * @param value 值
	 * @param path 文件路径
	 */
	public static void writeProperties(String key,String value,String path){
		Properties properties = new Properties();
		File f = new File(path);
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(">>>>创建配置文件失败:" + e.getMessage());
			}
		}
		FileInputStream fis = null;
		try {
			fis= new FileInputStream(f);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		
		Map<String,String> json = readProperties(path);
		if(json == null){
			json = new HashMap<String, String>();
			properties.setProperty(key, value);
			OutputStream output = null;
	        try {
				output = new FileOutputStream(path);
				properties.store(output, "");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(">>>>>写入配置文件异常："+ e.getMessage());
			}
		}
		else{
			for(String keytmp:json.keySet()){
				properties.setProperty(keytmp, json.get(keytmp));
			}
			properties.setProperty(key, value);
			OutputStream output = null;
	        try {
				output = new FileOutputStream(path);
				properties.store(output, "");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(">>>>>写入配置文件异常："+ e.getMessage());
			}
		}
	}
	
	/**
	 * 写入prop文件
	 * @param map 内容
	 * @param path 文件路径
	 */
	public static void writeProperties(Map map,String path){
		Properties properties = new Properties();
		File f = new File(path);
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(">>>>创建配置文件失败:" + e.getMessage());
			}
		}
		FileInputStream fis = null;
		try {
			fis= new FileInputStream(f);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		
		
		Iterator entries = map.entrySet().iterator();
		while (entries.hasNext()) {
		    Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		    String value = (String)entry.getValue();
			properties.setProperty(key, value);
		}
		OutputStream output = null;
        try {
			output = new FileOutputStream(path);
			properties.store(output, "");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(">>>>>写入配置文件异常："+ e.getMessage());
		}
		
	}
	
	public static String getRealPath() throws UnsupportedEncodingException{
		String path = FileUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.substring(1, path.length());
		path = java.net.URLDecoder.decode(path, "utf-8");
		if(!path.contains("jar")){
			path = System.getProperty("user.dir");
		}else{
			path = path.substring(0,path.lastIndexOf("/"));
		}
		return path;
	}
	
	/**
	 * 去除文件小于50kb的图片(无效图片、小图标)
	 * @param it
	 */
	public static void removeUselessImage(Iterator<File> it){
		while(it.hasNext()){
			File f = it.next();
			FileInputStream fis=null;
			try {
				fis = new FileInputStream(f);
				if(fis.available()<50 *1024){
					it.remove();
					f.deleteOnExit();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
}
