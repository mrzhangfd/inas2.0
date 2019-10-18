package com.sdu.inas.service;

import com.sdu.inas.beans.Relevance;

import java.util.List;

public interface RelevanceService {

    Relevance getRelevanceByReId(String reId);

    Relevance addRelevance(Relevance relevance);

    void delRelevance(String rId);

    List<Relevance> getRelevancesByEventId(String EventId);

    List<Relevance> getRelevancesByEntityIds(String sourceId, String targetId);

    List<Relevance> getActiveRelevancesForEntity(String objectId);

    List<Relevance> getPassiveRelevancesForEntity(String objectId);

}
