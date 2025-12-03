package com.example.bdget.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Custom metrics for CI/CD and application monitoring
 */
@Component
public class CustomMetrics {

    private final MeterRegistry meterRegistry;
    
    // CI/CD Metrics
    private final AtomicInteger testCoveragePercentage;
    private final AtomicInteger buildStatus;
    private final AtomicInteger codeSmellsCount;
    private final AtomicInteger bugsCount;
    private final AtomicInteger securityVulnerabilitiesCount;
    private final AtomicInteger technicalDebtRatio;
    private final AtomicInteger testsSuccessRate;
    private final AtomicInteger testsFailedCount;
    
    private final Counter deploymentSuccessCounter;
    private final Counter deploymentFailureCounter;
    
    private final Timer buildDurationTimer;
    private final Timer testsDurationTimer;
    private final Timer deploymentDurationTimer;

    public CustomMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        // Initialize CI/CD metrics
        this.testCoveragePercentage = new AtomicInteger(85); // Default 85%
        this.buildStatus = new AtomicInteger(0); // 0 = success, 1 = failed
        this.codeSmellsCount = new AtomicInteger(0);
        this.bugsCount = new AtomicInteger(0);
        this.securityVulnerabilitiesCount = new AtomicInteger(0);
        this.technicalDebtRatio = new AtomicInteger(3); // Default 3%
        this.testsSuccessRate = new AtomicInteger(100); // Default 100%
        this.testsFailedCount = new AtomicInteger(0);
        
        // Register gauges
        Gauge.builder("test_coverage_percentage", testCoveragePercentage, AtomicInteger::get)
            .description("Test coverage percentage")
            .tag("category", "quality")
            .register(meterRegistry);
        
        Gauge.builder("build_status", buildStatus, AtomicInteger::get)
            .description("Last build status (0=success, 1=failed)")
            .tag("category", "cicd")
            .register(meterRegistry);
        
        Gauge.builder("code_smells_count", codeSmellsCount, AtomicInteger::get)
            .description("Number of code smells detected")
            .tag("category", "quality")
            .register(meterRegistry);
        
        Gauge.builder("bugs_count", bugsCount, AtomicInteger::get)
            .description("Number of bugs detected")
            .tag("category", "quality")
            .register(meterRegistry);
        
        Gauge.builder("security_vulnerabilities_count", securityVulnerabilitiesCount, AtomicInteger::get)
            .description("Number of security vulnerabilities")
            .tag("category", "security")
            .register(meterRegistry);
        
        Gauge.builder("technical_debt_ratio", technicalDebtRatio, AtomicInteger::get)
            .description("Technical debt ratio percentage")
            .tag("category", "quality")
            .register(meterRegistry);
        
        Gauge.builder("tests_success_rate", testsSuccessRate, AtomicInteger::get)
            .description("Test success rate percentage")
            .tag("category", "testing")
            .register(meterRegistry);
        
        Gauge.builder("tests_failed_count", testsFailedCount, AtomicInteger::get)
            .description("Number of failed tests")
            .tag("category", "testing")
            .register(meterRegistry);
        
        // Register counters
        this.deploymentSuccessCounter = Counter.builder("deployment_success_total")
            .description("Total successful deployments")
            .tag("category", "deployment")
            .register(meterRegistry);
        
        this.deploymentFailureCounter = Counter.builder("deployment_failure_total")
            .description("Total failed deployments")
            .tag("category", "deployment")
            .register(meterRegistry);
        
        // Register timers
        this.buildDurationTimer = Timer.builder("build_duration_seconds")
            .description("Build duration in seconds")
            .tag("category", "cicd")
            .register(meterRegistry);
        
        this.testsDurationTimer = Timer.builder("tests_duration_seconds")
            .description("Tests execution duration in seconds")
            .tag("category", "testing")
            .register(meterRegistry);
        
        this.deploymentDurationTimer = Timer.builder("deployment_duration_seconds")
            .description("Deployment duration in seconds")
            .tag("category", "deployment")
            .register(meterRegistry);
    }
    
    // Setters for updating metrics
    public void setTestCoveragePercentage(int percentage) {
        this.testCoveragePercentage.set(percentage);
    }
    
    public void setBuildStatus(int status) {
        this.buildStatus.set(status);
    }
    
    public void setCodeSmellsCount(int count) {
        this.codeSmellsCount.set(count);
    }
    
    public void setBugsCount(int count) {
        this.bugsCount.set(count);
    }
    
    public void setSecurityVulnerabilitiesCount(int count) {
        this.securityVulnerabilitiesCount.set(count);
    }
    
    public void setTechnicalDebtRatio(int ratio) {
        this.technicalDebtRatio.set(ratio);
    }
    
    public void setTestsSuccessRate(int rate) {
        this.testsSuccessRate.set(rate);
    }
    
    public void setTestsFailedCount(int count) {
        this.testsFailedCount.set(count);
    }
    
    public void recordDeploymentSuccess() {
        this.deploymentSuccessCounter.increment();
    }
    
    public void recordDeploymentFailure() {
        this.deploymentFailureCounter.increment();
    }
    
    public Timer getBuildDurationTimer() {
        return buildDurationTimer;
    }
    
    public Timer getTestsDurationTimer() {
        return testsDurationTimer;
    }
    
    public Timer getDeploymentDurationTimer() {
        return deploymentDurationTimer;
    }
}
