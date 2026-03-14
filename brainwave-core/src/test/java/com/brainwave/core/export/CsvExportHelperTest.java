package com.brainwave.core.export;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class CsvExportHelperTest {

    @Test
    void writeCsv_emptyRows_producesHeaderOnly() throws IOException {
        List<String> headers = List.of("A", "B");
        List<List<String>> rows = List.of();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CsvExportHelper.writeCsv(out, headers, rows);
        String result = out.toString(StandardCharsets.UTF_8);
        assertTrue(result.startsWith("\uFEFF")); // BOM
        assertTrue(result.contains("A") && result.contains("B"));
    }

    @Test
    void writeCsv_withData_escapesCommaAndQuotes() throws IOException {
        List<String> headers = List.of("Name", "Note");
        List<List<String>> rows = List.of(
                List.of("a", "b"),
                List.of("x,y", "say \"hi\""));
        byte[] bytes = CsvExportHelper.toCsvBytes(headers, rows);
        String result = new String(bytes, StandardCharsets.UTF_8);
        assertTrue(result.startsWith("\uFEFF"));
        assertTrue(result.contains("x,y") || result.contains("\"x,y\""));
        assertTrue(result.contains("\"say \"\"hi\"\"\""));
    }

    @Test
    void toCsvBytes_returnsUtf8WithBom() throws IOException {
        List<String> headers = List.of("H");
        List<List<String>> rows = List.of(List.of("v"));
        byte[] bytes = CsvExportHelper.toCsvBytes(headers, rows);
        assertEquals(0xEF, bytes[0] & 0xFF);
        assertEquals(0xBB, bytes[1] & 0xFF);
        assertEquals(0xBF, bytes[2] & 0xFF);
    }
}
