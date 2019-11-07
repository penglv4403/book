package com.orange.book.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;

public class ApolloConfig {

public  void  test(){
/*    Config config = ConfigService.getAppConfig(); //config instance is singleton for each namespace and is never null
    config.addChangeListener(new ConfigChangeListener() {
        @Override
        public void onChange(ConfigChangeEvent changeEvent) {
            System.out.println("Changes for namespace " + changeEvent.getNamespace());
            for (String key : changeEvent.changedKeys()) {
                ConfigChange change = changeEvent.getChange(key);
                System.out.println(String.format("Found change - key: %s, oldValue: %s, newValue: %s, changeType: %s", change.getPropertyName(), change.getOldValue(), change.getNewValue(), change.getChangeType()));
            }
        }
    });*/
}

}
