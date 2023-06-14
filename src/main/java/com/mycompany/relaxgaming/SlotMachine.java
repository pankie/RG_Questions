/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.relaxgaming;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author freda
 */
public class SlotMachine 
{
    
    private final Random Rand             = new Random(RelaxGaming.Seed);
    private Map<Symbol, Integer> Paytable = new HashMap();
    protected final BonusGame BonusGame   = new BonusGame();
    
    protected enum Symbol
    {
        W1, H1, H2, H3, L1, L2, L3, L4, B1
    };
    
    protected Symbol[] Reel0, Reel1, Reel2;
    protected int[] Paylines;
    
    public SlotMachine()
    {
        Paytable.put(Symbol.W1, 2000);
        Paytable.put(Symbol.H1, 800);
        Paytable.put(Symbol.H2, 400);
        Paytable.put(Symbol.H3, 80);
        Paytable.put(Symbol.L1, 60);
        Paytable.put(Symbol.L2, 20);
        Paytable.put(Symbol.L3, 16);
        Paytable.put(Symbol.L4, 12);

        // This value will be obtained from GetBonus method
        Paytable.put(Symbol.B1, 0); 
    }
    
    
    protected final int GetBonus(final Symbol[][] Result)
    {
        // Since the B1 symbol is a scatter symbol, it only has to occur
        // at least once on every displaying reel.
        for (int i = 0; i < 3; i++)
        {
            Symbol S0 = Result[0][i];
            Symbol S1 = Result[1][i];
            Symbol S2 = Result[2][i];
            
            final boolean bContainsBonusSymbol 
                = S0 == Symbol.B1 || S1 == Symbol.B1 || S2 == Symbol.B1;
            
            if (!bContainsBonusSymbol)
            {
                return 0;
            }
        }
        
        final int TotalBet  = 10;
        final int Bonus     = BonusGame.Play(TotalBet);
        return Bonus;
    }
    
    // Returns the total amount in credits for 3 of a kind symbol
    protected final int GetCredits(Symbol Symbol)
    {
        final int Credits = Paytable.get(Symbol);
        return Credits;
    }
    
    // Returns the most frequent symbol from any payline
    protected final Symbol GetSymbol(Symbol[][] Result, int[] PaylineIndices)
    {
        Map<Symbol, Integer> FrequencyTable = new HashMap();
        
        // Generate frequency of the symbols by the given payline indices
        for (int k = 1; k < 4; k++)
        {
            final int x = PaylineIndices[(k - 1) * 2];
            final int y = PaylineIndices[(k - 1) * 2 + 1];
            Symbol Key  = Result[y][x];
            
            if (FrequencyTable.containsKey(Key))
            {
                Integer Value = FrequencyTable.get(Key);
                FrequencyTable.put(Key, Value + 1);
                
                continue;
            }
            
            FrequencyTable.put(Key, 1);
        }
        
        // Find out which is the most frequent symbol
        int Frequency               = 0;
        Symbol MostFrequentSymbol   = null;
        for (Symbol Symbol : FrequencyTable.keySet())
        {
            if (Frequency < FrequencyTable.get(Symbol))
            {
                Frequency           = FrequencyTable.get(Symbol);
                MostFrequentSymbol  = Symbol;
            }
        }
        
        // 3 of same symbol
        if (Frequency == 3)
        {
            return MostFrequentSymbol;
        }
        
        // All symbols are unique, no win
        if (Frequency == 1)
        {
            return null;
        }
        
        // at least one is Wild
        if (FrequencyTable.containsKey(Symbol.W1))
        {
            for (Symbol Symbol : FrequencyTable.keySet())
            {
                if (Symbol != Symbol.W1)
                {
                    // Return the symbol with same frequence
                    if (FrequencyTable.get(Symbol) == 2)
                    {
                        return Symbol;
                    }
                    else if (FrequencyTable.get(Symbol) == 1 && FrequencyTable.get(Symbol.W1) == 2)
                    {
                        return Symbol;
                    }
                }
            }
        }
        
        return null;
    }
    
    protected final int GetTotalPaylines()
    {
        return Paylines.length / 9;
    }
    
    // Displays the indices for any result matrix in the following format:
    // x0y0 x1y1 x2y2
    protected void Debug_Display(int[] PaylineIndices)
    {
        StringBuffer StringBuffer = new StringBuffer(256);
        StringBuffer.append("Payline indices: ");
        for (int i = 0; i < 6; i++)
        {
            StringBuffer.append("%d".formatted(PaylineIndices[i]));
            if ((i + 1) % 2 == 0)
            {
                StringBuffer.append(" ");
            }
        }
        
        final String Output = StringBuffer.toString();
        System.out.println(Output);
    }
    
    // Displays the resulting matrix with symbols
    protected void Debug_Display(Symbol[][] Result)
    {
        StringBuffer StringBuffer = new StringBuffer(256);
        StringBuffer.append("\n");
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                final String SymbolName = Result[i][j].name();
                StringBuffer.append(" %s\t".formatted(SymbolName));
            }
            
            StringBuffer.append("\n");
        }
        
        final String Output = StringBuffer.toString();
        System.out.println(Output);
    }
    
    // Display the resulting matrix together with a payline
    protected void Debug_Display(Symbol[][] Result, int[] PaylineIndices)
    {
        StringBuffer StringBuffer = new StringBuffer(256);
        int k = 1;
        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
                if ( k < 4 )
                {
                    final int M = (k - 1) * 2;
                    if (PaylineIndices[M] == x && PaylineIndices[M + 1] == y)
                    {
                        StringBuffer.append(
                        "[%s]\t".formatted(Result[y][x].name())
                        );

                        k += 1;
                        continue;
                    }
                }
                
                StringBuffer.append(" %s\t".formatted(Result[y][x].name()));
            }
            
            StringBuffer.append("\n");
        }
        
        final String Output = StringBuffer.toString();
        System.out.println(Output);
    }
    
    protected final Symbol[][] Spin()
    {
        Symbol[][] ResultMatrix = new Symbol[3][3];
        
        final int Reel0Length = Reel0.length;
        final int Reel1Length = Reel1.length;
        final int Reel2Length = Reel2.length;
        
        // Spin the reels
        final int Reel0Index = Rand.nextInt(0,Reel0Length);
        final int Reel1Index = Rand.nextInt(0,Reel1Length);
        final int Reel2Index = Rand.nextInt(0,Reel2Length);
        
        ResultMatrix[0][0] = Reel0[Reel0Index];
        ResultMatrix[0][1] = Reel1[Reel1Index];
        ResultMatrix[0][2] = Reel2[Reel2Index];
                
        ResultMatrix[1][0] = Reel0[(Reel0Index + 1) % Reel0Length];
        ResultMatrix[1][1] = Reel1[(Reel1Index + 1) % Reel1Length];
        ResultMatrix[1][2] = Reel2[(Reel2Index + 1) % Reel2Length];
        
        ResultMatrix[2][0] = Reel0[(Reel0Index + 2) % Reel0Length];
        ResultMatrix[2][1] = Reel1[(Reel1Index + 2) % Reel1Length];
        ResultMatrix[2][2] = Reel2[(Reel2Index + 2) % Reel2Length];

        return ResultMatrix;
    }
    
    protected final int[] GetPaylineIndices(int Payline)
    {
        // Store the indices in pairs of x and y in a row matrix
        // e.g. PaylineIndices = { x0,y0, x1,y1, x2,y2 }
        // where x represents column and y is the row in the Result matrix. 
        int[] PaylineIndices = new int[6];
        
        int K = 0;
        for (int i = 0; i < 9; i++)
        {
            if (K == 6)
            {
                break;
            }
            
            int j = Payline * 9 + i;
            if (Paylines[j] == 1)
            {
                // Store the x
                PaylineIndices[K] = i % 3;
                K += 1;
                
                // then store the y
                if (i < 3)
                {
                    PaylineIndices[K] = 0;
                }
                else if (i < 6)
                {
                    PaylineIndices[K] = 1;
                }
                else
                {
                    PaylineIndices[K] = 2;
                }
                
                K += 1;
            }
        }
        
        return PaylineIndices;
    }
    
    protected void InitializePaylines()
    {
        Paylines = new int[]
        {
            // Payline 0
            1, 1, 1,
            0, 0, 0,
            0, 0, 0,
            
            // Payline 1
            0, 0, 0,
            1, 1, 1,
            0, 0, 0,
            
            // Payline 2
            0, 0, 0,
            0, 0, 0,
            1, 1, 1,
            
            // Payline 3
            1, 0, 0,
            0, 1, 0,
            0, 0, 1,
            
            // Payline 4
            0, 0, 1,
            0, 1, 0,
            1, 0, 0
        };
       
    }
    
    protected void InitializeReels()
    {
        Symbol[] InitReel0 = { 
            Symbol.L4, Symbol.L3, Symbol.L2, 
            Symbol.B1, Symbol.H2, Symbol.H2,
            Symbol.H2, Symbol.L1, Symbol.L1,
            Symbol.L1, Symbol.H3, Symbol.H1,
            Symbol.H2, Symbol.L2, Symbol.L2,
            Symbol.L2, Symbol.W1, Symbol.L3,
            Symbol.L3, Symbol.L3, Symbol.H3,
            Symbol.H3, Symbol.H3, Symbol.L4,
            Symbol.L4, Symbol.L4, Symbol.H3,
            Symbol.L1, Symbol.L2, Symbol.L4,
            Symbol.H3, Symbol.L3, Symbol.L3,
            Symbol.L3, Symbol.B1, Symbol.L2,
            Symbol.L2, Symbol.L2, Symbol.L2,
            Symbol.H3, Symbol.H3, Symbol.H3,
            Symbol.L4, Symbol.L4, Symbol.L4,
            Symbol.B1, Symbol.L1, Symbol.L1,
            Symbol.L1, Symbol.H1, Symbol.L2,
            Symbol.L2, Symbol.L2, Symbol.B1,
            Symbol.L3, Symbol.L3, Symbol.L3
        };
        
        Symbol[] InitReel1 = { 
            Symbol.L4, Symbol.L3, Symbol.L2, 
            Symbol.B1, Symbol.H2, Symbol.H2,
            Symbol.H2, Symbol.L1, Symbol.L1,
            Symbol.L1, Symbol.H3, Symbol.H1,
            Symbol.H2, Symbol.L2, Symbol.L2,
            Symbol.L2, Symbol.W1, Symbol.L3,
            Symbol.L3, Symbol.L3, Symbol.H3,
            Symbol.H3, Symbol.H3, Symbol.L4,
            Symbol.L4, Symbol.L4, Symbol.H3,
            Symbol.L1, Symbol.L2, Symbol.L4,
            Symbol.H3, Symbol.L3, Symbol.L3,
            Symbol.L3, Symbol.B1, Symbol.L2,
            Symbol.L4, Symbol.H3, Symbol.H3,
            Symbol.H3, Symbol.B1, Symbol.L2,
            Symbol.L2, Symbol.L2, Symbol.L2,
            Symbol.B1, Symbol.L3, Symbol.L3,
            Symbol.L3, Symbol.H1, Symbol.L4,
            Symbol.L4, Symbol.L4, Symbol.L3
        };
        
        Symbol[] InitReel2 = { 
            Symbol.L4, Symbol.L3, Symbol.L2, 
            Symbol.B1, Symbol.H2, Symbol.H2,
            Symbol.H2, Symbol.L1, Symbol.L1,
            Symbol.L1, Symbol.H3, Symbol.H1,
            Symbol.H2, Symbol.L2, Symbol.L2,
            Symbol.L2, Symbol.W1, Symbol.L3,
            Symbol.L3, Symbol.L3, Symbol.H3,
            Symbol.H3, Symbol.H3, Symbol.L4,
            Symbol.L4, Symbol.L4, Symbol.H3,
            Symbol.L1, Symbol.L2, Symbol.L4,
            Symbol.H3, Symbol.L3, Symbol.L3,
            Symbol.L3, Symbol.B1, Symbol.L2,
            Symbol.H2, Symbol.H3, Symbol.H3,
            Symbol.H3, Symbol.L2, Symbol.L2,
            Symbol.L2, Symbol.L1, Symbol.L1,
            Symbol.B1, Symbol.L2, Symbol.L2,
            Symbol.L2, Symbol.L1
        };

        Reel0 = InitReel0;
        Reel1 = InitReel1;
        Reel2 = InitReel2;
    }
    
    protected Map<Symbol, Integer> GetTotalSymbolsTable(final Symbol[] Reel)
    {
        Map<Symbol, Integer> SymbolsTable = new HashMap();
        for (Symbol Symbol : Symbol.values())
        {
            SymbolsTable.put(Symbol, 0);
        }
         
        for (int i = 0; i < Reel.length; i++)
        {
            final Symbol Symbol = Reel[i];
            int Hit = SymbolsTable.get(Symbol);
            SymbolsTable.put(Symbol, Hit + 1);
        }
        
        return SymbolsTable;
    }
}


