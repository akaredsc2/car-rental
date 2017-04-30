package org.vitaly.security;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by vitaly on 30.04.17.
 */
public class UrlHttpMethodPair {
    static final UrlHttpMethodPair ENTRY_POINT = UrlHttpMethodPair.of("/", HttpMethod.GET);
    static final UrlHttpMethodPair PAGE_INDEX = UrlHttpMethodPair.of("/index.jsp", HttpMethod.GET);
    static final UrlHttpMethodPair PAGE_HOME = UrlHttpMethodPair.of("/home.jsp", HttpMethod.GET);

    static final UrlHttpMethodPair ERROR_PAGE_404 = UrlHttpMethodPair.of("/404.jsp", HttpMethod.GET);
    static final UrlHttpMethodPair ERROR_PAGE_550 = UrlHttpMethodPair.of("/550.jsp", HttpMethod.GET);
    static final UrlHttpMethodPair ERROR_PAGE = UrlHttpMethodPair.of("/error.jsp", HttpMethod.GET);

    static final UrlHttpMethodPair PAGE_REGISTRATION = UrlHttpMethodPair.of("/registration.jsp", HttpMethod.GET);
    static final UrlHttpMethodPair PAGE_ADD_MODEL = UrlHttpMethodPair.of("/add_car_model.jsp", HttpMethod.GET);
    static final UrlHttpMethodPair PAGE_ADD_LOCATION = UrlHttpMethodPair.of("/add_location.jsp", HttpMethod.GET);

    public static final UrlHttpMethodPair SIGN_IN = UrlHttpMethodPair.of("/sign_in", HttpMethod.POST);
    public static final UrlHttpMethodPair SIGN_OUT = UrlHttpMethodPair.of("/sign_out", HttpMethod.POST);
    public static final UrlHttpMethodPair REGISTRATION = UrlHttpMethodPair.of("/registration", HttpMethod.POST);

    public static final UrlHttpMethodPair CHANGE_LOCALE = UrlHttpMethodPair.of("/locale", HttpMethod.GET);

    public static final UrlHttpMethodPair ADD_MODEL_POST = UrlHttpMethodPair.of("/add_model", HttpMethod.POST);
    public static final UrlHttpMethodPair ADD_CAR_GET = UrlHttpMethodPair.of("/add_car", HttpMethod.GET);
    public static final UrlHttpMethodPair ADD_CAR_POST = UrlHttpMethodPair.of("/add_car", HttpMethod.POST);
    public static final UrlHttpMethodPair ADD_LOCATION_POST = UrlHttpMethodPair.of("/add_location", HttpMethod.POST);


    private String url;
    private HttpMethod httpMethod;

    private UrlHttpMethodPair(String url, HttpMethod httpMethod) {
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

    private static UrlHttpMethodPair of(String url, HttpMethod method) {
        return new UrlHttpMethodPair(url, method);
    }

    public static UrlHttpMethodPair fromRequest(HttpServletRequest request) {
        String url = request.getRequestURI().substring(request.getContextPath().length());
        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());

        return new UrlHttpMethodPair(url, httpMethod);
    }
}
