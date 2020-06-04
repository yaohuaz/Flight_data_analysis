import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TaxiTimeMapper
extends Mapper<Object, Text, Text, Text> {
    protected void map(Object object, Text text, Mapper<Object, Text, Text, Text> mapper) throws IOException, InterruptedException {
        String[] arrstring = text.toString().split(",");
        if (!"Year".equals(arrstring[0])) {
            if (!"NA".equals(arrstring[20])) {
                mapper.write((Object)new Text(arrstring[16]), (Object)new Text(arrstring[20]));
            }
            if (!"NA".equals(arrstring[19])) {
                mapper.write((Object)new Text(arrstring[17]), (Object)new Text(arrstring[19]));
            }
        }
    }
}

