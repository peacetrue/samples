package com.github.peacetrue.samples.sonar;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author : xiayx
 * @since : 2021-02-01 15:27
 **/
public class SonarUtilsTest {

    @Test
    public void add() {
        Assert.assertEquals(2, SonarUtils.add(1, 1));
    }

}
