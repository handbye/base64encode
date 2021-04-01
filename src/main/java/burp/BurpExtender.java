package burp;

import java.io.PrintWriter;

public class BurpExtender implements IBurpExtender, IHttpListener, IProxyListener {
    public static IBurpExtenderCallbacks callbacks;
    public static IExtensionHelpers helpers;
    private String extensionName = "base64Encoding";
    private String version ="1.0";
    public static PrintWriter stdout;
    public static PrintWriter stderr;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
        this.helpers = callbacks.getHelpers();
        callbacks.setExtensionName(String.format("%s %s",extensionName,version));
        callbacks.registerContextMenuFactory(new Menu());
        callbacks.registerHttpListener(this);
        callbacks.registerProxyListener(this);
        stdout = new PrintWriter(callbacks.getStdout(),true);
        stderr = new PrintWriter(callbacks.getStderr(),true);
        stdout.println(getBanner());
    }


    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
        // 处理除代理套件之外的套件流量
        if(messageIsRequest && isValidTool(toolFlag) && (toolFlag != IBurpExtenderCallbacks.TOOL_PROXY)){
            IRequestInfo reqInfo = helpers.analyzeRequest(messageInfo.getRequest());

            if(reqInfo.getMethod().equals("POST")){
                try {
                    byte[] request = Transfer.encoding(messageInfo);
                    if (request != null) {
                        messageInfo.setRequest(request);
                    }
                } catch (Exception e) {
                    e.printStackTrace(stderr);
                }
            }
        }
    }


    @Override
    public void processProxyMessage(final boolean messageIsRequest, final IInterceptedProxyMessage proxyMessage) {
        // 处理代理套件流量
        if(messageIsRequest && isValidTool(IBurpExtenderCallbacks.TOOL_PROXY)){
            IHttpRequestResponse messageInfo = proxyMessage.getMessageInfo();
            IRequestInfo reqInfo = helpers.analyzeRequest(messageInfo.getRequest());

            if(reqInfo.getMethod().equals("POST")){
                try {
                    byte[] request = Transfer.encoding(messageInfo);
                    if (request != null) {
                        messageInfo.setRequest(request);
                    }
                } catch (Exception e) {
                    e.printStackTrace(stderr);
                }
            }
        }
    }

    /**
     * 插件是否作用于该套件
     * @param toolFlag
     * @return
     */
    private boolean isValidTool(int toolFlag){
        return (Config.isAct_on_all_tools() ||
                (Config.isAct_on_proxy() && toolFlag== IBurpExtenderCallbacks.TOOL_PROXY) ||
                (Config.isAct_on_intruder() && toolFlag== IBurpExtenderCallbacks.TOOL_INTRUDER) ||
                (Config.isAct_on_repeater() && toolFlag== IBurpExtenderCallbacks.TOOL_REPEATER) ||
                (Config.isAct_on_scanner() && toolFlag== IBurpExtenderCallbacks.TOOL_SCANNER) ||
                (Config.isAct_on_sequencer() && toolFlag== IBurpExtenderCallbacks.TOOL_SEQUENCER) ||
                (Config.isAct_on_spider() && toolFlag== IBurpExtenderCallbacks.TOOL_SPIDER) ||
                (Config.isAct_on_extender() && toolFlag== IBurpExtenderCallbacks.TOOL_EXTENDER) ||
                (Config.isAct_on_target() && toolFlag== IBurpExtenderCallbacks.TOOL_TARGET));
    }


    /**
     * 插件Banner信息
     * @return
     */
    public String getBanner(){
        String bannerInfo =
                "[+]\n"
                        + "[+] ##############################################\n"
                        + "[+]    " + extensionName + " v" + version +"\n"
                        + "[+]    anthor: darkless"
                        + "[+]    blog:  https://darkless.cn\n"
                        + "[+] ##############################################";
        return bannerInfo;
    }
}