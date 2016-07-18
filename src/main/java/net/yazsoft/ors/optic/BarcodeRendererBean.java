package net.yazsoft.ors.optic;

import org.primefaces.application.resource.DynamicContentType;
import org.primefaces.component.barcode.Barcode;
import org.primefaces.component.barcode.BarcodeRenderer;
import org.primefaces.context.RequestContext;
import org.primefaces.util.Constants;
import org.primefaces.util.SharedStringBuilder;
import org.primefaces.util.StringEncrypter;

import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class BarcodeRendererBean extends BarcodeRenderer {

    private static final String SB_BUILD = BarcodeRenderer.class.getName() + "#build";

    public String encodeSrc(FacesContext context, UIComponent component) throws IOException {
        StringBuffer writer = new StringBuffer();
        Barcode barcode = (Barcode) component;
        String clientId = barcode.getClientId(context);
        String styleClass = barcode.getStyleClass();
        String src = null;
        Object value = barcode.getValue();
        String type = barcode.getType();
        DynamicContentType dynamicContentType = type.equals("qr") ? DynamicContentType.QR_CODE : DynamicContentType.BARCODE;

        if(value == null) {
            return null;
        }

        try {
            Resource resource = context.getApplication().getResourceHandler().createResource("dynamiccontent.properties", "primefaces", "image/png");
            String resourcePath = resource.getRequestPath();
            StringEncrypter encrypter = RequestContext.getCurrentInstance().getEncrypter();
            String rid = encrypter.encrypt((String) value);
            StringBuilder builder = SharedStringBuilder.get(context, SB_BUILD);

            src = builder.append(resourcePath).append("&").append(Constants.DYNAMIC_CONTENT_PARAM).append("=").append(URLEncoder.encode(rid, "UTF-8"))
                    .append("&").append(Constants.DYNAMIC_CONTENT_TYPE_PARAM).append("=").append(dynamicContentType.toString())
                    .append("&gen=").append(type)
                    .append("&fmt=").append(barcode.getFormat())
                    .append("&").append(Constants.DYNAMIC_CONTENT_CACHE_PARAM).append("=").append(barcode.isCache())
                    .append("&ori=").append(barcode.getOrientation())
                    .toString();
        }
        catch (UnsupportedEncodingException ex) {
            throw new IOException(ex);
        }

        writer.append(src);
        return writer.toString();
    }
}
