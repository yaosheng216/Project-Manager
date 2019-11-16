package com.eying.pcss.workflow.dao;

import com.eying.pcss.workflow.entity.FlowDefinition;
import com.eying.pcss.workflow.entity.QFlowDefinition;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

/**
 * 审批流程定义Dao类
 */
public interface FlowDefinitionDao extends JpaRepository<FlowDefinition, String>,
        QuerydslPredicateExecutor<FlowDefinition>, QuerydslBinderCustomizer<QFlowDefinition> {

    @Override
    default void customize(QuerydslBindings bindings, QFlowDefinition root) {
        bindings.bind (String.class).first ((StringPath path, String value) -> path.containsIgnoreCase (value));
    }
}
