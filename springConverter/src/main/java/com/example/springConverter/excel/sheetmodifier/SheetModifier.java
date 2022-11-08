package com.example.springConverter.excel.sheetmodifier;

import com.example.springConverter.excel.sheetmodifier.columnscommands.ColumnsModifier;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

public class SheetModifier {
    private final List<ColumnsModifier> columnsModifiers = new ArrayList<>();


    public void newModifier(ColumnsModifier modifier) {
        columnsModifiers.add(modifier);
    }

    public ColumnsModifier removeLast() {
        return columnsModifiers.remove(columnsModifiers.size() - 1);
    }

    public void applyAll(Sheet sheet) {
        columnsModifiers.forEach(modifier -> modifier.apply(sheet));
    }

    public void applyLastAdded(Sheet sheet) {
        columnsModifiers.get(columnsModifiers.size() - 1).apply(sheet);
    }
}
