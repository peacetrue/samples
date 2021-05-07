package com.github.peacetrue.sample.hadoop;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.stream.StreamSupport;

/**
 * 统计单词数量
 *
 * @author : xiayx
 * @since : 2021-04-06 13:07
 **/
@Slf4j
public class WordCountTest {

    //<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
    public static class WordCountMapper extends Mapper<Object, Text, Text, IntWritable> {
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] words = value.toString().split(" ");
            for (String word : words) {
                context.write(new Text(word), new IntWritable(1));
            }
        }
    }

    // <Object, Text, Text, IntWritable>
    public static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            Integer count = StreamSupport
                    .stream(values.spliterator(), false)
                    .map(IntWritable::get)
                    .reduce(0, Integer::sum);
            context.write(key, new IntWritable(count));
        }
    }


    @Test
    void driver() throws Exception {
        Job job = Job.getInstance(new Configuration());
        job.setJarByClass(WordCountTest.class);
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
//        job.setMapOutputKeyClass(Text.class);
//        job.setMapOutputValueClass(IntWritable.class);
//        job.setOutputKeyClass(Text.class);
//        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.setInputPaths(job, new Path("/Users/xiayx/Documents/Projects/samples/hadoop/src/test/resources/word-count"));
        FileOutputFormat.setOutputPath(job, new Path("/Users/xiayx/Documents/Projects/samples/hadoop/src/test/resources/output"));

        boolean waitForCompletion = job.waitForCompletion(true);
        log.info("waitForCompletion: {}", waitForCompletion);
    }
}
