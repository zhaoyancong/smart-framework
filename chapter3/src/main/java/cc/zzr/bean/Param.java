package cc.zzr.bean;

import cc.zzr.util.CastUtil;
import cc.zzr.util.CollectionUtil;
import cc.zzr.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Param {

    private Map<String, Object> paramMap;
    private List<FormParam> formParamList;
    private List<FileParam> fileParamList;

    public Param(List<FormParam> formParamList)
    {
        this.formParamList = formParamList;
    }

    public Param(List<FormParam> formParamList, List<FileParam> fileParamList) {
        this.formParamList = formParamList;
        this.fileParamList = fileParamList;
    }

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, Object> getFieldMap() {
        Map fieldMap = new HashMap();
        if (CollectionUtil.isNotEmpty(this.formParamList)) {
            for (FormParam formParam : this.formParamList) {
                String fieldName = formParam.getFieldName();
                Object fieldValue = formParam.getFieldValue();
                if (fieldMap.containsKey(fieldName)) {
                    fieldValue = fieldMap.get(fieldName) + StringUtil.SEPARATOR + fieldValue;
                }
                fieldMap.put(fieldName, fieldValue);
            }
        }

        return fieldMap;
    }

    public Map<String, List<FileParam>> getFileMap() {
        Map fileMap = new HashMap();
        if (CollectionUtil.isNotEmpty(this.fileParamList)) {
            for (FileParam fileParam : this.fileParamList) {
                String fieldName = fileParam.getFieldName();
                List<FileParam> fileParamList;
                if (fileMap.containsKey(fieldName))
                    fileParamList = (List)fileMap.get(fieldName);
                else {
                    fileParamList = new ArrayList();
                }
                fileParamList.add(fileParam);
                fileMap.put(fieldName, fileParamList);
            }
        }
        return fileMap;
    }

    public List<FileParam> getFileList(String fieldName) {
        return getFileMap().get(fieldName);
    }

    public FileParam getFile(String fieldName) {
        List fileParamList = getFileList(fieldName);
        if ((CollectionUtil.isNotEmpty(fileParamList)) && (fileParamList.size() == 1)) {
            return (FileParam)fileParamList.get(0);
        }
        return null;
    }

    public String getString(String name) {
        return CastUtil.castString(getFieldMap().get(name));
    }

    public double getDouble(String name) {
        return CastUtil.castDouble(getFieldMap().get(name));
    }

    public long getLong(String name) {
        return CastUtil.castLong(this.paramMap.get(name));
    }

    public int getInt(String name) {
        return CastUtil.castInt(getFieldMap().get(name));
    }

    public boolean getBoolean(String name) {
        return CastUtil.castBoolean(getFieldMap().get(name));
    }

    public Map<String, Object> getMap() {
        return this.paramMap;
    }

    public boolean isEmpty() {
        return CollectionUtil.isEmpty(this.paramMap);
    }
}
