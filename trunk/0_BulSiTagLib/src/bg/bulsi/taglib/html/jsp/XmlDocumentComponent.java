package bg.bulsi.taglib.html.jsp;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.w3c.dom.Document;

/**
 * @author tzvetan.stefanov@bul-si.bg
 *
 */
public class XmlDocumentComponent extends UIComponentBase {
	
	public static final String COMPONENT_TYPE = "Bsi.XMLDocument";
	
	private String document;

	@Override
    public Object saveState(FacesContext context) {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = document;
        return ((Object) (values));
    }

	@Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        document = (String)values[1];
    }

	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		Document doc = (Document) getAttributes().get("value");
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", this);
		writer.write("<table><tr><td>One</td></tr></table>");
		writer.endElement("div");
	}

	@Override
	public String getFamily() {
		return COMPONENT_TYPE;
	}

	/**
	 * @see javax.faces.component.UIComponent#encodeEnd(javax.faces.context.FacesContext)
	 */
	@Override
	public void encodeEnd(FacesContext context) throws IOException {
		return;
	}

	/**
	 * @see javax.faces.component.UIComponent#decode(javax.faces.context.FacesContext)
	 */
	@Override
	public void decode(FacesContext context) {
		return;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}



}
