package com.eying.pcss.workflow.dao;

import com.eying.pcss.workflow.entity.FlowGroupInit;
import com.eying.pcss.workflow.entity.QFlowGroupInit;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

/**
 * 审批流程分组初始化数据表Dao类
 */
public interface FlowGroupInitDao extends JpaRepository<FlowGroupInit,String>,
        QuerydslPredicateExecutor<FlowGroupInit>, QuerydslBinderCustomizer<QFlowGroupInit> {

    @Override
    default void customize(QuerydslBindings bindings, QFlowGroupInit root){
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
