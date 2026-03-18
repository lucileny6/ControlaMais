package com.controla.backend.service;

import com.controla.backend.dto.DashboardTransactionDTO;
import com.controla.backend.entity.AcaoFinanceira;
import com.controla.backend.entity.Despesa;
import com.controla.backend.entity.Receita;
import com.controla.backend.repository.AcaoFinanceiraRepository;
import com.controla.backend.repository.DespesaRepository;
import com.controla.backend.repository.ReceitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class TransacoesService {
    private final ReceitaRepository receitaRepository;
    private final DespesaRepository despesaRepository;
    private final AcaoFinanceiraRepository acaoFinanceiraRepository;

    public TransacoesService(ReceitaRepository receitaRepository, DespesaRepository despesaRepository, AcaoFinanceiraRepository acaoFinanceiraRepository) {
        this.receitaRepository = receitaRepository;
        this.despesaRepository = despesaRepository;
        this.acaoFinanceiraRepository = acaoFinanceiraRepository;


    }
    public String getUsuarioLogado() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
    public List<DashboardTransactionDTO> listarTodasTransacoes() {

        String emailUsuario = getUsuarioLogado();

        List<DashboardTransactionDTO> lista = new ArrayList<>();

        List<Receita> receitas = receitaRepository
                .findByUserEmailOrderByDataDesc(emailUsuario);

        for (Receita r : receitas) {
            lista.add(new DashboardTransactionDTO(
                    r.getId(),
                    r.getDescricao(),
                    r.getCategoria(),
                    "income",
                    r.getValor(),
                    r.getData(),
                    r.getRecorrente()
            ));
        }

        List<Despesa> despesas = despesaRepository
                .findByUserEmailOrderByDataDesc(emailUsuario);
        for (Despesa d : despesas) {
            lista.add(new DashboardTransactionDTO(
                    d.getId(),
                    d.getDescricao(),
                    d.getCategoria(),
                    "expense",
                    d.getValor(),
                    d.getData(),
                    d.getRecorrente()
            ));

        }
        List<AcaoFinanceira> acoe = acaoFinanceiraRepository
                .findByUsuarioEmailOrderByDataDesc(emailUsuario);
        for (AcaoFinanceira a : acoe) {
            String tipo = a.getTipo().name().equals("RECEITA")
                    ? "icome"
                    : "expense";
            lista.add(new DashboardTransactionDTO(
                    a.getId(),
                    a.getDescricao(),
                    a.getCategoria(),
                    tipo,
                    a.getValor(),
                    a.getData(),
                    false

            ));
        }
        lista.sort((t1,t2) -> t1.getData().compareTo(t2.getData()));

    return lista;
    }

    public void deletarTransacao(Long id, String tipo) {

        switch (tipo.toLowerCase()) {

            case "income":
                receitaRepository.deleteById(id);
                break;

            case "expense":
                despesaRepository.deleteById(id);
                break;

            case "ia":
                acaoFinanceiraRepository.deleteById(id);
                break;

            default:
                throw new RuntimeException("Tipo inválido: " + tipo);
        }
    }
    public void editarTransacao(Long id, String tipo, DashboardTransactionDTO dto) {

        switch (tipo.toLowerCase()) {

            case "income":
                Receita receita = receitaRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Receita não encontrada"));

                receita.setDescricao(dto.getDescricao());
                receita.setCategoria(dto.getCategoria());
                receita.setValor(dto.getValor());
                receita.setData(dto.getData());

                receitaRepository.save(receita);
                break;

            case "expense":
                Despesa despesa = despesaRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));

                despesa.setDescricao(dto.getDescricao());
                despesa.setCategoria(dto.getCategoria());
                despesa.setValor(dto.getValor());
                despesa.setData(dto.getData());

                despesaRepository.save(despesa);
                break;

            case "ia":
                AcaoFinanceira acao = acaoFinanceiraRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Ação não encontrada"));

                acao.setDescricao(dto.getDescricao());
                acao.setCategoria(dto.getCategoria());
                acao.setValor(dto.getValor());
                acao.setData(dto.getData());

                acaoFinanceiraRepository.save(acao);
                break;

            default:
                throw new RuntimeException("Tipo inválido: " + tipo);
        }
    }

}

