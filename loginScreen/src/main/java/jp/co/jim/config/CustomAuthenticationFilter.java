//package jp.co.jim.config;
//
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//
//import java.io.IOException;
//
//public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final String loginProcessingUrl;
//    private final String defaultTargetUrl;
//
//    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, String loginProcessingUrl, String defaultTargetUrl, String... paths) {
//        this.loginProcessingUrl = loginProcessingUrl;
//        this.defaultTargetUrl = defaultTargetUrl;
//        super.setAuthenticationManager(authenticationManager);
//        if (paths.length > 0) {
//            super.setFilterProcessesUrl(paths[0]); // 登录接口
//        }
////        super.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler()); // 登录成功处理器
////        super.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler()); // 登录失败处理器
//    }
//
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//                                            Authentication authResult) throws IOException, ServletException, jakarta.servlet.ServletException {
//        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
//        handler.setDefaultTargetUrl(defaultTargetUrl);
//        handler.setRedirectStrategy(new CustomRedirectStrategy());
//        handler.onAuthenticationSuccess( request,  response, authResult);
//    }
//
//    private static class CustomRedirectStrategy extends org.springframework.security.web.DefaultRedirectStrategy {
//
//        public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
//            super.sendRedirect(request,  response, response.encodeRedirectURL(url));
//        }
//    }
//}
//
