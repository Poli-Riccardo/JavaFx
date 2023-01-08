package com.example.blackjack;

import javafx.scene.image.ImageView;

public class Mazzo {
    private Carta[] carta = new Carta[52];
    //Nel costruttore vengono assegnati i valori di ogni carta
    public Mazzo (ImageView[] Figure ){
        int valore=0;
        int flag =0;
        for (int i = 0; i <52; i++) {
          if (valore<10){
              valore++;
          }
          else if (valore==10){
              flag++;
              if (flag==4)
              {
                  valore=1;
                  flag=0;
              }
          }
            carta[i]= new Carta(Figure[i] , valore);
        }
    }
    //vengono scelti casualmente 2 indici e le carte in quella posizione nel mazzo vengo scambiate.
    public void Mescola_Mazzo(){
        int n_mescolate=52;
        for (int i = 0; i < n_mescolate; i++) {
            int CartaMescolata_1 =(int)(Math.random()*52);
            int CartaMescolata_2 =(int)(Math.random()*52);
            Carta temp = carta[CartaMescolata_1];
            carta[CartaMescolata_1]=carta[CartaMescolata_2];
            carta[CartaMescolata_2]=temp;
        }
    }
    public int get_Valore(int i) {
        return carta[i].getValore();
    }

    public ImageView Get_Figura(int i){
        return carta[i].getFigura();
    }


}
