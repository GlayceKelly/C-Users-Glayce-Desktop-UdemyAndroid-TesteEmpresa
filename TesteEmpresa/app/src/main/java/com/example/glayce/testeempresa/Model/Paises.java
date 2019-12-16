package com.example.glayce.testeempresa.Model;

import java.io.Serializable;

/**
 * Created by Glayce on 04/12/2019.
 */

public class Paises implements Serializable
{
    // Variaveis da classe
    public String sPais = "";
    public String sCapital = "";
    public String sContinente = "";
    public boolean bFavorito = false;

    // Construtor da classe
    public Paises(String sPaisParam, String sCapitalParam, String sContinenteParam)
    {
        this.sPais = sPaisParam;
        this.sCapital = sCapitalParam;
        this.sContinente = sContinenteParam;
    }
}
