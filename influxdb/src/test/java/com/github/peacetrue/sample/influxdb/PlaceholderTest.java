package com.github.peacetrue.samples.influxdb;

import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.influxdb.InfluxDBTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author : xiayx
 * @since : 2021-01-03 18:23
 **/
@SpringBootTest(classes = {
        InfluxDBConfiguration.class
})
class PlaceholderTest {

    @Autowired
    private InfluxDBTemplate<Point> influxDBTemplate;

    @Test
    void basic() {
        influxDBTemplate.createDatabase();
        final Point p = Point.measurement("disk")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .tag("tenant", "default")
                .addField("used", 80L)
                .addField("free", 1L)
                .build();
        influxDBTemplate.write(p);
//        influxDBTemplate.query(Query.encode("select * from "))
    }
}
