package dexter.poker.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dexter.poker.action.DirChoose;
import dexter.poker.action.ImageCompareRunExact;
import dexter.poker.action.ImageCompareRunSimple;
import dexter.poker.util.FileUtil;

/**
* @author DexterPoker
* @date 2017年1月3日-下午4:52:02
**/
public class MainUI extends JFrame{

	private static final long serialVersionUID = 8860759828332187027L;
	private static final String description = "简要说明：\r\n"
			+ "1.如果用户文件夹在c盘,只需要输入用户文件夹名称(windows登录用户名称),"
			+ "若用户文件夹不在c盘,就要需要输入完整的文件夹路径(如:d:\\users\\zhangsan);\r\n"
			+ "2.简单收集：速度较快(只需几秒一般,随着保存文件里图片增多而变慢),但精准度不高,没有比对图片相似度(win10生成的聚焦图片存在相同);\r\n"
			+ "3.精确收集：速度较慢(需要几分钟或更多,随着保存文件里图片增多而变慢),但精准度较高,基本不存在相似图片重复问题,请耐心等待;\r\n"
			+ "4.完成后,请关闭程序,到保存路径中查看图片;\r\n"
			+ "5.第一次需要填写配置,之后启动会根据之前配置自动进行收集操作,如要更改配置可删除src文件夹;\r\n"
			+ "6.若程序出错,请重启程序尝试.\r\n";
	private static String command = "";
	
	JFileChooser fc = new JFileChooser();
//	JMenuBar menuBar = new JMenuBar();
//	JMenu operationMenu = new JMenu("操作");
//	JMenuItem simpleItem = new JMenuItem("简单收集");
//	JMenuItem exactItem = new JMenuItem("精确收集");
//	JMenuItem exitItem = new JMenuItem("退出");
	
	Font font = new Font("Microsoft YAHEI", Font.LAYOUT_LEFT_TO_RIGHT, 17);
	
//	JButton simple = new JButton("简单收集");
//	JButton exact = new JButton("精确收集");
	JButton start = new JButton("开始");
	
	public static JTextArea logArea = new JTextArea();
	
	JPanel userPanel = new JPanel();
	JPanel savePanel = new JPanel();
	JPanel buttonPanel = new JPanel();
	
	JPanel groupPanel = new JPanel();
	JScrollPane scroll = new JScrollPane(logArea);
	
	JTextField userField = new JTextField();
	JTextField saveFileField = new JTextField();
	JTextField tmpFileFiled = new JTextField();
	
	JLabel userLabel = new JLabel("  win10用户名称：");
	JLabel saveLabel = new JLabel("  图片最终保存路径：");
	
	JButton savePathButton = new JButton("选择");
	
	JProgressBar progressBar = new JProgressBar();
	
	JRadioButton simpleRadio = new JRadioButton("简单收集");
	JRadioButton exactRadio = new JRadioButton("精确收集");
	ButtonGroup buttonGroup = new ButtonGroup();
	
	
	public MainUI(){
		Container container = this.getContentPane();
//		operationMenu.add(simpleItem);
//		operationMenu.add(exactItem);
//		operationMenu.addSeparator();
//		operationMenu.add(exitItem);
//		menuBar.add(operationMenu);
//		setJMenuBar(menuBar);
		
		userLabel.setFont(font);
		saveLabel.setFont(font);
//		simple.setFont(font);
//		exact.setFont(font);
		simpleRadio.setFont(font);
		exactRadio.setFont(font);
		start.setFont(font);
		savePathButton.setFont(font);
		userField.setFont(font);
		saveFileField.setFont(font);
		
		container.setLayout(new BorderLayout());
		this.setTitle("win10自带壁纸收集");
		userPanel.setLayout(new BorderLayout());
		userPanel.add(userLabel,BorderLayout.WEST);
		userField.setSize(100, 20);
		userPanel.add(userField,BorderLayout.CENTER);
		
		savePanel.setLayout(new GridLayout(1, 3));
		savePanel.add(saveLabel);
		savePanel.add(saveFileField);
		savePanel.add(savePathButton);
		
		buttonPanel.setLayout(new GridLayout(1, 3));
		buttonGroup.add(simpleRadio);
		buttonGroup.add(exactRadio);
		buttonPanel.add(simpleRadio);
		buttonPanel.add(exactRadio);
//		buttonPanel.add(simple);
//		buttonPanel.add(exact);
		buttonPanel.add(start);
		
		
		groupPanel.setLayout(new BorderLayout());
		groupPanel.add(userPanel,BorderLayout.NORTH);
		groupPanel.add(savePanel,BorderLayout.CENTER);
		groupPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		container.add(groupPanel, BorderLayout.NORTH);
		logArea.setFont(font);
		logArea.setMargin(new Insets(10, 20, 10, 20));
		logArea.setDisabledTextColor(new Color(0, 0, 0));
		logArea.setText(description);
		logArea.setLineWrap(true);
		logArea.setEnabled(false);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		container.add(scroll, BorderLayout.CENTER);
		
		  
		progressBar.setStringPainted(true);
		container.add(progressBar, BorderLayout.SOUTH);
		
		savePathButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("选择")){
					String dir = DirChoose.open(fc);
					saveFileField.setText(dir);
				}
			}
		});
		
		simpleRadio.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				command = e.getActionCommand();
			}
		});
		
		exactRadio.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				command = e.getActionCommand();
			}
		});
		
		start.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("开始")){
					if(buttonGroup.getSelection()==null){
						logArea.setText("请选择收集方式!!!");
						return;
					}
					if(command.equals("简单收集")){
						String userName = userField.getText();
						String savePath = saveFileField.getText();
						if(userName == null||userName.equals("")){
							logArea.setText("用户名称不能为空!!!\r\n");
						}
						else if(savePath == null||savePath.equals("")){
							logArea.setText("图片保存路径不能为空!!!\r\n");
						}else{
							logArea.setText("开始\r\n");
							ImageCompareRunSimple runSimple = new ImageCompareRunSimple();
							runSimple.setLogArea(logArea);
							runSimple.setSavepath(savePath);
							runSimple.setUsername(userName);
							runSimple.setUserField(userField);
							runSimple.setSaveField(saveFileField);
							runSimple.setChooseButton(savePathButton);
							runSimple.setExact(exactRadio);
							runSimple.setSimple(simpleRadio);
							runSimple.setStart(start);
							runSimple.setProgressBar(progressBar);
							Thread t = new Thread(runSimple);
							t.start();
						}
					}else if (command.equals("精确收集")){
						String userName = userField.getText();
						String savePath = saveFileField.getText();
						if(userName == null||userName.equals("")){
							logArea.setText("用户名称不能为空!!!\r\n");
						}
						else if(savePath == null||savePath.equals("")){
							logArea.setText("图片保存路径不能为空!!!\r\n");
						}else{
							logArea.setText("开始\r\n");
							ImageCompareRunExact runExact = new ImageCompareRunExact();
							runExact.setLogArea(logArea);
							runExact.setSavepath(savePath);
							runExact.setUsername(userName);
							runExact.setUserField(userField);
							runExact.setSaveField(saveFileField);
							runExact.setChooseButton(savePathButton);
							runExact.setExact(exactRadio);
							runExact.setSimple(simpleRadio);
							runExact.setStart(start);
							runExact.setProgressBar(progressBar);
							Thread t = new Thread(runExact);
							t.start();
						}
					}
				}
			}
		});
		
		logArea.getDocument().addDocumentListener(new DocumentListener() {
			
			public void removeUpdate(DocumentEvent e) {
				logArea.setCaretPosition(logArea.getDocument().getLength());
			}
			
			public void insertUpdate(DocumentEvent e) {
				logArea.setCaretPosition(logArea.getDocument().getLength());
			}
			
			public void changedUpdate(DocumentEvent e) {
				logArea.setCaretPosition(logArea.getDocument().getLength());
			}
		});
		
		Map<String,String> map = FileUtil.readProperties(System.getProperty("user.dir") + "\\src\\init.properties");
		
		if(map!=null && map.get("username")!=null && !map.get("username").equals("")){
			userField.setText(map.get("username"));
		}
		if(map!=null && map.get("path")!=null && !map.get("path").equals("")){
			saveFileField.setText(map.get("path"));
		}
		if(map!=null && map.get("way")!=null && !map.get("way").equals("")){
			if(map.get("way").equals("simple")){
				simpleRadio.setSelected(true);
				command = "简单收集";
				start.doClick();
			}
			if(map.get("way").equals("exact")){
				exactRadio.setSelected(true);
				command = "精确收集";
				start.doClick();
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		MainUI mainUI = new MainUI();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		mainUI.setSize(600,400);
		int x = (screenWidth - mainUI.getSize().width) / 2;
		int y = (screenHeight - mainUI.getSize().height) / 2;
		
		mainUI.setVisible(true);
		mainUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainUI.setLocation(x, y);
		mainUI.setResizable(false);
		
		mainUI.setIconImage(Toolkit.getDefaultToolkit().getImage(
				MainUI.class.getClassLoader().getResource("icon.jpg")));
		
	}
	
}
