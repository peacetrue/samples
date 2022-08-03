package com.github.peacetrue.jvm;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author peace
 **/
@Slf4j
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PmapInfo {

    private String address;
    private String virtual;
    private String rss;
    private String dirty;
    private String mode;
    private String mapping;

    public static List<PmapInfo> resolves(String lines) {
        return resolves(Arrays.asList(lines.split("\n")));
    }

    public static List<PmapInfo> resolves(Collection<String> lines) {
        return lines.stream().skip(2)
                .map(PmapInfo::resolve)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static PmapInfo resolve(String line) {
        String[] parts = line.split(" +", 6);
        log.debug("split line: {} -> {}", line, Arrays.toString(parts));
        if (parts.length != 6) return null;
        return PmapInfo.builder()
                .address(parts[0])
                .virtual(parts[1])
                .rss(parts[2])
                .dirty(parts[3])
                .mode(parts[4])
                .mapping(parts[5])
                .build();
    }
}
