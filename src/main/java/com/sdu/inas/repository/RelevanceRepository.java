package com.sdu.inas.repository;

import com.sdu.inas.beans.Relevance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface RelevanceRepository extends ElasticsearchRepository<Relevance,String> {

    Relevance queryRelevanceByRId(String rId);
    List<Relevance> queryEventsBySourceEventId(String source);
    List<Relevance> queryRelevancesByTargetEventId(String target);

    /**
     * 根据源实体Id查询关联
     * @param sourceEntityId 源实体id
     * @return 关联列表
     */
    List<Relevance> queryRelevancesBySourceEntityId(String sourceEntityId);

    /**
     *根据目标实体Id查询关联
     * @param targetEntityId 目标实体Id
     * @return 关联列表
     */
    List<Relevance> queryRelevancesByTargetEntityId(String targetEntityId);
    List<Relevance> queryRelevancesBySourceEntityIdAndTargetEntityId(String sourceId,String targetId);
    void deleteRelevanceByTargetEventId(String target);
    void deleteRelevanceBySourceEventId(String source);
}
