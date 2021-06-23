package com.example.demo.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class SecurityContextPersistenceFilterCustom extends SecurityContextPersistenceFilter {
    static final String FILTER_APPLIED = "__spring_security_scpf_applied";
    private SecurityContextRepository repo;
    private boolean forceEagerSessionCreation;

    public SecurityContextPersistenceFilterCustom() {
        this(new HttpSessionSecurityContextRepository());
    }

    public SecurityContextPersistenceFilterCustom(SecurityContextRepository repo) {
        this.forceEagerSessionCreation = false;
        this.repo = repo;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request.getAttribute("__spring_security_scpf_applied") != null) {
            chain.doFilter(request, response);
        } else {
            request.setAttribute("__spring_security_scpf_applied", Boolean.TRUE);
            if (this.forceEagerSessionCreation) {
                HttpSession session = request.getSession();
                if (this.logger.isDebugEnabled() && session.isNew()) {
                    this.logger.debug(LogMessage.format("Created session %s eagerly", session.getId()));
                }
            }

            HttpRequestResponseHolder holder = new HttpRequestResponseHolder(request, response);
            SecurityContext contextBeforeChainExecution = this.repo.loadContext(holder);
            boolean var10 = false;

            try {
                var10 = true;
                SecurityContextHolder.setContext(contextBeforeChainExecution);
                if (contextBeforeChainExecution.getAuthentication() == null) {
                    this.logger.debug("Set SecurityContextHolder to empty SecurityContext");
                } else if (this.logger.isDebugEnabled()) {
                    this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", contextBeforeChainExecution));
                }

                chain.doFilter(holder.getRequest(), holder.getResponse());
                var10 = false;
            } finally {
                if (var10) {
                    SecurityContext contextAfterChainExecution = SecurityContextHolder.getContext();
                    SecurityContextHolder.clearContext();
                    this.repo.saveContext(contextAfterChainExecution, holder.getRequest(), holder.getResponse());
                    request.removeAttribute("__spring_security_scpf_applied");
                    this.logger.debug("Cleared SecurityContextHolder to complete request");
                }
            }

            SecurityContext contextAfterChainExecution = SecurityContextHolder.getContext();
            SecurityContextHolder.clearContext();
            this.repo.saveContext(contextAfterChainExecution, holder.getRequest(), holder.getResponse());
            request.removeAttribute("__spring_security_scpf_applied");
            this.logger.debug("Cleared SecurityContextHolder to complete request");
        }
    }

    public void setForceEagerSessionCreation(boolean forceEagerSessionCreation) {
        this.forceEagerSessionCreation = forceEagerSessionCreation;
    }
}
