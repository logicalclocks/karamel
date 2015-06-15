/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.karamel.backend.github;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import se.kth.karamel.backend.ExperimentContext;
import se.kth.karamel.client.api.KaramelApi;
import se.kth.karamel.client.api.KaramelApiImpl;
import se.kth.karamel.common.exception.KaramelException;

/**
 *
 * @author jdowling
 */
public class GithubUserTest {

  String user = "";
  String password = "";
  KaramelApi api;

  public GithubUserTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
    api = new KaramelApiImpl();
    try {
      GithubUser u2 = api.loadGithubCredentials();
      user = u2.getUser();
      password = u2.getPassword();
    } catch (KaramelException ex) {
      Logger.getLogger(GithubUserTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of getEmail method, of class GithubUser.
   */
  @Test
  public void testAccount() {
    try {
      api.registerGithubAccount(user, password);
      GithubUser u2 = api.loadGithubCredentials();
      assertEquals(this.user, u2.getUser());
      assertEquals(password, u2.getPassword());
      // TODO review the generated test code and remove the default call to fail.
    } catch (KaramelException ex) {
      Logger.getLogger(GithubUserTest.class.getName()).log(Level.SEVERE, null, ex);
      fail(ex.getMessage());
    }
  }

  /**
   * List Organizations in github
   */
  @Test
  public void testListOrgs() {
    try {
      api.registerGithubAccount(user, password);
      List<OrgItem> orgs = api.listGithubOrganizations();
      for (OrgItem o : orgs) {
        System.out.println("Organization: " + o.getName() + " : " + o.getGravitar());
      }
    } catch (KaramelException ex) {
      Logger.getLogger(GithubUserTest.class.getName()).log(Level.SEVERE, null, ex);
      fail(ex.getMessage());
    }
  }

  @Test
  public void testListRepos() {
    try {
      List<RepoItem> orgs = api.listGithubRepos("hopshadoop");
      for (RepoItem o : orgs) {
        System.out.println("Repo: " + o.getName() + " - " + o.getDescription() + " : " + o.getSshUrl());
      }
    } catch (KaramelException ex) {
      Logger.getLogger(GithubUserTest.class.getName()).log(Level.SEVERE, null, ex);
      fail(ex.getMessage());
    }
  }

  @Test
  public void testCreateRepo() {
    try {
      ExperimentContext ec = new ExperimentContext();
      ExperimentContext.Experiment exp = new ExperimentContext.Experiment("echo \"jim\"\n"
          + "java -jar -D%%maxHeapSize%% prog.jar", "blah", "%%maxHeapSize%%=128m\n%%log%%=true\n",
          "", ExperimentContext.ScriptType.bash);
      ec.addExperiment("experiment", exp);
      ec.setUser("blah");
      ec.setGroup("blah");
      ec.setResultsDirectory("results");
      ec.setUrl("http://snurran.sics.se/hops/prog.jar");
      ec.setDescription("Nice experiment");
      api.commitAndPushExperiment("hopshadoop", "test", ec);
    } catch (KaramelException ex) {
      Logger.getLogger(GithubUserTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

}
