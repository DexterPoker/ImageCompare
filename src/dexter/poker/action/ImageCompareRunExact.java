package dexter.poker.action;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import dexter.poker.util.ConvertUtil;
import dexter.poker.util.FileUtil;
import dexter.poker.util.ImageCompareUtil;
import dexter.poker.util.TimerUtil;

/**
* @author DexterPoker
* @date 2017年1月4日-下午4:55:21
**/
public class ImageCompareRunExact implements Runnable{

private JTextArea logArea;
	
	private String username;
	
	private String savepath;
	
	private JTextField userField;
	
	private JTextField saveField;
	
	private JButton chooseButton;
	
	private JRadioButton simple;
	
	private JRadioButton exact;
	
	private JButton start;
	
	public JButton getStart() {
		return start;
	}

	public void setStart(JButton start) {
		this.start = start;
	}

	private JProgressBar progressBar;
	
	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public JRadioButton getSimple() {
		return simple;
	}

	public void setSimple(JRadioButton simple) {
		this.simple = simple;
	}

	public JRadioButton getExact() {
		return exact;
	}

	public void setExact(JRadioButton exact) {
		this.exact = exact;
	}

	public JTextField getUserField() {
		return userField;
	}

	public void setUserField(JTextField userField) {
		this.userField = userField;
	}

	public JTextField getSaveField() {
		return saveField;
	}

	public void setSaveField(JTextField saveField) {
		this.saveField = saveField;
	}

	public JButton getChooseButton() {
		return chooseButton;
	}

	public void setChooseButton(JButton chooseButton) {
		this.chooseButton = chooseButton;
	}

	public JTextArea getLogArea() {
		return logArea;
	}

	public void setLogArea(JTextArea logArea) {
		this.logArea = logArea;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSavepath() {
		return savepath;
	}

	public void setSavepath(String savepath) {
		this.savepath = savepath;
	}

	public void run() {
		disableUI();
		double i = 0;
		progressBar.setValue(0);
		FileUtil.fileNotExistCreate(System.getProperty("user.dir") + "\\src\\pixels.properties");
		Map<String,String> oldMap = FileUtil.readProperties(System.getProperty("user.dir") + "\\src\\pixels.properties");
		boolean isFirst = oldMap == null ? true:false;
		Long startTime = System.currentTimeMillis();
		TimerUtil timer = new TimerUtil();
		logArea.append("此方法耗费时间较长请耐心等待...\r\n");
		logArea.append("正在初始化...\r\n");
		timer.add("初始化");
		String imgSourcePath = FileUtil.getWallPaperPath(username);
		if(!new File(imgSourcePath).isDirectory()){
			logArea.append(imgSourcePath + "\r\n");
			enableUI();
			return;
		}
		
		if(!new File(savepath).exists()){
			logArea.append("保存路径不存在或错误,请检查...\r\n");
			enableUI();
			return;
		}
		
		List<File> filesUseful = new ArrayList<File>();
		List<File> filesUseless = new ArrayList<File>();
		List<File> filesNew = new ArrayList<File>();
		List<File> filesSource = new ArrayList<File>();
		
		String usefulPath = savepath;
		String uselessPath = null;
		try {
			uselessPath = FileUtil.getRealPath() + "\\useless";
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String newPath = "d:\\tmpimg-dexter\\";
		FileUtil.dirNotExistCreate(newPath);
		timer.add("查找新壁纸并复制图片文件");
		i=1;
		progressBar.setValue((int)i);
		logArea.append(timer.printLastItem() + "\r\n");
		logArea.append("正在查找新壁纸并复制图片文件...\r\n");
		filesSource = FileUtil.getListAllFiles(imgSourcePath);
		
		for(File f : filesSource){
			try {
				i=i+2.0/filesSource.size();
				FileUtil.copyFileUsingFileChannels(f, new File(newPath + File.separator + f.getName() + ".jpg"));
				progressBar.setValue((int)i);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		filesUseless = FileUtil.getListFilesCurrentDirectory(uselessPath);
		filesNew = FileUtil.getListFilesCurrentDirectory(newPath);
		timer.add("去除无用图片文件");
		logArea.append(timer.printLastItem() + "\r\n");
		logArea.append("正在去除无用图片文件...\r\n");
		Iterator<File> fileIter = filesNew.iterator();
		FileUtil.removeUselessImage(fileIter);
		fileIter = filesNew.iterator();
		while(fileIter.hasNext()){
			i=i+2.0/filesNew.size();
			File ff = fileIter.next();
			for(File f : filesUseless){
				if(FileUtil.compareFile(f, ff)){
					ff.deleteOnExit();
					fileIter.remove();
					break;
				}
			}
			progressBar.setValue((int)i);
		}
		timer.add("获取图片文件的图像指纹序列");
		logArea.append(timer.printLastItem() + "\r\n");
		logArea.append("正在获取图片文件的图像指纹序列...\r\n");
		List<int[]> valueUseful = new ArrayList<int[]>();
		Map<String , int[]> newMap = new HashMap<String, int[]>();
		filesUseful = FileUtil.getListFilesCurrentDirectory(usefulPath);
		
		if(oldMap.size()>0){
			int[] tmpPixels;
			for(File f : filesUseful){
				try {
					if(oldMap.containsKey(f.getAbsolutePath())){
						valueUseful.add(ConvertUtil.string2IntArray(oldMap.get(f.getAbsolutePath())));
					}else{
						i=i+91.0/(filesUseful.size()-oldMap.size()+filesNew.size());
						tmpPixels = ImageCompareUtil.getPixelDeviateWeightsArray(f);
						valueUseful.add(tmpPixels);
						oldMap.put(f.getAbsolutePath(), ConvertUtil.intArray2String(tmpPixels));
						progressBar.setValue((int)i);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else{
			int[] tmpPixels;
			for(File f : filesUseful){
				try {
					i=i+91.0/(filesNew.size()+filesUseful.size());
					tmpPixels = ImageCompareUtil.getPixelDeviateWeightsArray(f);
					valueUseful.add(tmpPixels);
					oldMap.put(f.getAbsolutePath(), ConvertUtil.intArray2String(tmpPixels));
					progressBar.setValue((int)i);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		FileUtil.writeProperties(oldMap, System.getProperty("user.dir") + "\\src\\pixels.properties");

		for(File f : filesNew){
			try {
				if(isFirst)
					i=i+91.0/(filesNew.size()+filesUseful.size());
				else
					i=i+91.0/(filesNew.size()+filesUseful.size()-oldMap.size());
				newMap.put(f.getAbsolutePath(), ImageCompareUtil.getPixelDeviateWeightsArray(f));
				progressBar.setValue((int)i);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		timer.add("根据图片相似度筛选");
		logArea.append(timer.printLastItem() + "\r\n");
		logArea.append("正在根据图片相似度筛选...\r\n");
		Iterator entries = newMap.entrySet().iterator();
		entries = newMap.entrySet().iterator();
		while (entries.hasNext()) {
			i=i+2.0/newMap.size();
		    Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		    int[] value = (int[])entry.getValue();
		    
		    for(int[] useful : valueUseful){
			    int hammingDistance = ImageCompareUtil.getHammingDistance(value, useful);
				double similarity = ImageCompareUtil.calSimilarity(hammingDistance);
				if(similarity>0.9){
					entries.remove();
					new File(key).deleteOnExit();
					break;
				}
		    }
		    progressBar.setValue((int)i);
		}
		fileIter = filesNew.iterator();
		timer.add("复制图片到保存路径");
		logArea.append(timer.printLastItem() + "\r\n");
		logArea.append("正在复制图片到保存路径...\r\n");
		
		entries = newMap.entrySet().iterator();
		while(entries.hasNext()){
			Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
			File ff = new File(key);
			i+=2.0/newMap.size();
			try {
				FileUtil.copyFileUsingFileChannels(ff, new File(savepath + File.separator + ff.getName()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			progressBar.setValue((int)i);
		}
		
		FileUtil.deleteDir(new File(newPath));
		timer.finish();
		logArea.append(timer.printLastItem() + "\r\n");
		
		FileUtil.writeProperties("username", username, System.getProperty("user.dir") + "\\src\\init.properties");
		FileUtil.writeProperties("path", savepath, System.getProperty("user.dir") + "\\src\\init.properties");
		FileUtil.writeProperties("way", "exact", System.getProperty("user.dir") + "\\src\\init.properties");

		progressBar.setValue(100);
		Long endTime = System.currentTimeMillis();
		logArea.append("共耗时:" + (endTime - startTime) + "ms\r\n");
		logArea.append("新壁纸已经存放在保存路径中,请关闭程序到文件夹下查看.\r\n");
		logArea.append("程序将在5s后自行关闭.\r\n");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String cmd;
		if(filesUseful.size()>0)
			cmd = "cmd /c start explorer /select, " + savepath + File.separator + filesUseful.get(0).getName();
		else
			cmd = "cmd /c start explorer /select, " + savepath;
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		enableUI();
		System.exit(0);
	}
	
	private void disableUI(){
		start.setEnabled(false);
		simple.setEnabled(false);
		exact.setEnabled(false);
		userField.setEnabled(false);
		saveField.setEnabled(false);
		chooseButton.setEnabled(false);
	}
	
	private void enableUI(){
		start.setEnabled(true);
		simple.setEnabled(true);
		exact.setEnabled(true);
		userField.setEnabled(true);
		saveField.setEnabled(true);
		chooseButton.setEnabled(true);
	}
}
