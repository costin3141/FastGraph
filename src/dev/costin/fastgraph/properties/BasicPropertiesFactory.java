package dev.costin.fastgraph.properties;


/**
 * Factory for creating new instances of {@link BasicProperties}.
 * 
 * <p>
 * For custom subclasses of {@link BasicProperties} one need to create
 * a implementing the interface {@link PropertiesFactory}.
 * </p>
 * 
 * @author Stefan C. Ionescu
 *
 */
public class BasicPropertiesFactory implements PropertiesFactory<BasicProperties> {
   
   public final static BasicPropertiesFactory INSTANCE = new BasicPropertiesFactory();

   @Override
   public BasicProperties newInstance() {
      return new BasicProperties();
   }

}
