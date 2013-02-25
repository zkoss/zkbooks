/**
 * 
 */
package org.zkoss.demo.springsec.model;

import java.util.Date;
import java.util.List;


/**
 * @author Ian YT Tsai (zanyking)
 *
 */
public class ArticleServiceImpl implements ArticleService {

	private ArticleDao articleDao;
	
	public ArticleServiceImpl(ArticleDao articleDao) {
		super();
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
