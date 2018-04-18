package main.classes.controllers;

//@Configuration
//public class ConnectorConfig {
//    @Bean
//    public TomcatServletWebServerFactory createServletContainer() {
//
//        TomcatServletWebServerFactory tomcatContainer = new TomcatServletWebServerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint constraint = new SecurityConstraint();
//                constraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                constraint.addCollection(collection);
//                context.addConstraint(constraint);
//            }
//        };
//        tomcatContainer.addAdditionalTomcatConnectors(redirectConnectorHttp());
//        return tomcatContainer;
//    }
//
//    private Connector redirectConnectorHttp() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        connector.setPort(8090);
//        connector.setSecure(false);
//        connector.setRedirectPort(8443);
//        return connector;
//    }
//}