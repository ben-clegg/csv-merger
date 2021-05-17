package tech.clegg.csvmerger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CSVFile
{
    // Outer list = rows, inner = columns
    private List<List<String>> cells; // [rowIds.size()][columnNames.size()]
    private List<String> columnNames;
    private List<String> rowIds;

    private static final String SEP = ",";

    public CSVFile(CSVFile toCopy)
    {
        this.cells = toCopy.cells;
        this.columnNames = toCopy.columnNames;
        this.rowIds = toCopy.rowIds;
    }

    public CSVFile(Path location)
    {
        try
        {
            load(location);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private void load(Path location) throws FileNotFoundException
    {
        BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(location)));
        List<String> lines = reader.lines().collect(Collectors.toList());

        // Load column names
        columnNames = Arrays.stream(lines.remove(0).split(",")).collect(Collectors.toList());
        // First column = "solution"; not needed
        columnNames.remove(0);

        // Load row ids
        rowIds = new ArrayList<>();

        for (String l : lines)
        {
            rowIds.add(l.split(SEP)[0]);
        }

        // Load cells
        cells = new ArrayList<>();
        for (String l : lines)
        {
            List<String> values = Arrays.stream(l.split(SEP)).collect(Collectors.toList());
            values.remove(0);

            cells.add(values);
        }
    }

    public List<List<String>> getCells()
    {
        return cells;
    }

    public List<String> getColumnNames()
    {
        return columnNames;
    }

    public List<String> getRowIds()
    {
        return rowIds;
    }

    public void clearRows()
    {
        cells = new ArrayList<>();
        rowIds = new ArrayList<>();
    }

    public void addRow(String rowId, List<String> rowValues)
    {
        cells.add(rowValues);
        rowIds.add(rowId);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CSVFile csvFile = (CSVFile) o;
        return getCells().equals(csvFile.getCells()) && getColumnNames().equals(csvFile.getColumnNames()) && getRowIds().equals(csvFile.getRowIds());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getCells(), getColumnNames(), getRowIds());
    }
}
