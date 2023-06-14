/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.relaxgaming;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author freda
 */
public class BonusGame 
{
    private Random Rand = new Random(RelaxGaming.Seed);
    private int AccumulatedWeight = 0;
    
    private List<Entry> Entries = new ArrayList<>();
    
    private class Entry
    {
        Coin Coin;
        int AccumulatedWeight = 0;
    }
    
    private class Coin
    {
        public Coin(final int Value, final int Weight)
        {
            this.Value          = Value;
            this.Weight         = Weight;
            this.Probability    = Weight / 1401.0;
        }
        
        private final int Value;
        private final int Weight;
        private final double Probability;
       
        public @Override String toString()
        {
            return "%d \t %f".formatted(Weight, Probability);
        }
    }
    
    private void AddEntry(Coin Coin)
    {
        AccumulatedWeight += Coin.Weight;
        Entry Entry = new Entry();
        Entry.Coin = Coin;
        Entry.AccumulatedWeight = AccumulatedWeight;
        Entries.add(Entry);
    }
    
    private Coin GetRandomCoin()
    {
        final double Value = Rand.nextDouble() * AccumulatedWeight;
        
        for (Entry Entry : Entries)
        {
            if (Entry.AccumulatedWeight >= Value)
            {
                return Entry.Coin;
            }
        }
        
        return null;
    }
    
    
    public BonusGame()
    {
        InitializeWeightedCoins();
    }
    
    public void Display()
    {
        StringBuffer StringBuffer = new StringBuffer(512);
        StringBuffer.append("* --------------- BonusGame --------------- *\n");

        StringBuffer.append("Coins \t Probability\n");
        for (Entry Entry : Entries)
        {
            StringBuffer.append(Entry.Coin);
            StringBuffer.append("\n");
        }
        
        // Calculate the averages
        final int Sims = 1000000;
        double ValueSum = 0.0;
        double DieSum = 0.0;
        for (int i = 0; i < Sims; i++)
        {
            ValueSum += GetRandomCoin().Value;
            DieSum   += PlayDice();
        }
        
        final double ValueEst = ValueSum / Sims;
        final double ValueAvg = GetAverageCoin();
        final double DieEst   = DieSum / Sims;
        final double DieAvg   = GetAverageDice();
        
        
        StringBuffer.append("\nCoin value average vs true average: \n");
        StringBuffer.append("Coin value average   : %f\n".formatted(ValueEst));
        StringBuffer.append("Coin value true avg. : %f\n".formatted(ValueAvg));
        StringBuffer.append(
         " |Average - Estimated average| = %f".formatted(Math.abs(ValueEst - ValueAvg))
        );
        
        StringBuffer.append("\nDice average vs true average: \n");
        StringBuffer.append("Dice value average   : %f\n".formatted(DieEst));
        StringBuffer.append("Dice value true avg. : %f\n".formatted(DieAvg));
        StringBuffer.append(
                " |Average - Estimated average| = %f".formatted(Math.abs(DieEst - DieAvg))
        );
        StringBuffer.append("\n* --------------------------------------- *\n");
        
        System.out.println(StringBuffer);
    }
    
    public final int Play(final int TotalBet)
    {
        final int DiceValue = PlayDice();
        final int CoinValue = PlayCoin();
        
        return TotalBet * DiceValue * CoinValue;
    }
    
    private void InitializeWeightedCoins()
    {
        final Coin[] Coins = new Coin[]
        {
            new Coin(100, 10),
            new Coin(50,20),
            new Coin(30, 40),
            new Coin(20, 50),
            new Coin(10,151),
            new Coin(5,220),
            new Coin(4,260),
            new Coin(3,300),
            new Coin(2, 350)
        };
        
        for (Coin Coin : Coins)
        {
            AddEntry(Coin);
        }
    }
    
    private final int PlayDice()    
    {
        return 1 + Rand.nextInt(0, 6);
    }
    
    private final int PlayCoin()
    {
        Coin Coin = GetRandomCoin();
        final int Value = Coin.Value;
        return Value;
    }
    
    // Calculates the true average for coins
    private final double GetAverageCoin()
    {
        double Sum = 0.0;
        for (Entry Entry : Entries)
        {
            Coin Coin = Entry.Coin;
            Sum += Coin.Value * Coin.Probability;
        }
        
        return Sum;
    }
    
    // Calculats the true average for dice
    private final double GetAverageDice()
    {
        double Sum = 0.0;
        final double Probability = 1.0 / 6.0;
        for (int i = 1; i < 7; i++)
        {
            Sum += Probability * i;
        }
        
        return Sum;
    }
}
