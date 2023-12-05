package in.hocg.boot.test.sample.basic;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Created by hocgin on 2023/12/05
 * email: hocgin@gmail.com
 * BeanDefinition 扫描器类
 *
 * @author hocgin
 */
public class TestClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {
    private Class<? extends Annotation> annotationClass = Mapper.class;
    private Class<?> markerInterface;

    public TestClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        //
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }


    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        BeanDefinitionRegistry registry = getRegistry();
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        for (BeanDefinitionHolder holder : beanDefinitionHolders) {
            AbstractBeanDefinition definition = (AbstractBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();

            // todo
            definition.setScope(BeanDefinition.SCOPE_SINGLETON);
            definition.setBeanClass(TestFactoryBean.class);
            definition.setLazyInit(false);

            if (!definition.isSingleton()) {
                BeanDefinitionHolder proxyHolder = ScopedProxyUtils.createScopedProxy(holder, registry, true);
                if (registry.containsBeanDefinition(proxyHolder.getBeanName())) {
                    registry.removeBeanDefinition(proxyHolder.getBeanName());
                }
                registry.registerBeanDefinition(proxyHolder.getBeanName(), proxyHolder.getBeanDefinition());
            }
        }
        return beanDefinitionHolders;
    }

    public void registerFilters() {
        boolean acceptAllInterfaces = true;

        // if specified, use the given annotation and / or marker interface
        if (this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
            acceptAllInterfaces = false;
        }

        if (this.markerInterface != null) {
            addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
                @Override
                protected boolean matchClassName(String className) {
                    return false;
                }
            });
            acceptAllInterfaces = false;
        }

        if (acceptAllInterfaces) {
            // default include filter that accepts all classes
            addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
        }

        // exclude package-info.java
        addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }
}
