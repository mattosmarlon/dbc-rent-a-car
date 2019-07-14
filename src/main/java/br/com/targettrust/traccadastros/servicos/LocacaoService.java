package br.com.targettrust.traccadastros.servicos;

import br.com.targettrust.traccadastros.converter.LocacaoConverter;
import br.com.targettrust.traccadastros.dto.LocacaoDto;
import br.com.targettrust.traccadastros.entidades.Locacao;
import br.com.targettrust.traccadastros.entidades.Veiculo;
import br.com.targettrust.traccadastros.exceptions.NegocioException;
import br.com.targettrust.traccadastros.repositorio.LocacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocacaoService {

    @Autowired
    private ModeloService modeloService;

    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private LocacaoRepository locacaoRepository;

    @Autowired
    private LocacaoConverter locacaoConverter;

    public Long locarVeiculo(LocacaoDto locacaoDto) {
        Veiculo veiculo = veiculoService.definirVeiculoPorModelo(locacaoDto.getIdModelo());
        if (veiculo == null) {
            throw new NegocioException("Nenhum veiculo encontrado para este modelo");
        }
        if (!modeloService.modeloDisponivel(locacaoDto.getIdModelo(), locacaoDto.getDataInicial(), locacaoDto.getDataFinal())) {
            throw new NegocioException("Modelo indisponivel para este periodo");
        }
        Locacao locacao = locacaoConverter.converter(locacaoDto);
        locacao.setVeiculo(veiculo);
        locacaoRepository.save(locacao);
        return locacao.getId();
    }
}