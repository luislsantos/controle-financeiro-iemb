package com.example.testefrontiemb.service;

import com.example.testefrontiemb.models.RegistroContabil;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalculadoraService {
    private static final double PERCENTUAL_DO_SORTEIO_PARA_CUSTEIO = 0.3;
    private static final double PERCENTUAL_DO_SORTEIO_PARA_INVESTIMENTO = 0.7;

    public static double calculaLimiteCusteio(ArrayList<RegistroContabil> registros) {
        double limiteCusteio = 0;
        for(RegistroContabil registro:registros) {
            if(registro.getTipo().equals("Receita")) {
                if(registro.getOrigemOuDestinacao().equals("Sorteio")) {
                    limiteCusteio += registro.getValor() * PERCENTUAL_DO_SORTEIO_PARA_CUSTEIO;
                } else if (registro.getOrigemOuDestinacao().equals("Rateio")) {
                    limiteCusteio += registro.getValor();
                }
            }
            if(registro.getTipo().equals("Despesa")) {
                if(registro.getOrigemOuDestinacao().equals("Custeio")) {
                    limiteCusteio -= registro.getValor();
                } else if (registro.getOrigemOuDestinacao().equals("Investimento")) {
                    limiteCusteio -= registro.getValor();
                }
            }
        }
        return limiteCusteio;
    }

    public static double calculaLimiteInvestimento(ArrayList<RegistroContabil> registros) {
        double limiteInvestimento = 0;
        for(RegistroContabil registro:registros) {
            if(registro.getTipo().equals("Receita")) {
                limiteInvestimento += registro.getValor();
            }
            if(registro.getTipo().equals("Despesa")) {
                if(registro.getOrigemOuDestinacao().equals("Custeio")) {
                    limiteInvestimento -= registro.getValor();
                }
            } else if (registro.getOrigemOuDestinacao().equals("Investimento")) {
                limiteInvestimento -= registro.getValor();
            }
        }
        return limiteInvestimento;
    }

    public static double calculaValorPrestacaoContas(ArrayList<RegistroContabil> registros) {
        double valorTotalPrestacao = 0;
        LocalDate dataDeHoje = LocalDate.now();
        for(RegistroContabil registro:registros) {
            if(registro.getTipo().equals("Receita")) {
                valorTotalPrestacao += registro.getValor();
            }
            // TODO Tenho de saber qual a regra que eles utilizam para saber quando devem prestar contas de qual valor
            //  if(LocalDate.parse(registro.getData()).
        }
        return valorTotalPrestacao; //Provisório, só para não ficar apontado erro
    }
}
