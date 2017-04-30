package org.vitaly.security;

import org.vitaly.util.InputChecker;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by vitaly on 30.04.17.
 */
public class UrlHttpMethodPair {
    static final UrlHttpMethodPair ENTRY_POINT = new UrlHttpMethodPair("/", HttpMethod.GET);
    static final UrlHttpMethodPair PAGE_INDEX = new UrlHttpMethodPair("/index.jsp", HttpMethod.GET);
    static final UrlHttpMethodPair PAGE_HOME = new UrlHttpMethodPair("/home.jsp", HttpMethod.GET);

    static final UrlHttpMethodPair ERROR_PAGE_403 = new UrlHttpMethodPair("/403.jsp", HttpMethod.GET);
    static final UrlHttpMethodPair ERROR_PAGE_404 = new UrlHttpMethodPair("/404.jsp", HttpMethod.GET);
    static final UrlHttpMethodPair ERROR_PAGE = new UrlHttpMethodPair("/error.jsp", HttpMethod.GET);

    static final UrlHttpMethodPair PAGE_REGISTRATION = new UrlHttpMethodPair("/registration.jsp", HttpMethod.GET);
    static final UrlHttpMethodPair PAGE_ADD_MODEL = new UrlHttpMethodPair("/add_car_model.jsp", HttpMethod.GET);
    static final UrlHttpMethodPair PAGE_ADD_LOCATION = new UrlHttpMethodPair("/add_location.jsp", HttpMethod.GET);

    public static final UrlHttpMethodPair SIGN_IN = new UrlHttpMethodPair("/sign_in", HttpMethod.POST);
    public static final UrlHttpMethodPair SIGN_OUT = new UrlHttpMethodPair("/sign_out", HttpMethod.POST);
    public static final UrlHttpMethodPair REGISTRATION = new UrlHttpMethodPair("/registration", HttpMethod.POST);

    public static final UrlHttpMethodPair CHANGE_LOCALE = new UrlHttpMethodPair("/locale", HttpMethod.GET);

    public static final UrlHttpMethodPair ADD_MODEL_POST = new UrlHttpMethodPair("/add_model", HttpMethod.POST);
    public static final UrlHttpMethodPair ADD_CAR_GET = new UrlHttpMethodPair("/add_car", HttpMethod.GET);
    public static final UrlHttpMethodPair ADD_CAR_POST = new UrlHttpMethodPair("/add_car", HttpMethod.POST);
    public static final UrlHttpMethodPair ADD_LOCATION_POST = new UrlHttpMethodPair("/add_location", HttpMethod.POST);

    private String url;
    private HttpMethod httpMethod;

    UrlHttpMethodPair(String url, HttpMethod httpMethod) {
        this.url = url;
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UrlHttpMethodPair that = (UrlHttpMethodPair) o;

        return url.equals(that.url) && httpMethod == that.httpMethod;
    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + httpMethod.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UrlHttpMethodPair{" +
                "url='" + url + '\'' +
                ", httpMethod=" + httpMethod +
                '}';
    }

    public static UrlHttpMethodPair fromRequest(HttpServletRequest request) {
        InputChecker.requireNotNull(request, "Request must not be null!");

        String url = request.getRequestURI().substring(request.getContextPath().length());
        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());

        return new UrlHttpMethodPair(url, httpMethod);
    }
}
