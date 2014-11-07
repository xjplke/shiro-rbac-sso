package me.itsoho.rbac.config;


import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


public class PermissionDetect {
	public class Resource{
		String resource;
		List<String> permissions;
		
		public Resource(String resource){
			this.resource = resource;
			pushPermissions("*");
		}
		
		
		public String getResource() {
			return resource;
		}
		public void setResource(String resource) {
			this.resource = resource;
		}
		public List<String> getPermissions() {
			return permissions;
		}
		public void setPermissions(List<String> permissions) {
			this.permissions = permissions;
		}
		
		
		public void pushPermissions(String permission){
			if(null==this.permissions){
				this.permissions = new ArrayList<String>();
			}
			
			String[] array = permission.split(",");
			
			for(String perm : array){
				if(permissions.contains(perm)){
					continue;
				}
				this.permissions.add(perm);
			}
		}
	}
	
	private List<Resource> resources;
	
	
	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public PermissionDetect(){
		resources = new ArrayList<Resource>();
	}
	
	public Resource getResource(String resource){
		for(Resource r:this.resources){
			if(r.getResource().equals(resource)){
				return r;
			}
		}
		Resource res = new Resource(resource);
		this.resources.add(res);
		return res;
	}
	
	public void pushResourceAndPermissions(String resourcePerm){//"resource:perms1,perms2,perm3:id"  not process ids,just get perms
		String[] array = resourcePerm.split(":");
		
		if (array.length == 0) {
			return;
		}
		
		Resource res = getResource(array[0]);
		if(array.length>1){
			res.pushPermissions(array[1]);
		}
	}
	
	
	
	private static List<Class<?>> getRestControllerClass(String basePackage) throws IOException, ClassNotFoundException
	{
	    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	    MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

	    List<Class<?>> candidates = new ArrayList<Class<?>>();
	    String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
	                               resolveBasePackage(basePackage) + "/" + "**/*.class";
	    org.springframework.core.io.Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
	    for (org.springframework.core.io.Resource resource : resources) {
	        if (resource.isReadable()) {
	            MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
	            if (isCandidate(metadataReader)) {
	                candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
	            }
	        }
	    }
	    return candidates;
	}

	private static String resolveBasePackage(String basePackage) {
	    return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
	}

	private static boolean isCandidate(MetadataReader metadataReader) throws ClassNotFoundException
	{
	    try {
	        Class<?> c = Class.forName(metadataReader.getClassMetadata().getClassName());
	        if (c.getAnnotation(RestController.class) != null) {
	            return true;
	        }
	    }
	    catch(Throwable e){
	    }
	    return false;
	}

	
	public static PermissionDetect getPermissionDetect( RequestMappingHandlerMapping handlerMapping,
			WebApplicationContext webApplicationContext){
		PermissionDetect detect = new PermissionDetect();
		
		try {
			for(Class<?> clazz : getRestControllerClass("cn.adfi.radius.controller")){
				for(Method method: clazz.getMethods()){
					RequiresPermissions rp = method.getAnnotation(RequiresPermissions.class);
					if(rp!=null){
						for(String perm:rp.value()){
							detect.pushResourceAndPermissions(perm);
						}
					}
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return detect;
	}
}
