package com.example.glayce.testeempresa.Telas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.glayce.testeempresa.Firebase.ConfiguracaoFirebase;
import com.example.glayce.testeempresa.Model.Usuario;
import com.example.glayce.testeempresa.R;
import com.example.glayce.testeempresa.Util.Apoio;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FrmActivityMain extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener
{
    // Controles da classe
    private TextInputLayout inputEmail = null;
    private TextInputLayout inputSenha = null;
    private EditText txtEmail = null;
    private EditText txtSenha = null;
    private Button cmdEntrar = null;
    private Button cmdCadastroUsuario = null;

    // Variaveis da classe
    private AlertDialog dialogErroEmail = null;
    private FirebaseAuth autenticacao = null;
    private Usuario usuario = null;
    private String sEmail = "";
    private String sSenha = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.frm_activity_main);

            // Chama os métodos inicia controle
            iniciaControles();
            carregaDados();
        }
        catch (Exception err)
        {
            // Apresenta a mensagem de erro
            Toast.makeText(this, "Erro onCreate FrmMainActivity: " + err.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Inicia os controles da tela
     * */
    private void iniciaControles() throws Exception
    {
        // Obtendo as referencias do xml
        inputEmail = (TextInputLayout) findViewById(R.id.inputLogin);
        inputSenha = (TextInputLayout) findViewById(R.id.inputSenha);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        cmdEntrar = (Button) findViewById(R.id.cmdEntrar);
        cmdCadastroUsuario = (Button) findViewById(R.id.cmdCadastrarUsuario);

        // Definindo o listener
        cmdEntrar.setOnClickListener(this);
        cmdCadastroUsuario.setOnClickListener(this);
    }

    /**
     * Carrega os dados da tela
     * */
    private void carregaDados() throws Exception
    {
        // Se o usuario estiver logado
        if( usuarioLogado() )
        {
            // Chama a tela de adicionar pais
            abreTelaAdicionarPais();
        }
        else
        {
            // Se os campos forem validados
            if( validaCampos() )
            {
                usuario = new Usuario();

                // Define o email e a senha no objeto
                usuario.sEmail = sEmail;
                usuario.sSenha = sSenha;

                // Chama o método que valida o login no firebase
                validarLogin();
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        try
        {
            // Se clicou no botão entrar
            if( view == cmdEntrar )
            {
                // Se os campos forem validados
                if( validaCampos() )
                {
                    usuario = new Usuario();

                    // Define o email e a senha no objeto
                    usuario.sEmail = sEmail;
                    usuario.sSenha = sSenha;

                    // Chama o método que valida o login no firebase
                    validarLogin();
                }
            }
            // Se clicou no cadastrar novo usuario
            else if( view == cmdCadastroUsuario )
            {
                // Chama o método que abre a tela de cadastro do usuario
                abreTelaCadastroUsuario();
            }
        }
        catch (Exception err)
        {
            // Apresenta a mensagem de erro
            Toast.makeText(this, "Erro onClick FrmMainActivity: " + err.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Valida os campos da tela
     * */
    private boolean validaCampos() throws Exception
    {
        // Define nas variaveis o que foi digitado
        sEmail = txtEmail.getText().toString().trim();
        sSenha = txtSenha.getText().toString().trim();

        // Se o campo email estiver vazio
        if( sEmail.equals("") )
        {
            dialogErroEmail = Apoio.abreDialog(this, "Atenção" ,"Digite o e-mail!", "OK", "");
            dialogErroEmail.show();
            return false;
        }

        // Se o campo senha estiver vazio
        if( sSenha.equals("") )
        {
            dialogErroEmail = Apoio.abreDialog(this, "Atenção" ,"Digite a senha!", "OK", "");
            dialogErroEmail.show();
            return false;
        }

        return true;
    }

    // Chama a tela
    private void abreTelaAdicionarPais() throws Exception
    {
        Intent intent = null;

        intent = new Intent(this, FrmAdicionarPais.class);
        startActivity(intent);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int iEscolha)
    {
        try
        {
            if( dialogInterface == dialogErroEmail )
            {
            }
        }
        catch (Exception err)
        {
            // Apresenta a mensagem de erro
            Toast.makeText(this, "Erro onClickDialogInterface FrmMainActivity: " + err.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Valida o login utilizando o firebase
     * */
    private void validarLogin() throws Exception
    {
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();

        autenticacao.signInWithEmailAndPassword(usuario.sEmail.toString(), usuario.sSenha.toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                try
                {
                    // Se obteve sucesso
                    if( task.isSuccessful() )
                    {
                        // Salva o email nas preferencias
                        Apoio.gravaPrefsValorString(FrmActivityMain.this, Apoio.PREFS_EMAIL, sEmail);

                        // Chama a tela de adicionar pais
                        abreTelaAdicionarPais();

                        // Limpa os campos de texto
                        limpaCampos();

                        Toast.makeText(FrmActivityMain.this, "Login efetuado com sucesso!!!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // Limpa os campos de texto
                        limpaCampos();

                        Toast.makeText(FrmActivityMain.this, "Usuário ou senha inválidos! Tente novamente!" , Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception err)
                {
                    // Apresenta a mensagem de erro
                    Toast.makeText(FrmActivityMain.this, "Erro onComplete FrmMainActivity: " + err.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public Boolean usuarioLogado() throws Exception
    {
        // Obter a situação do usuario atual
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Se for diferente de nulo esta logado
        if( user != null )
        {
            return true;
        }
        // Se for igual a null não esta logado
        else
        {
            return false;
        }
    }

    /**
     * Limpa os campos de texto
     * */
    private void limpaCampos() throws Exception
    {
        txtEmail.setText("");
        txtSenha.setText("");
    }

    // Chama a tela
    private void abreTelaCadastroUsuario() throws Exception
    {
        Intent intent = null;

        intent = new Intent(this, FrmCadastroUsuario.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        try
        {
            // Se o usuario nao estiver logado, apresenta mensagem
            if( !usuarioLogado() )
            {
                Toast.makeText(this, "Realize o login!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception err)
        {
            // Apresenta a mensagem de erro
            Toast.makeText(FrmActivityMain.this, "Erro onBackPressed FrmMainActivity: " + err.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
