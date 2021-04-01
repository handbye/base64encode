package burp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 配置窗口类，负责显示配置窗口，处理窗口消息
 */
public class ConfigDlg extends JDialog {
    private final JPanel mainPanel = new JPanel();
    private final JPanel topPanel =  new JPanel();
    private final JPanel centerPanel = new JPanel();
    private final JPanel bottomPanel = new JPanel();;
    private final JLabel lbActOnModel = new JLabel("Act on:");
    private final JCheckBox chkAllTools = new JCheckBox("All Tools");
    private final JCheckBox chkSpider = new JCheckBox("Spider");
    private final JCheckBox chkIntruder = new JCheckBox("Intruder");
    private final JCheckBox chkScanner = new JCheckBox("Scanner");
    private final JCheckBox chkRepeater = new JCheckBox("Repeater");
    private final JCheckBox chkSequencer = new JCheckBox("Sequencer");
    private final JCheckBox chkProxy = new JCheckBox("Proxy");
    private final JCheckBox chkExtender = new JCheckBox("Extender");
    private final JCheckBox chkTarget = new JCheckBox("Target");
    private final JButton btSave = new JButton("Save");
    private final JButton btCancel = new JButton("Cancel");


    public ConfigDlg(){
        initGUI();
        initEvent();
        initValue();
        this.setTitle("encoding config");
    }


    /**
     * 初始化UI
     */
    private void initGUI(){
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        centerPanel.add(lbActOnModel);
        centerPanel.add(chkAllTools);
        centerPanel.add(chkTarget);
        centerPanel.add(chkProxy);
        centerPanel.add(chkSpider);
        centerPanel.add(chkIntruder);
        centerPanel.add(chkRepeater);
        centerPanel.add(chkScanner);
        centerPanel.add(chkSequencer);
        centerPanel.add(chkExtender);

        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(btSave);
        bottomPanel.add(btCancel);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(topPanel,BorderLayout.NORTH);
        mainPanel.add(centerPanel,BorderLayout.CENTER);
        mainPanel.add(bottomPanel,BorderLayout.SOUTH);

        this.setModal(true);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.add(mainPanel);
        //使配置窗口自动适应控件大小，防止部分控件无法显示
        this.pack();
        //居中显示配置窗口
        Dimension screensize=Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(screensize.width/2-this.getWidth()/2,screensize.height/2-this.getHeight()/2,this.getWidth(),this.getHeight());
    }


    /**
     * 初始化事件
     */
    private void initEvent(){
        chkAllTools.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(chkAllTools.isSelected()){
                    chkTarget.setSelected(true);
                    chkProxy.setSelected(true);
                    chkSpider.setSelected(true);
                    chkIntruder.setSelected(true);
                    chkRepeater.setSelected(true);
                    chkScanner.setSelected(true);
                    chkExtender.setSelected(true);
                }else{
                    chkTarget.setSelected(false);
                    chkProxy.setSelected(false);
                    chkSpider.setSelected(false);
                    chkIntruder.setSelected(false);
                    chkRepeater.setSelected(false);
                    chkScanner.setSelected(false);
                    chkExtender.setSelected(false);
                }

            }
        });

        btCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfigDlg.this.dispose();
            }
        });

        btSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Config.setAct_on_all_tools(chkAllTools.isSelected());
                Config.setAct_on_target(chkTarget.isSelected());
                Config.setAct_on_proxy(chkProxy.isSelected());
                Config.setAct_on_spider(chkSpider.isSelected());
                Config.setAct_on_intruder(chkIntruder.isSelected());
                Config.setAct_on_repeater(chkRepeater.isSelected());
                Config.setAct_on_scanner(chkScanner.isSelected());
                Config.setAct_on_sequencer(chkSequencer.isSelected());
                Config.setAct_on_extender(chkExtender.isSelected());
                ConfigDlg.this.dispose();
            }
        });
    }


    /**
     * 为控件赋值
     */
    public void initValue(){
        chkAllTools.setSelected(Config.isAct_on_all_tools());
        chkTarget.setSelected(Config.isAct_on_target());
        chkProxy.setSelected(Config.isAct_on_proxy());
        chkSpider.setSelected(Config.isAct_on_spider());
        chkIntruder.setSelected(Config.isAct_on_intruder());
        chkRepeater.setSelected(Config.isAct_on_repeater());
        chkScanner.setSelected(Config.isAct_on_scanner());
        chkSequencer.setSelected(Config.isAct_on_sequencer());
        chkExtender.setSelected(Config.isAct_on_extender());
    }
}