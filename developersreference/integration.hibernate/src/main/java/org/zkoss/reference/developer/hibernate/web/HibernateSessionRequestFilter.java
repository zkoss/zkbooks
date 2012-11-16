package org.zkoss.reference.developer.hibernate.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.zkoss.reference.developer.hibernate.dao.HibernateUtil;

public class HibernateSessionRequestFilter implements Filter{
    private static Log log = LogFactory.getLog(HibernateSessionRequestFilter.class);
    private SessionFactory sessionFactory;
 
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
 
        try {
            log.debug("Starting a database transaction");
            sessionFactory.getCurrentSession().beginTransaction();
 
            // Call the next filter (continue request processing)
            chain.doFilter(request, response);
 
            // Commit and cleanup
            log.debug("Committing the database transaction");
            sessionFactory.getCurrentSession().getTransaction().commit();
 
        } catch (StaleObjectStateException staleEx) {
            log.error("This interceptor does not implement optimistic concurrency control!");
            log.error("Your application will not work until you add compensation actions!");
            // Rollback, close everything, possibly compensate for any permanent changes
            // during the conversation, and finally restart business conversation. Maybe
            // give the user of the application a chance to merge some of his work with
            // fresh data... what you do here depends on your applications design.
            throw staleEx;
        } catch (HibernateException ex) {
            // Rollback only
            ex.printStackTrace();
            try {
                if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
                    log.debug("Trying to rollback database transaction after exception");
                    sessionFactory.getCurrentSession().getTransaction().rollback();
                }
            } catch (Throwable rbEx) {
                log.error("Could not rollback transaction after exception!", rbEx);
            }
 
            // Let others handle it... maybe another interceptor for exceptions?
            throw new ServletException(ex);
        }
    }
 
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("Initializing filter...");
        log.debug("Obtaining SessionFactory from static HibernateUtil singleton");
        sessionFactory = HibernateUtil.getSessionFactory();
    }
 
    public void destroy() {}
}