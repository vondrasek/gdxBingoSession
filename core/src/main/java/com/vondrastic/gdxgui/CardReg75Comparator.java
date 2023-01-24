package com.vondrastic.gdxgui;

import java.util.Comparator;

public class CardReg75Comparator implements Comparator<BingoCard>
{
    public int compare(BingoCard c1, BingoCard c2)
    {
        return c1.getScore() - c2.getScore();
    }
}