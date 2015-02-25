package org.nllptr;

import java.math.BigInteger;
import java.util.Random;

public class RSA {

    private static final BigInteger TWO = new BigInteger("2");
    private static final BigInteger FOUR = new BigInteger("4");

    public static void main(String[] args) {
        randomOddBigInteger(512);
        generateHundred(512);
//        int bitLength = 512;
//        BigInteger p = generatePrime(bitLength);
//        BigInteger q = generatePrime(bitLength);
//        System.out.println(p + "\n" + q);
//        BigInteger m = (p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))).divide(TWO);
//        BigInteger e = new BigInteger(String.valueOf(new Double(Math.pow(2, 16) + 1).longValue()));
//        System.out.println(m + "\n" + e);
//        BigInteger d = inverseModM(e, m);
//        System.out.println(d);
//        BigInteger N = p.multiply(q);
//        BigInteger s = new BigInteger("42");
//        System.out.println(s);
//        BigInteger c = s.modPow(e, N);
//        System.out.println(c);
//        BigInteger z = c.modPow(d, N);
//        System.out.println(z);

    }

    public static void testInverseMod() {
        System.out.println("21^-1 mod 93: expected null. Got " + inverseModM(new BigInteger("21"), new BigInteger("93")));
        System.out.println("11^-1 mod 28: expected 23. Got " + inverseModM(new BigInteger("11"), new BigInteger("28")));
        System.out.println("5^-1 mod 67: expected 27. Got " + inverseModM(new BigInteger("5"), new BigInteger("67")));
    }

    /**
     * Generates 100 primes of the supplied bit length and prints the running time.
     * This method is meant to be used for testing.
     *
     * @param bitLength The desired bit length of the primes.
     */
    public static void generateHundred(int bitLength) {
        long startTime = System.currentTimeMillis();
        BigInteger n = null;

        for (int i = 0; i < 100; i++) {
            n = generatePrime(bitLength);
            System.out.print("\r[" + (i < 10 ? "0" + String.valueOf(i + 1) : String.valueOf(i + 1)) + "%] Computing primes...");
//            if (n.bitLength() != bitLength) {
//                System.out.print(" Aborted!\nGenerated number is of incorrect bit length!");
//                break;
//            }
        }
        long runningTime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.print(" Finished in " + runningTime + " seconds\n");
    }

    /**
     * Calculates the inverse multiplicative modulo m for a.
     *
     * @param a The integer for which to calculate the inverse.
     * @param m The modulo.
     * @return A BigInteger
     */
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
            return null;
        if(v1.compareTo(BigInteger.ZERO) < 0)
            return v1.add(m);
        else
            return v1;
    }

    /**
     * Tests an odd integer n for primality using the Rabin Miller pseudoprime test.
     * Note that even if the method returns false, it can still be a composite.
     *
     * @param n         The integer under test.
     * @param bitLength The desired bit length of the primes.
     * @return True if n is found to be composite. False otherwise.
     */
    /**
     * @param n
     * @return
     */
    private static boolean isProbablyPrime(BigInteger n, int bitLength) {
        if (n.compareTo(FOUR) < 0)
            return false;
        BigInteger s = n.subtract(BigInteger.ONE);
        int r = 0;
        while (s.mod(TWO) == BigInteger.ZERO) {
            s = s.divide(TWO);
            r++;
        }
        for (int i = 0; i < 20; i++) {
            BigInteger a = new BigInteger(n.bitLength(), new Random());
            while (a.compareTo(BigInteger.ZERO) <= 0 || a.compareTo(n) >= 0) {
                a = new BigInteger(n.bitLength(), new Random());
            }
            BigInteger x = a.modPow(s, n);
            if (x.compareTo(BigInteger.ONE) == 0 || x.compareTo(n.subtract(BigInteger.ONE)) == 0)
                return true;
            for (int j = 1; j < r; j++) {
                x = x.modPow(TWO, n);
                if (x.compareTo(BigInteger.ONE) == 0) return false;
                if (x.compareTo(n.subtract(BigInteger.ONE)) == 0) return true;
            }
            return false;
        }
        System.out.println(n + " = 2^" + r + " * " + s + " + 1");
        System.out.println(n.bitLength());
        return true;
    }

    private static BigInteger generatePrime(int bitLength) {
        BigInteger n = randomOddBigInteger(bitLength);
        boolean prime = isProbablyPrime(n, bitLength);
        while (!prime) {
            n = randomOddBigInteger(bitLength);
            prime = isProbablyPrime(n, bitLength);
        }
        return n;
    }

    private static BigInteger randomOddBigInteger(int bitLength) {
//        BigInteger i = BigInteger.ZERO.flipBit(bitLength - 1); // This bit must be set to garantuee the desired bitlength.
        BigInteger rnd = new BigInteger(bitLength, new Random());
//        System.out.println(rnd.bitLength());
        while(rnd.mod(TWO).compareTo(BigInteger.ZERO) == 0) {  // Redo if BigInteger is even. Cuts running time in half.
            rnd = new BigInteger(bitLength - 2, new Random());
        }
//        return i.add(rnd);
        return rnd;
    }


    //------------------------------------------------------------------------------

    /**
     * Utility method for getting binary string representations of BigIntegers.
     *
     * @param n The integer to represent.
     * @return A string containing the binary representation.
     */
    private static String binaryToString(BigInteger n) {
        StringBuilder sb = new StringBuilder();
        for (byte b : n.toByteArray()) {
            sb.append(Integer.toBinaryString(b & 255 | 256).substring(1));
        }
        return sb.toString();
    }
}