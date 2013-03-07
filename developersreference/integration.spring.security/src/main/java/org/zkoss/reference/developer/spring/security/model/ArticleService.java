/**
 * 
 */
package org.zkoss.reference.developer.spring.security.model;

import java.util.List;

import org.springframework.security.access.annotation.Secured;

/**
 * @author Ian YT Tsai (zanyking)
 *
 */
public interface ArticleService {

	@Secured({"ROLE_USER", "IS_AUTHENTICATED_ANONYMOUSLY"})
	public List<Article> findAll();
	
	@Secured({"ROLE_USER", "IS_AUTHENTICATED_ANONYMOUSLY"})
	public Article find(long id);
	
	@Secured({"ROLE_USER"})
	public void create(Article a);
	
	@Secured({"ROLE_EDITOR","ROLE_USER"})
	public void update(Article a);
	
	@Secured({"ROLE_EDITOR"})
	public void delete(long id);
	
	
}
