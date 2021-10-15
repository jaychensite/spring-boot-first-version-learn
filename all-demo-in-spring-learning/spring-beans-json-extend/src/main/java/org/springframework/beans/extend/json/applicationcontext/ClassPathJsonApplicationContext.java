package org.springframework.beans.extend.json.applicationcontext;

import org.springframework.beans.BeansException;
import org.springframework.beans.extend.json.JsonBeanDefinitionReader;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import java.io.IOException;


public class ClassPathJsonApplicationContext extends AbstractRefreshableConfigApplicationContext {



    /**
     * Loads the bean definitions via an XmlBeanDefinitionReader.
     * @see XmlBeanDefinitionReader
     * @see #loadBeanDefinitions
     */
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
        // Create a new XmlBeanDefinitionReader for the given BeanFactory.
        JsonBeanDefinitionReader beanDefinitionReader = new JsonBeanDefinitionReader(beanFactory);

        // Configure the bean definition reader with this context's
        // resource loading environment.
        beanDefinitionReader.setEnvironment(this.getEnvironment());
        beanDefinitionReader.setResourceLoader(this);

        // Allow a subclass to provide custom initialization of the reader,
        // then proceed with actually loading the bean definitions.
        loadBeanDefinitions(beanDefinitionReader);
    }



    /**
     * Load the bean definitions with the given XmlBeanDefinitionReader.
     * <p>The lifecycle of the bean factory is handled by the {@link #refreshBeanFactory}
     * method; hence this method is just supposed to load and/or register bean definitions.
     * @param reader the XmlBeanDefinitionReader to use
     * @throws BeansException in case of bean registration errors
     * @throws IOException if the required XML document isn't found
     * @see #refreshBeanFactory
     * @see #getConfigLocations
     * @see #getResources
     * @see #getResourcePatternResolver
     */
    protected void loadBeanDefinitions(JsonBeanDefinitionReader reader) throws BeansException, IOException {
        String[] configResources = getConfigLocations();
        if (configResources != null) {
            reader.loadBeanDefinitions(configResources);
        }
    }


    /**
     * Create a new ClassPathJsonApplicationContext, loading the definitions
     * from the given json file and automatically refreshing the context.
     * @param configLocation resource location
     * @throws BeansException if context creation failed
     */
    public ClassPathJsonApplicationContext(String configLocation) throws BeansException {
        this(new String[] {configLocation}, true, null);
    }

    /**
     * Create a new ClassPathXmlApplicationContext with the given parent,
     * loading the definitions from the given XML files.
     * @param configLocations array of resource locations
     * @param refresh whether to automatically refresh the context,
     * loading all bean definitions and creating all singletons.
     * Alternatively, call refresh manually after further configuring the context.
     * @param parent the parent context
     * @throws BeansException if context creation failed
     * @see #refresh()
     */
    public ClassPathJsonApplicationContext(String[] configLocations, boolean refresh, ApplicationContext parent)
            throws BeansException {

        super(parent);
        setConfigLocations(configLocations);
        if (refresh) {
            refresh();
        }
    }


}
