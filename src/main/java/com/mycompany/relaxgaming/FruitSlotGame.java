/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.relaxgaming;

import java.util.HashMap;
import java.util.Map;


public class FruitSlotGame extends SlotMachine
{
    private Map<Symbol, Integer> FrequencyTable;

    private int Spins                   = 0;
    private int NumberOfWins            = 0;
    private int TotalBetAmount          = 0;
    private int TotalBaseGameWonAmount  = 0;
    private int TotalBonusGameWon       = 0;
    private int HitBonusGameFrequency   = 0;
    
    public FruitSlotGame()
    {
        
        InitializeReels();
        InitializePaylines();
        InitializeFrequencyTable();
    }
    
    public void Play()
    {
        final Symbol[][] Result = Spin();
        Spins                   = Spins + 1;
        TotalBetAmount          = TotalBetAmount + 10;
        // Debug_Display(Result);
        
        final int Bonus = GetBonus(Result);
        if (Bonus > 0)
        {
            HitBonusGameFrequency++;
            TotalBonusGameWon += Bonus;
        }
        
        for (int i = 0; i < GetTotalPaylines(); i++)
        {
            final int[] PaylineIndices  = GetPaylineIndices(i);
            final Symbol Symbol         = GetSymbol(Result, PaylineIndices);
            final boolean bHasWon       = Symbol != null;

            if (bHasWon)
            {
                AddToFrequencyTable(Symbol);
                // Debug_Display(Result, PaylineIndices);
                
                NumberOfWins           += 1;
                TotalBaseGameWonAmount += GetCredits(Symbol);
            }
        }
    }
    
    @Override protected void InitializeReels()
    {
        super.InitializeReels();
    }
    
    @Override protected void InitializePaylines()
    {
        super.InitializePaylines();
        // int[] Payline = GetPaylineIndices(1);
        // Debug_Display(Payline);
        
        /*
        Paylines = new int[]
        {
            1, 1, 1,
            0, 0, 0,
            0, 0, 0,
        };
        */
    }

    private void InitializeFrequencyTable()
    {
        FrequencyTable = new HashMap<Symbol, Integer>();
        for (Symbol Symbol : Symbol.values())
        {
            FrequencyTable.put(Symbol, 0);
        }
    }
    
    private void AddToFrequencyTable(Symbol Symbol)
    {
        Integer Hits    = FrequencyTable.get(Symbol);
        Hits            = Hits + 1;
        FrequencyTable.put(Symbol, Hits);
    }
    
    public void DisplayBonusGame()
    {
        BonusGame.Display();
    }
    
    // Show the win frequency table
    public void DisplayFrequencyTable()
    {
        StringBuffer StringBuffer = new StringBuffer(256);
        
        StringBuffer.append("*-----------  FrequencyTable  -----------*\n");
        StringBuffer.append("Symbol \t  Hits   \t Probability\n\n");

        double AccumulatedProbabilities = 0.0;
        for (Symbol Symbol : Symbol.values())
        {
            final String Name = Symbol.name();
            final int Value   = FrequencyTable.get(Symbol);
            final double Probability = (double) Value / (double) Spins; // assuming Spins != 0
            AccumulatedProbabilities += Probability;
            StringBuffer.append("%s \t  %d   \t %f\n".formatted(Name, Value, Probability));
        }
        
        final double TotalWonAmount = TotalBaseGameWonAmount + TotalBonusGameWon;
        
        StringBuffer.append("\nTotal \t  %d   \t %f\n".formatted(NumberOfWins,AccumulatedProbabilities));
        StringBuffer.append("\nBonus Game return: %f\n".formatted((double) TotalBonusGameWon / (double) TotalBetAmount));
        StringBuffer.append("Base Game return: %f\n".formatted((double) TotalBaseGameWonAmount / (double) TotalBetAmount));
        StringBuffer.append("Total return to player: %f\n".formatted(TotalWonAmount / (double) TotalBetAmount));
        StringBuffer.append("Bonus game hit frequency: %f\n".formatted((double) Spins / (double) HitBonusGameFrequency ));
        StringBuffer.append("*----------------------------------------*\n");
        final String Output = StringBuffer.toString();
        System.out.println(Output);
    }
    
    public void DisplayReels()
    {
        StringBuffer StringBuffer = new StringBuffer(256);
        Map<Symbol, Integer> SymbolsReel0 = GetTotalSymbolsTable(Reel0);
        Map<Symbol, Integer> SymbolsReel1 = GetTotalSymbolsTable(Reel1);
        Map<Symbol, Integer> SymbolsReel2 = GetTotalSymbolsTable(Reel2);

        for (Symbol Symbol : Symbol.values())
        {
            final String Name   = Symbol.name();
            final int Value0     = SymbolsReel0.get(Symbol);
            final int Value1     = SymbolsReel1.get(Symbol);
            final int Value2     = SymbolsReel2.get(Symbol);
            
            final String Text   = new String(
            "%s \t %d \t %d \t %d\n".formatted(Name, Value0, Value1, Value2)
            );
            
            StringBuffer.append(Text);
        }
        
        System.out.println(StringBuffer);
    }
}
