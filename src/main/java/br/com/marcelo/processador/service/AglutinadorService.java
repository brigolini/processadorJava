package br.com.marcelo.processador.service;

import br.com.marcelo.processador.model.ItemVendaModel;
import br.com.marcelo.processador.model.VendaModel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class AglutinadorService {

    public static long getIdVendaMaisCara(List<VendaModel> vendas) {
        var vendasAgrupadas = vendas
                .stream().map(VendaModel::getItems)
                .flatMap(List::stream).collect(Collectors.toList()).stream()
                .collect(Collectors.groupingBy(ItemVendaModel::getIdVenda,
                        Collectors.summingDouble(value -> value.getPreco() * value.getQuantidade())));
        long idMaiorVenda = 0;
        double maiorVenda = 0;
        for (Map.Entry<Long, Double> item : vendasAgrupadas.entrySet()) {
            if (maiorVenda < item.getValue()) {
                idMaiorVenda = item.getKey();
                maiorVenda = item.getValue();
            }
        }
        return idMaiorVenda;
    }

    public static String getPiorVendedor(List<VendaModel> vendas) {
        var vendasAgrupadas = vendas
                .stream().collect(Collectors.groupingBy(VendaModel::getNomeVendedor));
        var piorVendedor = "";
        var piorTotalVenda = Double.MAX_VALUE;
        for (Map.Entry<String, List<VendaModel>> vendasVendedor : vendasAgrupadas.entrySet()) {
            double totalVendas = 0d;
            for (VendaModel vendaModel : vendasVendedor.getValue()) {
                totalVendas += vendaModel.getItems().stream().mapToDouble(value -> value.getQuantidade() * value.getPreco()).sum();
            }
            if (totalVendas < piorTotalVenda) {
                piorVendedor = vendasVendedor.getKey();
                piorTotalVenda = totalVendas;
            }
        }
        return piorVendedor;
    }

}
