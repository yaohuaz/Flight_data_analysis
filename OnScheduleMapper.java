import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class OnScheduleMapper
extends Mapper<Object, Text, Text, Text> {
    protected void map(Object object, Text text, Mapper<Object, Text, Text, Text> mapper) throws IOException, InterruptedException {
        String[] arrstring = text.toString().split(",");
        if (!"Year".equals(arrstring[0])) {
            String string = "0";
            if (!"NA".equals(arrstring[14])) {
                if (Integer.parseInt(arrstring[14]) <=20) {
                    string = "1";
                }
                mapper.write((Object)new Text(arrstring[8]), (Object)new Text(string));
            }
        }
    }
}

