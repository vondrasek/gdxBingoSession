package com.vondrastic.gdxgui;

import java.util.Comparator;

public class CardDbl75Comparator implements Comparator<CardDbl75>
{
    public int compare(CardDbl75 c1, CardDbl75 c2)
    {
        return c1.getScore() - c2.getScore();
    }
}
