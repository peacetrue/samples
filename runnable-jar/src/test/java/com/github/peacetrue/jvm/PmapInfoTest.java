package com.github.peacetrue.jvm;

import com.github.peacetrue.test.SourcePathUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author peace
 **/
class PmapInfoTest {

    @Test
    void resolve() throws Exception {
        String path = SourcePathUtils.getTestResourceAbsolutePath("/pmap.txt");
        List<PmapInfo> pmapInfos = PmapInfo.resolves(Files.readAllLines(Paths.get(path)));
        Assertions.assertTrue(pmapInfos.size() > 0);
    }

    @Test
    void resolve2() throws Exception {
        String path = SourcePathUtils.getTestResourceAbsolutePath("/pmap.txt");
        List<PmapInfo> pmapInfos = PmapInfo.resolves(Files.readAllLines(Paths.get(path)));
        System.out.println(pmapInfos.stream().map(PmapInfo::getMapping).distinct().collect(Collectors.toList()));

        path = SourcePathUtils.getTestResourceAbsolutePath("/nmt.txt");
        NativeMemoryTracking tracking = NativeMemoryTracking.resolve(new String(Files.readAllBytes(Paths.get(path))));
        System.out.println(tracking.getVirtualMemoryMaps().stream().map(NativeMemoryTracking.VirtualMemoryMap::getType).distinct().collect(Collectors.toList()));

//        List<NativeMemoryTracking.Typed> typeds = new ArrayList<>();
//        typeds.addAll(tracking.getTotals());
//        typeds.addAll(tracking.getVirtualMemoryMaps());

        Map<String, List<NativeMemoryTracking.VirtualMemoryMap>> virtualMemoryMaps = tracking.getVirtualMemoryMaps()
                .stream().collect(Collectors.groupingBy(NativeMemoryTracking.VirtualMemoryMap::getAddressStart));
        Map<String, List<NativeMemoryTracking.VirtualMemoryMap>> virtualMemoryMaps2 = tracking.getVirtualMemoryMaps()
                .stream().collect(Collectors.groupingBy(NativeMemoryTracking.VirtualMemoryMap::getAddressEnd));
        virtualMemoryMaps.putAll(virtualMemoryMaps2);
        pmapInfos.forEach(item -> {
            List<NativeMemoryTracking.VirtualMemoryMap> virtualMemoryMap = virtualMemoryMaps.get(item.getAddress());
            System.out.printf("%s: %s", item, virtualMemoryMap);
            System.out.println();
        });
    }

    @Test
    void name2() {
        Matcher matcher = Pattern.compile("Windows (?=95|98|NT|2000)").matcher("Windows 2000");
        while (matcher.find()) {
            System.out.println(matcher.group());
            int count = matcher.groupCount();
            for (int i = 0; i < count; i++) {
                System.out.println(matcher.group(i));
            }
        }
    }
}
