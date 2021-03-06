package org.nllptr;

import java.math.BigInteger;
import java.util.Random;

public class RSA {

    private static final BigInteger TWO = new BigInteger("2");
    private static final BigInteger FOUR = new BigInteger("4");
    private static final Random rnd = new Random();

    public static void main(String[] args) {
//        int bitLength = 512;
//        BigInteger p = generatePrime(bitLength);
//        BigInteger q = generatePrime(bitLength);
        BigInteger p = new BigInteger("7196783082013959922705252156453947032155512098110600241518532136595441314693077053581923961745331720607226687877041023096195136049660250158819636144589893");
        BigInteger q = new BigInteger("7022039410784562750645367011775226028701070992603592054912743598591995911025455679529540299006713316856397262016000570070262232362624748700235036719333997");
        System.out.println("P: " + p + "\nQ: " + q);
        BigInteger m = (p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))).divide(TWO);
        BigInteger e = new BigInteger(String.valueOf(new Double(Math.pow(2, 16) + 1).longValue()));
        System.out.println("M: " + m + "\nE: " + e);
        BigInteger d = inverseModM(e, m);
        System.out.println("D: " + d);
        BigInteger N = p.multiply(q);
        BigInteger s = randomOddBigInteger(400);
        System.out.println("S: " + s);
        BigInteger c = s.modPow(e, N);
        System.out.println("C: " + c);
        BigInteger z = c.modPow(d, N);
        System.out.println("Z: " + z);
        System.out.println((z.equals(s) ? "OK!" : "INCORRECT!"));
        testGenerateHundred(512);
    }

    public static void testInverseMod() {
        System.out.println("21^-1 mod 93: expected null. Got " + inverseModM(new BigInteger("21"), new BigInteger("93")));
        System.out.println("11^-1 mod 28: expected 23. Got " + inverseModM(new BigInteger("11"), new BigInteger("28")));
        System.out.println("5^-1 mod 67: expected 27. Got " + inverseModM(new BigInteger("5"), new BigInteger("67")));
    }

    public static void testGenerateHundred(int bitLength) {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            generatePrime(bitLength);
            System.out.print("\r[" + (i < 10 ? "0" + String.valueOf(i + 1) : String.valueOf(i + 1)) + "%] Computing primes... ");
        }
        long runningTime = (System.currentTimeMillis() - startTime);
        System.out.print(" Finished in " + runningTime + " ms\n");
    }

    private static BigInteger inverseModM(BigInteger a, BigInteger m) {
        BigInteger v1 = BigInteger.ZERO;
        BigInteger v2 = BigInteger.ONE;
        BigInteger d1 = m;
        BigInteger d2 = a;
        while(d2.compareTo(BigInteger.ZERO) != 0) {
            BigInteger q = d1.divide(d2);
            BigInteger t2 = v1.subtract(q.multiply(v2));
            BigInteger t3 = d1.subtract(q.multiply(d2));
            v1 = v2;
            d1 = d2;
            v2 = t2;
            d2 = t3;
        }
        if(d1.compareTo(BigInteger.ONE) > 0)
            return null; // a has no inverse mod M
        if(v1.compareTo(BigInteger.ZERO) < 0)
            return v1.add(m);
        else
            return v1;
    }

    private static boolean isProbablyPrime(BigInteger n) {
        if (n.compareTo(FOUR) < 0)
            return true;
        BigInteger s = n.subtract(BigInteger.ONE);
        BigInteger nMinusOne = s; // Store value to avoid re-computation in loop.
        BigInteger nMinusTwo = n.subtract(TWO); // Store value to avoid re-computation in loop.
        int r = 0;
        while (s.mod(TWO).equals(BigInteger.ZERO)) {
            s = s.divide(TWO);
            r++;
        }
        nextTest: for (int i = 0; i < 20; i++) {
            BigInteger a;
            do {
                a = new BigInteger(n.bitLength(), rnd);
            } while (!(a.compareTo(TWO) > 0 && a.compareTo(nMinusTwo) < 0));
            BigInteger x = a.modPow(s, n);
            if (x.equals(BigInteger.ONE) || x.equals(nMinusOne)) continue nextTest;
            for (int j = 1; j < r; j++) {
                x = x.modPow(TWO, n);
                if (x.equals(BigInteger.ONE)) return false;
                if (x.equals(nMinusOne)) continue nextTest;
            }
            return false;
        }
        return true; // no witness found, passed test
    }

    private static BigInteger generatePrime(int bitLength) {
        BigInteger n;
        do {
            n = randomOddBigInteger(bitLength);
        } while (!isProbablyPrime(n));
        return n;
    }

    private static BigInteger randomOddBigInteger(int bitLength) {
        BigInteger i;
        do {
            i = new BigInteger(bitLength, rnd);
        } while (!i.testBit(0)); // If number is even, redo!
        return i;
    }
}