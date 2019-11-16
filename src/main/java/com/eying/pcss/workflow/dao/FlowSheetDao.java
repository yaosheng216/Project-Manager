package com.eying.pcss.workflow.dao;

import com.eying.pcss.workflow.entity.FlowSheet;
import com.eying.pcss.workflow.entity.QFlowSheet;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

/**
 * 自定义审批单Dao类
 */
public interface FlowSheetDao extends JpaRepository<FlowSheet, Long>,
        QuerydslPredicateExecutor<FlowSheet>, QuerydslBinderCustomizer<QFlowSheet> {

    @Override
    default void customize(QuerydslBindings bindings, QFlowSheet root) {
        bindings.bind (String.class).first ((StringPath path, String value) -> path.containsIgnoreCase (value));
    }

}



