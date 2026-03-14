package com.brainwave.core.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 列表資料匯出為 CSV 的通用工具（RFC 4180 風格，含 UTF-8 BOM 以利 Excel 正確開啟）。
 * 不依賴 Apache POI，僅用於產生 CSV；若需 xlsx 可另行引入 POI 並實作類似介面。
 */
public final class CsvExportHelper {

    private static final byte[] UTF8_BOM = new byte[] {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    private static final byte[] CRLF = new byte[] {'\r', '\n'};
    private static final byte COMMA = ',';

    private CsvExportHelper() {}

    /**
     * 將標題列與資料列寫入輸出串流，並可選寫入 UTF-8 BOM。
     *
     * @param out        輸出串流（呼叫端負責關閉）
     * @param headers    欄位標題（第一列）
     * @param rows       資料列，每列欄位數建議與 headers 一致
     * @param writeBom   是否寫入 UTF-8 BOM（Excel 辨識 UTF-8 用）
     */
    public static void writeCsv(
            OutputStream out,
            List<String> headers,
            List<List<String>> rows,
            boolean writeBom)
            throws IOException {
        if (writeBom) {
            out.write(UTF8_BOM);
        }
        writeRow(out, headers);
        for (List<String> row : rows) {
            out.write(CRLF);
            writeRow(out, row);
        }
    }

    /**
     * 同 {@link #writeCsv(OutputStream, List, List, boolean)}，預設寫入 BOM。
     */
    public static void writeCsv(OutputStream out, List<String> headers, List<List<String>> rows)
            throws IOException {
        writeCsv(out, headers, rows, true);
    }

    /**
     * 產生 CSV 位元組陣列（含 BOM），供 Controller 直接回傳下載。
     */
    public static byte[] toCsvBytes(List<String> headers, List<List<String>> rows) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeCsv(out, headers, rows, true);
        return out.toByteArray();
    }

    private static void writeRow(OutputStream out, List<String> cells) throws IOException {
        for (int i = 0; i < cells.size(); i++) {
            if (i > 0) {
                out.write(COMMA);
            }
            out.write(escapeCsvField(cells.get(i) != null ? cells.get(i) : ""));
        }
    }

    private static byte[] escapeCsvField(String value) {
        boolean needQuote = value.contains(",") || value.contains("\"") || value.contains("\r") || value.contains("\n");
        if (!needQuote) {
            return value.getBytes(StandardCharsets.UTF_8);
        }
        String escaped = "\"" + value.replace("\"", "\"\"") + "\"";
        return escaped.getBytes(StandardCharsets.UTF_8);
    }
}
