package com.example.blackjack;

public class Giocatore {
    private int punteggio;

    public Giocatore (){
        punteggio = 0;
    }
    public void AggiungiPunti (int punti){
        punteggio+=punti;
    }
    public int getPunteggio() {
        return punteggio;
    }

}
