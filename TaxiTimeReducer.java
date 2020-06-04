import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TaxiTimeReducer
extends Reducer<Text, Text, Text, Text> {
    private Map<String, Double> map = new TreeMap<String, Double>();
    private Map<String, Double> execptionMap = new TreeMap<String, Double>();

    protected void reduce(Text text, Iterable<Text> iterable, Reducer<Text, Text, Text, Text> reducer) throws IOException, InterruptedException {
        Iterator<Text> iterator = iterable.iterator();
        int n = 0;
        Integer n2 = 0;
        while (iterator.hasNext()) {
            String string = iterator.next().toString();
            int n3 = Integer.parseInt(string);
            n2 = n2 + n3;
            ++n;
        }
        double d = (double)n2.intValue() * 1.0 / (double)n;
        if (d == 0.0) {
            this.execptionMap.put(text.toString(), d);
        } else {
            this.map.put(text.toString(), d);
        }
    }

    protected void cleanup(Reducer<Text, Text, Text, Text> reducer) throws IOException, InterruptedException {
        if (!this.map.isEmpty()) {
            int n;
            ArrayList<Map.Entry<String, Double>> arrayList = new ArrayList<Map.Entry<String, Double>>(this.map.entrySet());
            Collections.sort(arrayList, new Comparator<Map.Entry<String, Double>>(){

                @Override
                public int compare(Map.Entry<String, Double> entry, Map.Entry<String, Double> entry2) {
                    return entry2.getValue().compareTo(entry.getValue());
                }
            });
            reducer.write((Object)new Text("highest"), (Object)new Text(""));
            for (n = 0; n < 3; ++n) {
                Map.Entry entry = (Map.Entry)arrayList.get(n);
                reducer.write((Object)new Text((String)entry.getKey()), (Object)new Text(entry.getValue() + ""));
            }
            reducer.write((Object)new Text("lowest"), (Object)new Text(""));
            n = arrayList.size();
            for (int i = n - 1; i > n - 4; --i) {
                Map.Entry<String, Double> entry = (Map.Entry<String, Double>)arrayList.get(i);
                reducer.write((Object)new Text((String)entry.getKey()), (Object)new Text(entry.getValue() + ""));
            }
            reducer.write((Object)new Text("zero data"), (Object)new Text(""));
            if (this.execptionMap.isEmpty()) {
                reducer.write((Object)new Text("NONE"), (Object)new Text(""));
            } else {
                for (Map.Entry<String, Double> entry : this.execptionMap.entrySet()) {
                    reducer.write((Object)new Text(entry.getKey()), (Object)new Text(entry.getValue() + ""));
                }
            }
        } else {
            reducer.write((Object)new Text("No output."), (Object)new Text(""));
        }
    }

}

