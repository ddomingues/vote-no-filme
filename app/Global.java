import models.Movie;
import play.Application;
import play.GlobalSettings;
import play.db.jpa.JPA;
import play.libs.Yaml;

import java.util.List;
import java.util.Map;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
        InitialData.insert(app);
    }
    
    static class InitialData {
        
        public static void insert(Application app) {
            JPA.withTransaction(new play.libs.F.Callback0() {
                public void invoke() {
                    if (Movie.findAll().isEmpty()) {

                        Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml.load("initial-data.yml");

                        for (Object movie : all.get("movies"))
                            ((Movie) movie).save();
                    }
                }
            });
        }
        
    }
    
}