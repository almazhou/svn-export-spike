import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
     public Properties getProperties(String fileName){
         Properties prop = new Properties();
         InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
         try {
             prop.load(inputStream);
         } catch (IOException e) {
             throw new RuntimeException("Property file"+ fileName +"not found");
         }

         return prop;
     }
}
