package com.money.repository.lancamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.money.model.Lancamento;
import com.money.model.TipoLancamento;
import com.money.repository.filter.LancamentoFilter;
import com.money.repository.projection.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Lancamento.class);
		
		Criterion[] predicates = criarRestricoes(lancamentoFilter);
		
		for (Criterion c : predicates) {
			criteria.add(c);
		}
		
		adicionarRestricoesDePaginacao(criteria, pageable);
		
		return new PageImpl<>(criteria.list(), pageable, total(lancamentoFilter));
	}
	

	@Override
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Lancamento.class);
		
		criteria.createAlias("categoria", "c");
		criteria.createAlias("pessoa", "p");
		
		ProjectionList projectionList = Projections.projectionList();
		
		projectionList.add(Projections.property("codigo"));
		projectionList.add(Projections.property("dataVencimento"));
		projectionList.add(Projections.property("descricao"));
		projectionList.add(Projections.property("dataPagamento"));
		projectionList.add(Projections.property("valor"));
		projectionList.add(Projections.property("tipo"));
		projectionList.add(Projections.property("c.nome"));
		projectionList.add(Projections.property("p.nome"));
		
		criteria.setProjection(projectionList);
		
		Criterion[] predicates = criarRestricoes(lancamentoFilter);
		for (Criterion c : predicates) {
			criteria.add(c);
		}
		
		criteria.setResultTransformer(new LancamentoTransformer());
		adicionarRestricoesDePaginacao(criteria, pageable);
		
		return new PageImpl<>(criteria.list(), pageable, total(lancamentoFilter));
	}

	private Criterion[] criarRestricoes(LancamentoFilter lancamentoFilter) {
		List<Criterion> predicates = new ArrayList<>();
		
		if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(Restrictions.like("descricao", "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		}
		
		if (lancamentoFilter.getDataVencimentoDe() != null) {
			predicates.add(Restrictions.ge("dataVencimento", lancamentoFilter.getDataVencimentoDe()));
		}
		
		if (lancamentoFilter.getDataVencimentoAte() != null) {
			predicates.add(Restrictions.le("dataVencimento", lancamentoFilter.getDataVencimentoAte()));
		}
		
		return predicates.toArray(new Criterion[predicates.size()]);
	}

	private void adicionarRestricoesDePaginacao(Criteria criteria, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		criteria.setFirstResult(primeiroRegistroDaPagina);
		criteria.setMaxResults(totalRegistrosPorPagina);
	}
	
	private Long total(LancamentoFilter lancamentoFilter) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Lancamento.class);
		
		Criterion[] predicates = criarRestricoes(lancamentoFilter);
		for (Criterion criterion : predicates) {
			criteria.add(criterion);
		}
		
		criteria.setProjection(Projections.rowCount());
		
		return (Long) criteria.uniqueResult();
	}
	
	private static class LancamentoTransformer implements ResultTransformer {
	   private static final long serialVersionUID = 8767683063836706565L;
	   
	   @SuppressWarnings("rawtypes")
	   @Override
	   public List transformList(List list) {
	      return list;
	   }
	 
	   @Override
	   public Object transformTuple(Object[] valores, String[] alias) {
	      ResumoLancamento resumoLancamento = new ResumoLancamento();
	      resumoLancamento.setCodigo((Long) valores[0]);
	      resumoLancamento.setDataVencimento((LocalDate) valores[1]);
	      resumoLancamento.setDescricao((String) valores[2]);
	      resumoLancamento.setDataPagamento((LocalDate) valores[3]);
	      resumoLancamento.setValor((BigDecimal) valores[4]);
	      resumoLancamento.setTipo((TipoLancamento) valores[5]);
	      resumoLancamento.setCategoria((String) valores[6]);
	      resumoLancamento.setPessoa((String) valores[7]);
	 
	      return resumoLancamento;
	   }
	}
}
