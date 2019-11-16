package com.eying.pcss.workflow.dao;

import com.eying.pcss.workflow.entity.QWorkFlowApp;
import com.eying.pcss.workflow.entity.WorkFlowApp;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

/**
 * 审批流程应用Dao类
 */
public interface WorkFlowAppDao extends JpaRepository<WorkFlowApp, Long>,
        QuerydslPredicateExecutor<WorkFlowApp>, QuerydslBinderCustomizer<QWorkFlowApp> {

    @Override
    default void customize(QuerydslBindings bindings, QWorkFlowApp root) {
        bindings.bind (String.class).first ((StringPath path, String value) -> path.containsIgnoreCase (value));
    }
}
