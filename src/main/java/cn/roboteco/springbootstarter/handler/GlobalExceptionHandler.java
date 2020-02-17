package cn.roboteco.springbootstarter.handler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alibaba.fastjson.JSONObject;
import cn.roboteco.springbootstarter.common.Constants;
import cn.roboteco.springbootstarter.common.FunUtil;
import cn.roboteco.springbootstarter.domain.Result;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ Exception.class })
    public Result exceptionHandler(Exception e, HttpServletRequest request) {
        log.error("请求方法：{}, 请求url：{},请求类型：{},请求参数：{}", request.getMethod(), request.getRequestURL().toString(),
                        request.getContentType(), getRequestBoby(request).toString(), e);
        String errorMessage = e.getMessage();
        return new Result(Constants.ERROR, FunUtil.isContainChinese(errorMessage) ? errorMessage : "系统繁忙，请稍后再试！");
    }

    /**
     * 
     * 获取请求参数
     * 
     * @param request
     * @return
     * @see
     */
    private JSONObject getRequestBoby(HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        String requestData = null;
        try {
            requestData = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            log.error("PayRequest.getRequestBoby.IOException", e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        boolean isJson = isJsonFormat(requestData);
        if (isJson) {
            obj = JSONObject.parseObject(requestData);
        } else {
            if (!StringUtils.isEmpty(requestData)) {
                obj.put("RequestBoby", requestData);
            }
        }

        Map requestParams = request.getParameterMap();
        if (!requestParams.isEmpty()) {
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                obj.put(name, valueStr);
            }
        }

        return obj;
    }

    private static boolean isJsonFormat(String requestData) {
        boolean isJson = false;
        if (!StringUtils.isEmpty(requestData)) {
            try {
                JSONObject.parseObject(requestData);
                isJson = true;
            } catch (Exception e) {}
        }
        return isJson;
    }
}
