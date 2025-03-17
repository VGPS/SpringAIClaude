package com.wgblackmon.docprocessing.utils.monitoring;

/**
 * PerformanceMonitor
 *
 * Aspect for monitoring method performance across the application.
 */
@Aspect
@Component
@Slf4j
public class PerformanceMonitor {
    private final MetricsService metricsService;

    @Around("@annotation(Monitored)")
    public Object monitorPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Timer.Sample sample = Timer.start();

        try {
            Object result = joinPoint.proceed();
            sample.stop(metricsService.getTimer("method.execution", methodName));
            return result;
        } catch (Exception e) {
            metricsService.recordError(methodName);
            throw e;
        }
    }
}