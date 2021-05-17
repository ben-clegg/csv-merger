package tech.clegg.csvmerger;

import static org.junit.Assert.*;

import org.junit.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CSVMergerTest
{
    private static final Path CSV_FILE_PATH = Paths.get("src/test/resources/test.csv");
    private static final CSVFile CSV_FILE = new CSVFile(CSV_FILE_PATH);

    @Test
    public void testCsvFileLoaded()
    {
        CSVMerger merger = new CSVMerger(CSV_FILE_PATH);
        CSVFile csvFile = merger.getCsvFile();

        assertEquals(CSV_FILE, csvFile);

        // Row names
        assertEquals("SolA", csvFile.getRowIds().get(0));
        assertEquals("SolC", csvFile.getRowIds().get(2));

        // Column names
        assertEquals("CheckA", csvFile.getColumnNames().get(0));
        assertEquals("CheckB", csvFile.getColumnNames().get(1));
        assertEquals("CheckD", csvFile.getColumnNames().get(3));

        // Cell values
        assertEquals("1", csvFile.getCells().get(0).get(0));
        assertEquals("0", csvFile.getCells().get(0).get(1));
        assertEquals("1", csvFile.getCells().get(0).get(2));
        assertEquals("0", csvFile.getCells().get(0).get(3));

        assertEquals("0", csvFile.getCells().get(1).get(0));
        assertEquals("0", csvFile.getCells().get(1).get(1));
        assertEquals("0", csvFile.getCells().get(1).get(2));
        assertEquals("1", csvFile.getCells().get(1).get(3));

        assertEquals("0", csvFile.getCells().get(2).get(0));
        assertEquals("0", csvFile.getCells().get(2).get(1));
        assertEquals("0", csvFile.getCells().get(2).get(2));
        assertEquals("1", csvFile.getCells().get(2).get(3));

        assertEquals("1", csvFile.getCells().get(3).get(0));
        assertEquals("0", csvFile.getCells().get(3).get(1));
        assertEquals("1", csvFile.getCells().get(3).get(2));
        assertEquals("0", csvFile.getCells().get(3).get(3));
    }

    @Test
    public void testMergeRows()
    {
        CSVFile merged = CSVMerger.mergeRows(CSV_FILE);

        // Only two rows should remain
        assertEquals(2, merged.getRowIds().size());
        assertEquals(2, merged.getCells().size());

        // Row names correct
        assertTrue(merged.getRowIds().get(0).contains("SolA"));
        assertTrue(merged.getRowIds().get(0).contains("SolD"));
        assertTrue(merged.getRowIds().get(1).contains("SolB"));
        assertTrue(merged.getRowIds().get(1).contains("SolC"));

        // Cell values are correct
        assertEquals("1", merged.getCells().get(0).get(0));
        assertEquals("0", merged.getCells().get(0).get(1));
        assertEquals("1", merged.getCells().get(0).get(2));
        assertEquals("0", merged.getCells().get(0).get(3));

        assertEquals("0", merged.getCells().get(1).get(0));
        assertEquals("0", merged.getCells().get(1).get(1));
        assertEquals("0", merged.getCells().get(1).get(2));
        assertEquals("1", merged.getCells().get(1).get(3));
    }

}
