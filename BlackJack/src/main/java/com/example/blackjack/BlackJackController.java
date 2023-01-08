package com.example.blackjack;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class BlackJackController {
    public HBox hboxCarte;
    public HBox hboxbanco;
    public HBox hboxmazzo;

    public Button btnPesca;
    public Button btnStop;
    public Button btnVal11;
    public Label TxtRisultato;
    public Button btnVal1;
    public VBox Campo;
    public Button btnRigioca;
    public Label ValAsso;

    private final int nCarte = 52;
    private final int PosizioneCartaBanco = 1;
    private int CartePescate ;

    private boolean assoBanco;
    private boolean assoGiocatore;

    private Mazzo mazzo;
    private Giocatore player;
    private Giocatore banco;
    private ImageView[] carte;

    @FXML
    void initialize() {

        CartePescate = 0;

        player = new Giocatore();
        banco = new Giocatore();

        assoBanco= false;
        assoGiocatore= false;
        btnRigioca.setVisible(false);
        btnVal1.setVisible(false);
        btnVal11.setVisible(false);
        btnPesca.setDisable(false);
        btnStop.setDisable(false);
        ValAsso.setText("");

        FileInputStream file = null;
        try {
            File f = Paths.get(BlackJackController.class.getResource("images/sfondo.jpg").toURI()).toFile();
            file = new FileInputStream(f);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Image sfondo = new Image(file);
        ImageView Sfondo_carte = new ImageView();
        Sfondo_carte.setImage(sfondo);
        ImageView Sfondo_carte2 = new ImageView();
        Sfondo_carte2.setImage(sfondo);

        carte = new ImageView[nCarte];

        for (int i = 0; i < nCarte; i++) {
            String Path_Carta = "images/" + i + ".jpg";
            try {
                File f = Paths.get(BlackJackController.class.getResource(Path_Carta).toURI()).toFile();
                file = new FileInputStream(f);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            Image Carta = new Image(file);
            carte[i] = new ImageView();
            carte[i].setImage(Carta);


        }

        mazzo = new Mazzo(carte);
        mazzo.Mescola_Mazzo();

        //Prime 2 carte giocatore
        for (int i = 0; i < 2; i++) {
            hboxCarte.getChildren().add(mazzo.Get_Figura(CartePescate));
            if (mazzo.get_Valore(CartePescate)==1 ) assoGiocatore= true;
            player.AggiungiPunti(mazzo.get_Valore(CartePescate));
            CartePescate++;
        }

        //prima carta banco
        hboxbanco.getChildren().add(mazzo.Get_Figura(CartePescate));
        if (mazzo.get_Valore(CartePescate)==1 ) assoBanco = true;
        banco.AggiungiPunti(mazzo.get_Valore(CartePescate));
        CartePescate++;

        //seconda carta banco coperta
        hboxbanco.getChildren().add(Sfondo_carte2);
        if (mazzo.get_Valore(CartePescate)==1 ) assoBanco = true;
        banco.AggiungiPunti(mazzo.get_Valore(CartePescate));
        CartePescate++;
        hboxmazzo.getChildren().add(Sfondo_carte);
        //Selezione valore asso
        if (assoGiocatore && player.getPunteggio()!=11){

            ValAsso.setText("Valore asso:");
            btnVal11.setVisible(true);
            btnVal1.setVisible(true);
            btnStop.setDisable(true);
            btnPesca.setDisable(true);
        }
        else if (assoGiocatore && player.getPunteggio()==11){
            btnPesca.setDisable(true);
            player.AggiungiPunti(10);
        }
        if (assoBanco){
            banco.AggiungiPunti(10);
        }
    }

      //Premendo il pulsante "stop" il turno del giocatore si conclude e inizia il turno del banco.

    public void onStopClick(ActionEvent actionEvent) {

        btnPesca.setDisable(true);
        btnStop.setDisable(true);

        hboxbanco.getChildren().remove(PosizioneCartaBanco);
        hboxbanco.getChildren().add(mazzo.Get_Figura(PosizioneCartaBanco+2));

        /*
        Finchè il valore di tutte le carte del banco è minore di 17 il banco continua a pescare.
        Se i punti totali del banco sono minori o uguali a 11 l'asso avrà come valore 11.
         */
        while (banco.getPunteggio()<17){
            hboxbanco.getChildren().add(mazzo.Get_Figura(CartePescate));
            banco.AggiungiPunti(mazzo.get_Valore(CartePescate));
            if(mazzo.get_Valore(CartePescate)==1 && banco.getPunteggio()<=11){
               banco.AggiungiPunti(10);
            }
            CartePescate++;

        }

        btnRigioca.setVisible(true);

        if (IsGameOver(player.getPunteggio() , banco.getPunteggio())){
            TxtRisultato.setText("Sconfitta!");
        }
        else if (IsWin(player.getPunteggio() , banco.getPunteggio() )){
            TxtRisultato.setText("Vittoria!");
        }
        else if (player.getPunteggio()==banco.getPunteggio()){
            TxtRisultato.setText("Pareggio!");
        }


    }
    //Cliccando il pulsante "Pesca" viene aggiunta una carta al giocatore
    public void onPescaClick(ActionEvent actionEvent) {
        hboxCarte.getChildren().add(mazzo.Get_Figura(CartePescate));
        player.AggiungiPunti(mazzo.get_Valore(CartePescate));
        //Selezione valore asso se il valore complessivo delle carte nun supera gli 11 punti
        if(mazzo.get_Valore(CartePescate)==1 && player.getPunteggio()<=11){
            ValAsso.setText("Valore asso:");
            btnVal11.setVisible(true);
            btnVal1.setVisible(true);
            btnStop.setDisable(true);
            btnPesca.setDisable(true);
        }
        CartePescate++;


        //Controllo per verificare che il giocatore non abbia superato i 21 punti
        if (IsGameOver(player.getPunteggio() , 0)){
            TxtRisultato.setText("sconfitta!");
            btnStop.setDisable(true);
            btnPesca.setDisable(true);
           btnRigioca.setVisible(true);

       }

    }

    /**
     *
     * @param puntiGiocatore punti totalizzati dal giocatore
     * @param puntiBanco punti totalizzati dal banco
     * @return true se il giocatoreha superato i 21 punti o e stato superato
     *         false se il giocator non ha sforato o non è stato superato
     *
     */
    public boolean IsGameOver(int puntiGiocatore, int puntiBanco) {
        if (puntiGiocatore > 21) return true;
        else if (puntiBanco > puntiGiocatore && puntiBanco <= 21) return true;
        else return false;

    }

    /**
     *
     * @param puntiGiocatore punti totalizzati dal giocatore
     * @param puntiBanco punti totalizzati dal banco
     * @return true se il banco ha superato i 21 punti o e stato superato
     *         false se il banco non ha superato i 21 punti e non è stato superato
     */
    public boolean IsWin(int puntiGiocatore, int puntiBanco) {
        if (puntiBanco > 21) return true;
        else if (puntiGiocatore > puntiBanco) return true;
        else return false;
    }
    //Cliccando "1" il punteggio non viene modificato
    public void onVal1Click(ActionEvent actionEvent) {
        ValAsso.setText("");
        btnVal11.setVisible(false);
        btnVal1.setVisible(false);
        btnStop.setDisable(false);
        btnPesca.setDisable(false);
    }
    //Cliccando "11" vengono aggiunti 10 punti al giocatore
    public void OnVal11Click(ActionEvent actionEvent) {
        ValAsso.setText("");
        player.AggiungiPunti(10);
        btnVal11.setVisible(false);
        btnVal1.setVisible(false);
        btnStop.setDisable(false);
        if(player.getPunteggio()!=21){
            btnPesca.setDisable(false);
        }

    }

    //Cliccando il pulsante "Rigioca" vengono resettati tutti i valori e contenitori
    public void OnRigiocaClick(ActionEvent actionEvent) {
        btnRigioca.setVisible(false);
        TxtRisultato.setText("");
        hboxCarte.getChildren().clear();
        hboxbanco.getChildren().clear();
        hboxmazzo.getChildren().clear();


        initialize();
    }

}