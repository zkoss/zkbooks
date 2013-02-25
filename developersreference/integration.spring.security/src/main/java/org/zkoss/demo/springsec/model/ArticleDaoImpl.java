/**
 * 
 */
package org.zkoss.demo.springsec.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ian YT Tsai (zanyking)
 *
 */
public class ArticleDaoImpl implements ArticleDao {

	private long id = 0;
	private Map<Long, Article> articles = new HashMap<Long, Article>();
	
	public Article find(Long articleId) {
		return articles.get(articleId);
	}

	public void createOrUpdate(Article article) {
		if(article.getId()<0){
			article.setId(++id);
		}
		System.out.println("> create: "+article);
		articles.put(new Long(article.getId()), article);
	}

	public List<Article> findAll() {
		return new ArrayList<Article>(articles.values());
	}

	public void delete(Long id) {
		articles.remove(id);
	}

}
