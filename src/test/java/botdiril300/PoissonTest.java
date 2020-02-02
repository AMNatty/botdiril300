package botdiril300;

import org.apache.commons.math3.random.RandomDataGenerator;

public class PoissonTest
{
    public static void main(String[] args)
    {
        var rdg = new RandomDataGenerator();

        var sum = 0;
        long max = 0;

        for (int i = 0; i < 10_000_000; i++)
        {
            var val = rdg.nextPoisson(0.01);
            sum += val;

            if (val > max)
            {
                max = val;
            }
        }

        System.out.println(sum);
        System.out.println(max);
    }
}
