package cc.zzr.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public final class StreamUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtil.class);

    public static String getString(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line);
        } catch (Exception e) {
            LOGGER.error("get string failure", e);
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public static void copyStream(InputStream inputStream, OutputStream outputStream) {
        try {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer, 0, buffer.length)) != -1)
                outputStream.write(buffer, 0, length);
        } catch (IOException e) {
            LOGGER.error("copy stream failure", e);
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                LOGGER.error("close stream failure", e);
            }
        }
    }
}