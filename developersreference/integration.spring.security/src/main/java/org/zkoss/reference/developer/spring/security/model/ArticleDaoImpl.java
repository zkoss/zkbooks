/**
 * 
 */
package org.zkoss.reference.developer.spring.security.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;

/**
 * @author Ian YT Tsai (zanyking)
 *
 */
@Repository
public class ArticleDaoImpl implements ArticleDao, InitializingBean {

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

	public void afterPropertiesSet() throws Exception {
		String title = "Leading Enterprise Java Web Framework | ZK";
		String author = "rod";
		String content = "Write once, mouse to touch \n" +
				"Write one web application, provide an excellent user experience across " +
				"desktops & touch devices, no separate component set needed.";
		this.createOrUpdate(new Article(title, content , author , new Date()));
		
		title = "Learn ZK in 10 Minutes";
		author = "dianne";
		content = "A brief introduction to the basic concepts of ZK";
		this.createOrUpdate(new Article(title, content , author , new Date()));
		
		title = "Get ZK Up and Running with MVC ";
		author = "scott";
		content = "Through an example application from start to finish, " +
				"learn the gist of ZK programming and how to effectively gain " +
				"full control of UI components' state and behaviour using ZK MVC";
		this.createOrUpdate(new Article(title, content , author , new Date()));
		
		title = "Get ZK Up and Running with MVVM ";
		author = "peter";
		content = "Through an example application from start to finish, learn how ZK MVVM's " +
				"data-binding mechanism works to automate tasks that we'd have otherwise " +
				"carried out manually under the MVC pattern.";
		this.createOrUpdate(new Article(title, content , author , new Date()));
		
	}
}
