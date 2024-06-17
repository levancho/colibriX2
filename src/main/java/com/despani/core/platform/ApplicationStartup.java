package com.despani.core.platform;

import com.despani.core.services.ModuleService;
import com.despani.core.services.RefDataServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {


    @Autowired
    private ApplicationContext context;

    @Autowired
    private RefDataServices refDataService;

    @Autowired
    private ModuleService moduleServices;
        /**
         * This event is executed as late as conceivably possible to indicate that
         * the application is ready to service requests.
         */

        @Override
        public void onApplicationEvent(final ApplicationReadyEvent event) {
            try {
                verifyDespaniHome();
            } catch (Exception e) {
                e.printStackTrace();
                SpringApplication.exit(context, () -> 0);
            }
            refDataService.loadReferenceData();
            moduleServices.loadModules();

        }


    private File verifyDespaniHome() throws FileNotFoundException {

        String desaniHome = System.getenv("DESPANI_HOME");

        if(desaniHome==null) {
            throw new RuntimeException("env variable DESPANI_HOME was not found, please defined DESPANI_HOME and point to despani home directory that has to" +
                    " be accessable by process that is running this application.");
        }
        File openfireHome = new File(desaniHome);
        File configFile = new File(openfireHome, "conf/despani.yml");
        if (!configFile.exists()) {
            throw new FileNotFoundException("despani.yml config file could not be read inside DESPANI_HOME ["+desaniHome+"]");
        }
        else {
            try {
                return new File(openfireHome.getCanonicalPath());
            }
            catch (Exception ex) {
                throw new FileNotFoundException();
            }
        }
    }

    } // class
