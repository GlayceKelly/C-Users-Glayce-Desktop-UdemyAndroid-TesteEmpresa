package com.example.glayce.testeempresa.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by Glayce on 04/12/2019.
 */

public class Apoio
{
    // Constantes da classe
    public static String KEY_PAIS = "KEY_PAIS";
    public static String KEY_ARRAY_PAISES = "KEY_ARRAY_PAISES";
    public static String KEY_ITEM_SELECIONADO = "KEY_ITEM_SELECIONADO";

    // Constantes das preferencias
    public static String PREFS_EMAIL = "PREFS_EMAIL";

    // Método para abrir dialog
    public static AlertDialog abreDialog(Context context, String sTitulo, String sMensagem, String sBotaoPositivo, String sBotaoNegativo) throws Exception
    {
        AlertDialog.Builder dialog = null;

        dialog = new AlertDialog.Builder(context);
        dialog.setTitle(sTitulo);
        dialog.setMessage(sMensagem);
        dialog.setPositiveButton(sBotaoPositivo, (DialogInterface.OnClickListener) context);
        dialog.setNegativeButton(sBotaoNegativo, (DialogInterface.OnClickListener) context);

        return dialog.create();
    }

    /**
     * Grava strings na preferencia
     * */
    public static void gravaPrefsValorString(Context context, String sChave, String sValor) throws Exception
    {
        SharedPreferences prefs = null;
        SharedPreferences.Editor editor = null;

        // Recupera a instancia
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();

        // Define a chave e o valor
        editor.putString(sChave, sValor);

        // Persiste os dados
        editor.commit();
    }

    /**
     * Realiza leitura do valor salvo nas preferencias
     * */
    public static String lePrefsValorString(Context context, String sChave, String sValorDefault) throws Exception
    {
        String sItemRecuperado = "";
        SharedPreferences prefs = null;

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        sItemRecuperado = prefs.getString(sChave, sValorDefault);

        return sItemRecuperado;
    }

    /**
     * Deleta das preferencias conforme a chave
     * */
    public static void deletaPrefs(Context context, String sChaveDeletar) throws Exception
    {
        SharedPreferences prefs = null;
        SharedPreferences.Editor editor = null;

        // Recupera a instancia
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();

        // Se contem a chave nas preferencias
        if( prefs.contains(sChaveDeletar) )
        {
            // Remove das preferencias
            editor.remove(sChaveDeletar).commit();
        }
    }

    public static void gravaPrefsArray(Context context, ArrayList<String> arrString, String sChave) throws Exception
    {
        SharedPreferences prefs = null;
        SharedPreferences.Editor editor = null;

        // Recupera a instancia
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();

        editor.putInt(sChave + "_size", arrString.size());

        // Loop pela array com as strings
        for( int iCont = 0; iCont < arrString.size(); iCont++ )
        {
            editor.putString(sChave + "_" + iCont, arrString.get(iCont));
        }

        // Persiste os dados
        editor.commit();
    }

    public static ArrayList<String> lePrefsArray(Context context, String sChave)
    {
        SharedPreferences prefs = null;
        int size = 0;
        ArrayList<String> arrAux = new ArrayList<>();

        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        size = prefs.getInt(sChave + "_size", 0);

        for(int iCont = 0; iCont < size ; iCont++)
        {
            arrAux.add(prefs.getString(sChave + "_" + iCont, null));
        }

        return arrAux;
    }

}
