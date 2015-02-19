package org.nllptr;

import java.math.BigInteger;
import java.util.Random;

public class RSA {

    private static final BigInteger TWO = new BigInteger("2");
    private static final BigInteger FOUR = new BigInteger("4");

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        BigInteger n = null;

        for(int i = 0; i < 100; i++) {
            n = generatePrime(1024);
            System.out.print("\r[" + (i < 10 ? "0" + String.valueOf(i) : String.valueOf(i)) + "%] Computing primes...");
        }
        long runningTime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.print(" Finished in " + runningTime + " seconds\n");
    }

    /**
     * Tests an odd integer n for primality using the Rabin Miller pseudoprime test.
     * Note that even if the method returns false, it can still be a composite.
     *
     * @param n     The integer under test.
     * @return      True if n is found to be composite. False otherwise.
     */
    private static boolean isProbablyPrime(BigInteger n, int bitLength) {
        if(n.compareTo(FOUR) < 0)
            return false;
        BigInteger s = n.subtract(BigInteger.ONE);
        int r = 0;
        while(s.mod(TWO) == BigInteger.ZERO) {
            s = s.divide(TWO);
            r++;
        }
        for(int i = 0; i < 20; i++) {
            BigInteger a = new BigInteger(n.bitLength(), new Random());
            while (a.compareTo(BigInteger.ZERO) <= 0 || a.compareTo(n) >= 0) {
                a = new BigInteger(n.bitLength(), new Random());
            }
            BigInteger x = a.modPow(s, n);
            if(x.compareTo(BigInteger.ONE) == 0|| x.compareTo(n.subtract(BigInteger.ONE)) == 0)
                return true;
            for(int j = 1; j < r; j++) {
                x = x.modPow(TWO, n);
//                x = a.modPow(TWO.pow(j).multiply(s), n);
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
        BigInteger n = randomBigInteger(bitLength);
        boolean prime = isProbablyPrime(n, bitLength);
        while (!prime) {
            n = randomBigInteger(bitLength);
            prime = isProbablyPrime(n, bitLength);
        }
        return n;
    }

    private static BigInteger randomBigInteger(int bitLength) {
        BigInteger i = BigInteger.ZERO.flipBit(bitLength - 1); // This bit must be set to garantuee the desired bitlength.
        BigInteger rnd = new BigInteger(bitLength - 1, new Random());
        return i.add(rnd);
    }


    //------------------------------------------------------------------------------
    /**
     * Utility method for getting binary string representations of BigIntegers.
     *
     * @param n     The integer to represent.
     * @return      A string containing the binary representation.
     */
    private static String binaryToString(BigInteger n) {
        StringBuilder sb = new StringBuilder();
        for (byte b : n.toByteArray()) {
            sb.append(Integer.toBinaryString(b & 255 | 256).substring(1));
        }
        return sb.toString();
    }
}