package ru.irlix.evaluation.utils.report.encoding;

import lombok.extern.log4j.Log4j2;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Log4j2
public class UTFEncoder {

    public static String encodeToUTF8(String fileName) {

        String encodedFileName = fileName;
        try {
            encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()).replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return encodedFileName;
    }
}
