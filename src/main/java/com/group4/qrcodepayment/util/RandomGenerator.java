package com.group4.qrcodepayment.util;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Random;

@Component
public class RandomGenerator {
    private static final int LENGTH =  15;

    public String  generateRandom(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        Random rand = new Random();
        char[] letter = new char[LENGTH];

        for ( int i =0; i<LENGTH; i++){
            letter[i] = characters.charAt(rand.nextInt(characters.length()-1));
        }
        String finalResult = Arrays.toString(letter);
        return finalResult.replaceAll("\\[","")
                .replaceAll("\\]","")
                .replaceAll(" ","")
                .replaceAll(",","")
                .toUpperCase();
    }
    }



