/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.relaxgaming;
import java.util.Random;

/**
 *
 * @author freda
 */
public class RelaxGaming {
    
    public static final int Seed = 1111111;
    
    private final Random Rand = new Random(Seed);
    
    /*
    private void PrintLn(String str)
    {
        System.out.println(str);
    }
    */
    
    // Returns a uniformly distributed int 1 <= x <= 6.
    private final int RollDice()
    {
        final int x = 1 + Rand.nextInt(6);
        return x;
    }
    
    private boolean WonGameB()
    {
        for (int j = 0; j < 24; j++)
        {
            int Die1 = RollDice();
            int Die2 = RollDice();
            if (Die1 == 6 && Die2 == 6)
            {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean WonGameA()
    {
        // We are allowed to roll the dice 4 times.
        for (int j = 0; j < 4; j++)
        {
            if (RollDice() == 6)
            {
                return true;
            }
        }
        
        return false;
    }
    
    private void Task1()
    {
        StringBuffer StringBuffer = new StringBuffer(512);
        int Wins                  = 0;
        final int Sims            = 1000000;
        
        // GameA -------------------------------------------------------------
        double STDDeviation = 0.00;
        double ProbabilityA = 1.0 - Math.pow(5.0 / 6.0, 4);
        double MeanA        = ProbabilityA;
        
        for (int i = 0; i < Sims; i++)
        {
            int W = 0;
            if (WonGameA())
            {
                W = 1;
                Wins += 1;
            }
            
            STDDeviation += (W - ProbabilityA) * (W - ProbabilityA);
        }
        
        STDDeviation = STDDeviation / Sims;
        STDDeviation = Math.sqrt(STDDeviation);
        double Average = (double) Wins / (double) Sims;
        
        StringBuffer.append("------------------- GAME A -----------------\n");
        StringBuffer.append("GameA average:  \t %f\n".formatted(Average));
        StringBuffer.append("std. deviation: \t %f\n".formatted(STDDeviation));
        StringBuffer.append("|MeanA - avg| = \t %f\n".formatted(Math.abs(MeanA - Average)));
        
        // GameB -------------------------------------------------------------
        Wins                = 0;
        STDDeviation        = 0.00;
        double ProbabilityB = 1.0 - Math.pow(35.0 / 36.0, 24);
        double MeanB        = ProbabilityB;
        
        for (int i = 0; i < Sims; i++)
        {
            int W = 0;
            if (WonGameB())
            {
                W = 1;
                Wins += 1;
            }
            
            STDDeviation += (W - ProbabilityB) * (W - ProbabilityB);
        }
        
        STDDeviation = STDDeviation / Sims;
        STDDeviation = Math.sqrt(STDDeviation);
        
        Average = (double) Wins / (double) Sims;
        
        StringBuffer.append("------------------- GAME B -----------------\n");
        StringBuffer.append("GameB average:  \t %f\n".formatted(Average));
        StringBuffer.append("std. deviation: \t %f\n".formatted(STDDeviation));
        StringBuffer.append("|MeanB - avg| = \t %f\n".formatted(Math.abs(MeanB - Average)));
        
        System.out.println(StringBuffer);
    }
    
    private void Task2()
    {
        FruitSlotGame FruitSlotGame = new FruitSlotGame();
        final int Sims = 10000000;
        for (int i = 0; i < Sims; i++)
        {
            FruitSlotGame.Play();
        }
        
        FruitSlotGame.DisplayFrequencyTable();
        // FruitSlotGame.DisplayReels();
        FruitSlotGame.DisplayBonusGame();

    }
    
    // Main code is running here
    private void Run()
    {
        System.out.println("Task1: \n");
        Task1();
        
        
        System.out.println("Task2: \n");
        Task2();
    }
    
    public static void main(String[] args) 
    {
        RelaxGaming RelaxGaming = new RelaxGaming();
        RelaxGaming.Run();
    }
}
