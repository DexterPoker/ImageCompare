package dexter.poker.action;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;

/**
* @author DexterPoker
* @date 2017年1月5日-上午11:13:43
**/
public class DirChoose {
	
	public static String open(JFileChooser fc){
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);  
        fc.showDialog(new JLabel(), "选择文件夹");
        File file=fc.getSelectedFile();  
        if(file == null)
        	return null;
        if(file.isDirectory()){
            return file.getAbsolutePath();
        }
        else
        	return null;
	}
}
