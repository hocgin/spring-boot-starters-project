//package in.hocg.boot.sso.client2.autoconfigure.core.authentication;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.*;
//import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
//import org.springframework.security.web.savedrequest.RequestCache;
//import org.springframework.security.web.util.RedirectUrlBuilder;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * Created by hocgin on 2021/7/3
// * email: hocgin@gmail.com
// *
// * @author hocgin
// */
//@Slf4j
//public class RedirectUrlAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    private PortResolver portResolver = new PortResolverImpl();
//    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//    private RequestCache requestCache = new HttpSessionRequestCache();
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        log.info("触发授权异常: ", authException);
//
//
//        // 如果是ajax
//        int serverPort = portResolver.getServerPort(request);
//        String scheme = request.getScheme();
//
//        RedirectUrlBuilder urlBuilder = new RedirectUrlBuilder();
//
//        urlBuilder.setScheme(scheme);
//        urlBuilder.setServerName(request.getServerName());
//        urlBuilder.setPort(serverPort);
//        urlBuilder.setContextPath(request.getContextPath());
//        urlBuilder.setPathInfo("/login");
//
//        String redirectUrl = urlBuilder.getUrl();
//        redirectStrategy.sendRedirect(request, response, redirectUrl);
//    }
//}
