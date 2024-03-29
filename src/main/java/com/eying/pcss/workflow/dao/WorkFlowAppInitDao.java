package com.eying.pcss.workflow.dao;

import com.eying.pcss.workflow.entity.QWorkFlowAppInit;
import com.eying.pcss.workflow.entity.WorkFlowAppInit;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

/**
 * 审批流程应用初始化数据表Dao类
 */
public interface WorkFlowAppInitDao extends JpaRepository<WorkFlowAppInit, Long>,
        QuerydslPredicateExecutor<WorkFlowAppInit>, QuerydslBinderCustomizer<QWorkFlowAppInit> {

    @Override
    default void customize(QuerydslBindings bindings, QWorkFlowAppInit root) {
        bindings.bind (String.class).first ((StringPath path, String value) -> path.containsIgnoreCase (value));
    }
}
