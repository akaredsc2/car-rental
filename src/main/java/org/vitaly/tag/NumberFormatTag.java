package org.vitaly.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by vitaly on 2017-05-10.
 */
public class NumberFormatTag extends SimpleTagSupport {

    private double number;
    private String locale;

    public void setNumber(double number) {
        this.number = number;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public void doTag() throws JspException, IOException {
        if (locale != null) {
            NumberFormat numberFormat;

            switch (locale) {
                case "en_US":
                    numberFormat = NumberFormat.getNumberInstance(Locale.US);
                    break;
                case "uk_UA":
                    numberFormat = NumberFormat.getNumberInstance(new Locale("uk", "UA"));
                    break;
                case "ru_RU":
                    numberFormat = NumberFormat.getNumberInstance(new Locale("ru", "RU"));
                    break;
                default:
                    numberFormat = NumberFormat.getNumberInstance(Locale.ROOT);
            }

            getJspContext().getOut()
                    .println(numberFormat.format(number));
        }
    }
}
