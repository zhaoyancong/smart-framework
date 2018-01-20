package cc.zzr.helper;

import cc.zzr.bean.FileParam;
import cc.zzr.bean.FormParam;
import cc.zzr.bean.Param;
import cc.zzr.util.CollectionUtil;
import cc.zzr.util.FileUtil;
import cc.zzr.util.StreamUtil;
import cc.zzr.util.StringUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class UploadHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);
    private static ServletFileUpload servletFileUpload;

    public static void init(ServletContext servletContext) {
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        servletFileUpload = new ServletFileUpload(new DiskFileItemFactory(10240, repository));
        int uploadLimit = ConfigHelper.getAppUploadLimit();
        if (uploadLimit != 0)
            servletFileUpload.setFileSizeMax(uploadLimit * 1024 * 1024);
    }

    public static boolean isMultipart(HttpServletRequest request) {
        return ServletFileUpload.isMultipartContent(request);
    }

    public static Param createParam(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<FormParam>();
        List<FileParam> fileParamList = new ArrayList<FileParam>();
        try {
            Map<String, List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(request);
            String fieldName;
            for (Map.Entry<String, List<FileItem>> fileItemListEntry : fileItemListMap.entrySet()) {
                fieldName = fileItemListEntry.getKey();
                List<FileItem> fileItemList = fileItemListEntry.getValue();
                if (CollectionUtil.isNotEmpty(fileItemList))
                    for (FileItem fileItem : fileItemList)
                        if (fileItem.isFormField()) {
                            String fieldValue = fileItem.getString("UTF-8");
                            formParamList.add(new FormParam(fieldName, fieldValue));
                        } else {
                            String fileName = FileUtil.getRealFileName(new String(fileItem.getName().getBytes(), "UTF-8"));
                            if (StringUtil.isNotEmpty(fileName)) {
                                long fileSize = fileItem.getSize();
                                String contentType = fileItem.getContentType();
                                InputStream inputStream = fileItem.getInputStream();
                                fileParamList.add(new FileParam(fieldName, fileName, fileSize, contentType, inputStream));
                            }
                        }
            }
        } catch (FileUploadException e) {

            LOGGER.error("create param error", e);
            throw new RuntimeException(e);
        }
        return new Param(formParamList, fileParamList);
    }

    public static void uploadFile(String basePath, FileParam fileParam) {
        try {
            if (fileParam != null) {
                String filePath = basePath + fileParam.getFileName();
                FileUtil.createFile(filePath);
                InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
                StreamUtil.copyStream(inputStream, outputStream);
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("upload file failure", e);
            throw new RuntimeException(e);
        }
    }

    public static void uploadFile(String basePath, List<FileParam> fileParamList) {
        try {
            if (CollectionUtil.isNotEmpty(fileParamList))
                for (FileParam fileParam : fileParamList)
                    uploadFile(basePath, fileParam);
        } catch (Exception e) {
            LOGGER.error("upload file list failure", e);
            throw new RuntimeException(e);
        }
    }
}