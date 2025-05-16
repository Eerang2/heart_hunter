package heart_link.infrastructure.config;

import heart_link.presentation.interceptor.TokenRedirectInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TokenRedirectInterceptor tokenRedirectInterceptor;

    @Autowired
    public WebConfig(TokenRedirectInterceptor tokenRedirectInterceptor) {
        this.tokenRedirectInterceptor = tokenRedirectInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenRedirectInterceptor)
                .addPathPatterns("/")
                .addPathPatterns("/signup")
                .addPathPatterns("/member/dongui");

    }

}
