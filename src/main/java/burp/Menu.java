package burp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单类，负责显示菜单，处理菜单事件
 */
public class Menu implements IContextMenuFactory {
    public List<JMenuItem> createMenuItems(final IContextMenuInvocation invocation) {
        List<JMenuItem> menus = new ArrayList();
        JMenu encodeMenu = new JMenu("base64encode");
        JMenuItem encode = new JMenuItem("Encoding request body");
        JMenuItem decode = new JMenuItem("Decoding request body");
        JMenuItem config = new JMenuItem("Config");
        encodeMenu.add(encode);
        encodeMenu.add(decode);
        encodeMenu.addSeparator();
        encodeMenu.add(config);

        //若数据包无法编辑，则将编码解码菜单项设置为禁用
        if(invocation.getInvocationContext() != IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST){
            encode.setEnabled(false);
            decode.setEnabled(false);
        }

        encode.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent arg0) {
                IHttpRequestResponse iReqResp = invocation.getSelectedMessages()[0];
                IRequestInfo reqInfo = BurpExtender.helpers.analyzeRequest(iReqResp.getRequest());
                // 不对GET请求进行编码
                if(!reqInfo.getMethod().equals("POST")){
                    JOptionPane.showConfirmDialog(null,"GET requests cannot be  encoded！","Warning",JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    byte[] request = Transfer.encoding(iReqResp);
                    if (request != null) {
                        iReqResp.setRequest(request);
                    }
                } catch (Exception e) {
                    BurpExtender.stderr.println(e.getMessage());
                }
            }
        });

        decode.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent arg0) {
                IHttpRequestResponse iReqResp = invocation.getSelectedMessages()[0];

                try {
                    byte[] request = Transfer.decoding(iReqResp);
                    if (request != null) {
                        iReqResp.setRequest(request);
                    }
                } catch (Exception e) {
                    BurpExtender.stderr.println(e.getMessage());
                }
            }
        });

        config.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent arg0) {
                try {
                    ConfigDlg dlg = new ConfigDlg();
                    BurpExtender.callbacks.customizeUiComponent(dlg);
                    dlg.setVisible(true);
                }catch (Exception e){
                    e.printStackTrace(BurpExtender.stderr);
                }
            }
        });

        menus.add(encodeMenu);
        return menus;
    }
}