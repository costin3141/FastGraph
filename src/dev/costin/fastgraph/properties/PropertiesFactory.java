package dev.costin.fastgraph.properties;


public interface PropertiesFactory<E extends BasicProperties> {

   E newInstance();
   
}
