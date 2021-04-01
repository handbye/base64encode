package burp;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 编码解码类，负责对目标请求进行编码解码
 */
public class Transfer {
    /**
     * 对请求包进行base64编码
     * @return
     */
    public static byte[] encoding(IHttpRequestResponse requestResponse) throws UnsupportedEncodingException {
        byte[] request = requestResponse.getRequest();
        IRequestInfo requestInfo = BurpExtender.helpers.analyzeRequest(request);
        int bodyOffset = requestInfo.getBodyOffset();
        int body_length = request.length - bodyOffset;
        String body = new String(request, bodyOffset, body_length, "UTF-8");
        if (request.length - bodyOffset > 10000){
            return request;
        }
        List<String> headers = BurpExtender.helpers.analyzeRequest(request).getHeaders();
        String decoding_body = BurpExtender.helpers.base64Encode(body);
        return BurpExtender.helpers.buildHttpMessage(headers, decoding_body.getBytes());
    }


    /**
     * 对编码过的请求包进行解码
     *
     * @param requestResponse 已编码过的请求响应对象
     * @return 解码后的请求包
     * @throws UnsupportedEncodingException
     */
    public static byte[] decoding(IHttpRequestResponse requestResponse) throws UnsupportedEncodingException {
        byte[] request = requestResponse.getRequest();
        IRequestInfo requestInfo = BurpExtender.helpers.analyzeRequest(request);
        int body_offset = requestInfo.getBodyOffset();
        String requestString = new String(request);
        String body = requestString.substring(body_offset);
        byte[] bodyBytes = body.getBytes();
        List<String> headers = BurpExtender.helpers.analyzeRequest(request).getHeaders();
        //Decoding
        byte[] decoding_body = BurpExtender.helpers.base64Decode(bodyBytes);
        return BurpExtender.helpers.buildHttpMessage(headers, decoding_body);
    }

}