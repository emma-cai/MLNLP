package org.mlnlp.memnn.tensor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Datum containing multiple separate entries.
 *
 * @author jamesgung
 */
public class TableDatum extends Datum {

    public Map<String, Datum> table;
    public List<Datum> list;

    public TableDatum() {
        this.table = new LinkedHashMap<>();
        this.list = new ArrayList<>();
    }

    public TableDatum(Datum... entries) {
        this();
        for (Datum datum : entries) {
            table.put(Integer.toString(table.size()), datum);
            list.add(datum);
        }
    }

    public TableDatum(Tensor... entries) {
        this();
        for (Tensor datum : entries) {
            addTensor(datum);
        }
    }

    public Datum get(int index) {
        return list.get(index);
    }

    public Datum get(String key) {
        return table.get(key);
    }

    public void addAt(int index, Datum datum) {
        table.put(Integer.toString(index), datum);
        list.remove(index);
        list.add(index, datum);
    }

    public void add(Datum datum) {
        table.put(Integer.toString(table.size()), datum);
        list.add(datum);
    }

    public void addAll(List<Datum> datumList) {
        for (Datum datum : datumList) {
            table.put(Integer.toString(table.size()), datum);
            list.add(datum);
        }
    }

    public void addTensor(Tensor tensor) {
        TensorDatum datum = new TensorDatum(tensor);
        add(datum);
    }

    public void resizeAs(Datum datum) {
        if (datum instanceof TensorDatum && numEntries() != 1) {
            this.list.clear();
            this.list.add(new TableDatum());
        } else if (datum instanceof TableDatum && numEntries() != datum.numEntries()) {
            this.list.clear();
            for (int i = 0; i < datum.numEntries(); ++i) {
                list.add(new TableDatum());
            }
        }
    }

    public void clear() {
        list.clear();
        table.clear();
    }

    @Override
    public void zero() {
        table.values().forEach(Datum::zero);
    }


    @Override
    public int numEntries() {
        return table.size();
    }


}
