package br.senai.sp.emendaai.api;

import java.util.List;

import br.senai.sp.emendaai.model.Cep;
import br.senai.sp.emendaai.model.Feriado;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BrasilApiService {

    @GET("feriados/v1/{ano}")
    Call<List<Feriado>> buscaFeriados (@Path("ano") int ano);

    @GET("cep/v1/{cep}")
    Call<Cep> buscaEndereco (@Path("cep") String cep);
}
