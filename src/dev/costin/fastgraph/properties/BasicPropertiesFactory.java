package dev.costin.fastgraph.properties;


public class BasicPropertiesFactory implements PropertiesFactory<BasicProperties> {
   
   public final static BasicPropertiesFactory INSTANCE = new BasicPropertiesFactory();

   @Override
   public BasicProperties newInstance() {
      return new BasicProperties();
   }

}
