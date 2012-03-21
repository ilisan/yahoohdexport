package bg.bulsi.taglib.html.jsp;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;

public class XmlDocumentTag extends UIComponentELTag {

	public static final String COMPONENT_TYPE = "Bsi.XMLDocument";
	
	private String document;

	@Override
	public String getComponentType() {
		return COMPONENT_TYPE;
	}

	@Override
	public String getRendererType() {
		return null;
	}

	/**
	 * @see javax.faces.webapp.UIComponentTag#setProperties(javax.faces.component.UIComponent)
	 */
	@Override
	protected void setProperties(UIComponent component) {
		/* you have to call the super class */
		super.setProperties(component);
		((XmlDocumentComponent)component).setDocument(document);
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}
	
	

}
