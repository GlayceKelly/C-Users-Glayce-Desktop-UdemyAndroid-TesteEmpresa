package com.example.glayce.testeempresa.Telas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glayce.testeempresa.Model.Paises;
import com.example.glayce.testeempresa.R;
import com.example.glayce.testeempresa.Util.Apoio;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FrmAdicionarPais extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener
{
    // Controles da classe
    private Spinner spinner = null;
    private TextView lblPaisEscolhido = null;
    private Button cmdSelecionar = null;

    // Variaveis da classe
    private ArrayList<String> arrItens = null;
    private ArrayAdapter<String> adapter = null;
    private Paises paises = null;
    private ArrayList<Paises> arrPaises = null;
    private int iPosicaoItemSelecioando = -1;
    private AlertDialog dialogErro = null;
    private AlertDialog dialogSair = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.frm_adicionar_pais);

            // Chama o método que inicia os controles da classe
            iniciaControles();
            carregaDados();
        }
        catch (Exception err)
        {
            Toast.makeText(this, "Erro onCreate FrmAdicionarPais: " + err.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Inicia os controles da classe
     * */
    private void iniciaControles() throws Exception
    {
        // Obtendo as referencias do xml
        spinner = (Spinner) findViewById(R.id.spinnerPaises);
        lblPaisEscolhido = (TextView) findViewById(R.id.lblPaisEscolhido);
        cmdSelecionar = (Button) findViewById(R.id.cmdSelecionar);

        // Define os listeners
        cmdSelecionar.setOnClickListener(this);
    }

    /**
     * Carrega os dados da tela
     * */
    private void carregaDados() throws Exception
    {
        // Chama o método que configura e preenche o spinner
        configuraSpinner();

        // Obtem o item e a posicao do item selecionado
        cliqueItemSpinner();
    }

    /**
     * Obtem o item e a posição do item selecionado do spinner
     * */
    private void cliqueItemSpinner() throws Exception
    {
        final String[] sItemSelecionado = {""};

        // Clique do item selecionado do spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int iPosicao, long l)
            {
                // Obtem a posicao do item
                iPosicaoItemSelecioando = iPosicao;

                // Obtem o item selecionado e define na label
                sItemSelecionado[0] = adapterView.getItemAtPosition(iPosicao).toString();
                lblPaisEscolhido.setText("País selecionado: " + sItemSelecionado[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });
    }

    /**
     * Configura e adiciona itens no spinner
     * */
    private void configuraSpinner() throws Exception
    {
        // Instancia a array list
        arrItens = new ArrayList<>();

        // Chama o método que preenche a array com os paises
        preenchePaises();

        // Loop pelo array e preenchendo os itens
        for( Paises paises : arrPaises )
        {
            arrItens.add(paises.sPais);
        }

        // Inicia o adapter
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrItens);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Define o adapter no spinner
        spinner.setAdapter(adapter);
    }

    /**
     * Preenche a array de paises com a capital e o continente correspondente
     * */
    private void preenchePaises() throws Exception
    {
        // Instancia da array
        arrPaises = new ArrayList<>();

        // Preenche a array list
        arrPaises.add(new Paises("", "", ""));
        arrPaises.add(new Paises("Brasil", "Brasilia", "América"));
        arrPaises.add(new Paises("Chile", "Santiago", "América"));
        arrPaises.add(new Paises("China", "Pequim", "Ásia"));
        arrPaises.add(new Paises("Espanha", "Madrid", "Europa"));
        arrPaises.add(new Paises("Grécia", "Atenas", "Europa"));
        arrPaises.add(new Paises("Paraguai", "Assunção", "América"));
        arrPaises.add(new Paises("Portugal", "Lisboa", "Europa"));
    }

    @Override
    public void onClick(View view)
    {
        try
        {
            // Se o clique for no botao
            if( view == cmdSelecionar )
            {
                // se foi selecionado algum pais da lista
                if( iPosicaoItemSelecioando != 0 )
                {
                    // Chama o método que abre a tela de detalhes
                    abreTelaDetalhePais();
                }
                else
                {
                    dialogErro = Apoio.abreDialog(this, "Atenção" ,"Selecione um país da lista!", "OK", "");
                    dialogErro.show();
                }
            }
        }
        catch (Exception err)
        {
            Toast.makeText(this, "Erro onClick FrmAdicionarPais: " + err.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Abre a tela de detalhes dos paises
     * */
    private void abreTelaDetalhePais() throws Exception
    {
        Intent intent = null;

        intent = new Intent(this, FrmDetalhesPais.class);
        intent.putExtra(Apoio.KEY_ARRAY_PAISES, arrPaises);
        intent.putExtra(Apoio.KEY_ITEM_SELECIONADO, iPosicaoItemSelecioando);
        startActivity(intent);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int iEscolha)
    {
        try
        {
            // Se for retorno do dialog de sair
            if( dialogInterface == dialogSair )
            {
                // Se clicou em sim
                if( iEscolha == DialogInterface.BUTTON_POSITIVE )
                {
                    // Desloga o usuario
                    FirebaseAuth.getInstance().signOut();

                    // Retorna para a tela inicial
                    retornaTelaMain();
                }
            }
        }
        catch (Exception err)
        {
            Toast.makeText(this, "Erro onClickDialogInterface FrmAdicionarPais: " + err.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed()
    {
        try
        {
            dialogSair = Apoio.abreDialog(this, "Atenção" ,"Deseja realmente sair da aplicação?", "Sim", "Não");
            dialogSair.show();
        }
        catch (Exception err)
        {
            Toast.makeText(this, "Erro onBackPressed FrmAdicionarPais: " + err.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Retorna para a tela inicial
     * */
    private void retornaTelaMain() throws Exception
    {
        Intent intent = null;

        intent = new Intent(this, FrmActivityMain.class);
        startActivity(intent);
    }
}
