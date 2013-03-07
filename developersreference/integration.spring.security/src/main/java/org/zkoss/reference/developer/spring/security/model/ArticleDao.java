/**
 * 
 */
package org.zkoss.reference.developer.spring.security.model;

import java.util.List;


/**
 * @author Ian YT Tsai (zanyking)
 *
 */
public interface ArticleDao {

	/**
	 * find article by id
	 * @param articleId
	 * @return an Article correspond to given id, null otherwise.
	 */
	public Article find(Long articleId);
	/**
	 * 
	 * @param article the article need to be created or updated.
	 */
	public void createOrUpdate(Article article);
	/**
	 * 
	 * @return a list of all article.
	 */
	public List<Article> findAll();
	/**
	 * 
	 * @param id
	 */
	public void delete(Long id);
}
