package com.example.demowithtests.util.annotations;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import static com.example.demowithtests.util.annotations.LogColorConstants.ANSI_BLUE;
import static com.example.demowithtests.util.annotations.LogColorConstants.ANSI_RESET;

@Aspect
@Component
@Slf4j
public class SQLQueryCountAspect {
    private final JdbcTemplate jdbcTemplate;
    private int queries = 0;

    public SQLQueryCountAspect(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Pointcut("execution(* com.example.demowithtests.service.fillDataBase.LoaderServiceBean.*(..))")
    private void loadServiceMethods() {}

    @Before("loadServiceMethods()")
    public void countSqlQueriesBefor(JoinPoint joinPoint) {
        queries = 0;
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            log.debug(ANSI_BLUE + "Loader: " + methodName + " - start. Args count - {}" + ANSI_RESET, args.length);
        } else {
            log.debug(ANSI_BLUE + "Loader: " + methodName + " - start." + ANSI_RESET);
        }
    }

    @After("loadServiceMethods()")
    public void countSqlQueriesAfter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        int executedQueryCount = jdbcTemplate.queryForObject("SELECT * FROM pg_stat_statements", Integer.class);
        queries += executedQueryCount;
        log.debug(ANSI_BLUE + "Loader: " + methodName + " - end. SQL queries executed: {}" + ANSI_RESET, queries);
    }
}
