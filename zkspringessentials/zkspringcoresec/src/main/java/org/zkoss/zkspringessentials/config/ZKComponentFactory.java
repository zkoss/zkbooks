/**
 * 
 */
package org.zkoss.zkspringessentials.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;

/**
 * @author ashish
 *
 */
@Configuration
public class ZKComponentFactory {

	@Bean(name="myBtn")
	@Lazy(true)
	@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
	public Button m1() {
		System.out.println("generating myBtn bean");
		return new Button();
	}
	@Bean(name="myLbl")
	@Lazy(true)
	@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
	public Label m2() {
		System.out.println("generating myLbl bean");
		return new Label();
	}
}
