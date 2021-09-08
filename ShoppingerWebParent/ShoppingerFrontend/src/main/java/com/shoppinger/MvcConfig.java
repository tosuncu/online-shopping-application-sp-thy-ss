package com.shoppinger;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        exposeDirectory("category-images",registry);
        exposeDirectory("brand-logos",registry);
        exposeDirectory("product-images",registry);
        exposeDirectory("site-logo",registry);


      /*  String categoryImagesDirName = "category-images";
        Path categoryImagesDir = Paths.get(categoryImagesDirName);
        String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/" + categoryImagesDirName + "/**")
                .addResourceLocations("file:/" + categoryImagesPath + "/");


        String brandLogosDirName = "brand-logos";
        Path brandLogosDir = Paths.get(brandLogosDirName);
        String brandLogosPath = brandLogosDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/" + brandLogosDirName + "/**")
                .addResourceLocations("file:/" + brandLogosPath + "/");*/

    }

    private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry){
        Path path = Paths.get(pathPattern);
        String absolutePath = path.toFile().getAbsolutePath();

        String logicalPath = pathPattern.replace("..", "") + "/**";

        registry.addResourceHandler(logicalPath)
                .addResourceLocations("file:/"+ absolutePath +"/");
    }
}
