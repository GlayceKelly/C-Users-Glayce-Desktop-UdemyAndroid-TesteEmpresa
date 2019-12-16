package com.example.glayce.testeempresa.Telas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glayce.testeempresa.Adapter.AdapterItens;
import com.example.glayce.testeempresa.Model.Paises;
import com.example.glayce.testeempresa.R;
import com.example.glayce.testeempresa.Util.Apoio;

import java.util.ArrayList;

public class FrmDetalhesPais extends AppCompatActivity implements View.OnClickListener
{

    // Controles da classe
    private TextView lblDetalhePais = null;
    private TextView lblDetalheCapital = null;
    private TextView lblDetalheContinente = null;
    private Button cmdAdicionarFavorito = null;
    private RecyclerView rcvItens = null;

    // Variaveis da classe
    private Paises paises = null;
    private ArrayList<Paises> arrPaises = null;
    private ArrayList<Paises> arrPaisesFavoritos = null;
    private ArrayList<String> arrAux = null;
    private int iItemSelecionado = -1;
    private String sPaisAux = "";
    private String sCapitalAux = "";
    private String sContinenteAux = "";
    private AdapterItens adapterItens = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.frm_detalhes_pais);

            // Chama o método que inicia os controles da classe
            iniciaControles();

            // Carrega os dados
            carregaDados();
        }
        catch (Exception err)
        {
            Toast.makeText(this, "Erro onCreate FrmDetalhePais: " + err.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Inicia os controles da classe
     * */
    private void iniciaControles() throws Exception
    {
        // Obtem as referencias do xml
        lblDetalhePais = (TextView) findViewById(R.id.lblDetalhePais);
        lblDetalheCapital = (TextView) findViewById(R.id.lblDetalheCapital);
        lblDetalheContinente = (TextView) findViewById(R.id.lblDetalheContinente);
        cmdAdicionarFavorito = (Button) findViewById(R.id.cmdAdicionarFavoritos);
        rcvItens = (RecyclerView) findViewById(R.id.rcvFavoritos);

        // Define os listeners
        cmdAdicionarFavorito.setOnClickListener(this);
    }

    /**
     * Carrega os dados da tela
     * */
    private void carregaDados() throws Exception
    {
        // Instancia a array
        arrPaisesFavoritos = new ArrayList<>();
        //arrAux = new ArrayList<>();

        // Obtem os dados da tela anterior
        arrPaises = (ArrayList) getIntent().getExtras().getSerializable(Apoio.KEY_ARRAY_PAISES);
        iItemSelecionado = getIntent().getExtras().getInt(Apoio.KEY_ITEM_SELECIONADO);

        // Preenche as labels com os detalhes do pais selecionado
        defineDetalhePaisSelecionado();

        arrAux = Apoio.lePrefsArray(this, Apoio.KEY_PAIS);

        carregaListaFavoritos();
    }

    @Override
    public void onClick(View view)
    {
        String sAux = "";

        try
        {
            // Se clicou em adicionar aos favoritos
            if( view == cmdAdicionarFavorito )
            {
                // Obtem o pais e adiciona na array auxiliar
                sAux = arrPaises.get(iItemSelecionado).sPais;
                arrAux.add(sAux);

                // Grava nas preferencias
                Apoio.gravaPrefsArray(this, arrAux, Apoio.KEY_PAIS);

                // Atualiza a lista
                adapterItens.notifyDataSetChanged();

                Toast.makeText(this, "País " + sPaisAux + " adicionado aos favoritos!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception err)
        {
            Toast.makeText(this, "Erro onClick FrmDetalhePais: " + err.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Define nas labels correspondentes o pais que foi selecionado e a respectiva capital e continente
     * */
    private void defineDetalhePaisSelecionado() throws Exception
    {
        // Define nas variaveis o item da array
        sPaisAux = arrPaises.get(iItemSelecionado).sPais;
        sCapitalAux = arrPaises.get(iItemSelecionado).sCapital;
        sContinenteAux = arrPaises.get(iItemSelecionado).sContinente;

        // Define nas labels
        lblDetalhePais.setText("País: " + sPaisAux);
        lblDetalheCapital.setText("Capital: " + sCapitalAux);
        lblDetalheContinente.setText("Continente: " + sContinenteAux);
    }

    /**
     * Carrega a lista de favoritos no recycler view
     * */
    private void carregaListaFavoritos() throws Exception
    {
        boolean bFavoritoAux = false;

        // Obtem a variavel booleana de favoritos
        bFavoritoAux = arrPaises.get(iItemSelecionado).bFavorito;

        // Se foi favoritado
        if( bFavoritoAux )
        {
            // Adiciona o item na array
           // arrAux.add(arrPaises.get(iItemSelecionado).sPais);
        }

        adapterItens = new AdapterItens(arrAux);

        // Configura recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rcvItens.setLayoutManager(layoutManager);
        rcvItens.setHasFixedSize(true);
        rcvItens.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        rcvItens.setAdapter(adapterItens);
    }
}
