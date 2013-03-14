/**
 * 
 */
package org.zkoss.reference.developer.spring.security.model;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;


/**
 * @author Ian YT Tsai (zanyking)
 *
 */
@Service("articleService")
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleDao articleDao;
	

	public ArticleDao getArticleDao() {
		return articleDao;
	}

	public void setArticleDao(ArticleDao articleDao) {
		this.articleDao = articleDao;
	}

	public List<Article> findAll() {
		return articleDao.findAll();
	}

	public Article find(long id) {
		return articleDao.find(id);
	}

	public void create(Article a) {
		if(a.getId()>0)
			throw new IllegalArgumentException("the Article Id cannot be set! "+a);
		articleDao.createOrUpdate(a);
	}

	public void update(Article a) {
		if(articleDao.find(a.getId())==null){
			throw new IllegalArgumentException("the given Article is not exist! "+a);
		}
		a.setLastModifiedDate(new Date());
		articleDao.createOrUpdate(a);
	}

	public void delete(long id) {
		articleDao.delete(id);
	}
}
