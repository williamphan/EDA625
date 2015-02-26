package wiii;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by will on 2015-02-25.
 */

public class Rsa {

    private static Random random = new Random();
    private static BigInteger ZERO = BigInteger.valueOf(0);
    private static BigInteger ONE = BigInteger.valueOf(1);
    private static BigInteger TWO = BigInteger.valueOf(2);

    public static void main(String[] args) {
        BigInteger test = new BigInteger(
            "11182862512480597003773178052387982726589694921383705217490047793042113798162294194312694783507941533238408108336625664397694418952020819183396060834902353");
        long startMillis = System.currentTimeMillis();
        System.out.println("test value is " + (checkPrime(test) ? "prime" : "not prime"));
        System.out.println("Running...");
        for (int i = 0; i < 100; i++) {
            BigInteger temp = newPrime(512);
            System.out.print("\r[" + (i < 9 ? "0" + String.valueOf(i + 1) : String.valueOf(i + 1))
                + "%] Computing primes...");
            System.out.println(temp);
        }
        System.out.println("\nTotal time: " + ((System.currentTimeMillis() - startMillis) / 1000));
    }

    public static BigInteger newPrime(int bits) {
        BigInteger nbr = new BigInteger(bits, random);
        while (!checkPrime(nbr)) {
            nbr = new BigInteger(bits, random);
        }
        return nbr;
    }

    public static boolean checkPrime(BigInteger nbr) {
        //Base cases
        if (nbr.equals(TWO)) {
            return true;
        }

        if (nbr.equals(BigInteger.valueOf(3))) {
            return true;
        }

        if (nbr.equals(BigInteger.valueOf(5))) {
            return true;
        }

        if (nbr.equals(BigInteger.valueOf(7))) {
            return true;
        }

        if (nbr.equals(BigInteger.valueOf(11))) {
            return true;
        }

        //...


        //Low numbers not allowed
        if (nbr.compareTo(ONE) <= 0) {
            return false;
        }

        //Odd only
        if (!nbr.testBit(0)) {
            return false;
        }

        //nbr-1
        BigInteger minusOne = nbr.subtract(ONE);

        //Factoring powers of 2 from nbr-1
        BigInteger s = minusOne;
        int r = s.getLowestSetBit();
        s = s.shiftRight(r);

        for (int i = 0; i < 20; i++) {
            // Random number 2 < a < nbr-1
            BigInteger a;
            do {
                a = new BigInteger(nbr.bitLength(), random);
            } while (!(a.compareTo(ONE) > 0 && a.compareTo(nbr) < 0));

            BigInteger x = a.modPow(s, nbr);
            int j = 0;
            while (!(x.equals(ONE) || x.equals(minusOne))) {
                if (j == r || x.equals(ONE)) {
                    return false;
                }
                j++;
                x = x.modPow(TWO, nbr);
                //                if(j > 0) {
                //                    System.out.println(j);
                //                }
            }
        }
        return true;

    }

    private static BigInteger exp_mod(BigInteger a, BigInteger x, BigInteger n) {
        if (a.equals(ZERO)) {
            return ZERO;
        }
        if (x.equals(ZERO)) {
            return ONE;
        }
        // if x is odd
        if (x.mod(TWO).compareTo(ZERO) != 0) {
            //(a * exp_mod(a,x-1,N)) mod N
            return (a.multiply(exp_mod(a, x.subtract(BigInteger.ONE), n))).mod(n);
        } else {
            //(exp_mod(a,(x div 2) ,n)^2) mod n
            return exp_mod(a, x.divide(TWO), n).pow(2).mod(n);
        }
    }
}
