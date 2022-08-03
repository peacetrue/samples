package com.github.peacetrue.jvm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author peace
 **/
@Slf4j
@Data
@ToString
@AllArgsConstructor
public class NativeMemoryTracking {

    private List<Total> totals;
    private List<VirtualMemoryMap> virtualMemoryMaps;

    public static NativeMemoryTracking resolve(String lines) {
        String[] parts = lines.split("Virtual memory map:", 2);
        return new NativeMemoryTracking(
                Total.resolves(parts[0]),
                VirtualMemoryMap.resolves(parts[1])
        );
    }

    public interface Typed {
        String getType();

        String getReserved();
    }

    @Data
    @ToString
    @AllArgsConstructor
    public static class Total implements Typed {
        private String type;
        private String reserved;
        private String committed;

        public static List<Total> resolves(String lines) {
            return Arrays.stream(lines.split("\n"))
                    .filter(line -> line.startsWith("-"))
                    .map(Total::resolve)
                    .collect(Collectors.toList());
        }

        public static Total resolve(String line) {
            //-                 Java Heap (reserved=253952KB, committed=16384KB)
            String regex = "- +([A-Za-z ]+)\\(reserved=(\\d+)KB, committed=(\\d+)KB\\)";
            String[] columns = RegexUtils.extractValue(line, regex);
            return new Total(columns[0].trim(), columns[1], columns[2]);
        }
    }

    @Data
    @AllArgsConstructor
    public static class VirtualMemoryMap implements Typed {

        private String addressStart;
        private String addressEnd;
        private String reserved;
        private String committed;
        private String type;

        public static List<VirtualMemoryMap> resolves(String lines) {
            String[] parts = lines.split("Details:", 2);
            String[] lineArray = parts[0].split("\n");
            List<VirtualMemoryMap> maps = new ArrayList<>(lineArray.length);
            int i = 0;
            for (String line : lineArray) {
                if (line.startsWith("[")) {
                    maps.add(resolve(line));
                    i++;
                } else if (line.startsWith("\t")) {
                    VirtualMemoryMap map = resolveCommitted(line);
                    map.setType(maps.get(i - 1).getType());
                    maps.add(map);
                    i++;
                }
            }
            return maps;
        }

        public static VirtualMemoryMap resolve(String line) {
            //[0x00000000f0800000 - 0x0000000100000000] reserved 253952KB for Java Heap from
            //[0x00007fb0f0afb000 - 0x00007fb0f0bfc000] reserved and committed 1028KB for Thread Stack from
            log.debug("resolve VirtualMemoryMap.reserved: {}", line);
            String regex = "\\[0x(\\w+) - 0x(\\w+)\\] reserved(?: and committed)? (\\d+)KB for ([A-Za-z ]+) from";
            String[] columns = RegexUtils.extractValue(line, regex);
            return new VirtualMemoryMap(columns[0].trim(), columns[1], columns[2], null, columns[3]);
        }

        public static VirtualMemoryMap resolveCommitted(String line) {
            //	[0x00000000f0800000 - 0x00000000f0d50000] committed 5440KB from
            log.debug("resolve VirtualMemoryMap.committed: {}", line);
            String regex = "\t\\[0x(\\w+) - 0x(\\w+)\\] committed (\\d+)KB from";
            String[] columns = RegexUtils.extractValue(line, regex);
            return new VirtualMemoryMap(columns[0], columns[1], null, columns[2], null);
        }
    }

}
