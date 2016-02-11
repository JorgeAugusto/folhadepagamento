package br.com.rcaneppele.folhadepagamento.funcionario.validacao;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.rcaneppele.folhadepagamento.funcionario.Funcionario;
import br.com.rcaneppele.folhadepagamento.funcionario.FuncionarioRepository;
import br.com.rcaneppele.folhadepagamento.util.ValidacaoException;

@Named
@Dependent
public class ValidadorFuncionarioExistente implements ValidadorCadastroFuncionario {
	
	private final FuncionarioRepository repository;
	
	@Inject
	public ValidadorFuncionarioExistente(FuncionarioRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public void valida(Funcionario funcionario) throws ValidacaoException {
		List<Funcionario> encontrados = repository.buscaPorCPFOuMatricula(funcionario.getDadosPessoais().getCpf(), funcionario.getDadosProfissionais().getMatricula());
		
		//Se nao encontrou ninguem cadastrado com o CPF ou Matricula informadas
		if (encontrados.isEmpty()) {
			return;
		}
		
		//Caso tenha encontrado alguem, mas este alguem seja o proprio funcionario(isso acontece quando estamos alterando o funcionario)
		if (encontrados.size() == 1 && encontrados.contains(funcionario)) {
			return;
		}
		
		throw new ValidacaoException("Já existe outro Funcionário cadastrado com o CPF e/ou Matrícula informada!");
	}

}