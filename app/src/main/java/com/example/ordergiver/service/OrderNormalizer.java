package com.example.ordergiver.service;

import java.text.Normalizer;

public class OrderNormalizer
{
    /**
     * Delete all white-spaces and useless words
     */
    public String subStringOrder(String sentence)
    {
        int length = sentence.length();
        if (sentence.contains(" ")) {
            length = sentence.indexOf(" ");
        }

        if (sentence.contains("-")) {
            length = sentence.indexOf("-");
        }

        String orderName = sentence.substring(0, length);
        orderName = normalize(orderName);

        return orderName.trim();
    }

    /**
     * Delete all accents and transform the string to lowercase
     */
    public String normalize(String str)
    {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return str.toLowerCase();
    }
}
