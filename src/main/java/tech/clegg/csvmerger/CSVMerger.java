package tech.clegg.csvmerger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CSVMerger
{
    public static void main(String[] args)
    {
        if(args.length != 1)
            throw new IllegalArgumentException("No CSV filename defined.");

        Path csvFilePath = Paths.get(args[0]);
        CSVMerger merger = new CSVMerger(csvFilePath);
        CSVFile merged = merge(merger.csvFile);
    }

    private CSVFile csvFile;

    public CSVMerger(Path csvFileLocation)
    {
        this.csvFile = new CSVFile(csvFileLocation);
    }

    public static CSVFile merge(CSVFile toMerge)
    {
        CSVFile merging = new CSVFile(toMerge);

        merging = mergeRows(merging);

        return merging;
    }

    static CSVFile mergeRows(CSVFile toMerge)
    {
        List<List<String>> uniqueRowValues = new ArrayList<>();
        List<Set<String>> matchingRowIds = new ArrayList<>();

        // Determine matching rows
        for (int r = 0; r < toMerge.getRowIds().size(); r++)
        {
            String rowId = toMerge.getRowIds().get(r);
            List<String> rowValues = toMerge.getCells().get(r);

            if (!uniqueRowValues.contains(rowValues))
            {
                uniqueRowValues.add(rowValues);
                matchingRowIds.add(new HashSet<>());
            }

            // Add row id
            int index = uniqueRowValues.indexOf(rowValues);
            matchingRowIds.get(index).add(rowId);
        }

        // Number of unique rows and their according row id groups should be equal
        assert (uniqueRowValues.size() == matchingRowIds.size());

        // Generate a new CSV with duplicate rows removed
        CSVFile merged = new CSVFile(toMerge);
        merged.clearRows();

        for (int r = 0; r < uniqueRowValues.size(); r++)
        {
            merged.addRow(
                    generateGroupedIdentifier(matchingRowIds.get(r)),
                    uniqueRowValues.get(r)
            );
        }

        return merged;
    }

    static CSVFile mergeColumns(CSVFile toMerge)
    {
        List<List<String>> uniqueColumnCells = new ArrayList<>();
        List<Set<String>> matchingColumnNames = new ArrayList<>();

        // Determine matching columns
        for (int c = 0; c < toMerge.getColumnNames().size(); c++)
        {
            String colName = toMerge.getColumnNames().get(c);

            // Construct cell values for column
            List<String> colCellValues = new ArrayList<>();
            for (List<String> rowValues : toMerge.getCells())
                colCellValues.add(rowValues.get(c));

            if (!uniqueColumnCells.contains(colCellValues))
            {
                uniqueColumnCells.add(colCellValues);
                matchingColumnNames.add(new HashSet<>());
            }

            // Add column name
            int index = uniqueColumnCells.indexOf(colCellValues);
            matchingColumnNames.get(index).add(colName);
        }

        // Number of unique columns and according column names should be equal
        assert (uniqueColumnCells.size() == matchingColumnNames.size());

        // Generate a new CSV with duplicate columns removed
        CSVFile merged = new CSVFile(toMerge);
        merged.clearColumns();

        for (int c = 0; c < matchingColumnNames.size(); c++)
        {
            merged.addColumn(
                    generateGroupedIdentifier(matchingColumnNames.get(c)),
                    uniqueColumnCells.get(c)
            );
        }

        return merged;
    }

    private static String generateGroupedIdentifier(Collection<String> identifiers)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\"{");

        Iterator<String> iter = identifiers.iterator();

        while (iter.hasNext())
        {
            sb.append(iter.next().replace("\"", ""));
            if(iter.hasNext())
                sb.append(";");
        }

        sb.append("}\"");

        return sb.toString();
    }

    public CSVFile getCsvFile()
    {
        return csvFile;
    }
}
