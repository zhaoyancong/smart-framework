package cc.zzr.helper;


import cc.zzr.bean.FormParam;
import cc.zzr.bean.Param;
import cc.zzr.util.ArrayUtil;
import cc.zzr.util.CodecUtil;
import cc.zzr.util.StreamUtil;
import cc.zzr.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public final class RequestHelper {
    public static Param createParam(HttpServletRequest request)
            throws IOException {
        List<FormParam> formParamList = new ArrayList<FormParam>();
        formParamList.addAll(parseParmeterNames(request));
        formParamList.addAll(parseInputStream(request));
        return new Param(formParamList);
    }

    private static List<FormParam> parseParmeterNames(HttpServletRequest request) {
        List formParamList = new ArrayList();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String fieldName = (String) paramNames.nextElement();
            String[] fieldValues = request.getParameterValues(fieldName);
            if (ArrayUtil.isNotEmpty(fieldValues)) {
                String fieldValue;
                if (fieldValues.length == 1) {
                    fieldValue = fieldValues[0];
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < fieldValues.length; i++) {
                        stringBuilder.append(fieldValues[i]);
                        if (i != fieldValues.length - 1) {
                            stringBuilder.append(StringUtil.SEPARATOR);
                        }
                    }
                    fieldValue = stringBuilder.toString();
                }
                formParamList.add(new FormParam(fieldName, fieldValue));
            }
        }
        return formParamList;
    }

    private static List<FormParam> parseInputStream(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<FormParam>();
        String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
        if (StringUtil.isNotEmpty(body)) {
            String[] kvs = StringUtil.splitString(body, "&");
            if (ArrayUtil.isNotEmpty(kvs)) {
                for (String kv : kvs) {
                    String[] array = StringUtil.splitString(kv, "=");
                    if ((ArrayUtil.isNotEmpty(array)) && (array.length == 2)) {
                        String fieldName = array[0];
                        String fieldValue = array[1];
                        formParamList.add(new FormParam(fieldName, fieldValue));
                    }
                }
            }
        }
        return formParamList;
    }
}
