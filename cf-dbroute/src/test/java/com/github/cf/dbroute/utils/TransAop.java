package com.github.cf.dbroute.utils;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 检测声明式事务，是否真正开启了事务。避免因为配置不当等原因导致事务没有开启。
 */
@Aspect
@Order(value = Integer.MAX_VALUE)// order值越大执行 越靠后，所以 该方法在事务内部执行，理应有事务，
public class TransAop {
    private static final Logger logger = LoggerFactory.getLogger(TransAop.class);

    @Before(value = "@annotation(org.springframework.transaction.annotation.Transactional)")
    public void checkTrans() throws Throwable {
        hasTrans();
    }

    /**
     * @return true :当前线程被事务管理器管理，且有事务
     * false:当前线程被事务管理器管理，但是没有事务。（可能是Propagation.NOT_SUPPORTED等）
     * @throws IllegalStateException: 当前线程没有被事务管理器管理。
     */
    public static boolean hasTrans() {
        logger.debug("TransactionSynchronizationManager.getCurrentTransactionName():" + TransactionSynchronizationManager.getCurrentTransactionName());
        logger.debug("TransactionSynchronizationManager.isActualTransactionActive():" + TransactionSynchronizationManager.isActualTransactionActive());
        logger.debug("TransactionSynchronizationManager.isCurrentTransactionReadOnly():" + TransactionSynchronizationManager.isCurrentTransactionReadOnly());
        logger.debug("TransactionSynchronizationManager.isSynchronizationActive():" + TransactionSynchronizationManager.isSynchronizationActive());
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            logger.error("当前方法没有事务.请检查配置");
            throw new IllegalStateException("当前方法没有事务.请检查配置.");
        }
        return TransactionSynchronizationManager.isActualTransactionActive();
    }
}
