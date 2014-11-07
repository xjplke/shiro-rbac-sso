package me.itsoho;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;




@Configuration
@ComponentScan
//@EnableAspectJAutoProxy
@EnableWebMvc
@EnableAutoConfiguration
public class Application  extends WebMvcConfigurerAdapter implements EmbeddedServletContainerCustomizer {

    public static void main(String[] args) {
    	 new SpringApplicationBuilder(Application.class)
         //.showBanner(false)
         .run(args);
    }
    
    
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        mappings.add("html", "text/html;charset=utf-8");
        container.setMimeMappings(mappings );
    }

	
    
}
