package org.zkoss.reference.developer.mvvm;

import org.zkoss.reference.developer.mvvm.databinding.InitVM;
import org.zkoss.xel.VariableResolver;
import org.zkoss.xel.XelException;

/**
 * Implement your own way to locate or instantiate a ViewModel object.
 * @author hawk
 *
 */
public class ViewModelResolver implements VariableResolver {

	/**
	 * instantiate a ViewModel or locate it e.g. a Spring bean
	 */
	@Override
	public Object resolveVariable(String name) throws XelException {
		if ("myvm".equals(name)){
			return new InitVM();
		}else{
			return null;
		}
	}
}
