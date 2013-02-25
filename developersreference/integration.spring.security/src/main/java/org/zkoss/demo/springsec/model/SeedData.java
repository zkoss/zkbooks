/**
 * 
 */
package org.zkoss.demo.springsec.model;

import java.util.Date;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author Ian YT Tsai (zanyking)
 *
 */
public class SeedData  implements InitializingBean{
	
	private ArticleDao articleDao; 

	public void afterPropertiesSet() throws Exception {
		String title = "Leading Enterprise Java Web Framework | ZK";
		String author = "rod";
		String content = "Write once, mouse to touch \n" +
				"Write one web application, provide an excellent user experience across " +
				"desktops & touch devices, no separate component set needed.";
		articleDao.createOrUpdate(new Article(title, content , author , new Date()));
		
		title = "Learn ZK in 10 Minutes";
		author = "dianne";
		content = "A brief introduction to the basic concepts of ZK";
		articleDao.createOrUpdate(new Article(title, content , author , new Date()));
		
		title = "Get ZK Up and Running with MVC ";
		author = "scott";
		content = "Through an example application from start to finish, " +
				"learn the gist of ZK programming and how to effectively gain " +
				"full control of UI components' state and behaviour using ZK MVC";
		articleDao.createOrUpdate(new Article(title, content , author , new Date()));
		
		title = "Get ZK Up and Running with MVVM ";
		author = "peter";
		content = "Through an example application from start to finish, learn how ZK MVVM's " +
				"data-binding mechanism works to automate tasks that we'd have otherwise " +
				"carried out manually under the MVC pattern.";
		articleDao.createOrUpdate(new Article(title, content , author , new Date()));
		
	}

	public ArticleDao getArticleDao() {
		return articleDao;
	}

	public void setArticleDao(ArticleDao articleDao) {
		this.articleDao = articleDao;
	}

	
}
