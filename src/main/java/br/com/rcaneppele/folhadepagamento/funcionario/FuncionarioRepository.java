package br.com.rcaneppele.folhadepagamento.funcionario;

import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Named
@RequestScoped
public class FuncionarioRepository {

	private EntityManager em;
	
	/**
	 * CDI Eyes Only!
	 */
	protected FuncionarioRepository() {
	}

	@Inject
	public FuncionarioRepository(EntityManager em) {
		this.em = em;
	}
	
	public Funcionario buscaPorId(Long id) {
		return em.find(Funcionario.class, id);
	}
	
	public Funcionario carregaFuncionarioComReajustes(Long id) {
		String jpql = "SELECT f FROM " +Funcionario.class.getName() + " f LEFT JOIN FETCH f.reajustes WHERE f.id = :id";
		return em.createQuery(jpql, Funcionario.class)
				.setParameter("id", id)
				.getSingleResult();
	}
	
	public List<Funcionario> buscaTodosOrdenadosPeloNome() {
		String jpql = "SELECT f FROM " +Funcionario.class.getName() + " f ORDER BY f.dadosPessoais.nome";
		return em.createQuery(jpql, Funcionario.class).getResultList();
	}
	
	public List<Funcionario> buscaPorCPFOuMatricula(String cpf, String matricula) {
		String jpql = "SELECT f FROM " +Funcionario.class.getName() + " f WHERE f.dadosPessoais.cpf = :cpf OR f.dadosProfissionais.matricula = :matricula";
		return em.createQuery(jpql, Funcionario.class)
				.setParameter("cpf", cpf)
				.setParameter("matricula", matricula)
				.getResultList();
	}
	
	public BigDecimal buscaSalarioAtualDoFuncionario(Funcionario funcionario) {
		String jpql = "SELECT f.dadosProfissionais.salario FROM " +Funcionario.class.getName() + " f WHERE f = :funcionario";
		return em.createQuery(jpql, BigDecimal.class)
				.setParameter("funcionario", funcionario)
				.getSingleResult();
	}
	
	public void cadastra(Funcionario novo) {
		this.em.joinTransaction();
		this.em.persist(novo);
	}
	
	public void atualiza(Funcionario existente) {
		this.em.joinTransaction();
		this.em.merge(existente);
	}
	
	public void remove(Funcionario removido) {
		this.em.joinTransaction();
		removido = buscaPorId(removido.getId());
		this.em.remove(removido);
	}

}
