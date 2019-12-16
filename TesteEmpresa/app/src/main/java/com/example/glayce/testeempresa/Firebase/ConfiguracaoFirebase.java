package com.example.glayce.testeempresa.Firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Glayce on 04/12/2019.
 */
public class ConfiguracaoFirebase
{
    // Variaveis da classe
    private static DatabaseReference refereciaFirebase;
    private static FirebaseAuth autenticacao;

    /**
     * Obtem a instancia do database
     * */
    public static DatabaseReference getFirebase()
    {
        // Se estiver nulo, obtem a referencia
        if( refereciaFirebase == null )
        {
          refereciaFirebase = FirebaseDatabase.getInstance().getReference();
        }

        return refereciaFirebase;
    }

    /**
     * Obtem a instancia do auth
     * */
    public static FirebaseAuth getFirebaseAuth()
    {
        // Se a autenticacao estiver nula, obtem a instancia
        if( autenticacao == null )
        {
            autenticacao = FirebaseAuth.getInstance();
        }

        return autenticacao;
    }
}
