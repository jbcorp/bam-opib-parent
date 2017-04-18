package com.kpn.opib.bam.batch.rest.test;

import java.io.File;
import java.net.URL;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.kpn.opib.bam.batch.rest.ProcessResource;
import com.kpn.opib.bam.framework.DataSourceProducer;

@RunWith(Arquillian.class)
public class ProcessResourceTest {
	
	
	@ArquillianResource
    private URL deploymentURL;

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		WebArchive war = ShrinkWrap.create(WebArchive.class,"bam-test.war").addClass(ProcessResource.class).addClass(DataSourceProducer.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		
		File[] files = Maven.resolver().loadPomFromFile("pom.xml")
	            .importRuntimeDependencies().resolve().withTransitivity().asFile();
		
		war.addAsLibraries(files);
		System.out.println(war.toString(true));
		return war;
	}
	
	@Test
	public void testProcessResourceGET(@ArquillianResource URL baseURI)
	{
		System.out.println(baseURI);
	}
	
	@Test
	@RunAsClient
	@Ignore
	public void getProcess(@ArquillianResteasyResource("process/bam-sync-process") ResteasyWebTarget  webTarget)
	{
		final Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
		System.out.println(response.getEntity());
		
	}

}
