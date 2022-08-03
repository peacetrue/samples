package com.github.peacetrue.jvm;

import com.github.peacetrue.test.SourcePathUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr022.html#BABHIFJC
 * https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr007.html
 * @author peace
 **/
class NativeMemoryTrackingTest {

    @Test
    void resolve() throws Exception{
        String path = SourcePathUtils.getTestResourceAbsolutePath("/nmt.txt");
        NativeMemoryTracking tracking = NativeMemoryTracking.resolve(new String(Files.readAllBytes(Paths.get(path))));
        System.out.println(tracking);
    }

    @Test
    void TotalResolve() {
        NativeMemoryTracking.Total total = NativeMemoryTracking.Total.resolve("-                 Java Heap (reserved=253952KB, committed=16384KB)");
        System.out.println(total);
    }
}
