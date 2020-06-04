import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CancellationsReducer
extends Reducer<Text, IntWritable, Text, Text> {
    private Map<String, Integer> map = new TreeMap<String, Integer>();

    protected void reduce(Text text, Iterable<IntWritable> iterable, Reducer<Text, IntWritable, Text, Text> reducer) throws IOException, InterruptedException {
        Iterator<IntWritable> iterator = iterable.iterator();
        Integer n = 0;
        while (iterator.hasNext()) {
            int n2 = Integer.parseInt(iterator.next().toString());
            n = n + n2;
        }
        this.map.put(text.toString(), n);
    }

    protected void cleanup(Reducer<Text, IntWritable, Text, Text> reducer) throws IOException, InterruptedException {
        if (!this.map.isEmpty()) {
            ArrayList<Map.Entry<String, Integer>> arrayList = new ArrayList<Map.Entry<String, Integer>>(this.map.entrySet());
            Collections.sort(arrayList, new Comparator<Map.Entry<String, Integer>>(){

                @Override
                public int compare(Map.Entry<String, Integer> entry, Map.Entry<String, Integer> entry2) {
                    return entry2.getValue().compareTo(entry.getValue());
                }
            });
            Map.Entry entry = (Map.Entry)arrayList.get(0);
            boolean bl = false;
            if ("A".equals(entry.getKey())) {
                bl = true;
                reducer.write((Object)new Text("Cancellation Code is A: carrier"), (Object)new Text("The count is: " + entry.getValue() + ""));
            } else if ("B".equals(entry.getKey())) {
                bl = true;
                reducer.write((Object)new Text("Cancellation Code is B: weather"), (Object)new Text("The count is: " + entry.getValue() + ""));
            } else if ("C".equals(entry.getKey())) {
                bl = true;
                reducer.write((Object)new Text("Cancellation Code is C: NAS"), (Object)new Text("The count is: " + entry.getValue() + ""));
            } else if ("D".equals(entry.getKey())) {
                bl = true;
                reducer.write((Object)new Text("Cancellation Code is D: security"), (Object)new Text("The count is: " + entry.getValue() + ""));
            }
            if (!bl) {
                reducer.write((Object)new Text("No Reason"), (Object)new Text(""));
            }
        } else {
            reducer.write((Object)new Text("No outout."), (Object)new Text(""));
        }
    }

}

