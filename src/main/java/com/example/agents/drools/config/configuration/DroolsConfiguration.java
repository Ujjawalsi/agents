package com.example.agents.drools.config.configuration;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//
//public class DroolsConfiguration {
//
////	private final KieServices kieServices = KieServices.Factory.get();
//
////	@Bean
////	public KieContainer kieContainer() {
//////		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
//////		kieFileSystem.write(ResourceFactory.newClassPathResource("/opt/ES_Java_Apps_WS/BullsEye/src/com/vel/drools/resources/rules/rules.xlsx"));
//////		KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);
//////		kb.buildAll();
//////		KieModule kieModule = kb.getKieModule();
//////		return kieServices.newKieContainer(kieModule.getReleaseId());
////
////        KieServices kieServices = KieServices.Factory.get();
////        KieContainer kContainer = kieServices.getKieClasspathContainer();
////        KieSession kSession = kContainer.newKieSession("resources.rules");
////        return (KieContainer) kSession;
////
////	}
//
//
//    @Bean
//    public KieServices kieServices() {
//        KieServices kieServices = KieServices.Factory.get();
//        System.out.println("KieServices instance: " + kieServices);
//        return kieServices;
//    }
//
//
//
//    @Bean
//    public KieContainer kieContainer() {
//        return kieServices().getKieClasspathContainer();
//
//    }
//
//    @Bean
//    public KieSession kieSession() {
//        KieSession kieSession = kieContainer().newKieSession();
//        System.out.println(kieSession);
//        return kieSession;
//    }
//}



//import org.kie.api.runtime.KieSession;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.kie.api.KieServices;
//import org.kie.api.runtime.KieContainer;
//
//@Configuration
//public class DroolsConfiguration {
//
//
//
//    @Bean
//    public KieContainer kieContainer() {
//        return kieServices().getKieClasspathContainer();
//    }
//
//    @Bean
//    public KieSession kieSession() {
//        return kieContainer().newKieSession();
//    }


import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfiguration {

    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        // Load the XLSX file as a resource
        kieFileSystem.write(ResourceFactory.newClassPathResource("rules/rules.xlsx"));

        // Build the KieBase
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();

        KieRepository kieRepository = kieServices.getRepository();
        return kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
    }

    @Bean
    public KieSession kieSession() {
        return kieContainer().newKieSession();
    }
}






