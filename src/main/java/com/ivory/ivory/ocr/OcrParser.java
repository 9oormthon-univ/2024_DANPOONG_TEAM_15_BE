package com.ivory.ivory.ocr;

import java.util.Map;

public interface OcrParser {
    Map<String, String> parse(String response);
}
