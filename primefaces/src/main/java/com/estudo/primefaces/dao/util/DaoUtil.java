package com.estudo.primefaces.dao.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.jpa.QueryHints;
import org.primefaces.model.SortOrder;

public abstract class DaoUtil {
    /**
     * Método que monta o Sorter para consultas
     * 
     * @param sortField
     *            - Coluna de ordenação
     * @param sortOrder
     *            - SorterOrder ASCENDING DESCENDING
     * @param sb
     *            - Query
     */
    public static void sorterQuery(String aliasObject, String sortField, SortOrder sortOrder, StringBuilder sb) {
        if (StringUtils.isNotBlank(sortField) && StringUtils.isNotBlank(aliasObject) && sortOrder != null) {
            sb.append(" ORDER BY ");
            if (sortOrder.equals(SortOrder.ASCENDING)) {
                sb.append(aliasObject + "." + sortField + " ASC");
            } else if (sortOrder.equals(SortOrder.DESCENDING)) {
                sb.append(aliasObject + "." + sortField + " DESC");
            }
        }
    }

    /**
     * Método usado para setar os parametros de acordo com atributos de pesquisa
     * 
     * @param parameters
     * @param query
     */
    public static void setParameters(HashMap<String, Object> parameters, Query query) {
        Iterator<String> keySetIterator = parameters.keySet().iterator();

        while (keySetIterator.hasNext()) {
            String key = keySetIterator.next();
            query.setParameter(key, parameters.get(key));
        }
    }

    /**
     * Ativa o cache de consultas.
     * 
     * @param query
     * @return {@link Query}
     */
    public static Query enableCache(Query query) {
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        return query;
    }

    /**
     * Ativa o cache de consultas.
     * 
     * @param query
     * @return {@link Query}
     */
    public static Query enableCache(Query query, String region) {
        enableCache(query);
        query.setHint(QueryHints.HINT_CACHE_REGION, region);
        return query;
    }

    /**
     * Remove os dados para entidades do tipo especificado do cache.
     * 
     * @param manager
     * @param cls
     */
    public static void evictCache(EntityManager manager, Class<?> cls) {
        Cache cache = manager.getEntityManagerFactory().getCache();
        cache.evict(cls);
    }

    /**
     * Metodo responsavel em
     * 
     * @param isAddFiltro
     *            true para o termo AND e false para o termo WHERE
     * @return
     */
    public static String getAndOuWhere(Boolean isAddFiltro) {
        return isAddFiltro ? " AND " : " WHERE ";
    }

    /**
     * Metodo responsavel em setar o parametro se o valor for diferente de nulo.
     * 
     * @param query
     * @param parameterName1
     * @param value1
     * @param parameterName2
     * @param value2
     */
    public static Query setParameterIfNotNull(Query query, String parameterName1, Object value1, String parameterName2,
            Object value2) {
        if (value1 != null && value2 != null) {
            query.setParameter(parameterName1, value1);
            query.setParameter(parameterName2, value2);
        }
        return query;
    }

    /**
     * Metodo responsavel em setar o parametro se o valor for diferente de nulo.
     * 
     * @param query
     * @param string
     * @param value
     */
    public static Query setParameterIfNotNull(Query query, String parameterName, Object value) {
        if (value != null) {
            query.setParameter(parameterName, value);
        }
        return query;
    }

    public static Query setParameterIfObjectNotNull(Query query, String parameterName, Object value) {
        if (value != null) {
            query.setParameter(parameterName, value);
        }
        return query;
    }

    public static Query setParameterIfNotNullOrBlank(Query query, String parameterName, String value) {
        if (!StringUtils.isEmpty(value)) {
            query.setParameter(parameterName, value);
        }
        return query;
    }

    @SuppressWarnings("rawtypes")
    public static Query setParameterIfNotEmpty(Query query, String parameterName, Collection value) {
        if (CollectionUtils.isNotEmpty(value)) {
            query.setParameter(parameterName, value);
        }
        return query;
    }

    /**
     * Metodo responsavel em controlar a adicao de criterio se o valor for
     * diferente de nulo.
     * 
     * @param qlStringBuider
     * @param isAddFiltro
     * @param criterio
     * @param value
     */
    public static Boolean addCriterioIfNotNull(StringBuilder qlStringBuider, Boolean isAddFiltro, String criterio,
            Object value) {
        if (validValue(value)) {
            qlStringBuider.append(DaoUtil.getAndOuWhere(isAddFiltro));
            qlStringBuider.append(criterio);
            return true;
        }
        return isAddFiltro ? isAddFiltro : false;
    }

    public static Boolean addCriterioIfObjectNotNull(StringBuilder qlStringBuider, Boolean isAddFiltro, String criterio,
            Object value) {
        if (value != null) {
            qlStringBuider.append(DaoUtil.getAndOuWhere(isAddFiltro));
            qlStringBuider.append(criterio);
            return true;
        }
        return isAddFiltro ? isAddFiltro : false;
    }

    public static Boolean addCriterioComOperadorIfNotNull(StringBuilder qlStringBuider, Boolean isAddFiltro,
            String criterio, Object value) {
        if (validValue(value)) {
            qlStringBuider.append(criterio);
            return true;
        }
        return isAddFiltro ? isAddFiltro : false;
    }

    /**
     * Metodo responsavel em controlar a adicao de criterio se o valor for
     * diferente de nulo.
     * 
     * @param qlStringBuider
     * @param isAddFiltro
     * @param criterio
     * @param value1
     */
    public static Boolean addCriterioIfNotNull(StringBuilder qlStringBuider, Boolean isAddFiltro, String criterio,
            Object value1, Object value2) {
        if (value1 != null && value2 != null) {
            qlStringBuider.append(DaoUtil.getAndOuWhere(isAddFiltro));
            qlStringBuider.append(criterio);
            return true;
        }
        return isAddFiltro ? isAddFiltro : false;
    }

    public static Query setPaginationIfNotNull(Query query, Pagination pagination, boolean count) {
        if ((pagination != null) && (!count)) {
            query.setFirstResult(pagination.getOffset());
            query.setMaxResults(pagination.getSize());
        }
        return query;
    }

    private static boolean validValue(Object value) {
        if (value instanceof String) {
            return !StringUtils.isEmpty((String) value);
        } else {
            return value != null;
        }
    }
}