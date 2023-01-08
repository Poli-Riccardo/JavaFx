package com.example.blackjack;

import javafx.scene.image.ImageView;

public class Carta {
    int valore;
    ImageView figura;

    public Carta(ImageView carta , int valore){
        this.figura =carta;
        this.valore = valore;
    }

    public int getValore() {
        return valore;
    }

    public ImageView getFigura() {
        return figura;
    }
}
