package br.senai.sp.emendaai;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import br.senai.sp.emendaai.api.BrasilApiService;
import br.senai.sp.emendaai.model.Cep;
import br.senai.sp.emendaai.model.Feriado;
import br.senai.sp.emendaai.util.Datas;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DetalheFeriadoActivity extends AppCompatActivity {


    private EditText campoCep;
    private ProgressBar progressoCep;
    private TextView txtResultadoCep;
    private Button btnCompartilhar;
    private Feriado feriado;
    private TextView txtMensagemErro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_feriado);

        TextView txtNome = findViewById(R.id.txtNomeFeriado);
        TextView txtData = findViewById(R.id.txtDataFeriado);
        TextView txtContagem = findViewById(R.id.txtContagemFeriado);
        TextView txtEmenda = findViewById(R.id.txtAvisoEmenda);

        campoCep = findViewById(R.id.campoCep);
        progressoCep = findViewById(R.id.progressoCep);
        txtResultadoCep = findViewById(R.id.txtResultadoCep);
        btnCompartilhar = findViewById(R.id.btnCompartilhar);
        btnCompartilhar.setOnClickListener(view -> compartilhar());

        Button btnBuscarCep = findViewById(R.id.btnBuscarCep);
        btnBuscarCep.setOnClickListener(v -> {
            String cep = campoCep.getText().toString();
            if (!cep.isEmpty()) {
                carregarEndereco(cep);
            }
        });

        feriado = (Feriado)getIntent().getSerializableExtra("feriado");
        if (feriado == null){
            finish();
            return;
        }

        txtNome.setText(feriado.getNome());
        txtData.setText(Datas.porExtenso(feriado.getData()) + " - " + Datas.diaDaSemana(feriado.getData()));
        txtContagem.setText(Datas.contagem(feriado.getData()));

        if (Datas.ehEmenda(feriado.getData())) {
            txtEmenda.setVisibility(View.VISIBLE);
        }

    }

    private void compartilhar() {
        String texto = "Feriado: " + feriado.getNome() + "\n" +
                "Data: " + Datas.porExtenso(feriado.getData()) + " (" + Datas.diaDaSemana(feriado.getData()) + ")\n" +
                "Encontro em: " + txtResultadoCep.getText().toString();

        Intent rota = new Intent(Intent.ACTION_SEND);
        rota.setType("text/plain");
        rota.putExtra(Intent.EXTRA_TEXT, texto);
        startActivity(Intent.createChooser(rota, "Compartilhar"));
    }

    private void mostrarEndereco(String endereco) {
        txtResultadoCep.setText(endereco);
        txtResultadoCep.setVisibility(View.VISIBLE);
        btnCompartilhar.setEnabled(true);
        progressoCep.setVisibility(View.GONE);
    }

    private void carregarEndereco(String cep) {
        progressoCep.setVisibility(View.VISIBLE);
        txtResultadoCep.setVisibility(View.GONE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://brasilapi.com.br/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BrasilApiService servico = retrofit.create(BrasilApiService.class);
        Call<Cep> chamadaCep = servico.buscaEndereco(cep);
        chamadaCep.enqueue((new Callback<Cep>() {
            @Override
            public void onResponse(@NonNull Call<Cep> call, @NonNull Response<Cep> response) {
                progressoCep.setVisibility(View.GONE);
                if(!response.isSuccessful()) {
                    mostrarErro("CEP não encontrado");
                    return;
                }
                Cep resultado = response.body();
                if (resultado == null) {
                    mostrarErro("Erro na resposta da API");
                    return;
                }
                String endereco = resultado.getStreet() + ", " + resultado.getNeighborhood() + " - " + resultado.getCity() + "/" + resultado.getState();
                mostrarEndereco(endereco);
            }
            @Override
            public void onFailure(@NonNull Call<Cep> call, @NonNull Throwable t) {
                progressoCep.setVisibility(View.GONE);
                mostrarErro("Erro de conexão");
            }
        }));

    }

    private void mostrarErro(String mensagem) {
        txtResultadoCep.setText(mensagem);
        txtResultadoCep.setVisibility(View.VISIBLE);
        btnCompartilhar.setEnabled(false);
    }

}
