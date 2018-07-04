package com.estudo.primefaces.dao.util;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;

import com.estudo.primefaces.entity.AbstractEntity;

/**
 * Contem uma implementacao dos metodos mais comuns utilizados pelas classes de
 * modelo. Deve ser estendida nas DAOs especializadas
 * 
 * @param <T>
 *            Classe que sera utilizada nas operacoes.
 */
@SuppressWarnings("unchecked")
public abstract class GenericDAO<T extends AbstractEntity> {
    public static String FMT_FIELD_ORDER = "%s %s";

    @PersistenceContext(unitName = "projetoEstudo")
    protected EntityManager manager;

    /* Constantes para ordenacao */
    public static final String ORDEM_ASC = "asc";
    public static final String ORDEM_DESC = "desc";

    /**
     * Responsavel por recuperar uma instância de AuditReader para obter
     * revisões pela API do Envers
     * 
     * @return AuditReader
     */
    public AuditReader getAuditReader() {
        AuditReader reader = AuditReaderFactory.get(manager);
        return reader;
    }

    /**
     * Método responsável por obter e entidade por id e revisão desejada
     * 
     * @since 23/11/2015 15:21:28
     * @param id
     *            :Object
     * @param revisao
     *            :Number
     * @return T
     */
    public T obterPorIdERevisao(Object id, Number revisao) {
        AuditQuery query = getAuditReader().createQuery().forRevisionsOfEntity(getThisClass(), false, true);
        query.add(AuditEntity.id().eq(id));
        query.add(AuditEntity.revisionNumber().eq(revisao));

        Object[] result = (Object[]) query.getSingleResult();
        return (T) result[0];
    }

    /**
     * Método responsável por obter a ultima revisão por id
     * 
     * @since 23/11/2015 15:21:28
     * @param id
     *            :Object
     * @return T
     */
    public T obterUltimaRevisaoPorId(Object id) {
        AuditQuery query = getAuditReader().createQuery().forRevisionsOfEntity(getThisClass(), false, true);
        query.add(AuditEntity.id().eq(id));
        query.add(AuditEntity.revisionNumber().maximize().computeAggregationInInstanceContext());
        query.setMaxResults(1);

        Object[] result = (Object[]) query.getSingleResult();
        return (T) result[0];
    }

    /**
     * Torna a entidade gerenciada (managed) e persistente. Deve ser substituido
     * pelo incluirOuAlterar
     * 
     * @param obj
     *            Objeto a ser salvo
     */
    public void incluir(T obj) {
        this.manager.persist(obj);
    }

    /**
     * Atualiza o estado da entidade para o contexto de persistência corrente.
     * Deve ser substituido pelo incluirOuAlterar
     * 
     * @param obj
     *            Objeto a ser atualizado
     */
    public void alterar(T obj) {
        this.manager.merge(obj);
    }

    /**
     * Adiciona ou Atualiza o estado da entidade para o contexto de persistência
     * corrente.
     * 
     * @param obj
     *            Objeto a ser adicionado ou atualizado
     */
    public void incluirOuAlterar(T entity) {
        if (entity == null) {
            return;
        }
        if (entity.getId() == null) {
            this.manager.persist(entity);
        } else {
            // if (!this.manager.contains(entity)) {
            this.manager.merge(entity);
            // }
        }
    }

    /**
     * Remove a entidade do contexto de persistência e da base de dados.
     * 
     * @param id
     *            Objeto a ser removido
     */
    public void excluir(Object obj) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Collection<?>) {
            Collection<AbstractEntity> col = (Collection<AbstractEntity>) obj;
            for (Object object : col) {
                object = this.manager.merge(object);
                this.manager.remove(object);
            }
        } else {
            obj = this.manager.merge(obj);
            this.manager.remove(obj);
        }
    }

    /**
     * Procura por uma entidade, da classe específica, por chave primária.
     * 
     * @param id
     * @return Object
     */
    public T obter(Object id) {
        return this.manager.find(getThisClass(), id);
    }

    private Class<T> getThisClass() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) (type).getActualTypeArguments()[0];
    }

    /**
     * Verifica se existe conexão com banco de dados
     * 
     * @author joelcoelho (projeto)
     * @since 24/04/2015
     * @return Boolean
     */
    public Boolean testaConexaoBancoDeDados() {
        try {
            Query query = manager.createNativeQuery("SELECT 1=1");
            return (boolean) query.getSingleResult() ? Boolean.TRUE : Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    /**
     * Método responsável por retornar a funcao Translate e Upper pra consultar
     * por nome/descricao
     * 
     * @since 15/09/2015 10:04:03
     * @author Roberto G Claro <roberto.claro@sigma.com.br>
     * @param param
     *            : String
     * @return String
     */
    public String translateUpper(String param) {
        return " UPPER(translate( trim(" + param + ") ,'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç','AAAAAAAAEEEEIIOOOOOOUUUUCC')) ";
    }

    public EntityManager getManager() {
        return manager;
    }
}