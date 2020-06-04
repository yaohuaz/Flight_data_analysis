import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CancellationsMapper
extends Mapper<Object, Text, Text, IntWritable> {
    protected void map(Object object, Text text, Mapper<Object, Text, Text, IntWritable> mapper) throws IOException, InterruptedException {
        String[] arrstring = text.toString().split(",");
        if (!"Year".equals(arrstring[0]) && "1".equals(arrstring[21]) && !"NA".equals(arrstring[22]) && arrstring[22].trim().length() > 0) {
            mapper.write((Object)new Text(arrstring[22]), (Object)new IntWritable(1));
        }
    }
}

