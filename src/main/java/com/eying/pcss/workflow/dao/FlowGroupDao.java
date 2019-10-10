package com.eying.pcss.workflow.dao;

import com.eying.pcss.workflow.entity.FlowGroup;
import com.eying.pcss.workflow.entity.QFlowGroup;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

/**
 * 审批流程分组Dao类
 */
public interface FlowGroupDao extends JpaRepository<FlowGroup,String>,
        QuerydslPredicateExecutor<FlowGroup>, QuerydslBinderCustomizer<QFlowGroup> {

    @Override
    default void customize(QuerydslBindings bindings, QFlowGroup root){
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
