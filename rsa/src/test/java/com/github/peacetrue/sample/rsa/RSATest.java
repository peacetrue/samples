package com.github.peacetrue.sample.rsa;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <ul>
 *      <li>欧几里得算法： gcd(a,b) = gcd(b,a mod b)</li>
 *      <li>贝祖等式：ax + by = gcd(a, b)</li>
 *      <li>ax+by=1->a(x+y)+(b-a)y=1</li> 23/27  23 4
 *  </ul>
 */
class RSATest {

    /**
     * 求两个数的最大公约数
     * <p>
     * 4、6 -> 2
     * 6、9 -> 3
     */
    public static int gcdChecked(int greater, int lesser) {
        if (greater <= 1) throw new IllegalArgumentException("greater must > 1");
        if (lesser <= 1) throw new IllegalArgumentException("lesser must > 1");
        if (greater == lesser) throw new IllegalArgumentException("greater must != lesser");
        return greater < lesser
                ? gcd(lesser, greater)
                : gcd(greater, lesser);
    }

    /**
     * remainder 是 lesser 的余数，所以 remainder 总是小于 lesser
     */
    public static int gcd(int greater, int lesser) {
        int remainder = greater % lesser;
        if (remainder == 0) return lesser;
        return gcd(lesser, remainder);
    }

    @Test
    void gcd() {
        Assertions.assertEquals(2, gcd(4, 6));
        Assertions.assertEquals(3, gcd(9, 6));
    }

    @Data
    @ToString
    @AllArgsConstructor
    public static class Seq {
        private int prev;
        private int curr;

        public static Seq s() {
            return new Seq(1, 0);
        }

        public static Seq t() {
            return new Seq(0, 1);
        }
    }


    /**
     * 求两个数的最大公约数
     */
    public static int gcd(int greater, int lesser, Seq s, Seq t) {
        int quotient = greater / lesser;
        int remainder = greater - lesser * quotient;
        change(s, quotient);
        change(t, quotient);
        return remainder == 0
                ? lesser
                : gcd(lesser, remainder, s, t);
    }

    private static void change(Seq s, int quotient) {
        int curr = s.curr;
        s.curr = s.prev - quotient * s.curr;
        s.prev = curr;
    }

    private static void println(int greater, int lesser, int gcd, Seq s, Seq t) {
        System.out.printf("%s * %s + %s * %s = %s", greater, s.curr, lesser, t.curr, 0);
        System.out.println();
        System.out.printf("%s * %s + %s * %s = %s", greater, s.prev, lesser, t.prev, gcd);
        System.out.println();
    }

    @Test
    void gcdx() {
        int greater = 133;
        int lesser = 123;
        Seq s = Seq.s();
        Seq t = Seq.t();
        int gcd = gcd(greater, lesser, s, t);
        Assertions.assertEquals(1, gcd);
        println(greater, lesser, gcd, s, t);
    }

}

