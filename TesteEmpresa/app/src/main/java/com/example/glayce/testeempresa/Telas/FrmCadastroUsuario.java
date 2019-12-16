package com.example.glayce.testeempresa.Telas;

import android.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FrmCadastroUsuario extends AppCompatActivity implements View.OnClickListener
{
    // Controles da classe
    private EditText txtEmail = null;
    private EditText txtSenha = null;
    private EditText txtRepitaSenha = null;
    private Button cmdCadastrar = null;
    private Button cmdCancelar = null;
    private FirebaseAuth autenticacao;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Usuario usuario = null;

    // Variaveis da classe

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.frm_cadastro_usuario);

            // Chama os métodos inicia controle
            iniciaControles();
            carregaDados();
        }
        catch (Exception err)
        {
            // Apresenta a mensagem de erro
            Toast.makeText(this, "Erro onCreate FrmCadastroUsuario: " + err.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Inicia os controles da tela
     * */
    private void iniciaControles() throws Exception
    {
        // Obtem as referencias do xml
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        txtRepitaSenha = (EditText) findViewById(R.id.txtSenhaRepitir);
        cmdCadastrar = (Button) findViewById(R.id.cmdCadastrar);
        cmdCancelar = (Button) findViewById(R.id.cmdCancelar);

        // Define os listeners
        cmdCadastrar.setOnClickListener(this);
        cmdCancelar.setOnClickListener(this);
    }

    /**
     * Carrega os dados da tela
     * */
    private void carregaDados() throws Exception
    {
    }

    @Override
    public void onClick(View view)
    {
        String sSenha = "";
        String sSenhaRepetir = "";

        try
        {
            // Define nas variaveis os valores digitados
            sSenha = txtSenha.getText().toString();
            sSenhaRepetir = txtRepitaSenha.getText().toString();

            // Se clicou no botão cadastrar
            if( view == cmdCadastrar )
            {
                // Se as senhas forem igual
                if( sSenha.equals(sSenhaRepetir) )
                {
                    usuario = new Usuario();

                    usuario.sEmail = txtEmail.getText().toString();
                    usuario.sSenha = txtSenha.getText().toString();

                    // Chama o método para cadastrar o usuario
                    cadastrarUsuario();
                }
                else
                {
                    Toast.makeText(this, "As senhas precisam ser iguais!", Toast.LENGTH_SHORT).show();
                }
            }
            // Se clicou no botão cancelar
            else if( view == cmdCancelar )
            {
                finish();
            }
        }
        catch (Exception err)
        {
            // Apresenta a mensagem de erro
            Toast.makeText(this, "Erro onClick FrmCadastroUsuario: " + err.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Cadastra usuario no firebase
     * */
    private void cadastrarUsuario() throws Exception
    {
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();

        autenticacao.createUserWithEmailAndPassword
        (
            usuario.sEmail,
            usuario.sSenha
        ).addOnCompleteListener(FrmCadastroUsuario.this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                String sErroExcecao = "";

                // Se conseguiu criar o usuario com sucesso
                if( task.isSuccessful() )
                {
                    // Insere o usuario no firebase
                    insereUsuario(usuario);

                    // Limpa os campos de texto
                    limpaCampos();
                }
                else
                {
                    try
                    {
                        throw task.getException();
                    }
                    catch (FirebaseAuthWeakPasswordException err)
                    {
                        sErroExcecao = "A senha precisa ter no mínimo 6 caracteres.";
                    }
                    catch (FirebaseAuthInvalidCredentialsException err)
                    {
                        sErroExcecao = "O e-mail digita é inválido, digite outro e-amil.";
                    }
                    catch (FirebaseAuthUserCollisionException err)
                    {
                        sErroExcecao = "Este e-mail já está cadastrado.";
                    }
                    catch (Exception e)
                    {
                        sErroExcecao = "Erro ao efetuar o cadastro.";
                        e.printStackTrace();
                    }

                    Toast.makeText(FrmCadastroUsuario.this, "Erro: " + sErroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * insere o usuario no banco e na autenticação do firebase
     * */
    private boolean insereUsuario(Usuario usuario)
    {
        try
        {
            // Obtem a referencia do database e insere o usuario
            reference = ConfiguracaoFirebase.getFirebase().child("usuarios");
            reference.push().setValue(usuario);

            // Apresenta a mensagem de sucesso
            Toast.makeText(this, "Usuário cadastrado com sucesso!" , Toast.LENGTH_LONG).show();

            finish();

            return true;
        }
        catch (Exception err)
        {
            Toast.makeText(this, "Erro ao gravar usuário. " , Toast.LENGTH_LONG).show();
            err.printStackTrace();

            return false;
        }
    }

    /**
     * Limpa os campos de texto
     * */
    private void limpaCampos()
    {
        txtEmail.setText("");
        txtSenha.setText("");
        txtRepitaSenha.setText("");
    }
}
